package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.HashSet;
import java.util.Iterator;

public class TrivialDependenciesAnalysis extends DefaultAnalysis implements Analysis {

	private HashSet trivialDependencies;

	public TrivialDependenciesAnalysis() {
		super();
		this.trivialDependencies = new HashSet();
	}

	public void addTrivialDependency(FunctionalDependency dependency){
		this.trivialDependencies.add(dependency);
	}

	public HashSet getTrivialDependencies() {
		return (HashSet)this.trivialDependencies.clone();
	}
	
	public Iterator iterTrivialDependencies(){
		return this.trivialDependencies.iterator();
	}

	public void setTrivialDependencies(HashSet trivialDependencies) {
		this.trivialDependencies = trivialDependencies;
	}
}
