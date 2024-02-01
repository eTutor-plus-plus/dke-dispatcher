package at.jku.dke.etutor.modules.nf.analysis.minimalcover;

import at.jku.dke.etutor.modules.nf.analysis.NFAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.LinkedList;
import java.util.List;

public class DependenciesCoverAnalysis extends NFAnalysis {

	final List<FunctionalDependency> missingDependencies;
	final List<FunctionalDependency> additionalDependencies;

	public DependenciesCoverAnalysis() {
		super();
		this.missingDependencies = new LinkedList<>();
		this.additionalDependencies = new LinkedList<>();
	}

	public List<FunctionalDependency> getMissingDependencies(){
		return new LinkedList<>(this.missingDependencies);
	}
	
	public List<FunctionalDependency> getAdditionalDependencies(){
		return new LinkedList<>(this.additionalDependencies);
	}

	public void addMissingDependency(FunctionalDependency dependency){
		this.missingDependencies.add(dependency);
	}

	public void addAdditionalDependency(FunctionalDependency dependency){
		this.additionalDependencies.add(dependency);
	}

}
