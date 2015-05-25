package cliente;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;


public class Client {

	private ChatClient client;
	private ClientGUI interfaz;
	private PrivateGUI converPrivada;
	private String nombreUsuario;
	private ChatServer chatServer;
	private String urlClient;
	private int puertoClient;

	public Client() {
		inicializarGUI();
	}

	public void inicializarGUI() {
		interfaz = new ClientGUI(this);
	}

	public void crearConexion(String nombre) {
		try {
			try {
				chatServer = (ChatServer) Naming.lookup(Server.SERVER_URL);
			} catch (NotBoundException e) {
				e.printStackTrace();
			}

			client = new ChatClientImpl(interfaz, this);
			nombreUsuario = nombre;
			client.setNick(nombre);
			
			puertoClient = chatServer.connect(client, nombreUsuario);

			urlClient = chatServer.getUrl()+puertoClient;

			LocateRegistry.createRegistry(puertoClient);
			Naming.rebind(urlClient, client);

		} catch (RemoteException | MalformedURLException e) {

			System.err.println("Cliente no iniciado");
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Client cliente = new Client();
	}

	public void conectarClientesPrivado() throws RemoteException {
		String nick;
		nick = interfaz.getMensaje().getText();
		
		try {
			String urlRemota = chatServer.getUrl(nick);
			System.out.println(urlRemota);
			client = (ChatClient) Naming.lookup(urlRemota);
			chatServer.connectPrivate(nombreUsuario, nick);
		} catch (MalformedURLException | RemoteException | NotBoundException e1) {
			e1.printStackTrace();
		}
		
		converPrivada = new PrivateGUI(this);
		converPrivada.setTitle("Conversación privada con " + client.getNick());

		interfaz.getMensaje().setText("");
	}

	public void enviar() {
		String msg;
		msg = interfaz.getMensaje().getText();
		try {
			client.callback(msg, client.getNick(), converPrivada);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		interfaz.getMensaje().setText("");
	}

	public void enviarPrivado() {
		String msg;
		msg = converPrivada.getSend().getText();
		
		converPrivada.getMensajes().append(nombreUsuario+": "+msg+"\n");
		try {
			client.callback(msg, nombreUsuario, converPrivada);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		converPrivada.getSend().setText("");
	}
	
	public void conectaMR(ChatClient ch, String nombreUsuario, String nick, String mensaje) {
		if(converPrivada==null){
			converPrivada = new PrivateGUI(this);
			converPrivada.setTitle("Conversación privada con "
					+ nick);
			converPrivada.getMensajes().setText(nombreUsuario+" quiere hablar contigo\n");
			try {
				ch.callback("quiere hablar contigo", nombreUsuario, converPrivada);
				ch.callback(mensaje, nick,converPrivada);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		else{
			try {
				ch.callback(mensaje, nick,converPrivada);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		

	}

	public void conectar() throws RemoteException, MalformedURLException {
		interfaz.getRootPane().setDefaultButton(interfaz.getBtnEnviar());
		interfaz.getBtnConectar().setEnabled(false);
		interfaz.getBtnDesconectar().setEnabled(true);
		interfaz.getClientesConectados().setEnabled(true);
		interfaz.getBtnEnviar().setEnabled(true);

		String nombre = interfaz.getMensaje().getText();

		crearConexion(nombre);
		actualizarListaUsuarios();
		interfaz.getMensaje().setText("");
	}

	public void desconectar() {
	}

	public void actualizarListaUsuarios() {
		String aux = ""; 
		List<ChatClient> lista;
		try {
			lista = chatServer.getListaConectados();
		
		for(ChatClient ch : lista){
			aux = aux + ch.getNick()+"\n";
		}
		
		interfaz.getClientesConectados().setText(aux);
		}catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
