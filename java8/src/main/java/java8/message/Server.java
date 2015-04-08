/**
 * 
 */
package java8.message;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
public class Server {

	private final String	serverName;

	Server(String serverName) {
		this.serverName = serverName;
		Network.register(serverName, this);
	}

	public Connection open() {
		return new SimpleConnection(this);
	}

	/**
	 * @param msg
	 * @return
	 */
	public Connection accept(String msg) {
		return null;
	}

}
