package at.jku.dke.etutor.modules.nf.analysis.minimalcover;

import at.jku.dke.etutor.modules.nf.analysis.NFAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.HashSet;
import java.util.Set;

public class TrivialDependenciesAnalysis extends NFAnalysis {

	private final Set<FunctionalDependency> trivialDependencies;

	public TrivialDependenciesAnalysis() {
		super();
		this.trivialDependencies = new HashSet<>();
	}

	public void addTrivialDependency(FunctionalDependency dependency){
		this.trivialDependencies.add(dependency);
	}

	public Set<FunctionalDependency> getTrivialDependencies() {
		return new HashSet<>(this.trivialDependencies);
	}

}
