/**
 * 
 */
package java8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
public class SimpleSplitter {

	static class Router<S> {

		private AtomicReference<Function<S, S>>	routerRef	= new AtomicReference<>(s -> s);

		private AtomicBoolean										stop			= new AtomicBoolean(false);

		public Stream<S> select(final String name, Function<S, Boolean> selector) {

			// The dedicated queue for the selector
			final BlockingQueue<S> routeeQueue = new ArrayBlockingQueue<>(10);

			// The piece of router, from the given selector.
			// Anything matching the given selector ends up in its dedicated queue.
			final Function<S, S> op = s -> {
				if (!stop.get() && selector.apply(s)) {
					int countdown = 10;
					while (0 < countdown--) {
						try {
							routeeQueue.put(s);
							if (9 != countdown) {
								System.out.println("[" + name + "]: Retried " + (10 - countdown) + " before dispatching");
							}
							return s;
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
					System.out.println("[" + name + "]: Could not dispatch");
				}
				return s;
			};

			// Composing the new router
			System.out.println("[" + name + "]: Mixing up");
			routerRef.getAndUpdate(x -> x.compose(op));

			return Stream.generate(() -> {
				while (true) {
					System.out.println("[" + name + "]: Waiting next");
					try {
						return routeeQueue.take();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		void close() {
			System.out.println("Closing router");
			routerRef.getAndUpdate(x -> s -> s);
			stop.set(true);
		}

		Stream<S> plug(Stream<S> producer) {
			return producer.onClose(this::close).map(routerRef.get()::apply);
		}

		void consume(Stream<S> producer) {
			plug(producer).forEach(s -> System.out.println("Routed " + s));
		}
	}

	public static void main(String[] args) {

		ExecutorService es = Executors.newSingleThreadExecutor();

		Router<String> router = new Router<>();

		Stream<String> _all = Stream.empty();
		for (char c = 'a'; c <= 'z'; c++) {
			final String cs = new String("" + c);
			Stream<String> st = router.select(cs, s -> s.contains(cs)).map(s -> {
				System.out.println("[" + cs + "]: Got " + s);
				return s;
			});
			_all = Stream.concat(_all, st);
		}

		Stream<String> all = _all;

		es.submit(() -> all.forEach(s -> {})); // Pumping outputs

		// es.submit(() -> router //
		// .select(s -> {
		// boolean matches = "quit".equals(s);
		// System.out.println(s + ": " + matches);
		// return matches;
		// }) //
		// .forEach(s -> router.close()) //
		// );
		// es.shutdown();

		// from(Arrays.asList("abc", "bbb", "aac"), router);

		fromConsole(router);

		try {
			es.awaitTermination(1, TimeUnit.DAYS);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	// private static void from(List<String> source, Router<String> router) {
	// Stream<String> stream = source.stream();
	// router.consume(stream);
	// stream.close();
	// }

	private static void fromConsole(Router<String> router) {
		System.out.println("Listening to you");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			Stream<String> st = Stream.generate(() -> {
				try {
					return br.readLine();
				}
				catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			});

			router.consume(st);
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
