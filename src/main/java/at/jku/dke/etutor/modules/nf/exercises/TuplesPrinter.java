package at.jku.dke.etutor.modules.nf.exercises;

import java.util.StringJoiner;
import java.util.Vector;

public class TuplesPrinter {
	
	public static void printToConsole(TupleSet tuplePopulation, Vector<String> prefixes) {
		System.out.println(toString(tuplePopulation, prefixes));
	}

	public static String toString(TupleSet tuplePopulation, Vector<String> prefixes) {
		String s = "";

        for (int[] ints : tuplePopulation) {
            s = s.concat(toString(ints, prefixes));
            s = s.concat("\n");
        }
		
		return s;
	}

	public static String toString(int[] tuple) {
		StringJoiner s = new StringJoiner(" ");
		
		for (int i : tuple) {
			s.add(String.valueOf(i));
		}

		return s.toString();
	}

	public static String toString(int[] tuple, Vector<String> prefixes) {
		String s = "";
		
		for (int i=0; i<tuple.length; i++){
			s = s.concat(prefixes.get(i));
			s = s.concat(String.valueOf(tuple[i]));
			s = s.concat(" ");
		}

		return s;
	}
}
