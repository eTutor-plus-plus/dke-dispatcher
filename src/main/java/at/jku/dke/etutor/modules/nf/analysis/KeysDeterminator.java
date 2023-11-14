package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.modules.nf.algorithms.Closure;
import at.jku.dke.etutor.modules.nf.exercises.TupleSet;
import at.jku.dke.etutor.modules.nf.model.*;

import java.util.*;

public class KeysDeterminator {
	
	public static TupleSet determineMinimalKeys(TupleSet tuples){
		TupleSet keys = new TupleSet();
		Vector<int[]> keyCandidates = calculateKeyCandidates(tuples.get(0).length);
		 
		for (int i=0; i<keyCandidates.size(); i++){
			int[] key = keyCandidates.get(i);
			
			boolean isKey = true;
			for (int j=0; j<tuples.size(); j++){
				if (!holdsKey(key, tuples.get(j), tuples)){
					isKey = false;
				}
			}

			if (isKey){
				keys.add(keyCandidates.get(i));
			}
		}
		 
		return keys;
	}

	public static Vector<int[]> calculateKeyCandidates(int valueNumber){
		int[] keyAttributePositions = new int[valueNumber];;

		Vector<int[]> keyCandidates = new Vector<>();

		//CALCULATE KEY ATTRIBUTE POSITIONS
		for (int i=0; i<valueNumber; i++){
			keyAttributePositions[i] = i;
		}

		//CALCULATE KEY CANDIDATES
		for (int i = 1; i <= keyAttributePositions.length; i++) {
			CombinationGenerator generator = new CombinationGenerator(keyAttributePositions.length, i);

			while (generator.hasMore()) {
				int[] indices = generator.getNext();
				
				int [] keyCandidate = new int[indices.length];
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
		TupleSet keyValueCombinations = new TupleSet();
		
		for (int tupleNumber=0; tupleNumber<tuples.size(); tupleNumber++){
			int[] currTuple = tuples.get(tupleNumber);
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
		TreeSet<Key> keys = new TreeSet<Key>(new KeyComparator());
		TreeSet<Key> superKeys = determineSuperKeys(relation);

		Iterator<Key> superKeysIterator = superKeys.iterator();
		while (superKeysIterator.hasNext()) {
			Key superKey = superKeysIterator.next();
			if (isMinimalKey(superKey, relation)) {
				keys.add(superKey);
			}
		}
	
		container.setMinimalKeys(keys);
		container.setSuperKeys(superKeys);
	}

	public static TreeSet<Key> determineMinimalKeys(Relation relation){
		KeysContainer container = new KeysContainer();
		determineMinimalKeys(relation, container);

		return container.getMinimalKeys();
	}
	
	public static TreeSet<Key> determinePartialKeys(Collection<Key> minimalKeys) {
		TreeSet<Key> partialKeys = new TreeSet<Key>(new KeyComparator());

		Iterator<Key> keysIterator = minimalKeys.iterator();
		while (keysIterator.hasNext()) {
			Key currKey = keysIterator.next();

			for (int i = 1; i <= currKey.getAttributes().size(); i++) {
				CombinationGenerator generator = new CombinationGenerator(currKey.getAttributes().size(), i);

				while (generator.hasMore()) {
					int[] indices = generator.getNext();
					Key partialKey = new Key();
					if (indices.length != currKey.getAttributes().size()) {
						for (int j = 0; j < indices.length; j++) {
							partialKey.addAttribute((String)currKey.getAttributes().toArray()[indices[j]]);
						}
					}
					if (!partialKey.getAttributes().isEmpty()) {
						partialKeys.add(partialKey);
					}
				}
			}
		}
		
		return partialKeys;
	}

	private static boolean isMinimalKey(Key key, Relation relation) {
		Key candidate = new Key();

		for (int i = 1; i <= key.getAttributes().size(); i++) {
			CombinationGenerator generator = new CombinationGenerator(key.getAttributes().size(), i);

			while (generator.hasMore()) {
				int[] indices = generator.getNext();
				candidate.removeAllAttributes();
				if (indices.length != key.getAttributes().size()) {
					for (int j = 0; j < indices.length; j++) {
						candidate.addAttribute((String)key.getAttributes().toArray()[indices[j]]);
					}
				}

				Vector<String> attributes = new Vector<>();
				Iterator<String> attributesIterator = candidate.iterAttributes();
				while (attributesIterator.hasNext()) {
					attributes.add(attributesIterator.next());
				}

				Collection<String> closure = Closure.execute(attributes, relation.getFunctionalDependencies());
				if (closure.containsAll(relation.getAttributes())) {
					return false;
				}
			}
		}

		return true;
	}

	public static TreeSet<Key> determineSuperKeys(Relation relation) {
		Key superKey;

		TreeSet<Key> superKeys = new TreeSet<Key>(new KeyComparator());

		//CALCULATING ATTRIBUTES THAT ARE PART OF EVERY KEY - RESTRICTING SET OF CANDIDATE ATTRIBUTES
        HashSet<String> constantAttributes = new HashSet<>(relation.getAttributes());
		Iterator<FunctionalDependency> dependenciesIterator = relation.iterFunctionalDependencies();
		while (dependenciesIterator.hasNext()){
			FunctionalDependency currDependency = dependenciesIterator.next();
			constantAttributes.removeAll(currDependency.getLHSAttributes());
			constantAttributes.removeAll(currDependency.getRHSAttributes());
		}

        HashSet<String> candidateAttributes = new HashSet<>(relation.getAttributes());
		candidateAttributes.removeAll(constantAttributes);
		
		if (!candidateAttributes.isEmpty()){
			//CALCULATING SUPER KEYS
			Key candidate = new Key();

			for (int i = 1; i <= candidateAttributes.size(); i++) {
				CombinationGenerator generator = new CombinationGenerator(candidateAttributes.size(), i);

				while (generator.hasMore()) {
					candidate.removeAllAttributes();
					int[] indices = generator.getNext();
					for (int j = 0; j < indices.length; j++) {
						candidate.addAttribute((String)candidateAttributes.toArray()[indices[j]]);
					}

					candidate.addAllAttributes(constantAttributes);

					Vector<String> attributes = new Vector<>();
					Iterator<String> attributesIterator = candidate.iterAttributes();
					while (attributesIterator.hasNext()) {
						attributes.add(attributesIterator.next());
					}

					Collection<String> closure = Closure.execute(attributes, relation.getFunctionalDependencies());
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
