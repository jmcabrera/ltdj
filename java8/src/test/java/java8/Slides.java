/**
 * 
 */
package java8;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Slides {

	public static void main(String[] args) throws Exception {
		new Slides().slide8();
	}

	static class A {
		static String m(String s) {
			return String.join(" ", "Got ", s);
		}

		long add(int a, int b) {
			System.out.println(this);
			return (long) (a + b);
		}
	}

	private void slide8() {
		Function<String, String> m0 = A::m;

		BiFunction<Integer, Integer, Long> m1 = new A()::add;

		System.out.println(m0.apply("Hello"));

		System.out.println(m1.apply(1, 2));
	}

	public void slide7() throws InterruptedException, ExecutionException {
		IntStream ints = IntStream.generate(new Random()::nextInt).limit(30).parallel();

		ForkJoinPool pool = ForkJoinPool.commonPool();
		int[] values = ints.toArray(); // the values contained in the stream
		class MyTask extends RecursiveTask<Void> {
			private final int[]	values;
			private final int		lo, hi;

			public MyTask(int[] values) {
				this(values, 0, values.length);
			}

			private MyTask(int[] values, int lo, int hi) {
				this.values = values;
				this.lo = lo;
				this.hi = hi;
			}

			@Override
			protected Void compute() {
				if (hi - lo < 10) execute();
				else {
					int mid = lo + (hi - lo) / 2;
					MyTask left = new MyTask(values, lo, mid);
					MyTask right = new MyTask(values, mid, hi);
					left.fork();
					right.compute();
					left.join();
				}
				return null;
			}

			private void execute() {
				int i, j;
				for (int k = lo; k < hi; k++) { // forEach
					i = values[k];
					if (0 == i % 2) { // filter
						j = i * i; // map
						System.out.println(j);
					}
				}
			}
		};
		pool.submit(new MyTask(values)).get();
		Thread.sleep(1000);
	}

	public void slide6() {
		List<Integer> ints = Arrays.asList(1, 2, 3);

		int j;
		for (int i : ints) { // forEach
			if (0 == i % 2) { // filter
				j = i * i; // map
				System.out.println(j);
			}
		}

	}

	public void slide5() {
		List<Integer> ints = Arrays.asList(1, 2, 3);

		Stream<Integer> stream = ints.stream();

		stream.forEach(System.out::println);
		stream.forEach(i -> System.out.println(i));

	}

	public void slide4() {
		LongStream longs = LongStream.iterate(1L, x -> x + 1);

		IntStream rands = IntStream.generate(new Random()::nextInt).parallel();

	}

	public void slide3() {
		List<Integer> ints = Arrays.asList(1, 2, 3);

		Stream<Integer> stream = ints.stream();
		stream //
				.filter(i -> 0 == 1 % 2) //
				.map(i -> i * i) //
				.forEach(System.out::println);

	}

	@FunctionalInterface
	static interface MyLambda<A, B, C> {
		C consume(A a, B b);
	}

	public void slide2() {
		MyLambda<Integer, Boolean, String> f = (i, b) -> "Got " + i + " and " + b;
	}

	public void slide2bis() {
		MyLambda<Integer, Boolean, String> f = new MyLambda<Integer, Boolean, String>() {
			public String consume(Integer a, Boolean b) {
				return "Got " + a + " and " + b;
			}
		};
	}

	public void slide1() {
		Function<Integer, Boolean> f0 = p -> p % 2 == 0;

		Function<Integer, Boolean> f1 = p -> {
			return p % 2 == 0;
		};

		Consumer<String> c = s -> {/* does something with the string */};

		Supplier<String> s = () -> "Hello";

		Runnable r = () -> {/* does something */};
	}

}
