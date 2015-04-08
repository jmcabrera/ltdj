/**
 * 
 */
package java8.future;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
public class Future<T> {

	private Supplier<T>	supplier;

	/**
	 * @param supplier
	 */
	private Future(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	public static final <T> Future<T> of(Supplier<T> supplier) {
		return new Future<T>(supplier);
	}

	public T run() {
		return supplier.get();
	}

	public <V> Future<V> andThen(Function<T, V> transform) {
		return Future.of(() -> transform.apply(supplier.get()));
	}

}
