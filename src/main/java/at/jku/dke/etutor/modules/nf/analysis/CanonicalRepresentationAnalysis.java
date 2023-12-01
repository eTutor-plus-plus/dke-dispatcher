package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CanonicalRepresentationAnalysis extends DefaultAnalysis implements Analysis {

	private final HashSet<FunctionalDependency> notCanonicalDependencies;

	public CanonicalRepresentationAnalysis() {
		super();
		this.notCanonicalDependencies = new HashSet<>();
	}
	
	public void addNotCanonicalDependency(FunctionalDependency dependency){
		this.notCanonicalDependencies.add(dependency);
	}

	public HashSet<FunctionalDependency> getNotCanonicalDependencies(){
		return (HashSet<FunctionalDependency>)this.notCanonicalDependencies.clone();
	}
	
	public void setNotCanonicalDependencies(Collection<FunctionalDependency> c){
		this.notCanonicalDependencies.clear();
		this.notCanonicalDependencies.addAll(c);
	}
}
