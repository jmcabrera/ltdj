/**
 * 
 */
package java8.bench;

public final class Stat {

	private long	n;

	private double	mean, var, m2, delta, tot, min = Long.MAX_VALUE, max = Long.MIN_VALUE;

	public void add(long value) {
		double valms = value / 1_000_000.0;
		min = Double.min(min, valms);
		max = Double.max(max, valms);
		n++;
		tot += valms;
		delta = valms - mean;
		mean = mean + delta / n;
		m2 = m2 + delta * (valms - mean);
		var = n < 2 ? 0 : m2 / (n - 1);
	}

	public double getMean() {
		return mean;
	}

	public double getVar() {
		return var;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder() //
				.append("nb    : ").append(String.format("%,11d", n)).append("\n") //
				.append("mean  : ").append(String.format("%#,15.3f", mean)).append(" ms\n") //
				.append("stddev: ").append(String.format("%#,15.3f", Math.sqrt(var))).append(" ms\n") //
				.append("min   : ").append(String.format("%#,15.3f", min)).append(" ms\n") //
				.append("max   : ").append(String.format("%#,15.3f", max)).append(" ms\n") //
				.append("total : ").append(String.format("%#,15.3f", tot)).append(" ms\n") //
		;
		return sb.toString();
	}
}