package java8.bench;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface Bencher3 {

  default <D> Bench<D> bench(int nb, Consumer<D> task) {
    return data -> {
      repeat(nb / 10, i -> {
        loop(data, task);
      });

      System.gc();

      Stat stat = new Stat(nb * data.size());

      long apparent = time(() -> {
        repeat(nb, i -> {
          loop(data, d -> stat.add(time(() -> task.accept(d))));
        });
      });

      stat.setApparent(apparent);
      return stat.consolidate();
    };
  }

  default long time(Runnable task) {
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
