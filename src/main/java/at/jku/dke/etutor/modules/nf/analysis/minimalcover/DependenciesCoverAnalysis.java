package at.jku.dke.etutor.modules.nf.analysis.minimalcover;

import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.Vector;

public class DependenciesCoverAnalysis extends DefaultAnalysis {

	final Vector<FunctionalDependency> missingDependencies;
	final Vector<FunctionalDependency> additionalDependencies;

	public DependenciesCoverAnalysis() {
		super();
		this.missingDependencies = new Vector<>();
		this.additionalDependencies = new Vector<>();
	}

	public Vector<FunctionalDependency> getMissingDependencies(){
		return (Vector<FunctionalDependency>)this.missingDependencies.clone();
	}
	
	public Vector<FunctionalDependency> getAdditionalDependencies(){
		return (Vector<FunctionalDependency>)this.additionalDependencies.clone();
	}

	public void addMissingDependency(FunctionalDependency dependency){
		this.missingDependencies.add(dependency);
	}

	public void addAdditionalDependency(FunctionalDependency dependency){
		this.additionalDependencies.add(dependency);
	}

}
