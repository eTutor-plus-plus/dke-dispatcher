package at.jku.dke.etutor.modules.nf.analysis;

import etutor.core.evaluation.Analysis;
import etutor.core.evaluation.DefaultAnalysis;
import etutor.modules.rdbd.model.FunctionalDependency;

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
