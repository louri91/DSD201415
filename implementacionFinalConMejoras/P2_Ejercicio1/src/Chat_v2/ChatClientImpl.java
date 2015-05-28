package Chat_v2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatClientImpl extends UnicastRemoteObject implements ChatClient {
	private String nombreCliente;
	private ClientGUI interfaz;
	private Client cliente;
	protected ChatClientImpl(ClientGUI interfaz,Client cliente) throws RemoteException {
		super(0);
		this.interfaz = interfaz;
		this.cliente = cliente;
	}
	@Override
	public void callback(String content, String nombreUsuario) {
		interfaz.getListaMensajes().append(nombreUsuario+": " + content+"\n");
		interfaz.getListaMensajes().setCaretPosition(
				interfaz.getListaMensajes().getText().length());
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
}
