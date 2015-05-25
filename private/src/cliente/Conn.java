package cliente;

public class Conn {
	public String nick;
	public String conexionURL;
	public int puertoConexion;
	
	public Conn(String nick, String conexionURL, int puertoConexion) {
		super();
		this.nick = nick;
		this.conexionURL = conexionURL;
		this.puertoConexion = puertoConexion;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getConexionURL() {
		return conexionURL;
	}
	public void setConexionURL(String conexionURL) {
		this.conexionURL = conexionURL;
	}
	public int getPuertoConexion() {
		return puertoConexion;
	}
	public void setPuertoConexion(int puertoConexion) {
		this.puertoConexion = puertoConexion;
	}
	

}
