/**
 * 
 */
package java8;

import static java.util.Arrays.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
public class SimpleCalc {

	public static enum Command {
		Default {
			@Override
			public boolean canRender(String s) {
				return false;
			}

			@Override
			public String render(String s) {
				return "Cannot render " + s;
			}
		}, //
		Quit {
			private static final String	PREFIX	= "q";

			@Override
			public boolean canRender(String s) {
				return s.equalsIgnoreCase(PREFIX);
			}

			@Override
			public String render(String s) {
				return "Cannot render " + s;
			}
		}, //
		Add {
			private static final String	PREFIX	= "+ ";

			@Override
			public boolean canRender(String s) {
				return s.startsWith(PREFIX);
			}

			@Override
			public String render(String s) {
				s = s.substring(PREFIX.length());
				return "=" + asList(s.split(" ")) //
						.stream() //
						.mapToInt(Integer::parseInt) //
						.sum();
			}
		}, //
		Mult {
			private static final String	PREFIX	= "* ";

			@Override
			public boolean canRender(String s) {
				return s.startsWith(PREFIX);
			}

			@Override
			public String render(String s) {
				s = s.substring(PREFIX.length());
				return "=" + asList(s.split(" ")) //
						.stream() //
						.mapToInt(Integer::parseInt) //
						.reduce((a, b) -> a * b).getAsInt(); //
			}
		};

		public abstract String render(String s);

		public abstract boolean canRender(String s);

		public static Command get(String s) {
			for (Command c : values())
				if (c.canRender(s)) return c;
			return Default;
		}
	}

	public static void main(String[] args) {
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

			plug(st).forEach(System.out::println);
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("End");

	}

	public static final Stream<String> plug(Stream<String> st) {
		return st.map(s -> Command.get(s).render(s));
	}

}
