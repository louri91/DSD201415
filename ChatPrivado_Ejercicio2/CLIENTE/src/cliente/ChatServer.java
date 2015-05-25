package cliente;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatServer extends Remote {
	
  int connect(ChatClient chatClient, String nick) throws RemoteException;

  void disconnect(ChatClient chatClient, String nick) throws RemoteException;
    
  List<ChatClient> getListaConectados() throws RemoteException;
        
  String getUrl() throws RemoteException;
  
  String getUrl(String nick) throws RemoteException;
      
  ChatClient getStub(String nick) throws RemoteException;
}
