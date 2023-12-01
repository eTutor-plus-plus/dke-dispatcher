package at.jku.dke.etutor.modules.nf.algorithms;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.text.Collator;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class Closure {

	public static TreeSet<String> execute(Collection<String> attributes, Collection<FunctionalDependency> dependencies) {
		boolean again = true;
		TreeSet<String> closure = new TreeSet<>(Collator.getInstance());
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