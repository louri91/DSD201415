package cliente;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
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
	private Map<String, String> conversacionesActivas;
	private Map<String, PrivateGUI> framesActivos;

	/**
	 * Método constructor de la clase, inicializa la interfaz
	 */
	public Client() {
		inicializarGUI();
		conversacionesActivas = Collections
				.synchronizedMap(new HashMap<String, String>());
		framesActivos = Collections
				.synchronizedMap(new HashMap<String, PrivateGUI>());
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
			if (comprobarNick(nombre)) {
				JOptionPane
						.showMessageDialog(null,
								"Ese nick ya está ocupado por un usuario, pruebe con otro");
			} else {
				nombreUsuario = nombre;
				client.setNick(nombreUsuario);

				puertoClient = chatServer.connect(client, nombreUsuario);

				urlClient = chatServer.getUrl() + puertoClient;

				LocateRegistry.createRegistry(puertoClient);
				Naming.rebind(urlClient, client);

				avisarConectado(nombreUsuario);

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
		boolean encontrado = false;
		String nick;
		nick = interfaz.getListUsuarios().getSelectedValue();

		if (!conversacionesActivas.isEmpty()) {
			for (Entry<String, String> e : conversacionesActivas.entrySet()) {
				if (e.getKey().equals(nick)) {
					if (!framesActivos.isEmpty()) {
						for (Entry<String, PrivateGUI> g : framesActivos
								.entrySet()) {
							if (g.getKey().equals(nick)) {
								PrivateGUI value = g.getValue();
								value.show();
								encontrado = true;
								System.out.println(encontrado);
							}
						}
					}
				}
			}
		}
		if (!nick.equals(this.nombreUsuario) && encontrado == false) {
			try {
				ChatClient remoto = chatServer.getStub(nick);

				String urlRemota = chatServer.getUrl(nick);
				String miUrl = chatServer.getUrl(this.nombreUsuario);
				System.out.println(urlRemota);
				client = (ChatClient) Naming.lookup(urlRemota);
				remoto.conectarClientes(this.nombreUsuario, nick, miUrl);

				conversacionesActivas.put(nick, this.nombreUsuario);

			} catch (MalformedURLException | RemoteException
					| NotBoundException e1) {
				e1.printStackTrace();
			}

			converPrivada = new PrivateGUI(this, nick);
			converPrivada.setTitle("["+this.nombreUsuario+"] "+"Conversación privada con "
					+ client.getNick());
			framesActivos.put(nick, converPrivada);

			interfaz.getMensaje().setText("");
		}
	}

	/**
	 * Método enviar que únicamente se utiliza desde la interfaz principal de
	 * cada uno de los clientes
	 */
	public void enviar() {
		String msg;
		msg = interfaz.getMensaje().getText();
		List<ChatClient> lista;
		try {
			lista = chatServer.getListaConectados();
			for (ChatClient ch : lista) {
				ch.callbackPublico(msg, this.nombreUsuario);
			}

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
		for (Entry<String, PrivateGUI> e : framesActivos.entrySet()) {
			if (e.getKey().equals(nombreConversacion)) {
				converPrivada = e.getValue();
			}
		}
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

		for (Entry<String, PrivateGUI> e : framesActivos.entrySet()) {
			if (e.getKey().equals(nombreUsuario)) {
				converPrivada = e.getValue();
			}
		}

		if (content.equals(" se ha desconectado")) {
			converPrivada.getMensajes().append(
					"Servidor : " + nombreUsuario + content + "\n");
			converPrivada.getMensajes().setEnabled(false);
			converPrivada.getBtnEnviar().setEnabled(false);
			converPrivada.getSend().setEnabled(false);

		} else {
			converPrivada.getMensajes().append(
					nombreUsuario + ": " + content + "\n");
		}

		converPrivada.getMensajes().setCaretPosition(
				converPrivada.getMensajes().getText().length());

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
		converPrivada.setTitle("["+nick+"] "+"Conversación privada con " + nombreUsuario);
		converPrivada.getMensajes().setText(
				nombreUsuario + " quiere hablar contigo\n");
		conversacionesActivas.put(nombreUsuario, nick);
		framesActivos.put(nombreUsuario, converPrivada);
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
		if (!comprobarNick(nombre)) {
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
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Método desconectar, desconecta a un cliente del sistema completamente.
	 */
	public void desconectar() throws RemoteException {
		List<String> aux = new ArrayList<String>();
		interfaz.getBtnConectar().setEnabled(true);
		interfaz.getBtnDesconectar().setEnabled(false);
		interfaz.getBtnEnviar().setEnabled(false);
		interfaz.getClientesConectados().setEnabled(false);
		interfaz.getListUsuarios().setEnabled(false);
		interfaz.getRootPane().setDefaultButton(interfaz.getBtnConectar());

		if (!conversacionesActivas.isEmpty()) {
			for (String converActiva : conversacionesActivas.keySet()) {
				if (!framesActivos.isEmpty()) {
					for (Entry<String, PrivateGUI> e : framesActivos
							.entrySet()) {
						if (e.getKey().equals(converActiva)) {
							PrivateGUI value = e.getValue();
							value.setVisible(false);
							value.dispose();
							aux.add(converActiva);
							framesActivos.remove(e);
						}
					}

				}
			}
			for (String aux1 : aux) {
				conversacionesActivas.remove(aux1);
			}
		}
		try {
			urlClient = chatServer.getUrl() + puertoClient;
			System.out.println("ESTA ES:" + urlClient);
			Naming.unbind(urlClient);

			avisarDesconectado(this.nombreUsuario);

			chatServer.disconnect(client, this.nombreUsuario);

		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	private boolean comprobarUsuarioConectado(String converActiva) {
		boolean usuarioConectado = false;
		List<ChatClient> lista;
		try {
			lista = chatServer.getListaConectados();
			for (ChatClient ch : lista) {
				if(ch.getNick().equals(converActiva)){
					usuarioConectado = true;
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		System.out.println(usuarioConectado);
		return usuarioConectado;
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

		List<ChatClient> lista;
		try {
			lista = chatServer.getListaConectados();
			DefaultListModel<String> model = interfaz.getModel();
			model.clear();
			int contador = 0;

			for (ChatClient ch : lista) {
				model.add(contador, ch.getNick());
				++contador;
			}
			interfaz.setModel(model);
			interfaz.repaint();
			interfaz.getListUsuarios().repaint();
			interfaz.getPanel().repaint();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void avisarConectado(String usuarioConectado) {
		List<ChatClient> lista;
		try {
			lista = chatServer.getListaConectados();
			for (ChatClient ch : lista) {
				ch.callbackPublico(" se ha conectado", usuarioConectado);
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void avisarDesconectado(String usuarioConectado) {
		List<ChatClient> lista;
		try {
			lista = chatServer.getListaConectados();
			if (!lista.isEmpty()) {
				for (ChatClient ch : lista) {
					ch.callbackPublico(" se ha desconectado", usuarioConectado);
				}
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void actualizarInterfazPublica(String content, String nombreUsuario) {
		interfaz.getClientesConectados().append(
				"[" + nombreUsuario + "]: " + content + "\n");
		interfaz.getClientesConectados().setCaretPosition(
				interfaz.getClientesConectados().getText().length());
	}

	public void desactivarConverPrivadas(String converActiva) {
		if (!framesActivos.isEmpty()) {
			for (Entry<String, PrivateGUI> e : framesActivos.entrySet()) {
				if (e.getKey().equals(converActiva)) {
					PrivateGUI value = e.getValue();
					value.setEnabled(false);
				}
			}

		}
	}

}
