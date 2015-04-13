/**
 * 
 */
package java8.bench;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
public class Bencher3Test implements Patterns {

  private final Map<String, Collection<String>> dataset = new HashMap<>();

  @Before
  public void init() {
    System.out.println("######################################");
    dataset.put("abc", Arrays.asList("aaaaa", "bbbbb", "ccccc"));
    dataset.put("def", Arrays.asList("ddddd", "eeeee", "fffff"));
  }

  @Test
  public void bench() throws InterruptedException {
    Bencher3 bencher = new Bencher3() {};
    for (Map.Entry<String, Collection<String>> e : dataset.entrySet()) {
      Stat stats = run(e.getValue(), bencher);
      System.out.println("For " + e.getKey() + ":");
      System.out.println(stats);
      System.out.println("......................................");
    }
  }

  @Test
  public void parallelBench() {
    System.out.println("Repeat in //");
    for (Map.Entry<String, Collection<String>> e : dataset.entrySet()) {
      Stat stats = runInParallel(e.getValue(), 8);
      System.out.println("For " + e.getKey() + ":");
      System.out.println(stats);
      System.out.println("......................................");
    }
  }

  @Test
  public void compareNbThreads() {
    System.out
        .println(" nb_threads number     apparent_(ms)   total_(ms)      parallelism loss        mean_(ms)       stddev_(ms)     min_(ms)        p999_(ms)       p9999_(ms)      p99999_(ms)     max_(ms) ");
    for (int i = 1; i <= 24; i++) {
      Stat stats = runInParallel(dataset.get("abc"), i);
      System.out.printf("%10d %10d %#15.3f %#15.3f %#11.3f %#11.3f %#15.3f %#15.3f %#15.3f %#15.3f %#15.3f %#15.3f %#15.3f\n", //
          i, //
          stats.getNumber(), //
          stats.getApparent(), //
          stats.getTotal(), //
          stats.getParallelism(), //
          i - stats.getParallelism(), //
          stats.getMean(), //
          Math.sqrt(stats.getVar()), //
          stats.getMin(), //
          stats.getPercentile(99.9), //
          stats.getPercentile(99.99), //
          stats.getPercentile(99.999), //
          stats.getMax() //
          );
    }
    {
      Stat stats = runInParallel2(dataset.get("abc"));
      System.out.printf(" (unknown)  %10d %#15.3f %#15.3f %#11.3f %#11.3f %#15.3f %#15.3f %#15.3f %#15.3f %#15.3f %#15.3f %#15.3f\n", //
          stats.getNumber(), //
          stats.getApparent(), //
          stats.getTotal(), //
          stats.getParallelism(), //
          Double.NaN,//
          stats.getMean(), //
          Math.sqrt(stats.getVar()), //
          stats.getMin(), //
          stats.getPercentile(99), //
          stats.getPercentile(99.9), //
          stats.getPercentile(99.99), //
          stats.getMax() //
          );
    }
  }

  private static Stat run(Collection<String> data, Bencher3 bencher) {
    Function<Collection<String>, Stat> bench = //
    bencher.bench(//
        500_000, //
        d -> {
          for (Pattern p : PATTERNS) {
            p.matcher(d).matches();
          }
        });

    return bench.apply(data);
  }

  private static Stat runInParallel(Collection<String> data, int nbThreads) {
    ExecutorService es = Executors.newFixedThreadPool(nbThreads);
    Bencher3 bencher = new Bencher3() {
      @Override
      public void repeat(int nb, IntConsumer f) {
        final AtomicInteger aj = new AtomicInteger(0);
        Future<?>[] futures = new Future[nbThreads];
        for (int i = 0; i < futures.length; i++)
          futures[i] = es.submit(() -> {
            int j;
            while ((j = aj.getAndIncrement()) < nb) {
              f.accept(j);
            };
          });

        for (Future<?> future : futures)
          try {
            future.get();
          }
          catch (Exception e) {
            e.printStackTrace();
          }
      }
    };
    Stat stat = run(data, bencher);
    if (!es.shutdownNow().isEmpty()) {
      System.err.println("Thread leak !!!");
    }
    System.gc();
    return stat;
  }

  private static Stat runInParallel2(Collection<String> data) {
    Bencher3 bencher = new Bencher3() {
      @Override
      public void repeat(int nb, IntConsumer f) {
        IntStream.range(0, nb).parallel().forEach(f::accept);
      }
    };
    Stat stat = run(data, bencher);
    System.gc();
    return stat;
  }

}
