package at.jku.dke.etutor.modules.fd.solve;

import at.jku.dke.etutor.modules.fd.entities.Closure;
import at.jku.dke.etutor.modules.fd.entities.Dependency;
import at.jku.dke.etutor.modules.fd.entities.Exercise;
import org.basex.query.value.array.Array;

import java.util.*;

/**
 *
 *
 * 
 */
public class CalculateClosure {

	public static Closure calculateClosure(Exercise exercise, String [] leftSide) {

		HashSet<String> result= new HashSet<>();
		/** Ausgangspunkt ist die linke Seite jeder Abhängigkeit */
		result.addAll(Arrays.asList(leftSide));
		int startSize;
		do {
			/** Speicher der Anfangsgröße, wenn kein weiteres Attribut hinzugefügt wird kann die Schleife beendet werden */
			startSize = result.size();
			/** für jede Abhängigkeit wird überprüft ob die gesamte linke Seite bereits in der vorläufigen Überdeckung
			 * beinhaltet ist, wenn ja wird die rechte Seite hinzugefügt */
			for (Dependency toCheck : exercise.getDependencies()) {
				if (result.containsAll(Arrays.asList(toCheck.getLeftSide()))) {
					result.addAll(Arrays.asList(toCheck.getRightSide()));
				}
			}
		} while (startSize != result.size());
		/** Eine neue Abhängigkeit wird erstellt */
		return new Closure(leftSide, result.toArray(new String[0]), exercise);

	}

	public static HashSet<Closure> calculateClosures(Exercise exercise) {
		HashSet<Closure> resultList = new HashSet<>();
		TreeSet<String[]> attributeCombinations = new TreeSet<>(new ArrayComparator());

		/** Alle möglichen Kombinationen */
		for (String attribute: exercise.getRelation()) {
			String[] current = new String[] {attribute};
			attributeCombinations.add(current);
			resursiveAddAll(current,exercise.getRelation(),attributeCombinations);
		}

		/** Nur Hüllen mit Mehrwert aufnehmen */
		for (String [] attribute: attributeCombinations) {
			Closure toAdd = calculateClosure(exercise, attribute);
			if (toAdd.getLeftSide().length != toAdd.getRightSide().length) {
				resultList.add(toAdd);
			}
		}
		return resultList;
	}


	private static void resursiveAddAll(String[] current, String[] attributes, TreeSet<String[]> resultlist) {
		if (current.length == attributes.length) {
			return;
		} else {
			for (String attribute: attributes) {
				resursiveAddAll(addOne(current, attribute, resultlist, attributes),attributes, resultlist);
			}
		}
	}

	private static String [] addOne(String[] combineTo, String element, TreeSet<String[]> resultlist, String [] attributes){
		HashSet<String> original = new HashSet<>();
		for (String c: combineTo) {
			original.add(c);
		}
		if (!original.contains(element)) {
			original.add(element);
			String[] result = original.toArray(new String[0]);
			if (!resultlist.contains(result)) {
				resultlist.add(result);
				return result;
			}
		}
		return attributes;
	}

	static class ArrayComparator implements Comparator<String []> {
		@Override public int compare(String[] e1, String[] e2) {
			return Arrays.compare(e1, e2);
		}
	}

}
