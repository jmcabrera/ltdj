/**
 * 
 */
package java8.time;

import java.util.function.Consumer;
import java.util.function.Predicate;

class TestTime implements Time {

	@Override
	public long now() {
		return time;
	}

	@Override
	public void plan(Predicate<Long> when, Consumer<Long> what) {
		calendar = calendar.andThen(l -> {
			if (when.test(l)) what.accept(l);
		});
	}

	long						time			= 0;

	Consumer<Long>	calendar	= l -> {};

	public void tick() {
		calendar.accept(++time);
	}

	public static void main(String[] args) {
		passing();

		infinite();
	}

	private static void passing() {
		TestTime tt = new TestTime();
		tt.callOn(5, l -> System.out.println("Wake up, it's " + l));
		tt.repeat(4, l -> System.out.println(l + ": 4 seconds have lapsed"));

		for (int i = 0; i < 20; i++) {
			tt.tick();
		}
	}

	private static void infinite() {
		TestTime tt = new TestTime();
		tt.callOn(5, l -> System.out.println("Wake up, it's " + l));
		tt.repeat(1, l -> {});
		tt.repeat(1000000, System.out::println);

		while (true) {
			tt.tick();
		}
	}
}