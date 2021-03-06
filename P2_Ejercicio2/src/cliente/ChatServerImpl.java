package cliente;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {

	/**
	 * Declaramos las variables necesarias - connectedClients va a almacenar
	 * todos y cada uno de los clientes que se conecten a nuestro servidor. -
	 * conexionClientes va a almacenar todos los clientes como objetos de la
	 * clase Conn, que contendrá los parámetros de conexión necesarios para que
	 * se haga la conexión entre clientes. - puerto inicializado a 1100 por
	 * defecto, será el puerto que tendrá el primer cliente que se conecte. -
	 * URL variable estática que contiene la referencia a la dirección donde se
	 * alojará nuestro RMI de cliente.
	 */

	private static final long serialVersionUID = 1L;
	private List<ChatClient> connectedClients;
	private List<Conn> conexionClientes;
	private int puerto = 1100;
	private final static String URL = "//localhost/RmiChatClient";

	/**
	 * Constructor de la clase ChatServerImpl que implementa la interfaz remota
	 * de ChatServer. Éste se encarga de inicializar las dos listas necesarias
	 * para realizar las conexiones con clientes.
	 */
	protected ChatServerImpl() throws RemoteException {
		super(0);
		connectedClients = Collections
				.synchronizedList(new ArrayList<ChatClient>());
		conexionClientes = Collections.synchronizedList(new ArrayList<Conn>());
	}

	/**
	 * Metodo de conexión al servidor. Este método se llamará cada vez que se
	 * conecte un cliente, añadiendo dicho cliente a la lista de
	 * connectedClients y creando los parámetros de conexión que va a utilizar
	 * el cliente para crear su instancia RMI. Devuelve el puerto por el que va
	 * a escuchar dicho cliente.
	 */
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

	/**
	 * Método de desconexión del servidor. Este método será llamado desde el
	 * cliente una vez decida cerrar sesión en nuestro programa. El método
	 * eliminará dicho cliente de la lista de connectedClients, así como
	 * eliminará el objeto de conexión del mismo.
	 * 
	 * @throws RemoteException
	 */
	@Override
	public void disconnect(ChatClient chatClient, String nick)
			throws RemoteException {
		Conn aux = new Conn("","",0);
		ChatClient auxiliar = null;
		boolean encontrado= false;
		boolean encontrado2 = false;
		for(int i=0;i<connectedClients.size();i++){
			if(connectedClients.get(i).getNick().equals(nick)){
				encontrado2 = true;
				auxiliar = connectedClients.get(i);
				System.out.println(auxiliar.getNick());
			}
		}
		if (encontrado2) {
			connectedClients.remove(auxiliar);
			System.out.println("ENCONTRADO2");
			for (Conn conexion : conexionClientes) {
				if (conexion.getNick().equals(nick))
				{	System.out.println(conexion.getNick());
					encontrado = true;
					aux = conexion;
				}
			}
			if(encontrado){
				conexionClientes.remove(aux);
			}
		}
		System.out.println(connectedClients.toString());
		
		if (!(connectedClients.isEmpty()) || !(connectedClients.size()==1)) {
			for (ChatClient ch : connectedClients) {
				ch.callback();
			}
		}
	}

	/**
	 * Devuelve la lista de usuarios conectados.
	 */
	@Override
	public List<ChatClient> getListaConectados() throws RemoteException {
		return connectedClients;
	}

	/**
	 * Devuelve la URL por defecto
	 */
	@Override
	public String getUrl() {
		return URL;
	}

	/**
	 * Devuelve la URL que se le ha asignado al cliente con nombre de usuario
	 * nick. Recorre la lista de conexionClientes hasta encontrar la conexión de
	 * dicho usuario.
	 */
	@Override
	public String getUrl(String nick) throws RemoteException {
		for (Conn conexion : conexionClientes) {
			if (conexion.getNick().equals(nick)) {
				return conexion.getConexionURL();
			}
		}
		return null;
	}

	/**
	 * Devuelve el objeto ChatClient de la lista connectedClients Recorre la
	 * lista de connectedClients hasta encontrar el cliente con el nick pasado
	 * por parámetro.
	 */
	@Override
	public ChatClient getStub(String nick) throws RemoteException {
		for (ChatClient client : connectedClients) {
			if (client.getNick().equals(nick)) {
				return client;
			}
		}
		return null;
	}

}
