package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.HashSet;
import java.util.Iterator;

public class DependenciesPreservationAnalysis extends DefaultAnalysis implements Analysis {

	private final HashSet<FunctionalDependency> lostDependencies;

	public DependenciesPreservationAnalysis() {
		super();
		this.lostDependencies = new HashSet<>();
	}
	
	public void addLostFunctionalDependency(FunctionalDependency lostDependency){
		this.lostDependencies.add(lostDependency);
	}

	public Iterator<FunctionalDependency> iterLostFunctionalDependencies(){
		return this.lostDependencies.iterator();
	}
	
	public int lostFunctionalDependenciesCount(){
		return this.lostDependencies.size();
	}
	
	public HashSet<FunctionalDependency> getLostFunctionalDependencies(){
		return (HashSet<FunctionalDependency>)this.lostDependencies.clone();
	}
}
