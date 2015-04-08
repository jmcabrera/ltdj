/**
 * 
 */
package java8.message;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
public class Client {

	final private String	clientName;
	final private String	serverName;

	public Client(String clientName, String serverName) {
		this.clientName = clientName;
		this.serverName = serverName;
	}

	void run() {
		Connection cnx = Network.open(serverName);
		cnx.put("Hello").onReply((c0, m0) -> {
			switch (m0) {
				case "Hello":
					c0.put("My name is " + clientName + ". Who are you?").onReply((c1, m1) -> {
						c1.put("Nice to meet you, " + m1 + ". Bye.").close();
					});
				default:
					throw new RuntimeException("Unexpected reply");
			}
		});
	}
}
