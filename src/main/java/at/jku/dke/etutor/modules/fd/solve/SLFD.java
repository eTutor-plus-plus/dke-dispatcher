//package at.jku.dke.etutor.modules.fd.solve;
//
//import at.jku.dke.etutor.modules.fd.entities.Closure;
//import at.jku.dke.etutor.modules.fd.entities.FunctionalDependency;
//
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set;
//import java.util.TreeSet;
//
//public class SLFD {
//
//    private SLFD() {
//        throw new IllegalStateException("Utility class");
//    }
//    public static Closure slfd (String [] a, Set<FunctionalDependency> dependencies) {
//
//
//        Set<String> aNew = new TreeSet<>();
//        Set<String> aOld = new TreeSet<>();
//        aNew.addAll(List.of(a));
//        aOld.addAll(List.of(a));
//
//        for (Iterator<FunctionalDependency> i = dependencies.iterator(); i.hasNext(); ) {
//            FunctionalDependency functionalDependency = i.next();
//            if (aNew.containsAll(List.of(functionalDependency.getLeftSide()))) {
//                aNew.addAll(List.of(functionalDependency.getRightSide()));
//                i.remove();
//            }
//            else if (aNew.containsAll(List.of(functionalDependency.getRightSide()))) {
//                i.remove();
//            }
//        }
//        return null;
//    }
//}
