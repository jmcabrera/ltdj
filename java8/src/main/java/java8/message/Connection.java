/**
 * 
 */
package java8.message;

import java.util.function.BiConsumer;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
public interface Connection {

	void close();

	/**
	 * @param string
	 * @return
	 */
	Connection put(String string);

	/**
	 * 
	 */
	void onReply(BiConsumer<Connection, String> callback);

}
