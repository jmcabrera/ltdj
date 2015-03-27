/**
 * 
 */
package java8.time;

import java.util.function.Consumer;
import java.util.function.Predicate;

interface Time {

	void plan(Predicate<Long> when, Consumer<Long> what);

	default long now() {
		return System.nanoTime();
	}

	default void repeat(long delay, Consumer<Long> what) {
		plan(l -> now() % delay == 0, //
				l -> what.accept(l));
	}

	default void callOn(long when, Consumer<Long> what) {
		plan(l -> l == when, what);
	}
}