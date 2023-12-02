package at.jku.dke.etutor.modules.nf.algorithms;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.Collection;

public class Member {

	private Member() {
		// This class is not meant to be instantiated. (Gerald Wimmer, 2023-12-02)
	}

	public static boolean execute(FunctionalDependency dependency, Collection<FunctionalDependency> dependencies){
		return Closure.execute(dependency.getLhsAttributes(), dependencies).containsAll(dependency.getRhsAttributes());
	}
}