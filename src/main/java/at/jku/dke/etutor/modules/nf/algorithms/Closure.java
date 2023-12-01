package at.jku.dke.etutor.modules.nf.algorithms;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.text.Collator;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class Closure {

	public static TreeSet<String> execute(Collection<String> attributes, Collection<FunctionalDependency> dependencies){
		FunctionalDependency dependency;

		boolean again;
		boolean containsAllLHSAttributes;
		boolean containsAllRHSAttributes;

		again = true;
		TreeSet<String> closure = new TreeSet<>(Collator.getInstance());
		closure.addAll(attributes);
		
		while (again) {
			Iterator<FunctionalDependency> dependenciesIterator = dependencies.iterator();
			while (dependenciesIterator.hasNext()) {
				dependency = dependenciesIterator.next();
				containsAllLHSAttributes = closure.containsAll(dependency.getLhsAttributes());
				
				if (containsAllLHSAttributes) {
					closure.addAll(dependency.getRhsAttributes());
				}
			}

			again = false;
			dependenciesIterator = dependencies.iterator();
			while (dependenciesIterator.hasNext()) {
				dependency = dependenciesIterator.next();
				containsAllLHSAttributes = closure.containsAll(dependency.getLhsAttributes());
				containsAllRHSAttributes = closure.containsAll(dependency.getRhsAttributes());
				
				if ((!containsAllRHSAttributes) && (containsAllLHSAttributes)){
					again = true;
				}
			}
		}
		return closure;
	}
}