package cliente;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ChatClientImpl extends UnicastRemoteObject implements ChatClient {
	private String nombreCliente;
	private ClientGUI interfaz;
	private Client cliente;
	private ChatClient client;
	private PrivateGUI interfazPrivate;

	
	protected ChatClientImpl(ClientGUI interfaz,Client cliente) throws RemoteException {
		super(0);
		this.interfaz = interfaz;
		this.cliente = cliente;
	}
	@Override
	public void callback(String nick, String content, String nombreUsuario) throws RemoteException {

		interfazPrivate.getMensajes().append(nombreUsuario+": " + content);
		System.out.println(nombreUsuario+": " + content);
		cliente.actualizarListaUsuarios();
	}

	@Override
	public void setNick(String nombreUsuario) throws RemoteException {
		this.nombreCliente = nombreUsuario;
	}
	public String getNick(){
		return nombreCliente;
	}
	
	public void conectarClientes(String nick, String URL) {
		System.out.println(URL);
		try {
			this.client = (ChatClient) Naming.lookup(URL);
			interfazPrivate = new PrivateGUI(cliente);
			interfazPrivate.setTitle("Conversación privada con "
					+ nick);
			interfazPrivate.getMensajes().setText(nick+" quiere hablar contigo\n");
		} catch (MalformedURLException | NotBoundException | RemoteException e) {
			e.printStackTrace();
		}
	
	}
	@Override
	public void callback(String content, String nombreUsuario)
			throws RemoteException {
		interfazPrivate.getMensajes().append(nombreUsuario+": " + content+"\n");
		System.out.println(nombreUsuario+": " + content);
	}
}
