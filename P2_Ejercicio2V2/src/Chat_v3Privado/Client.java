package Chat_v3Privado;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;

public class Client {

	private ChatServer chatServer;
	private ChatClient client;
	private ClientGUI interfaz;
	private List<ChatClient> lista;
	private PrivateGUI converPrivada;

	public Client() {
		inicializarGUI();
	}

	public void inicializarGUI() {
		interfaz = new ClientGUI(this);
	}

	public void crearConexion(String nombre) {
		try {
			//LocateRegistry.createRegistry(1100);

			chatServer = (ChatServer) Naming.lookup(Server.SERVER_URL);
			client = new ChatClientImpl(interfaz, this);
			client.setNick(nombre);

			chatServer.connect(client, nombre);
		} catch (NotBoundException | RemoteException | MalformedURLException e) {
			System.err.println("Cliente no iniciado");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Client cliente = new Client();
	}

	public void conectarClientesPrivado() {
		try {
			Naming.rebind("//localhost/RmiChatClient", client);
			System.out.println("¡Cliente iniciado!");
		} catch (RemoteException | MalformedURLException e) {
			System.err.println("Cliente no iniciado");
			e.printStackTrace();
		}

		String nick;
		nick = interfaz.getMensaje().getText();
		try {
			lista = chatServer.getListaConectados();
			for (ChatClient client : lista) {
				if (client.getNick() == nick) {

					client.conectarClientes(this.client, client);

					client.callback("¡" + client.getNick().toString()
							+ " quiere hablar contigo!", client.getNick(), nick);
					converPrivada = new PrivateGUI(this);
					converPrivada.setTitle("Conversación privada con"
							+ client.getNick());
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		interfaz.getMensaje().setText("");
	}

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

	public void conectar() {
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
		try {
			interfaz.getBtnConectar().setEnabled(true);
			interfaz.getBtnDesconectar().setEnabled(false);
			interfaz.getClientesConectados().setEnabled(false);
			interfaz.getBtnEnviar().setEnabled(false);
			chatServer.disconnect(client, client.getNick());

		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	public void actualizarListaUsuarios() {
		String aux = "";
		try {
			lista = chatServer.getListaConectados();
			for (ChatClient client : lista) {
				aux = aux + client.getNick() + "\n";
			}
			interfaz.getClientesConectados().setText(aux);
			System.out.println(aux);

		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}
}
