/**
 * 
 */
package java8.message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
public class Network {

	static final Map<String, Server>	dns	= new HashMap<>();

	public static Connection open(String name) {
		Server server = dns.get(name);
		if (null == server) {
			throw new RuntimeException("name unbound");
		}
		return server.open();
	}

	public static void register(String name, Server server) {
		if (null != dns.get(name)) {
			throw new RuntimeException("name unavailable");
		}
		dns.put(name, server);
	}

}
