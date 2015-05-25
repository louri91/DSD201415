package cliente;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ChatClient> connectedClients;
	private List<Conn> conexionClientes;
	private int puerto = 1100;
	private final static String URL = "//localhost/RmiChatClient";

	protected ChatServerImpl() throws RemoteException {
		super(0);
		connectedClients = Collections
				.synchronizedList(new ArrayList<ChatClient>());
		conexionClientes = Collections.synchronizedList(new ArrayList<Conn>());
	}

	@Override
	public int connect(ChatClient chatClient, String nick) {
		connectedClients.add(chatClient);
		String direccion = URL + puerto;
		System.out.println(direccion);
		Conn conexion = new Conn(nick, direccion, puerto);
		conexionClientes.add(conexion);
		for (ChatClient ch : connectedClients) {
			try {
				ch.callback();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		puerto++;
		return puerto - 1;
	}
	@Override
	public void connectPrivate(String nombreUsuario, String nick) {

		for (ChatClient ch : connectedClients) {
			try {
				if (ch.getNick().equals(nick)) {
					for (Conn conexion : conexionClientes) {
						if (conexion.getNick().equals(nombreUsuario)) {
							ch.conectarClientes(nombreUsuario, nick, conexion.getConexionURL());
						}
					}
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

	}

	/*
	 * public String comprobarDireccion(String url) { if
	 * (conexionClientes.size() == 0) { System.out.println(1); return URL +
	 * contador; } else { for (Conn conexion : conexionClientes) {
	 * System.out.println(url); System.out.println(conexion.getConexionURL());
	 * if (conexion.getConexionURL().toString().equals(url)) {
	 * System.out.println(3); contador++; } } System.out.println(4); return URL
	 * + contador; } }
	 */

	/*
	 * public int comprobarPuerto(int puerto) { if (conexionClientes.size() ==
	 * 0) { return puerto; } else { for (Conn conexion : conexionClientes) { if
	 * (conexion.getPuertoConexion() == puerto) { puerto++; } else { return
	 * puerto; } } } return puerto; }
	 */

	@Override
	public void disconnect(ChatClient chatClient, String nick) {
		if (connectedClients.contains(chatClient)) {
			connectedClients.remove(chatClient);
			for (Conn conexion : conexionClientes) {
				if (conexion.getNick().equals(nick)) {
					conexionClientes.remove(conexion);
				}
			}
		}
	}

	@Override
	public List<ChatClient> getListaConectados() throws RemoteException {
		return connectedClients;
	}

	@Override
	public List<Conn> getConexionClientes() {
		return conexionClientes;
	}

	@Override
	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}

	@Override
	public int getPuerto() {
		return puerto;
	}

	@Override
	public String getUrl() {
		return URL;
	}

	@Override
	public String getUrl(String nick) throws RemoteException {
		for (Conn conexion : conexionClientes) {
			if (conexion.getNick().equals(nick)) {
				return conexion.getConexionURL();
			}
		}
		return null;
	}

}
