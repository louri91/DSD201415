package cliente;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;

import javax.swing.JOptionPane;

public class Client {
	/**
	 * Parámetros globales de la clase Client.java
	 * 
	 * - ChatClient client instancia a un cliente - ClientGUI interfaz instancia
	 * a una interfaz principal - PrivateGUI converPrivada instancia a una
	 * interfaz privada - String nombreUsuario nombre del usuario de dicha
	 * clase. - ChatServer chatServer instancia al servidor con el que se va a
	 * conectar - String urlClient instancia a la direccion URL del cliente -
	 * int puertoClient instancia al puerto donde va a escuchar el cliente
	 * 
	 */
	private ChatClient client;
	private ClientGUI interfaz;
	private PrivateGUI converPrivada;
	private String nombreUsuario;
	private ChatServer chatServer;
	private String urlClient;
	private int puertoClient;
	private int cnt = 0;

	/**
	 * Método constructor de la clase, inicializa la interfaz
	 */
	public Client() {
		inicializarGUI();
	}

	/**
	 * Método inicializarGUI, que inicializa la interfaz principal del cliente
	 */
	public void inicializarGUI() {
		interfaz = new ClientGUI(this);
	}

	/**
	 * Método que crea la conexión del cliente con el servidor y además crea el
	 * RMI del propio cliente para que otros usuarios se puedan comunicar con
	 * él.
	 * 
	 * @param nombre
	 */
	public void crearConexion(String nombre) {
		try {
			try {
				chatServer = (ChatServer) Naming.lookup(Server.SERVER_URL);
			} catch (NotBoundException e) {
				e.printStackTrace();
			}

			client = new ChatClientImpl(interfaz, this);
			if(comprobarNick(nombre)){
				JOptionPane.showMessageDialog(null, "Ese nick ya está ocupado por un usuario, pruebe con otro");
			}
			else{
			nombreUsuario = nombre;
			client.setNick(nombreUsuario);

			puertoClient = chatServer.connect(client, nombreUsuario);

			urlClient = chatServer.getUrl() + puertoClient;

			LocateRegistry.createRegistry(puertoClient);
			Naming.rebind(urlClient, client);
			}
		} catch (RemoteException | MalformedURLException e) {

			System.err.println("Cliente no iniciado");
			e.printStackTrace();
		}

	}

	/**
	 * Método main, únicamente instancia la clase Client
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Client cliente = new Client();
	}

	/**
	 * Método que se utiliza para la conexión de clientes en conversaciones
	 * privadas. Llama al servidor para pedirle el objeto cliente remoto con
	 * quien se quiere comunicar y establece la conexión creando la GUI privada
	 * 
	 * @throws RemoteException
	 */
	public void conectarClientesPrivado() throws RemoteException {
		String nick;
		nick = interfaz.getMensaje().getText();

		try {
			ChatClient remoto = chatServer.getStub(nick);

			String urlRemota = chatServer.getUrl(nick);
			String miUrl = chatServer.getUrl(this.nombreUsuario);
			System.out.println(urlRemota);
			client = (ChatClient) Naming.lookup(urlRemota);
			remoto.conectarClientes(this.nombreUsuario, nick, miUrl);
			// chatServer.connectPrivate(nombreUsuario, nick);
		} catch (MalformedURLException | RemoteException | NotBoundException e1) {
			e1.printStackTrace();
		}

		converPrivada = new PrivateGUI(this, nick);
		converPrivada.setTitle("Conversación privada con " + client.getNick());

		interfaz.getMensaje().setText("");
	}

	/**
	 * Método enviar que únicamente se utiliza desde la interfaz principal de
	 * cada uno de los clientes
	 */
	public void enviar() {
		String msg;
		msg = interfaz.getMensaje().getText();
		try {
			client.callback(msg, client.getNick());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		interfaz.getMensaje().setText("");
	}

	/**
	 * Método que envía mensajes desde la interfaz privada. Selecciona el
	 * mensaje del campo de texto mensaje y llama al servidor para que le
	 * devuelva la instancia de cliente con quien se quiere comunicar, de forma
	 * que se le avisa únicamente a él/ella de que nos queremos comunicar y se
	 * propaga el mensaje.
	 * 
	 * @param nombreConversacion
	 */
	public void enviarPrivado(String nombreConversacion) {
		String msg;
		msg = converPrivada.getSend().getText();

		converPrivada.getMensajes().append(nombreUsuario + ": " + msg + "\n");
		try {
			ChatClient remoto = chatServer.getStub(nombreConversacion);
			remoto.callback(msg, nombreUsuario);

		} catch (RemoteException e) {
			e.printStackTrace();
		}
		converPrivada.getSend().setText("");
	}

	/**
	 * Método que actualiza la interfaz del usuario a quien se dirige el
	 * mensaje. Hasta aquí se propaga el mensaje que ha enviado el primer
	 * cliente. El cliente remoto actualiza su interfaz con el mensaje recibido
	 * por parámetro
	 * 
	 * @param content
	 * @param nombreUsuario
	 */
	public void actualizarInterfazMensajes(String content, String nombreUsuario) {
		converPrivada.getMensajes().append(
				nombreUsuario + ": " + content + "\n");

	}

	/**
	 * Método que únicamente sirve para crear la interfaz en el cliente remoto.
	 * Esto es, en el método conectarClientesPrivado se crea la GUI privada para
	 * el cliente que solicita una conversación privada, éste método crea la GUI
	 * para el cliente remoto que recibe la petición de conversación privada.
	 * 
	 * @param ch
	 * @param nombreUsuario
	 * @param nick
	 */
	public void conectaMR(ChatClient ch, String nombreUsuario, String nick) {
		converPrivada = new PrivateGUI(this, nombreUsuario);
		converPrivada.setTitle("Conversación privada con " + nombreUsuario);
		converPrivada.getMensajes().setText(
				nombreUsuario + " quiere hablar contigo\n");
	}

	/**
	 * Método de la interfaz principal, recoge el nombre de usuario que ha
	 * pedido el cliente y registra la conexión con dicho nombre.
	 * 
	 * @throws RemoteException
	 * @throws MalformedURLException
	 */
	public void conectar() throws RemoteException, MalformedURLException {
		String nombre = interfaz.getMensaje().getText();
		
		crearConexion(nombre);
		if(!comprobarNick(nombre)){
		
		actualizarListaUsuarios();

		}
		interfaz.getMensaje().setText("");
		
	}

	/**
	 * Metodo que comprueba si existe ese usuario en la lista de usuarios
	 * conectados
	 * 
	 * @param nick
	 * @return
	 */
	private boolean comprobarNick(String nick) {
		List<ChatClient> lista;
		try {
			lista = chatServer.getListaConectados();
				if (lista.size() != 0) {
					for (ChatClient cliente : lista) {
						if (cliente.getNick().equals(nick)) {
							return true;
						}
					
					}
					return false;
				}
		}catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * Método desconectar, desconecta a un cliente del sistema completamente.
	 */
	public void desconectar() {
		interfaz.getBtnConectar().setEnabled(true);
		interfaz.getBtnDesconectar().setEnabled(false);
		interfaz.getBtnEnviar().setEnabled(false);
		interfaz.getClientesConectados().setEnabled(false);
		interfaz.getRootPane().setDefaultButton(interfaz.getBtnConectar());
		try {
			chatServer.disconnect(client, client.getNick());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método actualizar la lista de usuarios conectados.
	 */
	public void actualizarListaUsuarios() {
		interfaz.getClientesConectados().setEnabled(true);
		interfaz.getBtnConectar().setEnabled(false);
		interfaz.getBtnDesconectar().setEnabled(true);
		interfaz.getBtnEnviar().setEnabled(true);
		interfaz.getRootPane().setDefaultButton(interfaz.getBtnEnviar());
		
		
		String aux = "";
		List<ChatClient> lista;
		try {
			lista = chatServer.getListaConectados();

			for (ChatClient ch : lista) {
				aux = aux + ch.getNick() + "\n";
			}

			interfaz.getClientesConectados().setText(aux);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}