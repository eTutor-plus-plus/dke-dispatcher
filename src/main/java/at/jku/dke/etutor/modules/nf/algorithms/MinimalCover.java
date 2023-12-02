package at.jku.dke.etutor.modules.nf.algorithms;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.*;

public class MinimalCover {
	
	public static Set<FunctionalDependency> execute(Collection<FunctionalDependency> dependencies){
		//DERIVE CANONICAL FORM OF FUNCTIONAL DEPENDENCIES
		Set<FunctionalDependency> minimalCover = unfold(dependencies);

		//DELETING EXTRANEOUS ATTRIBUTES
		Map<FunctionalDependency, Vector<String>> extraneousAttributes = calculateExtraneousAttributes(minimalCover);

		for (FunctionalDependency keyFD : extraneousAttributes.keySet()){
			for (FunctionalDependency currFD : minimalCover){
				if (currFD.equals(keyFD)){
					currFD.removeLhsAttributes(extraneousAttributes.get(keyFD));
				}
			}
		}

		//DELETING DUPLICATES
		minimalCover = new HashSet<>(minimalCover);
	
		//DELETING REDUNDANT FUNCTIONAL DEPENDENCIES
		minimalCover.removeAll(calculateRedundantFunctionalDependencies(minimalCover));

		return minimalCover;
	}

	public static Set<FunctionalDependency> calculateRedundantFunctionalDependencies(Collection<FunctionalDependency> dependencies){
		Vector<FunctionalDependency> tempDependencies = new Vector<>();
		HashSet<FunctionalDependency> redundantDependencies = new HashSet<>();

        for (FunctionalDependency currDependency : dependencies) {
            tempDependencies.clear();
            tempDependencies.addAll(dependencies);
            tempDependencies.remove(currDependency);
            tempDependencies.removeAll(redundantDependencies);

            if (Cover.execute(tempDependencies, dependencies)) {
                redundantDependencies.add(currDependency);
            }
        }

		return redundantDependencies;
	}


	public static Map<FunctionalDependency, Vector<String>> calculateExtraneousAttributes (Collection<FunctionalDependency> dependencies) {
		List<FunctionalDependency> tempFDs = new Vector<>();
		FunctionalDependency tempFD = new FunctionalDependency();
		Map<FunctionalDependency, Vector<String>> extraneousAttributes = new HashMap<>();

        for (FunctionalDependency currFD : dependencies) {
			for (String currAttribute : currFD.getLhsAttributes()) {
                tempFD.setLhsAttributes(currFD.getLhsAttributes());
                tempFD.setRhsAttributes(currFD.getRhsAttributes());
                tempFD.removeLhsAttribute(currAttribute);

                if (extraneousAttributes.containsKey(currFD)) {
                    tempFD.removeLhsAttributes(extraneousAttributes.get(currFD));
                }

                tempFDs.clear();
                tempFDs.addAll(dependencies);
                tempFDs.remove(currFD);
                tempFDs.add(tempFD);

                if (Cover.execute(dependencies, tempFDs)) {
                    if (!extraneousAttributes.containsKey(currFD)) {
                        extraneousAttributes.put(currFD, new Vector<>());
                    }
                    extraneousAttributes.get(currFD).add(currAttribute);
                }
            }
        }

		return extraneousAttributes;
	}

	public static Set<FunctionalDependency> fold(Collection<FunctionalDependency> dependencies) {
		Set<FunctionalDependency> foldedDependencies = new HashSet<>();
		List<FunctionalDependency> processedDependencies = new Vector<>();

		for (int i = 0; i < dependencies.size(); i++) {
			FunctionalDependency currDependency = (FunctionalDependency)dependencies.toArray()[i];

			if (!processedDependencies.contains(currDependency)) {

				for (int j = i; j < dependencies.size(); j++) {
					FunctionalDependency compDependency = (FunctionalDependency)dependencies.toArray()[j];

					if (compareLHS(compDependency, currDependency)) {
						currDependency.addAllRhsAttributes(compDependency.getRhsAttributes());
						processedDependencies.add(compDependency);
					}
				}
				foldedDependencies.add(currDependency);
				processedDependencies.add(currDependency);
			}
		}

		return foldedDependencies;
	}

	/**
	 * Checks whether the supplied FunctionalDependencies have identical left-hand-sides. (Gerald Wimmer, 2023-12-02)
	 * @param fd1 The second FunctionalDependency
	 * @param fd2 The first FunctionalDependency
	 * @return true if the left-hand-sides of both FunctionalDependencies are equal (i.e., contain the same attributes)
	 */
	private static boolean compareLHS(FunctionalDependency fd1, FunctionalDependency fd2) {
		return fd2.getLhsAttributes().containsAll(fd1.getLhsAttributes()) && fd1.getLhsAttributes().containsAll(fd2.getLhsAttributes());
	}

	public static Set<FunctionalDependency> unfold(Collection<FunctionalDependency> dependencies) {
		HashSet<FunctionalDependency> unfoldedDependencies = new HashSet<>();

        for (FunctionalDependency dependency : dependencies) {
            unfoldedDependencies.addAll(dependency.unfold());
        }

		return unfoldedDependencies;
	}
}
