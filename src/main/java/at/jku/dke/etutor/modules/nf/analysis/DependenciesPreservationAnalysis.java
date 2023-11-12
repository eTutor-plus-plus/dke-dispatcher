package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import etutor.core.evaluation.Analysis;
import etutor.core.evaluation.DefaultAnalysis;

import java.util.HashSet;
import java.util.Iterator;

public class DependenciesPreservationAnalysis extends DefaultAnalysis implements Analysis {

	private HashSet lostDependencies;

	public DependenciesPreservationAnalysis() {
		super();
		this.lostDependencies = new HashSet();
	}
	
	public void addLostFunctionalDependency(FunctionalDependency lostDependency){
		this.lostDependencies.add(lostDependency);
	}

	public Iterator iterLostFunctionalDependencies(){
		return this.lostDependencies.iterator();
	}
	
	public int lostFunctionalDependenciesCount(){
		return this.lostDependencies.size();
	}
	
	public HashSet getLostFunctionalDependencies(){
		return (HashSet)this.lostDependencies.clone();
	}
}
