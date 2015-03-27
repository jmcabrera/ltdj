/**
 * 
 */
package java8;

import static org.junit.Assert.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.Test;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
public class DateAPITest {

	@Test
	public void instants() {
		Instant now = Instant.now();

		System.out.println("Now is: " + now);
		System.out.printf("Now is: %d s and %d nanos after Epoch%n", now.getEpochSecond(), now.getNano());

		Instant t0 = Instant.parse("2015-03-26T10:15:30.00Z");

		// Both lines are equivalent.
		Instant t1 = t0.minusSeconds(10);
		Instant t2 = t0.minus(10, ChronoUnit.SECONDS);
		assertEquals(t1, t2);
		System.out.println(t1 + " == " + t2);

		assertTrue(t0.isAfter(t1));
		assertTrue(t1.isBefore(t0));
	}

	@Test
	public void durations() {
		Duration d0 = Duration.parse("P1DT1H5M2.5S");

		Duration d1 = d0.minus(Duration.parse("PT30M"));

		assertEquals(Duration.ofMinutes(30), d0.minus(d1));

		System.out.println("d0 is " + d0 + " and d1 is " + d1);

		Instant now = Instant.now();

		Instant later = now.plus(d0);

		assertEquals(later.minus(d0), now);
	}

	@Test
	public void localTimes() {
		// // Temps UTC
		// System.out.println("UTC time:  " + LocalDateTime.now(Clock.systemUTC()));
		//
		// // Temps dans la zone système (Temps "wallclock")
		// System.out.println("wallclock: " + LocalDateTime.now());

		System.out.println();
		ZonedDateTime winter = LocalDateTime.of(2015, 3, 29, 1, 59).atZone(ZoneId.systemDefault());
		System.out.println(winter);
		System.out.println(winter.plusMinutes(1));
		System.out.println();

		ZonedDateTime summer = LocalDateTime.of(2015, 10, 25, 2, 59).atZone(ZoneId.systemDefault());
		System.out.println(summer);
		System.out.println(summer.plusMinutes(1));
		System.out.println();

		LocalDateTime local = LocalDateTime.of(2015, 10, 25, 2, 59);
		System.out.println(local);
		System.out.println(local.plusMinutes(1));
		System.out.println(local.plusMinutes(1).atZone(ZoneId.systemDefault()));
	}
}
