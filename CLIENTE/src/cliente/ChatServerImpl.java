package cliente;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {

  private List<ChatClient> connectedClients;
  private List<Conn> conexionClientes;
  private int contador = 0;
  private int puerto = 1100;
  private  String URL = "//localhost/RmiChatClient";
  
  protected ChatServerImpl() throws RemoteException {
    super(0);
    connectedClients = Collections.synchronizedList(new ArrayList<ChatClient>());
  }

  @Override public void connect(ChatClient chatClient, String nick) {
    connectedClients.add(chatClient);
    String direccion = comprobarDireccion(URL+contador);
    int puerto = comprobarPuerto(this.puerto);
    Conn conexion = new Conn(nick,direccion,puerto);
    conexionClientes.add(conexion);
  }
  
  public String comprobarDireccion(String url){
	  for(Conn conexion : conexionClientes){
		  if(conexion.getConexionURL().equals(url)){
			  contador++;
		  }
		  else{
			  break;
		  }
		  
	  }
	return URL+contador;
  }
  
  public int comprobarPuerto(int puerto){
	  for(Conn conexion : conexionClientes){
		  if(conexion.getPuertoConexion()==puerto){
			  puerto++;
		  }else{
			  break;
		  }
	  }
	  return puerto;
  }

  @Override 
  public void disconnect(ChatClient chatClient, String nick) {
    if (connectedClients.contains(chatClient)) {
      connectedClients.remove(chatClient);
      for(Conn conexion : conexionClientes){
    	  if(conexion.getNick().equals(nick)){
    		  conexionClientes.remove(conexion);
    	  }
      }
    }
  }

  @Override
	public List<ChatClient> getListaConectados() throws RemoteException {
		return connectedClients;
	}

	public List<Conn> getConexionClientes() {
		return conexionClientes;
	}
  
}

