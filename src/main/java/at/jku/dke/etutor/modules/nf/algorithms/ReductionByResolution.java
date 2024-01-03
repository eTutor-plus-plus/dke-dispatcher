package at.jku.dke.etutor.modules.nf.algorithms;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.Relation;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class ReductionByResolution {

	private ReductionByResolution() {
		// This class is not meant to be instantiated (Gerald Wimmer, 2024-01-01).
	}

	public static void main(String[] args){
		
		
		
		/*
		FunctionalDependency fd;
		Relation r = new Relation();
		r.addAttribute("A");
		r.addAttribute("B");
		r.addAttribute("C");
		r.addAttribute("D");
		r.addAttribute("E");
		r.addAttribute("F");

		fd = new FunctionalDependency();
		fd.addLHSAttribute("C");
		fd.addLHSAttribute("F");
		fd.addRHSAttribute("B");
		r.addFunctionalDependency(fd);
		
		fd = new FunctionalDependency();
		fd.addLHSAttribute("B");
		fd.addLHSAttribute("C");
		fd.addRHSAttribute("D");
		r.addFunctionalDependency(fd);

		fd = new FunctionalDependency();
		fd.addLHSAttribute("C");
		fd.addLHSAttribute("D");
		fd.addRHSAttribute("A");
		r.addFunctionalDependency(fd);

		fd = new FunctionalDependency();
		fd.addLHSAttribute("B");
		fd.addLHSAttribute("D");
		fd.addRHSAttribute("E");
		r.addFunctionalDependency(fd);

		fd = new FunctionalDependency();
		fd.addLHSAttribute("D");
		fd.addLHSAttribute("E");
		fd.addRHSAttribute("B");
		r.addFunctionalDependency(fd);

		fd = new FunctionalDependency();
		fd.addLHSAttribute("D");
		fd.addLHSAttribute("E");
		fd.addRHSAttribute("F");
		r.addFunctionalDependency(fd);

		fd = new FunctionalDependency();
		fd.addLHSAttribute("F");
		fd.addRHSAttribute("A");
		r.addFunctionalDependency(fd);

		fd = new FunctionalDependency();
		fd.addLHSAttribute("F");
		fd.addRHSAttribute("D");
		r.addFunctionalDependency(fd);
		*/

		//System.out.println("BASE RELATION");
		//System.out.println(Arrays.toString(r.getAttributesArray()));
		/*
		Iterator deps = r.getFunctionalDependencies().iterator();
		while (deps.hasNext()){
			System.out.println(deps.next());
		}*/
		
		/*
		Vector<String> subScheme = new Vector<String>();
		subScheme.add("A");
		subScheme.add("C");
		subScheme.add("E");
		subScheme.add("F");
	
		//System.out.println("\nRESULT\n");
		*/

		/*
		HashSet h = execute(r, subScheme);
		Iterator i = h.iterator();
		while (i.hasNext()){
			System.out.println(i.next());
		}
		System.out.println("FERTIG");*/

		/*
		System.out.println("RESULT IN MINIMAL COVER:");
		Iterator x = MinimalCover.execute(h).iterator();
		while (x.hasNext()){
			System.out.println(x.next());
		}*/
		
	}

	/**
	 * Determines the functional dependencies of the subScheme of a base relation.
	 * @param rel The base relation
	 * @param subScheme The subScheme
	 * @return The functional dependencies of the subScheme
	 */
	public static Set<FunctionalDependency> execute(Relation rel, Collection<String> subScheme) {
		List<String> attributes = new Vector<>();

		/*
		 * Add only those attributes of the base relation that are absent from the subScheme
		 * (Gerald Wimmer, 2024-01-01).
		 */
		for (String currAttribute : rel.getAttributes()) {
			if (!subScheme.contains(currAttribute)) {
				attributes.add(currAttribute);
			}
		}

		Set<FunctionalDependency> dependencies = rel.getFunctionalDependencies();
		Set<FunctionalDependency> result = MinimalCover.unfold(dependencies);

		for (String currAttribute : attributes) {
			result.addAll(calculateResolvents(currAttribute, result));

			/*
			 * Remove all those functional dependencies which contain currAttribute on their left-hand side
			 * (i.e., cannot be resolved) or right-hand side (because currAttribute is NOT present in the subScheme).
			 */
            result.removeIf(currFD -> (currFD.getLhsAttributes().contains(currAttribute)) || (currFD.getRhsAttributes().contains(currAttribute)));
		}

		return MinimalCover.fold(result);
	}

	/**
	 * Determines the resolvents for an attribute given a <code>Collection</code> of functional dependencies.
	 * @param a The attribute whose resolvents are to be determined
	 * @param dependencies The functional dependencies on which the resolvents are to be based
	 * @return A <code>Set</code> of resolved functional dependencies
	 */
	public static Set<FunctionalDependency> calculateResolvents(String a, Collection<FunctionalDependency> dependencies) {
		List<FunctionalDependency> containingDependencies = new Vector<>();
        for (FunctionalDependency currDependency : dependencies) {
            if (currDependency.getRhsAttributes().contains(a)) {
                containingDependencies.add(currDependency);
            }
        }

		Set<FunctionalDependency> resolvents = new HashSet<>();
        for (FunctionalDependency currDependency : dependencies) {
            if (currDependency.getLhsAttributes().contains(a)) {
                for (FunctionalDependency containingDependency : containingDependencies) {
                    FunctionalDependency resolvent = new FunctionalDependency();
                    resolvent.addAllLhsAttributes(currDependency.getLhsAttributes());
                    resolvent.removeLhsAttribute(a);
                    resolvent.addAllLhsAttributes(containingDependency.getLhsAttributes());
                    resolvent.addAllRhsAttributes(currDependency.getRhsAttributes());

                    if (!resolvent.isTrivial()) {
                        resolvents.add(resolvent);
                    }
                }
            }
        }

        return resolvents;
	}
}