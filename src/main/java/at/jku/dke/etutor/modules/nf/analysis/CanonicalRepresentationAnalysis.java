package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CanonicalRepresentationAnalysis extends DefaultAnalysis {

	private final HashSet<FunctionalDependency> notCanonicalDependencies;

	public CanonicalRepresentationAnalysis() {
		super();
		this.notCanonicalDependencies = new HashSet<>();
	}
	
	public void addNotCanonicalDependency(FunctionalDependency dependency){
		this.notCanonicalDependencies.add(dependency);
	}

	public Set<FunctionalDependency> getNotCanonicalDependencies() {
		return new HashSet<>(this.notCanonicalDependencies);
	}
	
	public void setNotCanonicalDependencies(Collection<FunctionalDependency> c){
		this.notCanonicalDependencies.clear();
		this.notCanonicalDependencies.addAll(c);
	}
}
