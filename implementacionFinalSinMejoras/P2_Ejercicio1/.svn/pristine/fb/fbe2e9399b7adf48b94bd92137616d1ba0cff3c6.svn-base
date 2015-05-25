package Chat_v2Privado;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatServer extends Remote {
	
  void connect(ChatClient chatClient, String nick) throws RemoteException;

  void disconnect(ChatClient chatClient, String nick) throws RemoteException;
    
  List<ChatClient> getListaConectados() throws RemoteException;
  
}
