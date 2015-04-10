/**
 * 
 */
package java8.bench;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
public interface Patterns {

  public static final Pattern[] PATTERNS = buildPatterns();

  static Pattern[] buildPatterns() {
    List<Pattern> _pat = new ArrayList<>();
    for (char c = 'a'; c <= 'z'; c++) {
      _pat.add(Pattern.compile(".*" + c + ".*"));
      _pat.add(Pattern.compile(".*" + c + c + ".*"));
    }
    return _pat.toArray(new Pattern[0]);
  }

}
