package at.jku.dke.etutor.modules.nf.algorithms;

import etutor.modules.rdbd.model.FunctionalDependency;

import java.util.Collection;

public class Member{
	
	public static boolean execute(FunctionalDependency dependency, Collection dependencies){
		return Closure.execute(dependency.getLHSAttributes(), dependencies).containsAll(dependency.getRHSAttributes());
	}
}