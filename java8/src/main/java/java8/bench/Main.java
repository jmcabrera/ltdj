/**
 * 
 */
package java8.bench;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.regex.Pattern;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
public class Main {

	private static final Pattern[]	pat;
	static {
		{
			List<Pattern> _pat = new ArrayList<>();
			for (char c = 'a'; c <= 'z'; c++) {
				_pat.add(Pattern.compile(".*" + c + ".*"));
				_pat.add(Pattern.compile(".*" + c + c + ".*"));
			}
			pat = _pat.toArray(new Pattern[0]);
		}
	}

	public static void main(String[] args) {
		Bencher4 bench = new Bencher4() {

			@Override
			public <D> void loop(Collection<D> data, Consumer<D> f) {
				data.stream().parallel().forEach(f);
			}

		};

		Function<Collection<String>, Stat> b = bench.bench(1000, //
				data -> {
					for (Pattern p : pat) {
						p.matcher(data).matches();
					}
				});

		System.out.println(b.apply(Arrays.asList("aaaaa", "bbbbb", "ccccc")));

		System.out.println(b.apply(Arrays.asList("ddddd", "eeeee", "fffff")));
	}
}
