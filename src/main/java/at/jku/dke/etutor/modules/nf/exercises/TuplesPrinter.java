package at.jku.dke.etutor.modules.nf.exercises;

import java.util.List;
import java.util.StringJoiner;

public class TuplesPrinter {
	
	public static void printToConsole(TupleSet tuplePopulation, List<String> prefixes) {
		System.out.println(toString(tuplePopulation, prefixes));
	}

	public static String toString(TupleSet tuplePopulation, List<String> prefixes) {
		StringBuilder s = new StringBuilder();

        for (int[] ints : tuplePopulation) {
            s.append(toString(ints, prefixes));
            s.append("\n");
        }
		
		return s.toString();
	}

	public static String toString(int[] tuple) {
		StringJoiner s = new StringJoiner(" ");
		
		for (int i : tuple) {
			s.add(String.valueOf(i));
		}

		return s.toString();
	}

	public static String toString(int[] tuple, List<String> prefixes) {
		String s = "";
		
		for (int i=0; i<tuple.length; i++){
			s = s.concat(prefixes.get(i));
			s = s.concat(String.valueOf(tuple[i]));
			s = s.concat(" ");
		}

		return s;
	}
}
