package Chat_v2Privado;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClient extends Remote {
	void callback(String content, String nombreUsuario) throws RemoteException;
	void callback(String nick, String content, String nombreUsuario) throws RemoteException;
	void setNick(String nombreUsuario) throws RemoteException;
	String getNick() throws RemoteException;
	void conectarClientes(ChatClient client, ChatClient cliente2) throws RemoteException;
}
