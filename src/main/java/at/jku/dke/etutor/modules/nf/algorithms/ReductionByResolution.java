package at.jku.dke.etutor.modules.nf.algorithms;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.Relation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

public class ReductionByResolution {

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
	
	public static HashSet<FunctionalDependency> execute_old(Relation rel, Collection<String> subScheme) {
		String currAttribute;

		Vector<String> attributes = new Vector<>();
		Iterator<String> attributesIterator = rel.iterAttributes();
		while (attributesIterator.hasNext()) {
			currAttribute = attributesIterator.next();
			if (!subScheme.contains(currAttribute)) {
				attributes.add(currAttribute);
			}
		}

		HashSet<FunctionalDependency> dependencies = rel.getFunctionalDependencies();
		HashSet<FunctionalDependency> result = MinimalCover.unfold(dependencies);

		while (!attributes.isEmpty()) {
			currAttribute = attributes.get(0);
			
			/*
			System.out.println("Processing Attribute '" + currAttribute + "'");
			resultIterator = result.iterator();
			System.out.println("STARTING SET OF DEPENDENCIES:");
			while (resultIterator.hasNext()) {
				System.out.println(resultIterator.next());
			}*/
			
			result.addAll(calculateResolvents(currAttribute, dependencies));
			//result.addAll(calculateResolvents(currAttribute, result));
			
			/*
			resultIterator = result.iterator();
			System.out.println("EXTENDED SET OF DEPENDENCIES:");
			while (resultIterator.hasNext()) {
				System.out.println(resultIterator.next());
			}*/

			Iterator<FunctionalDependency> resultIterator = result.iterator();
			while (resultIterator.hasNext()) {
				FunctionalDependency currFD = resultIterator.next();

				//HACK - VORIGE VERSION - WAR FALSCH
				//if ((!subScheme.containsAll(currFD.getLHSAttributes())) || (!subScheme.containsAll(currFD.getRHSAttributes()))){
				//	resultIterator.remove();
				//} else {
					//original code
					if ((currFD.getLHSAttributes().contains(currAttribute))	|| (currFD.getRHSAttributes().contains(currAttribute))) {
						resultIterator.remove();
						//System.out.println("REMOVED DEPENDENCY: " + currFD);
					}
				//}
			}
			/*
			resultIterator = result.iterator();
			System.out.println("CLEANED SET OF DEPENDENCIES:");
			while (resultIterator.hasNext()) {
				System.out.println(resultIterator.next());
			}*/
			
			attributes.remove(currAttribute);
		}

		return MinimalCover.fold(result);
	}
	
	
	public static HashSet<FunctionalDependency> execute(Relation rel, Collection<String> subScheme) {
		String currAttribute;

		Vector<String> attributes = new Vector<>();
		Iterator<String> attributesIterator = rel.iterAttributes();
		while (attributesIterator.hasNext()) {
			currAttribute = attributesIterator.next();
			if (!subScheme.contains(currAttribute)) {
				attributes.add(currAttribute);
			}
		}

		HashSet<FunctionalDependency> dependencies = rel.getFunctionalDependencies();
		HashSet<FunctionalDependency> result = MinimalCover.unfold(dependencies);

		while (!attributes.isEmpty()) {
			currAttribute = attributes.get(0);
			
			/*
			System.out.println("Processing Attribute '" + currAttribute + "'");
			resultIterator = result.iterator();
			System.out.println("STARTING SET OF DEPENDENCIES:");
			while (resultIterator.hasNext()) {
				System.out.println(resultIterator.next());
			}*/
			
			//result.addAll(calculateResolvents(currAttribute, dependencies)); VORIGE VERSION - WAR FALSCH
			result.addAll(calculateResolvents(currAttribute, result));
			
			/*
			resultIterator = result.iterator();
			System.out.println("EXTENDED SET OF DEPENDENCIES:");
			while (resultIterator.hasNext()) {
				System.out.println(resultIterator.next());
			}*/

			Iterator<FunctionalDependency> resultIterator = result.iterator();
			while (resultIterator.hasNext()) {
				FunctionalDependency currFD = resultIterator.next();

				//HACK - VORIGE VERSION - WAR FALSCH
				//if ((!subScheme.containsAll(currFD.getLHSAttributes())) || (!subScheme.containsAll(currFD.getRHSAttributes()))){
				//	resultIterator.remove();
				//} else {
					//original code
					if ((currFD.getLHSAttributes().contains(currAttribute))	|| (currFD.getRHSAttributes().contains(currAttribute))) {
						resultIterator.remove();
						//System.out.println("REMOVED DEPENDENCY: " + currFD);
					}
				//}
			}
			/*
			resultIterator = result.iterator();
			System.out.println("CLEANED SET OF DEPENDENCIES:");
			while (resultIterator.hasNext()) {
				System.out.println(resultIterator.next());
			}*/
			
			attributes.remove(currAttribute);
		}

		return MinimalCover.fold(result);
	}

	public static HashSet<FunctionalDependency> calculateResolvents(String a, Collection<FunctionalDependency> dependencies) {
		FunctionalDependency currDependency;

		HashSet<FunctionalDependency> resolvents = new HashSet<>();
		Vector<FunctionalDependency> containingDependencies = new Vector<>();

        for (FunctionalDependency functionalDependency : dependencies) {
            currDependency = functionalDependency;
            if (currDependency.getRHSAttributes().contains(a)) {
                containingDependencies.add(currDependency);
            }
        }

        for (FunctionalDependency dependency : dependencies) {
            currDependency = dependency;
            if (currDependency.getLHSAttributes().contains(a)) {
                for (FunctionalDependency containingDependency : containingDependencies) {
                    FunctionalDependency resolvent = new FunctionalDependency();
                    resolvent.addAllLHSAttributes(currDependency.getLHSAttributes());
                    resolvent.removeLHSAttribute(a);
                    resolvent.addAllLHSAttributes(containingDependency.getLHSAttributes());
                    resolvent.addAllRHSAttributes(currDependency.getRHSAttributes());

                    if (!resolvent.isTrivial()) {
                        resolvents.add(resolvent);
                    }
                }
            }
        }

		/*
		System.out.println("-----------------------------------------------");
		System.out.println("CALCULATED RESOLVENTS FOR ATTRIBUTE '" + a + "'");
		Iterator resolventsIterator = resolvents.iterator();
		while (resolventsIterator.hasNext()){
			System.out.println(resolventsIterator.next());
		}
		System.out.println("-----------------------------------------------\n");
		*/
		
		return resolvents;
	}
}