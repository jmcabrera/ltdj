/**
 * 
 */
package java8;

import static org.junit.Assert.*;
import java.util.Base64;

import org.junit.Test;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
public class Base64Test {

	@Test
	public void testEncoder() {
		String b64 = Base64.getEncoder().encodeToString("This has been decoded from Base 64".getBytes());
		assertEquals("VGhpcyBoYXMgYmVlbiBkZWNvZGVkIGZyb20gQmFzZSA2NA==", b64);
	}

	@Test
	public void testDecoder() {
		String raw = new String(Base64.getDecoder().decode("SGVsbG8="));
		assertEquals("Hello", raw);
	}

}
