package at.jku.dke.etutor.modules.nf.exercises;

import java.util.Iterator;
import java.util.Vector;

public class TuplesPrinter {
	
	public static void printToConsole(TupleSet tuplePopulation, Vector<String> prefixes){
		System.out.println(toString(tuplePopulation, prefixes));
	}

	public static String toString(TupleSet tuplePopulation, Vector<String> prefixes){
		String s = "";
		Iterator tuplesIterator = tuplePopulation.iterator();
		
		while (tuplesIterator.hasNext()){
			s = s.concat(toString((int[])tuplesIterator.next(), prefixes));
			s = s.concat("\n");
		}
		
		return s;
	}

	public static String toString(int[] tuple){
		String s = "";
		
		for (int i=0; i<tuple.length; i++){
			s = s.concat(String.valueOf(tuple[i]));
			if (!(i == tuple.length-1)){
				s = s.concat(" ");
			}
		}

		return s;
	}

	public static String toString(int[] tuple, Vector<String> prefixes){
		String s = "";
		
		for (int i=0; i<tuple.length; i++){
			s = s.concat(prefixes.get(i));
			s = s.concat(String.valueOf(tuple[i]));
			s = s.concat(" ");
		}

		return s;
	}
}
