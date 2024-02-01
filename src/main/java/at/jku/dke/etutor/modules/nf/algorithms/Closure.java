package at.jku.dke.etutor.modules.nf.algorithms;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.text.Collator;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class Closure {

	private Closure() {
		// This class is not meant to be instantiated (Gerald Wimmer, 2024-01-01).
	}

	/**
	 * Returns the attribute closure for the passed attributes given the passed dependencies
	 * @param attributes The <code>Collection</code> of attributes whose closure is to be determined.
	 * @param dependencies The <code>Collection</code> of <code>FunctionalDependency</code> objects based on which the
	 *                        closure is to be determined.
	 * @return The attribute closure for the passed attributes given the passed dependencies
	 */
	public static Set<String> execute(Collection<String> attributes, Collection<FunctionalDependency> dependencies) {
		Set<String> closure = new TreeSet<>(Collator.getInstance());
		closure.addAll(attributes);

		Set<FunctionalDependency> remainingDependencies = new HashSet<>(dependencies);

		// While there are any functional dependencies suitable for extending the closure ... (Gerald Wimmer, 2024-01-11)
		while (remainingDependencies.stream().anyMatch(fd -> closure.containsAll(fd.getLhsAttributes()))) {
			// Extend the closure and remove the utilized functional dependency (Gerald Wimmer, 2024-01-11)
			Iterator<FunctionalDependency> dependencyIterator = remainingDependencies.iterator();
			while(dependencyIterator.hasNext()) {
				FunctionalDependency dependency = dependencyIterator.next();

				if(closure.containsAll(dependency.getLhsAttributes())) {
					closure.addAll(dependency.getRhsAttributes());
					dependencyIterator.remove();
				}
			}
		}

		return closure;
	}
}