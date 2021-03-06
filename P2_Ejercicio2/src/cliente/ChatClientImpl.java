package cliente;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatClientImpl extends UnicastRemoteObject implements ChatClient {
	/**
	 * Parámetros globales de ChatClientImpl que implementa la interfaz remota
	 * de ChatClient - nombreCliente para asignarle un nombre - interfaz para
	 * asignar una interfaz primaria - cliente para especificar qué ejecución
	 * está relacionada con cada una de las instancias de cliente.
	 * 
	 */
	private String nombreCliente;
	private ClientGUI interfaz;
	private Client cliente;

	/**
	 * Método constructor de la clase
	 * 
	 * @param interfaz
	 * @param cliente
	 * @throws RemoteException
	 */
	protected ChatClientImpl(ClientGUI interfaz, Client cliente)
			throws RemoteException {
		super(0);
		this.interfaz = interfaz;
		this.cliente = cliente;
	}

	/**
	 * Método de propagación de mensajes a través del servidor. Únicamente se
	 * utiliza para que cada uno de los clientes actualice la lista de usuarios
	 * conectados.
	 */
	@Override
	public void callback() throws RemoteException {
		cliente.actualizarListaUsuarios();
	}

	/**
	 * Método setNick que establece el nombre de usuario especificado
	 */
	@Override
	public void setNick(String nombreUsuario) throws RemoteException {
		this.nombreCliente = nombreUsuario;
	}

	/**
	 * Método getNick que devuelve el nombre del cliente
	 */
	@Override
	public String getNick() {
		return nombreCliente;
	}

	/**
	 * Método conectar clientes de forma privada. Se llama a este método desde
	 * el cliente primario que quiere establecer una conversación privada con
	 * nosotros. Éste realiza un lookup a la dirección donde se encuentra el
	 * cliente primario para así poder establecer los dos canales de
	 * comunicación.
	 */
	public void conectarClientes(String nombreUsuario, String nick, String URL) {
		System.out.println(URL);
		try {
			ChatClient ch = (ChatClient) Naming.lookup(URL);

			cliente.conectaMR(ch, nombreUsuario, nick);
		} catch (MalformedURLException | NotBoundException | RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método de propagación de mensajes de cliente. Cada vez que se llame a
	 * éste método se imprime el mensaje que nos han enviado y además llamamos a
	 * que se nos actualice la interfaz de mensajes
	 * 
	 */
	@Override
	public void callback(String content, String nombreUsuario)
			throws RemoteException {
		System.out.println(nombreUsuario + ": " + content);

		if(content.equals(" se ha conectado")){
			cliente.actualizarInterfazMensajes(nombreUsuario+content, "Servidor");
		}else if(content.equals(" se ha desconectado")){
			cliente.actualizarInterfazMensajes(content, nombreUsuario);
		}
		else{
			cliente.actualizarInterfazMensajes(content, nombreUsuario);
		}

	}
	@Override
	public void callbackPublico(String content, String nombreUsuario)
			throws RemoteException {
		System.out.println(nombreUsuario + ": " + content);

		if(content.equals(" se ha conectado")){
			cliente.actualizarInterfazPublica(nombreUsuario+content, "Servidor");
		}else if(content.equals(" se ha desconectado")){
			cliente.actualizarInterfazPublica(nombreUsuario+content, "Servidor");
			cliente.desactivarConverPrivadas(nombreUsuario);
		}
		else{
			cliente.actualizarInterfazPublica(content, nombreUsuario);
		}

	}
}
