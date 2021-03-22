/**
 * Computational-Genomics 
 * Part 3 - Multiple sequence alignment.
 */

package clustering;
import java.util.Collection;

public interface LinkageStrategy {

	public Distance calculateDistance(Collection<Distance> distances);
}
