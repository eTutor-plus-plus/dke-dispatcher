package at.jku.dke.etutor.modules.nf.algorithms;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.text.Collator;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class Closure {

	/**
	 * Returns the attribute closure for the passed attributes given the passed dependencies
	 * @param attributes The <code>Collection</code> of attributes whose closure is to be determined.
	 * @param dependencies The <code>Collection</code> of <code>FunctionalDependency</code> objects based on which the
	 *                        closure is to be determined.
	 * @return The attribute closure for the passed attributes given the passed dependencies
	 */
	public static Set<String> execute(Collection<String> attributes, Collection<FunctionalDependency> dependencies) {
		boolean again = true;
		Set<String> closure = new TreeSet<>(Collator.getInstance());
		closure.addAll(attributes);
		
		while (again) {
			for (FunctionalDependency dependency : dependencies) {
				boolean containsAllLHSAttributes = closure.containsAll(dependency.getLhsAttributes());
				
				if (containsAllLHSAttributes) {
					closure.addAll(dependency.getRhsAttributes());
				}
			}

			again = false;
			for (FunctionalDependency dependency : dependencies) {
				boolean containsAllLHSAttributes = closure.containsAll(dependency.getLhsAttributes());
				boolean containsAllRHSAttributes = closure.containsAll(dependency.getRhsAttributes());
				
				if ((!containsAllRHSAttributes) && (containsAllLHSAttributes)){
					again = true;
				}
			}
		}
		return closure;
	}
}