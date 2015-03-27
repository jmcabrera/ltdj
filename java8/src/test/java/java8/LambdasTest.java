/**
 * 
 */
package java8;

import static java.util.Arrays.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
public class LambdasTest {

	@Test
	public void listIteration() {
		List<Integer> ints = new ArrayList<>(asList(1, 2, 3, 4, 5, 6));

		List<Integer> got = new ArrayList<>();
		ints.forEach(p -> got.add(p));

		List<Integer> exp = ints;
		assertEquals(exp, got);
	}

	@Test
	public void filterOdds() {
		List<Integer> ints = new ArrayList<>(asList(1, 2, 3, 4, 5, 6));

		List<Integer> got = new ArrayList<>();
		ints.removeIf(p -> p % 2 == 1);
		ints.forEach(p -> got.add(p));

		List<Integer> exp = new ArrayList<>(asList(2, 4, 6));
		assertEquals(exp, got);
	}

}
