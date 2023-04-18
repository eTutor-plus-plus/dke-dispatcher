package at.jku.dke.etutor.modules.fd.solve;

import at.jku.dke.etutor.modules.fd.entities.Dependency;
import at.jku.dke.etutor.modules.fd.entities.Exercise;
import org.basex.query.value.array.Array;

import java.util.*;

/**
 * Contains static methods for calculating the closure of attributes based
 * on a list of given functional dependencies.
 * 
 */
public class CalculateClosure {

	public static void calculateClosure(Exercise exercise) {
		HashSet<Dependency> resultList = new HashSet<>();
		for (Dependency dependency: exercise.getDependencies()) {
			HashSet<String> result= new HashSet<>();
			/** Ausgangspunkt ist die linke Seite jeder Abhängigkeit */
			result.addAll(Arrays.asList(dependency.getLeftSide()));
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
			Dependency toAdd = new Dependency(dependency.getLeftSide(), result.toArray(new String[0]));
			/** Wenn diese noch nicht im Ergebnis enthalten ist, wird sie diesem hinzugefügt */
			for (Dependency dependencyToCheckEquality: resultList) {
				if (dependencyToCheckEquality.equals(toAdd)) 	{
					toAdd = null;
				}
			}
			if (!(toAdd == null)) resultList.add(toAdd);
		}
		/** jede so erstellte Überdeckung wird mit allen anderen kombiniert um alle Kombinationen abzudecken */


		System.out.println(resultList.toString());


	}
}
