package at.jku.dke.etutor.modules.nf.algorithms;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.text.Collator;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class Closure {

	public static TreeSet execute(Collection attributes, Collection dependencies){
		TreeSet closure;
		Iterator attributesIterator;
		Iterator dependenciesIterator;
		FunctionalDependency dependency;

		boolean again;
		boolean containsAllLHSAttributes;
		boolean containsAllRHSAttributes;

		again = true;
		closure = new TreeSet(Collator.getInstance());
		closure.addAll(attributes);
		
		while (again) {
			dependenciesIterator = dependencies.iterator();
			while (dependenciesIterator.hasNext()) {
				dependency = (FunctionalDependency)dependenciesIterator.next();
				containsAllLHSAttributes = closure.containsAll(dependency.getLHSAttributes());
				
				if (containsAllLHSAttributes) {
					closure.addAll(dependency.getRHSAttributes());
				}
			}

			again = false;
			dependenciesIterator = dependencies.iterator();
			while (dependenciesIterator.hasNext()) {
				dependency = (FunctionalDependency)dependenciesIterator.next();
				containsAllLHSAttributes = closure.containsAll(dependency.getLHSAttributes());
				containsAllRHSAttributes = closure.containsAll(dependency.getRHSAttributes());
				
				if ((!containsAllRHSAttributes) && (containsAllLHSAttributes)){
					again = true;
				}
			}
		}
		return closure;
	}
}