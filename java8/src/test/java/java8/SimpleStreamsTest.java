/**
 * 
 */
package java8;

import static java.util.Arrays.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java8.SimpleCalc.Command;

import org.junit.Test;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
public class SimpleStreamsTest {

  @Test
  public void basiStreamOps() {

    Stream<String> strings = Arrays.asList("Hello", "How to use", "Duck").stream();
    List<String> out = strings //
        .filter(s -> s.contains("H")) //
        .map(s -> s + " Lambdas") //
        .collect(Collectors.toList());

    assertEquals(out, asList("Hello Lambdas", "How to use Lambdas"));
  }

  @Test
  public void basicStreamOps2() {

    IntStream ints = IntStream.range(1, 7);

    List<Integer> got = ints //
        .filter(p -> p % 2 == 0) //
        .collect( //
            ArrayList<Integer>::new, //
            (l, v) -> l.add(v), //
            (a, b) -> a.addAll(b) //
        );

    List<Integer> exp = new ArrayList<>(asList(2, 4, 6));
    assertEquals(exp, got);
  }

  @Test
  public void flatMapStream() {

    IntStream ints = IntStream.range(2, 5);

    List<Integer> got = ints //
        .flatMap(p -> IntStream.iterate(p, x -> p * x).limit(3)) //
        .collect( //
            ArrayList<Integer>::new, //
            (l, v) -> l.add(v), //
            (a, b) -> a.addAll(b) //
        );

    List<Integer> exp = new ArrayList<>(asList( //
        2, 4, 8, // 2 , 4 = 2*2, 8 = 2*2*2
        3, 9, 27, // 3 , 9 = 3*3, 27 = 3*3*3
        4, 16, 64 // 4 , 16 = 4*4, 64 = 4*4*4
        ));

    assertEquals(exp, got);
  }

  @Test
  public void calculator() throws InterruptedException {
    List<String> in = new ArrayList<>();
    List<String> exp = new ArrayList<>();
    {
      in.add("a");
      exp.add(Command.Default.render("a"));
      in.add("+ 1 2 3");
      exp.add(Command.Add.render("+ 1 2 3"));
      in.add("* 8 5 2");
      exp.add(Command.Mult.render("* 8 5 2"));
    }

    Stream<String> st = in.stream();

    Stream<String> calc = SimpleCalc.plug(st);

    List<String> out = calc.collect(Collectors.toList());

    assertEquals(exp, out);
  }
}
