/**
 * 
 */
package java8.bench;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
public class Bencher0Test implements Patterns {

  @Test
  public void bench() {
    Bencher0 bencher = new Bencher0();

    Consumer<String> task = data -> {
      for (Pattern p : PATTERNS) {
        p.matcher(data).matches();
      }
    };

    Stat abc = bencher.bench(1000, task, Arrays.asList("aaaaa", "bbbbb", "ccccc"));
    System.out.println(abc);

    Stat def = bencher.bench(1000, task, Arrays.asList("ddddd", "eeeee", "fffff"));
    System.out.println(def);

  }

}
