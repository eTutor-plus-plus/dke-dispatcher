package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CanonicalRepresentationAnalysis extends DefaultAnalysis implements Analysis {

	private HashSet notCanonicalDependencies;

	public CanonicalRepresentationAnalysis() {
		super();
		this.notCanonicalDependencies = new HashSet();
	}
	
	public void addNotCanonicalDependency(FunctionalDependency dependency){
		this.notCanonicalDependencies.add(dependency);
	}
	
	public Iterator iterNotCananonicalDependencies(){
		return this.notCanonicalDependencies.iterator();
	}
	
	public HashSet getNotCanonicalDependencies(){
		return (HashSet)this.notCanonicalDependencies.clone();
	}
	
	public void setNotCanonicalDependencies(Collection c){
		this.notCanonicalDependencies.clear();
		this.notCanonicalDependencies.addAll(c);
	}
}
