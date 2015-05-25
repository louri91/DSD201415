package cliente;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatServer extends Remote {
	
  int connect(ChatClient chatClient, String nick) throws RemoteException;

  void disconnect(ChatClient chatClient, String nick) throws RemoteException;
    
  List<ChatClient> getListaConectados() throws RemoteException;
  
  List<Conn> getConexionClientes() throws RemoteException;
    
  int getPuerto() throws RemoteException;
  
  void setPuerto(int puerto) throws RemoteException;
  
  String getUrl() throws RemoteException;
  
  String getUrl(String nick) throws RemoteException;
  
  void connectPrivate(String nombreUsuario, String nick) throws RemoteException;
  
}
