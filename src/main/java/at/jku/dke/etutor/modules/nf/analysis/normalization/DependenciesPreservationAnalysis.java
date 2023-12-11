package at.jku.dke.etutor.modules.nf.analysis.normalization;

import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.HashSet;

public class DependenciesPreservationAnalysis extends DefaultAnalysis {

	private final HashSet<FunctionalDependency> lostDependencies;

	public DependenciesPreservationAnalysis() {
		super();
		this.lostDependencies = new HashSet<>();
	}
	
	public void addLostFunctionalDependency(FunctionalDependency lostDependency){
		this.lostDependencies.add(lostDependency);
	}

	public int lostFunctionalDependenciesCount(){
		return this.lostDependencies.size();
	}
	
	public HashSet<FunctionalDependency> getLostFunctionalDependencies(){
		return (HashSet<FunctionalDependency>)this.lostDependencies.clone();
	}
}
