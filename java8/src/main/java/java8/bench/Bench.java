/**
 * 
 */
package java8.bench;

import java.util.Collection;
import java.util.function.Function;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
@FunctionalInterface
public interface Bench<T> extends Function<Collection<T>, Stat> {

}
