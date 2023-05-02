package at.jku.dke.etutor.modules.fd.solve;

import at.jku.dke.etutor.modules.fd.entities.Closure;
import at.jku.dke.etutor.modules.fd.entities.Exercise;
import at.jku.dke.etutor.modules.fd.entities.Key;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CalculateKeys {
    private CalculateKeys() {
        throw new IllegalStateException("Utility class");
    }

    public static Set<Key> calculateKeys(Exercise exercise) {
        Set<Closure> closures = exercise.getClosures();
        Set<Closure> candidateKeys = new HashSet<>();
        int minimum = Integer.MAX_VALUE;
        /** Alle Schlüsselkandidaten finden und geringste Anzahl an notwendigen Attribute */
        for (Closure closure : closures) {
            if (Arrays.equals(closure.getRightSide(),exercise.getRelation())) {
                candidateKeys.add(closure);
                if (minimum > closure.getLeftSide().length) {
                    minimum = closure.getLeftSide().length;
                }
            }
        }

        Set<Key> superKeys = new HashSet<>();
        for (Closure closure: candidateKeys) {
            if (closure.getLeftSide().length == minimum) {
                superKeys.add(new Key(closure.getLeftSide(), closure.getRightSide(), exercise));
            }
        }
        return superKeys;
    }
}
