package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class RedundandDependenciesAnalysis extends DefaultAnalysis implements Analysis {

	private HashSet redundandDependencies; 

	public RedundandDependenciesAnalysis() {
		super();
		this.redundandDependencies = new HashSet();
	}

	public void setRedundandDependencies(Collection redundandDependencies) {
		this.redundandDependencies.clear();
		this.redundandDependencies.addAll(redundandDependencies);
	}

	public void addRedundandDependency(FunctionalDependency dependency){
		this.redundandDependencies.add(dependency);
	}

	public HashSet getRedundandDependencies() {
		return (HashSet)this.redundandDependencies.clone();
	}
	
	public Iterator iterRedundandDependencies(){
		return this.redundandDependencies.iterator();
	}
}
