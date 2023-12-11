package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.Collection;
import java.util.HashSet;

public class RedundantDependenciesAnalysis extends DefaultAnalysis {

	private final HashSet<FunctionalDependency> redundantDependencies;

	public RedundantDependenciesAnalysis() {
		super();
		this.redundantDependencies = new HashSet<>();
	}

	public void setRedundantDependencies(Collection<FunctionalDependency> redundantDependencies) {
		this.redundantDependencies.clear();
		this.redundantDependencies.addAll(redundantDependencies);
	}

	public void addRedundantDependency(FunctionalDependency dependency){
		this.redundantDependencies.add(dependency);
	}

	public HashSet<FunctionalDependency> getRedundantDependencies() {
		return (HashSet<FunctionalDependency>)this.redundantDependencies.clone();
	}

}
