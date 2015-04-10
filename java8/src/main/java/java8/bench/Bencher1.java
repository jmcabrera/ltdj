package java8.bench;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface Bencher1 {

  default <D> Stat bench(int nb, Consumer<D> task, Collection<D> data) {
    System.out.println("Warmup");
    loop(data, task);

    Stat stat = new Stat();

    repeat(nb, i -> {
      if (i % 100 == 0) System.out.print("#");
      loop(data, d -> {
        long t = System.nanoTime();
        task.accept(d);
        stat.add(System.nanoTime() - t);
      });
    });
    System.out.println();

    return stat;
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
