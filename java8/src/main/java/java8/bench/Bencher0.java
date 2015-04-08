/**
 * 
 */
package java8.bench;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
public class Bencher0 {

	public <D> Stat bench(int nb, Consumer<D> task, Collection<D> data) {
		System.out.println("Warmup");
		for (D d : data) {
			task.accept(d);
		}

		Stat stat = new Stat();

		for (int i = 0; i < nb; i++) {
			System.out.print(".");
			for (D d : data) {
				long t = System.nanoTime();
				task.accept(d);
				stat.add(System.nanoTime() - t);
			}
		}
		System.out.println();

		return stat;
	}
}