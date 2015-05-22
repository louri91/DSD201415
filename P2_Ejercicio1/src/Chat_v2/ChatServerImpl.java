package Chat_v2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {

	private List<ChatClient> connectedClients;

	protected ChatServerImpl() throws RemoteException {
		super(0);
		connectedClients = Collections
				.synchronizedList(new ArrayList<ChatClient>());
	}

	@Override
	public void connect(ChatClient chatClient, String nick) {
		connectedClients.add(chatClient);
		for (ChatClient client : connectedClients) {
			try {
				client.callback(nick + " se ha conectado", "Servidor");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void disconnect(ChatClient chatClient, String nick) {
		if (connectedClients.contains(chatClient)) {
			connectedClients.remove(chatClient);
			for (ChatClient client : connectedClients) {
				try {
					client.callback(nick + " se ha desconectado", "Servidor");
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void send(String message, String nick) {
		System.out.println("Se ha enviado el mensaje a "
				+ connectedClients.size() + " clientes.");
		for (ChatClient client : connectedClients) {
			try {
				client.callback(message, nick);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<ChatClient> getListaConectados() throws RemoteException {
		return connectedClients;
	}
}
