package at.jku.dke.etutor.modules.nf.analysis;

import etutor.modules.rdbd.algorithms.Closure;
import etutor.modules.rdbd.exercises.TupleSet;
import etutor.modules.rdbd.model.*;

import java.util.*;

public class KeysDeterminator {
	
	public static TupleSet determineMinimalKeys(TupleSet tuples){
		int[] key;
		boolean isKey;
		TupleSet keys = new TupleSet();
		Vector keyCandidates = calculateKeyCandidates(tuples.get(0).length);
		 
		for (int i=0; i<keyCandidates.size(); i++){
			key = (int[])keyCandidates.get(i);
			
			isKey = true;
			for (int j=0; j<tuples.size(); j++){
				if (!holdsKey(key, tuples.get(j), tuples)){
					isKey = false;
				}
			}

			if (isKey){
				keys.add(keyCandidates.get(i));
			}
		}

		for (int i=0; i<keys.size(); i++){
			key = (int[])keys.toArray()[i];
		}
		 
		return keys;
	}

	public static Vector calculateKeyCandidates(int valueNumber){
		int[] indices;
		int[] keyCandidate;
		int[] keyAttributePositions = new int[valueNumber];;

		CombinationGenerator generator;
		Vector keyCandidates = new Vector();

		//CALCULATE KEY ATTRIBUTE POSITIONS
		for (int i=0; i<valueNumber; i++){
			keyAttributePositions[i] = i;
		}

		//CALCULATE KEY CANDIDATES
		for (int i = 1; i <= keyAttributePositions.length; i++) {
			generator = new CombinationGenerator(keyAttributePositions.length, i);

			while (generator.hasMore()) {
				indices = generator.getNext();
				
				keyCandidate = new int[indices.length];
				for (int j = 0; j < indices.length; j++) {
					keyCandidate[j] = keyAttributePositions[indices[j]];
				}
				keyCandidates.add(keyCandidate);
			}
		}
		
		return keyCandidates;
	}

	public static boolean holdsKey(int[] key, int[] tuple, TupleSet tuples){
		TupleSet temp = new TupleSet();
		temp.addAll(tuples);
		temp.remove(tuple);
		
		TupleSet existingKeyValueCombinations = extractKeyValueCombinations(key, temp);
		
		int[] keyValueCombination = extractKeyValueCombination(key, tuple);
		
		return !existingKeyValueCombinations.contains(keyValueCombination);
	}
	
	private static int[] extractKeyValueCombination(int[] key, int[] tuple){
		int[] keyValueCombination = new int[key.length];
		
		for (int pos=0; pos<key.length; pos++){
			keyValueCombination[pos] = tuple[key[pos]]; 
		}

		return keyValueCombination;
	}
	
	private static TupleSet extractKeyValueCombinations(int[] key, TupleSet tuples){
		int[] currTuple;
		TupleSet keyValueCombinations = new TupleSet();
		
		for (int tupleNumber=0; tupleNumber<tuples.size(); tupleNumber++){
			currTuple = (int[])tuples.get(tupleNumber);
			keyValueCombinations.add(extractKeyValueCombination(key, currTuple));
		}
		
		return keyValueCombinations;
	}

	public static KeysContainer determineAllKeys(Relation relation){
		KeysContainer container = new KeysContainer();
		determineMinimalKeys(relation, container);
		container.setPartialKeys(determinePartialKeys(container.getMinimalKeys()));

		return container;
	}

	private static void determineMinimalKeys(Relation relation, KeysContainer container){
		TreeSet keys;
		Key superKey;
		TreeSet superKeys;
		Iterator superKeysIterator;

		keys = new TreeSet(new KeyComparator());
		superKeys = determineSuperKeys(relation);

		superKeysIterator = superKeys.iterator();
		while (superKeysIterator.hasNext()) {
			superKey = (Key)superKeysIterator.next();
			if (isMinimalKey(superKey, relation)) {
				keys.add(superKey);
			}
		}
	
		container.setMinimalKeys(keys);
		container.setSuperKeys(superKeys);
	}

	public static TreeSet determineMinimalKeys(Relation relation){
		KeysContainer container = new KeysContainer();
		determineMinimalKeys(relation, container);

		return container.getMinimalKeys();
	}
	
	public static TreeSet determinePartialKeys(Collection minimalKeys) {
		Key currKey;
		int[] indices;
		Key partialKey;
		TreeSet partialKeys;
		Iterator keysIterator;
		CombinationGenerator generator;

		partialKeys = new TreeSet(new KeyComparator());

		keysIterator = minimalKeys.iterator();
		while (keysIterator.hasNext()) {
			currKey = (Key)keysIterator.next();

			for (int i = 1; i <= currKey.getAttributes().size(); i++) {
				generator = new CombinationGenerator(currKey.getAttributes().size(), i);

				while (generator.hasMore()) {
					indices = generator.getNext();
					partialKey = new Key();
					if (indices.length != currKey.getAttributes().size()) {
						for (int j = 0; j < indices.length; j++) {
							partialKey.addAttribute((String)currKey.getAttributes().toArray()[indices[j]]);
						}
					}
					if (partialKey.getAttributes().size() != 0) {
						partialKeys.add(partialKey);
					}
				}
			}
		}
		
		return partialKeys;
	}

	private static boolean isMinimalKey(Key key, Relation relation) {
		Key candidate;
		int[] indices;
		Vector attributes;
		Collection closure;
		Iterator subKeysIterator;
		Iterator attributesIterator;
		CombinationGenerator generator;

		candidate = new Key();

		for (int i = 1; i <= key.getAttributes().size(); i++) {
			generator = new CombinationGenerator(key.getAttributes().size(), i);

			while (generator.hasMore()) {
				indices = generator.getNext();
				candidate.removeAllAttributes();
				if (indices.length != key.getAttributes().size()) {
					for (int j = 0; j < indices.length; j++) {
						candidate.addAttribute((String)key.getAttributes().toArray()[indices[j]]);
					}
				}

				attributes = new Vector();
				attributesIterator = candidate.iterAttributes();
				while (attributesIterator.hasNext()) {
					attributes.add(attributesIterator.next());
				}

				closure = Closure.execute(attributes, relation.getFunctionalDependencies());
				if (closure.containsAll(relation.getAttributes())) {
					return false;
				}
			}
		}

		return true;
	}

	public static TreeSet determineSuperKeys(Relation relation) {
		Key superKey;
		Key candidate;
		int[] indices;
		TreeSet superKeys;
		Vector attributes;
		Collection closure;
		CombinationGenerator generator;
		FunctionalDependency currDependency;

		Iterator superKeysIterator;
		Iterator attributesIterator;
		Iterator dependenciesIterator;

		HashSet allLHSAttributes;
		HashSet constantAttributes;
		HashSet candidateAttributes;

		superKeys = new TreeSet(new KeyComparator());

		//CALCULATING ATTRIBUTES THAT ARE PART OF EVERY KEY - RESTRICTING SET OF CANDIDATE ATTRIBUTES
		constantAttributes = new HashSet();
		constantAttributes.addAll(relation.getAttributes());
		dependenciesIterator = relation.iterFunctionalDependencies();
		while (dependenciesIterator.hasNext()){
			currDependency = (FunctionalDependency)dependenciesIterator.next();
			constantAttributes.removeAll(currDependency.getLHSAttributes());
			constantAttributes.removeAll(currDependency.getRHSAttributes());
		}

		candidateAttributes = new HashSet();
		candidateAttributes.addAll(relation.getAttributes());
		candidateAttributes.removeAll(constantAttributes);
		
		if (candidateAttributes.size() != 0){
			//CALCULATING SUPER KEYS
			candidate = new Key();

			for (int i = 1; i <= candidateAttributes.size(); i++) {
				generator = new CombinationGenerator(candidateAttributes.size(), i);

				while (generator.hasMore()) {
					candidate.removeAllAttributes();
					indices = generator.getNext();
					for (int j = 0; j < indices.length; j++) {
						candidate.addAttribute((String)candidateAttributes.toArray()[indices[j]]);
					}

					candidate.addAllAttributes(constantAttributes);

					attributes = new Vector();
					attributesIterator = candidate.iterAttributes();
					while (attributesIterator.hasNext()) {
						attributes.add(attributesIterator.next());
					}

					closure = Closure.execute(attributes, relation.getFunctionalDependencies());
					if (closure.containsAll(relation.getAttributes())) {
						superKey = new Key();
						superKey.addAllAttributes(attributes);
						superKeys.add(superKey);
					}
				}
			}
		} else {
			superKey = new Key();
			superKey.addAllAttributes(relation.getAttributes());
			superKeys.add(superKey);
		}

		return superKeys;
	}

	private static void test_HOLDS_KEY(){
		int[] key = new int[]{0,1};
		TupleSet tuples = new TupleSet();
		
		tuples.add(new int[]{1,2,3});
		tuples.add(new int[]{1,3,4});
		tuples.add(new int[]{3,4,1});
		tuples.add(new int[]{4,1,2});
		
		System.out.println("Holds key: " + holdsKey(key, new int[]{4,1,3}, tuples));		
	}

	private static void test_DETERMINE_MINIMAL_KEYS(){
		TupleSet tuples = new TupleSet();

		tuples.add(new int[]{1,2,3});
		tuples.add(new int[]{1,3,4});
		tuples.add(new int[]{3,4,1});
		tuples.add(new int[]{4,1,2});

		determineMinimalKeys(tuples);
	}

	public static void main(String[] args){
		test_DETERMINE_MINIMAL_KEYS();
	}
}
