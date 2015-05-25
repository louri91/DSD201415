package Chat_v2;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

import javax.swing.SwingUtilities;

public class Client {

	private ChatServer chatServer;
	private ChatClient client;
	private ClientGUI interfaz;

	public Client() {
		inicializarGUI();
	}

	public void inicializarGUI() {
		interfaz = new ClientGUI(this);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				interfaz.getListaMensajes().setText(
						"Escriba un nick y pulse Conectar\n");
			}
		});
	}

	public void crearConexion(String nombre) {
		try {
			chatServer = (ChatServer) Naming.lookup(Server.SERVER_URL);
			client = new ChatClientImpl(interfaz, this);
			client.setNick(nombre);

			chatServer.connect(client, nombre);

		} catch (NotBoundException | RemoteException | MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Client cliente = new Client();

		/*
		 * System.out.println("Escriba un nick"); Scanner scanner = new
		 * Scanner(System.in); String nombreCliente=scanner.nextLine();
		 * System.out.println("¡Bienvenido/a " + nombreCliente+"!");
		 * 
		 * 
		 * try { chatServer = (ChatServer) Naming.lookup(Server.SERVER_URL);
		 * client = new ChatClientImpl();
		 * chatServer.connect(client,nombreCliente);
		 * 
		 * client.setNick(nombreCliente);
		 * 
		 * String msg = "";
		 * 
		 * while (!msg.equals("exit")) { msg = scanner.nextLine();
		 * chatServer.send(msg,nombreCliente); }
		 * 
		 * chatServer.disconnect(client,nombreCliente); } catch
		 * (NotBoundException | RemoteException | MalformedURLException e) {
		 * e.printStackTrace(); }
		 */
	}

	public void enviar() {
		String msg;
		msg = interfaz.getMensaje().getText();
		try {
			chatServer.send(msg, client.getNick());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		interfaz.getMensaje().setText("");
	}

	public void conectar() {
		interfaz.getRootPane().setDefaultButton(interfaz.getBtnEnviar());
		interfaz.getBtnConectar().setEnabled(false);
		interfaz.getBtnDesconectar().setEnabled(true);
		interfaz.getListaMensajes().setEnabled(true);
		interfaz.getClientesConectados().setEnabled(true);
		interfaz.getBtnEnviar().setEnabled(true);

		String nombre = interfaz.getMensaje().getText();

		interfaz.getListaMensajes().setText("¡Bienvenido/a " + nombre + "!\n");
		crearConexion(nombre);
		actualizarListaUsuarios();
		interfaz.getMensaje().setText("");
	}

	public void desconectar() {
		try {
			interfaz.getBtnConectar().setEnabled(true);
			interfaz.getBtnDesconectar().setEnabled(false);
			interfaz.getListaMensajes().setEnabled(false);
			interfaz.getClientesConectados().setEnabled(false);
			interfaz.getBtnEnviar().setEnabled(false);
			chatServer.disconnect(client, client.getNick());

		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	public void actualizarListaUsuarios() {
		String aux = "";
		try {
			List<ChatClient> lista = chatServer.getListaConectados();
			for (ChatClient client : lista) {
				aux = aux + client.getNick() + "\n";
			}
			interfaz.getClientesConectados().setText(aux);

		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}
}
