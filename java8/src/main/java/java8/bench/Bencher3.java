/**
 * 
 */
package java8.bench;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
public interface Bencher3 {

	default <D> Stat bench(int nb, Consumer<D> task, Collection<D> data) {
		System.out.println("Warmup");
		loop(data, task);

		Stat stat = new Stat();

		repeat(nb, i -> {
			System.out.print(".");
			loop(data, d -> stat.add(time(() -> task.accept(d))));
		});
		System.out.println();

		return stat;
	}

	default Long time(Runnable task) {
		long t = System.nanoTime();
		task.run();
		return System.nanoTime() - t;
	}

	default <D> void loop(Collection<D> data, Consumer<D> f) {
		for (D d : data) {
			f.accept(d);
		}
	}

	default void repeat(int nb, IntConsumer f) {
		for (int i = 0; i < nb; i++) {
			f.accept(i);
		}
	}
}
