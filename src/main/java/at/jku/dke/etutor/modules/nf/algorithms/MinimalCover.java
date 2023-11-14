package at.jku.dke.etutor.modules.nf.algorithms;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.*;

public class MinimalCover {
	
	public static HashSet<FunctionalDependency> execute(Collection<FunctionalDependency> dependencies){
		Iterator<FunctionalDependency> keyFDsIterator;
		Iterator<FunctionalDependency> dependenciesIterator;

		//DERIVE CANONICAL FORM OF FUNCTIONAL DEPENDENCIES
		HashSet<FunctionalDependency> minimalCover = unfold(dependencies);

		//DELETING EXTRANEUS ATTRIBUTES
		HashMap<FunctionalDependency, Vector<String>> extraneusAttributes = calculateExtraneusAttributes(minimalCover);

		keyFDsIterator = extraneusAttributes.keySet().iterator();
		while (keyFDsIterator.hasNext()){
			FunctionalDependency keyFD = keyFDsIterator.next();
			
			dependenciesIterator = minimalCover.iterator();
			while (dependenciesIterator.hasNext()){
				FunctionalDependency currFD = dependenciesIterator.next();
				if (currFD.equals(keyFD)){
					currFD.removeLHSAttributes(extraneusAttributes.get(keyFD));
				}
			}
		}

		//DELETING DUPLICATES
		minimalCover = new HashSet<>(minimalCover);
	
		//DELETING REDUNDAND FUNCTIONAL DEPENDENCIES
		minimalCover.removeAll(calculateRedundantFunctionalDependencies(minimalCover));

		return minimalCover;
	}

	public static HashSet<FunctionalDependency> calculateRedundantFunctionalDependencies(Collection<FunctionalDependency> dependencies){
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


	public static HashMap<FunctionalDependency, Vector<String>> calculateExtraneusAttributes(Collection<FunctionalDependency> dependencies) {
		Vector<FunctionalDependency> tempFDs = new Vector<>();
		FunctionalDependency tempFD = new FunctionalDependency();
		HashMap<FunctionalDependency, Vector<String>> extraneusAttributes = new HashMap<>();

        for (FunctionalDependency currFD : dependencies) {
            Iterator<String> attributesIterator = currFD.iterLHSAttributes();

            while (attributesIterator.hasNext()) {
                String currAttribute = attributesIterator.next();

                tempFD.setLHSAttributes(currFD.getLHSAttributes());
                tempFD.setRHSAttributes(currFD.getRHSAttributes());
                tempFD.removeLHSAttribute(currAttribute);

                if (extraneusAttributes.containsKey(currFD)) {
                    tempFD.removeLHSAttributes(extraneusAttributes.get(currFD));
                }

                tempFDs.clear();
                tempFDs.addAll(dependencies);
                tempFDs.remove(currFD);
                tempFDs.add(tempFD);

                if (Cover.execute(dependencies, tempFDs)) {
                    if (!extraneusAttributes.containsKey(currFD)) {
                        extraneusAttributes.put(currFD, new Vector<>());
                    }
                    extraneusAttributes.get(currFD).add(currAttribute);
                }
            }
        }

		return extraneusAttributes;
	}

	public static HashSet<FunctionalDependency> fold(Collection<FunctionalDependency> dependencies) {
		HashSet<FunctionalDependency> foldedDependencies = new HashSet<>();
		Vector<FunctionalDependency> processedDependencies = new Vector<>();

		for (int i = 0; i < dependencies.size(); i++) {
			FunctionalDependency currDependency = (FunctionalDependency)dependencies.toArray()[i];

			if (!processedDependencies.contains(currDependency)) {

				for (int j = i; j < dependencies.size(); j++) {
					FunctionalDependency compDependency = (FunctionalDependency)dependencies.toArray()[j];

					if (compareLHS(compDependency, currDependency)) {
						currDependency.addAllRHSAttributes(compDependency.getRHSAttributes());
						processedDependencies.add(compDependency);
					}
				}
				foldedDependencies.add(currDependency);
				processedDependencies.add(currDependency);
			}
		}

		return foldedDependencies;
	}

	private static boolean compareLHS(FunctionalDependency fd1, FunctionalDependency fd2) {
		return fd2.getLHSAttributes().containsAll(fd1.getLHSAttributes()) && fd1.getLHSAttributes().containsAll(fd2.getLHSAttributes());
	}

	public static HashSet<FunctionalDependency> unfold(Collection<FunctionalDependency> dependencies) {
		HashSet<FunctionalDependency> unfoldedDependencies = new HashSet<>();

        for (FunctionalDependency dependency : dependencies) {
            unfoldedDependencies.addAll(dependency.unfold());
        }

		return unfoldedDependencies;
	}
}
