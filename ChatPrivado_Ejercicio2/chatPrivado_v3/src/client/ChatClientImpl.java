package client;

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
	private Send cliente;
	private PrivateGUI interfazPrivate;
	private ChatClient ch; 
	
	protected ChatClientImpl(ClientGUI interfaz,Send cliente) throws RemoteException {
		super(0);
		this.interfaz = interfaz;
		this.cliente = cliente;
	}

	@Override
	public void callback() throws RemoteException {
		cliente.actualizarListaUsuarios();
	}

	@Override
	public void setNick(String nombreUsuario) throws RemoteException {
		this.nombreCliente = nombreUsuario;
	}
	public String getNick(){
		return nombreCliente;
	}
	
	public void conectarClientes(String nombreUsuario, String nick, String URL) {
		System.out.println(URL);
		try {
			ch = (ChatClient) Naming.lookup(URL);
			interfazPrivate = new PrivateGUI(cliente);
			interfazPrivate.setTitle("Conversación privada con "
					+ nick);
			interfazPrivate.getMensajes().setText(nombreUsuario+" quiere hablar contigo\n");
			//cliente.conectaMR(ch, nombreUsuario, nick, URL);
		} catch (MalformedURLException | NotBoundException | RemoteException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void callback(String content, String nombreUsuario)
		throws RemoteException {
		//cliente.conectaMR(ch, nombreUsuario, this.nombreCliente, content);
		interfazPrivate.getMensajes().append(nombreUsuario+": " + content+"\n");
		System.out.println(nombreUsuario+": " + content);
	}
}