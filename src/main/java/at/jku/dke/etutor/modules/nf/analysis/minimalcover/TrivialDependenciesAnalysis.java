package at.jku.dke.etutor.modules.nf.analysis.minimalcover;

import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.HashSet;

public class TrivialDependenciesAnalysis extends DefaultAnalysis {

	private HashSet<FunctionalDependency> trivialDependencies;

	public TrivialDependenciesAnalysis() {
		super();
		this.trivialDependencies = new HashSet<>();
	}

	public void addTrivialDependency(FunctionalDependency dependency){
		this.trivialDependencies.add(dependency);
	}

	public HashSet<FunctionalDependency> getTrivialDependencies() {
		return (HashSet<FunctionalDependency>)this.trivialDependencies.clone();
	}

	public void setTrivialDependencies(HashSet<FunctionalDependency> trivialDependencies) {
		this.trivialDependencies = trivialDependencies;
	}
}
