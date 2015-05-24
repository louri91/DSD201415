package cliente;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;

public class Client {

	//private ChatServer chatServer;
	private ChatClient client;
	private ClientGUI interfaz;
	private List<ChatClient> lista;
	private PrivateGUI converPrivada;
	private String nombreUsuario;

	public Client() {
		inicializarGUI();
	}

	public void inicializarGUI() {
		interfaz = new ClientGUI(this);
	}
	
	public void crearConexion(String nombre) {
		try{
			client = new ChatClientImpl(interfaz, this);
			nombreUsuario = nombre;
			client.setNick(nombre);
			LocateRegistry.createRegistry(1100);
			Naming.rebind("//localhost/RmiChatClient1", client);
			System.out.println("¡Cliente iniciado!");
		}catch(RemoteException | MalformedURLException e){
			System.err.println("Cliente no iniciado");
			e.printStackTrace();
		}
			
	}

	public static void main(String[] args) {
		Client cliente = new Client();
	}

	public void conectarClientesPrivado() throws RemoteException {
		try {
			client = (ChatClient) Naming.lookup("//localhost/RmiChatClient");
			client.conectarClientes(nombreUsuario, "//localhost/RmiChatClient1");
		} catch (MalformedURLException | RemoteException | NotBoundException e1) {
			e1.printStackTrace();
		}

		String nick;
		nick = interfaz.getMensaje().getText();
		
					converPrivada = new PrivateGUI(this);
					converPrivada.setTitle("Conversación privada con "
							+ client.getNick());
				
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
	
	public void enviarPrivado(){
		String msg;
		msg = converPrivada.getSend().getText();
		try {
			client.callback(msg, nombreUsuario);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		converPrivada.getSend().setText("");
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
		

	}
}
