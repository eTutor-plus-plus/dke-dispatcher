package at.jku.dke.etutor.modules.fd.solve;

import at.jku.dke.etutor.modules.fd.entities.Closure;
import at.jku.dke.etutor.modules.fd.entities.Dependency;
import at.jku.dke.etutor.modules.fd.entities.Exercise;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SLFD {
    /**
     * Algorithm SLFD-Closure(α: set of attributes,
     * F : set of FDs):
     * return: set of attributes
     * This is a linear algorithm for computing the closure of
     * the set α of attributes with respect to the set F of FDs */
    public static Closure slfd (String [] a, Set<Dependency> dependencies) {

        /** αnew =α
         * αold =α */

        Set<String> aNew = new TreeSet<>();
        Set<String> aOld = new TreeSet<>();
        aNew.addAll(List.of(a));
        aOld.addAll(List.of(a));


//     *repeat
        /**
         * foreach FD
         * β → γ in F do */
        for (Iterator<Dependency> i = dependencies.iterator(); i.hasNext(); ) {
            Dependency dependency = i.next();
            /**
             * if β ⊆ αnew then αnew =αnew ∪ γ
             *       F = F − β → γ */
            if (aNew.containsAll(List.of(dependency.getLeftSide()))) {
                aNew.addAll(List.of(dependency.getRightSide()));
                i.remove();
            }
            /** elsif γ ⊆ αnew then
             *     F =F − β →γ */
            else if (aNew.containsAll(List.of(dependency.getRightSide()))) {
                i.remove();
            }

        }


//     *else
//             *F =F − β →γ
//     *F =F ∪
//
//    {
//        β −αnew →γ −αnew
//    }
//     *end if
//            *
//    end foreach
//     *
//
//    until((αnew =αold) or |F | =0)
//            *return α+ =αnew
//     */
        return null;
    }
}
