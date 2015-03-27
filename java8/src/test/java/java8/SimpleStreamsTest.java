/**
 * 
 */
package java8;

import static java.util.Arrays.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java8.SimpleCalc.Command;

import org.junit.Test;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
public class SimpleStreamsTest {

	@Test
	public void incStream() {

		Stream<String> strings = Arrays.asList("Hello", "How to use", "Duck").stream();
		strings //
				.filter(s -> s.contains("H")) //
				.map(s -> s + " Lambda") //
				.forEach(s -> System.out.println(s));

		Stream<Integer> ints = Stream.iterate(1, x -> x + 1).limit(6);

		Runnable f = () -> {};

		Supplier<String> g = () -> "Hello";

		List<Integer> got = ints.filter(p -> p % 2 == 0).collect(Collectors.toList());

		List<Integer> exp = new ArrayList<>(asList(2, 4, 6));
		assertEquals(exp, got);
	}

	@Test
	public void flatMapStream() {
		Stream<Integer> ints = Stream.iterate(2, x -> x + 1).limit(6);

		ints.flatMap(p -> Stream.iterate(p, x -> p * x).limit(5)).forEach(System.out::println);

		// List<Integer> exp = new ArrayList<>(asList(2, 4, 6));
		// assertEquals(exp, got);
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
