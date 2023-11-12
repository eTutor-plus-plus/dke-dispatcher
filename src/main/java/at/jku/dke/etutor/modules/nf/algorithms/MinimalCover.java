package at.jku.dke.etutor.modules.nf.algorithms;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.*;

public class MinimalCover {
	
	public static HashSet execute(Collection dependencies){
		FunctionalDependency keyFD;
		FunctionalDependency currFD;

		HashSet minimalCover;
		HashMap extraneusAttributes;

		Iterator keyFDsIterator;
		Iterator dependenciesIterator;

		minimalCover = new HashSet(dependencies);
		
		//DERIVE CANONICAL FORM OF FUNCTIONAL DEPENDENCIES
		minimalCover = unfold(dependencies);

		//DELETING EXTRANEUS ATTRIBUTES
		extraneusAttributes = calculateExtraneusAttributes(minimalCover);

		keyFDsIterator = extraneusAttributes.keySet().iterator();
		while (keyFDsIterator.hasNext()){
			keyFD = (FunctionalDependency)keyFDsIterator.next();
			
			dependenciesIterator = minimalCover.iterator();
			while (dependenciesIterator.hasNext()){
				currFD = (FunctionalDependency)dependenciesIterator.next();
				if (currFD.equals(keyFD)){
					currFD.removeLHSAttributes((Collection)extraneusAttributes.get(keyFD));
				}
			}
		}

		//DELETING DUPLICATES
		minimalCover = new HashSet(minimalCover);
	
		//DELETING REDUNDAND FUNCTIONAL DEPENDENCIES
		minimalCover.removeAll(calculateRedundandFunctionalDependencies(minimalCover));

		return minimalCover;
	}

	public static HashSet calculateRedundandFunctionalDependencies(Collection dependencies){
		Vector tempDependencies;
		HashSet redundandDependencies;
		Iterator dependenciesIterator;
		FunctionalDependency currDependency;
		
		tempDependencies = new Vector();
		redundandDependencies = new HashSet();
		dependenciesIterator = dependencies.iterator();
				
		while (dependenciesIterator.hasNext()){
			currDependency = (FunctionalDependency)dependenciesIterator.next();

			tempDependencies.clear();
			tempDependencies.addAll(dependencies);
			tempDependencies.remove(currDependency);
			tempDependencies.removeAll(redundandDependencies);

			if (Cover.execute(tempDependencies, dependencies)){
				redundandDependencies.add(currDependency);
			}
		}

		return redundandDependencies;
	}


	public static HashMap calculateExtraneusAttributes(Collection dependencies) {
		Vector tempFDs;
		String currAttribute;
		HashMap extraneusAttributes;
		FunctionalDependency tempFD;
		FunctionalDependency currFD;
		Iterator attributesIterator;
		Iterator dependenciesIterator;


		tempFDs = new Vector();
		tempFD = new FunctionalDependency();
		extraneusAttributes = new HashMap();
		dependenciesIterator = dependencies.iterator();

		while (dependenciesIterator.hasNext()) {
			currFD = (FunctionalDependency)dependenciesIterator.next();
			attributesIterator = currFD.iterLHSAttributes();

			while (attributesIterator.hasNext()) {
				currAttribute = (String)attributesIterator.next();

				tempFD.setLHSAttributes(currFD.getLHSAttributes());
				tempFD.setRHSAttributes(currFD.getRHSAttributes());
				tempFD.removeLHSAttribute(currAttribute);

				if (extraneusAttributes.containsKey(currFD)){
					tempFD.removeLHSAttributes(((Vector)extraneusAttributes.get(currFD)));
				}

				tempFDs.clear();
				tempFDs.addAll(dependencies);
				tempFDs.remove(currFD);
				tempFDs.add(tempFD);
				
				if (Cover.execute(dependencies, tempFDs)){
					if (!extraneusAttributes.containsKey(currFD)){
						extraneusAttributes.put(currFD, new Vector());
					}
					((Vector)extraneusAttributes.get(currFD)).add(currAttribute);
				}
			}
		}

		return extraneusAttributes;
	}

	public static HashSet fold(Collection dependencies) {
		HashSet foldedDependencies;
		Iterator attributesIterator;
		Vector processedDependencies;
		FunctionalDependency currDependency;
		FunctionalDependency compDependency;

		foldedDependencies = new HashSet();
		processedDependencies = new Vector();

		for (int i = 0; i < dependencies.size(); i++) {
			currDependency = (FunctionalDependency)dependencies.toArray()[i];

			if (!processedDependencies.contains(currDependency)) {

				for (int j = i; j < dependencies.size(); j++) {
					compDependency = (FunctionalDependency)dependencies.toArray()[j];

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

	public static HashSet unfold(Collection dependencies) {
		HashSet unfoldedDependencies;
		Iterator dependenciesIterator;

		unfoldedDependencies = new HashSet();
		dependenciesIterator = dependencies.iterator();

		while (dependenciesIterator.hasNext()) {
			unfoldedDependencies.addAll(((FunctionalDependency)dependenciesIterator.next()).unfold());
		}

		return unfoldedDependencies;
	}
}
