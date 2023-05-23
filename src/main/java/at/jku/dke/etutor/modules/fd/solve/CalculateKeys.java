package at.jku.dke.etutor.modules.fd.solve;

import at.jku.dke.etutor.modules.fd.entities.Closure;
import at.jku.dke.etutor.modules.fd.entities.Dependency;
import at.jku.dke.etutor.modules.fd.entities.Relation;
import at.jku.dke.etutor.modules.fd.entities.Key;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CalculateKeys {
    private CalculateKeys() {
        throw new IllegalStateException("Utility class");
    }

    public static Set<Key> calculateKeys(Relation relation) {
        Set<Closure> candidateKeys = calculateSuperKeys(relation);
        Set<Key> superKeys = calculateCandidateKeys(candidateKeys);

        return superKeys;
    }

    /** Aus den Superschlüsseln, alle Schlüsselkandidaten */
    private static Set<Key> calculateCandidateKeys(Set<Closure> superKeys) {
        Set<Key> candidateKeys = new HashSet<>();
        for (Closure key: superKeys) {
            Boolean isCandidateKey = true;
            for (Closure smallerKey: superKeys) {
                if (!key.equals(smallerKey) && Set.of(key.getLeftSide()).containsAll(Set.of(smallerKey.getLeftSide()))) {
                    isCandidateKey = false;
                    break;
                }
            }
            if (isCandidateKey) {
                candidateKeys.add(new Key(key));
            }
        }
        return candidateKeys;
    }

    private static int minLength(Set<Closure> candidateKeys) {
        int minimum = Integer.MAX_VALUE;
        for (Dependency key: candidateKeys){
            if (key.getLeftSide().length<minimum) {
                minimum = key.getLeftSide().length;
            }
        }
        return minimum;
    }
    /** Alle Superschlüssel finden */
    public static Set<Closure> calculateSuperKeys(Relation relation) {
        Set<Closure> superKeys = new HashSet<>();
        for (Closure closure : relation.getClosures()) {
            if (Arrays.equals(closure.getRightSide(), relation.getAttributes())) {
                superKeys.add(closure);
            }
        }
        return superKeys;
    }
}
