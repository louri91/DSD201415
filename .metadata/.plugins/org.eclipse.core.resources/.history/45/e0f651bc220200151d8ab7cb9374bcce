package Chat_v2Privado;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {

  private List<ChatClient> connectedClients;

  protected ChatServerImpl() throws RemoteException {
    super(0);
    connectedClients = Collections.synchronizedList(new ArrayList<ChatClient>());
  }

  @Override public void connect(ChatClient chatClient, String nick) {
    connectedClients.add(chatClient);
  }

  @Override public void disconnect(ChatClient chatClient, String nick) {
    if (connectedClients.contains(chatClient)) {
      connectedClients.remove(chatClient);
    }
  }

  @Override
	public List<ChatClient> getListaConectados() throws RemoteException {
		return connectedClients;
	}
}

