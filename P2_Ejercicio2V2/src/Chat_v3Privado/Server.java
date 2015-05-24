package Chat_v3Privado;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {

  public static final String SERVER_URL = "//localhost/RmiChatServer";

  public static void main(String[] args) {
    try {

      LocateRegistry.createRegistry(1099);

      ChatServerImpl chatServer = new ChatServerImpl();
      Naming.rebind(SERVER_URL, chatServer);
      System.out.println("¡Servidor preparado!");
    } catch (Exception e) {
      System.err.println("Error en el Servidor: " + e.toString());
      e.printStackTrace();
    }
  }
}
