package at.jku.dke.etutor.modules.nf.exercises;

import at.jku.dke.etutor.modules.nf.NFHelper;
import at.jku.dke.etutor.modules.nf.analysis.normalization.KeysDeterminator;
import at.jku.dke.etutor.modules.nf.model.Key;

import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

public class TuplesGenerator{
	
	public static TupleSet generate(TupleSet keys, int valuesNumber, int tuplesNumber, int minValue, int maxValue){
		Vector<String> prefixes = new Vector<>();
		prefixes.add("A");
		prefixes.add("B");
		prefixes.add("C");
		prefixes.add("D");
		prefixes.add("E");
		prefixes.add("F");
		prefixes.add("G");
		prefixes.add("H");

		int[] tuple;
		TupleSet tuples = new TupleSet();
		TupleSet triedMutations = new TupleSet();
		RandomCyphersGenerator cyphersGenerator = new RandomCyphersGenerator(minValue, maxValue, false);
	
		for (int tuplePos=0; tuplePos<tuplesNumber; tuplePos++){
			tuple = generateRandomTuple(valuesNumber, minValue, maxValue, cyphersGenerator);
			while (tuples.contains(tuple)){
				//Diese Schleife ist eine potentielle Endlos-Schleife! 
				tuple = generateRandomTuple(valuesNumber, minValue, maxValue, cyphersGenerator);
			}
			tuples.add(tuple);
		}

		TupleSet invalidKeys = calcInvalidKeys(tuples, keys);
		TupleSet undesiredKeys = calcUndesiredKeys(tuples, keys);

		while ((!invalidKeys.isEmpty()) || (!undesiredKeys.isEmpty())){
			System.out.println("\nINITIAL POPULATION:");
			TuplesPrinter.printToConsole(tuples, prefixes);

			/*
			System.out.println("\nDESIRED KEYS:");
			for (int[] arr : keys){
				System.out.println(TuplesPrinter.toString(arr));
			}

			System.out.println("\nCALCULATED KEYS:");
			TupleSet calculatedKeys = KeysDeterminator.determineMinimalKeys(tuples);
			for (int[] arr : calculatedKeys){
				System.out.println(TuplesPrinter.toString(arr));
			}*/

			System.out.println("\nINVALID KEYS:");
			for (int[] arr : invalidKeys){
				System.out.print("(" + TuplesPrinter.toString(arr) + ") ");
			}
			System.out.print("\n");

			System.out.println("\nUNDESIRED KEYS:");
			for (int [] arr : undesiredKeys){
				System.out.print("(" + TuplesPrinter.toString(arr) + ") ");
			}
			System.out.print("\n");
			
			//BUSINESS LOGIC
			/*
			for (int pos=0; pos<invalidKeys.size(); pos++){
				System.out.println("\nENABLING KEY: (" + TuplesPrinter.toString((int[])invalidKeys.get(pos)) + ")");
				tuples = enableKey((int[])invalidKeys.get(pos), tuples);
			}

			for (int pos=0; pos<undesiredKeys.size(); pos++){
				System.out.println("\nDISABLING KEY: (" + TuplesPrinter.toString((int[])undesiredKeys.get(pos)) + ")");
				disableKey((int[])undesiredKeys.get(pos), tuples);
			}*/

			if (!invalidKeys.isEmpty()){
				System.out.println("\nENABLING KEY: (" + TuplesPrinter.toString(invalidKeys.get(0)) + ")");
				enableKey(invalidKeys.get(0), tuples);
			}

			if (!undesiredKeys.isEmpty()){
				System.out.println("\nDISABLING KEY: (" + TuplesPrinter.toString(undesiredKeys.get(0)) + ")");
				disableKey(undesiredKeys.get(0), tuples);
			}			
			
			invalidKeys = calcInvalidKeys(tuples, keys);
			undesiredKeys = calcUndesiredKeys(tuples, keys);			
		}

		return tuples;
	}
	
	private static void enableKey(int[] key, TupleSet tuples){
	}
	
	private static void disableKey(int[] key, TupleSet tuples){
		Vector<String> prefixes = new Vector<>();
		prefixes.add("A");
		prefixes.add("B");
		prefixes.add("C");
		prefixes.add("D");
		prefixes.add("E");
		prefixes.add("F");
		prefixes.add("G");
		prefixes.add("H");

		int[] tuple;
		int[] sampleTuple;

		for (int pos=0; pos<tuples.size(); pos++){
			tuple = tuples.get(pos);

			if (KeysDeterminator.holdsKey(key, tuple, tuples)){
				tuples.remove(tuple);

				System.out.println("\nREMOVED TUPLE '" + TuplesPrinter.toString(tuple, prefixes) + "'.");
				System.out.println("\nTUPLE POPULATION AFTER DELETION.");
				TuplesPrinter.printToConsole(tuples, prefixes);

				if (pos == tuples.size()-1){
					sampleTuple = tuples.get(0);				
				} else {
					sampleTuple = tuples.get(tuples.size()-1);				
				}

                for (int i : key) {
                    tuple[i] = sampleTuple[i];
                }
				
				tuples.add(tuple);

				System.out.println("\nADDED TUPLE '" + TuplesPrinter.toString(tuple, prefixes) + "'.");
				System.out.println("\nTUPLE POPULATION AFTER INSERTION.");
				TuplesPrinter.printToConsole(tuples, prefixes);
			}
		}
	}
	
	private static TupleSet calcInvalidKeys(TupleSet tuples, TupleSet desiredKeys){
		TupleSet invalidKeys = new TupleSet();
		invalidKeys.addAll(desiredKeys); 
		invalidKeys.removeAll(KeysDeterminator.determineMinimalKeys(tuples));
		
		return invalidKeys; 
	}

	private static TupleSet calcUndesiredKeys(TupleSet tuples, TupleSet desiredKeys){
		TupleSet undesiredKeys = KeysDeterminator.determineMinimalKeys(tuples);
		undesiredKeys.removeAll(desiredKeys);

		return undesiredKeys; 
	}

	private static int[] mutateTuple(int[] tuple, int[] key, int maxValue, int minValue, TupleSet triedMutations) throws Exception{
		int newValue;
		int oldValue;

        NFHelper.getTestLogger().log(Level.INFO, "MUTATE TUPLE: " + TuplesPrinter.toString(tuple));
		
		for (int pos=0; pos<key.length; pos++){
			oldValue = tuple[key[pos]];
			newValue = incrementValue(oldValue, maxValue, minValue);
			tuple[key[pos]] = newValue;

			triedMutations.add(tuple);
			
			if (!triedMutations.contains(tuple)){
				return tuple;
			} else {
				if (pos == key.length-1){
					throw new Exception("No more mutations available for tuple '" + TuplesPrinter.toString(tuple) + "'!");
				}
			}
		}
		return tuple;
	}
	
	private static boolean causesNewKey(int[] newTuple, TupleSet tuples, Vector<Key> expectedKeys){
		TupleSet keys;
		TupleSet temp; 

		temp = tuples;
		temp.add(newTuple);
		
		keys = KeysDeterminator.determineMinimalKeys(temp);
		return !(keys.size() == expectedKeys.size());
	}
	
	private static boolean holdsAllKeys(Vector<int[]> keys, TupleSet tuples, int[] tuple){
        for (int[] key : keys) {
            if (!KeysDeterminator.holdsKey(key, tuple, tuples)) {
                return false;
            }
        }
		return true;
	}

	private static int incrementValue(int value, int maxValue, int minValue){
		int incrementedValue = value + 1;
		if (incrementedValue > maxValue){
			incrementedValue = minValue;
		}
		return incrementedValue;
	}

	private static int[] generateRandomTuple(int valuesNumber, int minValue, int maxValue, RandomCyphersGenerator generator){
		int[] tuple = new int[valuesNumber];
		
		for (int pos=0; pos<valuesNumber; pos++){
			tuple[pos] = generator.nextCypher();
		}
		
		return tuple;
	}	
	
	public static void main(String[] args) {
		int[] key;
		TupleSet tuples;
		TupleSet keys = new TupleSet();
		
		key = new int[] {0, 1};
		keys.add(key);

		key = new int[] {0, 2};
		keys.add(key);
		
		/*
		System.out.println("SPECIFIED KEYS:");
		Iterator it = keys.iterator();
		while (it.hasNext()){
			System.out.println(TuplesPrinter.toString((int[])it.next()));
		}*/
		
		
		tuples = TuplesGenerator.generate(keys, 5, 5, 0, 9);
		
		List<String> prefixes = new Vector<>();
		prefixes.add("A");
		prefixes.add("B");
		prefixes.add("C");
		prefixes.add("D");
		prefixes.add("E");
		prefixes.add("F");
		prefixes.add("G");
		prefixes.add("H");

		//TuplesPrinter.printToConsole(tuples, prefixes);
	}
}
