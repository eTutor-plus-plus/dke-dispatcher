package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class RedundantDependenciesAnalysis extends DefaultAnalysis implements Analysis {

	private HashSet redundantDependencies;

	public RedundantDependenciesAnalysis() {
		super();
		this.redundantDependencies = new HashSet();
	}

	public void setRedundantDependencies(Collection redundantDependencies) {
		this.redundantDependencies.clear();
		this.redundantDependencies.addAll(redundantDependencies);
	}

	public void addRedundandDependency(FunctionalDependency dependency){
		this.redundantDependencies.add(dependency);
	}

	public HashSet getRedundantDependencies() {
		return (HashSet)this.redundantDependencies.clone();
	}
	
	public Iterator iterRedundandDependencies(){
		return this.redundantDependencies.iterator();
	}
}
