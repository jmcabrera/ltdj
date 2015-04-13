/**
 * 
 */
package java8.bench;

import static java.lang.String.*;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLongArray;

public final class Stat {

  /**
   * 
   */
  private static final double   NANO_TO_MILLIS = 1_000_000.0;

  private long                  n;

  private double                mean, var, m2, delta, tot, min = Long.MAX_VALUE, max = Long.MIN_VALUE;

  private final AtomicLongArray values;

  private final AtomicInteger   idx            = new AtomicInteger(0);

  private double                apparent;

  private long[]                avalues;

  /**
   * @param i
   */
  public Stat(int length) {
    values = new AtomicLongArray(length);
  }

  public void add(long value) {
    values.lazySet(idx.getAndIncrement(), value);
  }

  public Stat consolidate() {
    avalues = new long[values.length()];
    double valms;
    for (int i = 0; i < values.length(); i++) {
      avalues[i] = values.get(i);
      valms = avalues[i] / NANO_TO_MILLIS;
      min = Double.min(min, valms);
      max = Double.max(max, valms);
      n++;
      tot += valms;
      delta = valms - mean;
      mean = mean + delta / n;
      m2 = m2 + delta * (valms - mean);
      var = n < 2 ? 0 : m2 / (n - 1);
    }
    Arrays.sort(avalues);
    return this;
  }

  public void setApparent(long apparent) {
    this.apparent = apparent / NANO_TO_MILLIS;
  }

  public synchronized double getApparent() {
    return apparent;
  }

  public double getMean() {
    return mean;
  }

  public double getVar() {
    return var;
  }

  public double getTotal() {
    return tot;
  }

  public long getNumber() {
    return n;
  }

  public double getParallelism() {
    return tot / apparent;
  }

  public double getPercentile(double p) {
    double pos = p * (n + 1) / 100.0;
    if (pos < 1) {
      return min;
    }
    if (pos >= n) {
      return max;
    }
    double fpos = Math.floor(pos);
    int intPos = (int) fpos;
    double dif = pos - fpos;
    double lower = values.get(intPos - 1);
    double upper = values.get(intPos);
    return (lower + dif * (upper - lower)) / NANO_TO_MILLIS;
  }

  @Override
  public String toString() {
    double p95 = getPercentile(95);
    double p99 = getPercentile(99);
    double p999 = getPercentile(99.9);
    double p9999 = getPercentile(99.99);
    double p99999 = getPercentile(99.999);
    double stddev = Math.sqrt(var);
    return join("\n", //
        format("nb         : %,11d", n), //
        format("mean       : %#,15.3f ms (%#,2.3f %% of total)", mean, mean / tot * 100), //
        format("stddev     : %#,15.3f ms", stddev), //
        format("p1   (min) : %#,15.3f ms (%#,5.3f %% of total)", min, min / tot * 100), //
        format("p95        : %#,15.3f ms (%#,5.3f %% of total)", p95, p95 / tot * 100), //
        format("p99        : %#,15.3f ms (%#,5.3f %% of total)", p99, p99 / tot * 100), //
        format("p99.9      : %#,15.3f ms (%#,5.3f %% of total)", p999, p999 / tot * 100), //
        format("p99.99     : %#,15.3f ms (%#,5.3f %% of total)", p9999, p9999 / tot * 100), //
        format("p99.999    : %#,15.3f ms (%#,5.3f %% of total)", p99999, p99999 / tot * 100), //
        format("p100 (max) : %#,15.3f ms (%#,5.3f %% of total)", max, max / tot * 100), //
        format("total      : %#,15.3f ms", tot), //
        format("apparent   : %#,15.3f ms", apparent), //
        format("parallelism: %#,15.3f", tot / apparent), //
        "");
  }

  public double getMin() {
    return min;
  }

  public double getMax() {
    return max;
  }

}