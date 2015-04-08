/**
 * 
 */
package java8.message;

import java.util.function.BiConsumer;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
public class SimpleConnection implements Connection {

	private Server	server;
	private boolean	closed;

	/**
	 * @param server
	 */
	public SimpleConnection(Server server) {
		this.server = server;
	}

	@Override
	public void close() {
		closed = true;
	}

	@Override
	public Connection put(String msg) {
		return server.accept(msg);
	}

	@Override
	public void onReply(BiConsumer<Connection, String> callback) {
	}

}
