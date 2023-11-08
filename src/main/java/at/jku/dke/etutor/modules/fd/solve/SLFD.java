package at.jku.dke.etutor.modules.fd.solve;

import at.jku.dke.etutor.modules.fd.entities.Closure;
import at.jku.dke.etutor.modules.fd.entities.FunctionalDependency;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SLFD {

    private SLFD() {
        throw new IllegalStateException("Utility class");
    }
    /**
     * Algorithm SLFD-Closure(α: set of attributes,
     * F : set of FDs):
     * return: set of attributes
     * This is a linear algorithm for computing the closure of
     * the set α of attributes with respect to the set F of FDs */
    public static Closure slfd (String [] a, Set<FunctionalDependency> dependencies) {

        /** αnew =α
         * αold =α */

        Set<String> aNew = new TreeSet<>();
        Set<String> aOld = new TreeSet<>();
        aNew.addAll(List.of(a));
        aOld.addAll(List.of(a));

        /**
         * foreach FD
         * β → γ in F do */
        for (Iterator<FunctionalDependency> i = dependencies.iterator(); i.hasNext(); ) {
            FunctionalDependency functionalDependency = i.next();
            /**
             * if β ⊆ αnew then αnew =αnew ∪ γ
             *       F = F − β → γ */
            if (aNew.containsAll(List.of(functionalDependency.getLeftSide()))) {
                aNew.addAll(List.of(functionalDependency.getRightSide()));
                i.remove();
            }
            /** elsif γ ⊆ αnew then
             *     F =F − β →γ */
            else if (aNew.containsAll(List.of(functionalDependency.getRightSide()))) {
                i.remove();
            }

        }
        return null;
    }
}
