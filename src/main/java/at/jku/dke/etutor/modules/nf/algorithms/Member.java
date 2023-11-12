package at.jku.dke.etutor.modules.nf.algorithms;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.Collection;

public class Member{
	
	public static boolean execute(FunctionalDependency dependency, Collection dependencies){
		return Closure.execute(dependency.getLHSAttributes(), dependencies).containsAll(dependency.getRHSAttributes());
	}
}