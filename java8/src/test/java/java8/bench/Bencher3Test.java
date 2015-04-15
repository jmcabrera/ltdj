/**
 * 
 */
package java8.bench;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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

    Map<String, Function<Stat, Number>> format = new LinkedHashMap<>();
    format.put("nb", s -> s.getNumber());
    format.put("apparent_ms", s -> s.getApparent());
    format.put("total_ms", s -> s.getTotal());
    format.put("mean_ms", s -> s.getMean());
    format.put("stddev_ms", s -> Math.sqrt(s.getVar()));
    for (int i = 0; i < 100; i += 10) {
      final int j = i;
      format.put("p" + i, s -> s.getPercentile(j));
    }
    format.put("p99.9", s -> s.getPercentile(99.9));
    format.put("p99.99", s -> s.getPercentile(99.99));
    format.put("p99.999", s -> s.getPercentile(99.999));

    System.out.println("nb_threads " + String.join(" ", format.keySet()));
    for (int i = 1; i <= 16; i++) {
      Stat stats = runInParallel(dataset.get("abc"), i);
      System.out.println(i + " " + String.join( //
          " ", //
          format.values().stream() //
              .map(f -> "" + f.apply(stats)) //
              .collect(Collectors.toList())));
    }
    {
      Stat stats = runInParallel2(dataset.get("abc"));
      System.out.println("FJP " + String.join( //
          " ", //
          format.values().stream() //
              .map(f -> "" + f.apply(stats)) //
              .collect(Collectors.toList())));
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
