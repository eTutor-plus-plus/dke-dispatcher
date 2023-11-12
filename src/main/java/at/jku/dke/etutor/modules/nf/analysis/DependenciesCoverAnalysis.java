package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import etutor.core.evaluation.Analysis;
import etutor.core.evaluation.DefaultAnalysis;

import java.util.Iterator;
import java.util.Vector;

public class DependenciesCoverAnalysis extends DefaultAnalysis implements Analysis {

	Vector missingDependencies;
	Vector additionalDependencies;

	public DependenciesCoverAnalysis() {
		super();
		this.missingDependencies = new Vector();
		this.additionalDependencies = new Vector();
	}

	public Vector getMissingDependencies(){
		return (Vector)this.missingDependencies.clone();
	}
	
	public Vector getAdditionalDependencies(){
		return (Vector)this.additionalDependencies.clone();
	}

	public void addMissingDependency(FunctionalDependency dependency){
		this.missingDependencies.add(dependency);
	}
	
	public Iterator iterMissingDependencies(){
		return this.missingDependencies.iterator();
	}
	
	public void addAdditionalDependency(FunctionalDependency dependency){
		this.additionalDependencies.add(dependency);
	}
	
	public Iterator iterAdditionalDependencies(){
		return this.additionalDependencies.iterator();
	}
}
