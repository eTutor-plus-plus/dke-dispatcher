package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.TreeSet;

public class CoverAnalysis extends DefaultAnalysis implements Analysis {

	private TreeSet missingDependenciesInSet1;
	private TreeSet missingDependenciesInSet2;

	public CoverAnalysis() {
		super();
		
		this.missingDependenciesInSet1 = new TreeSet();
		this.missingDependenciesInSet2 = new TreeSet();
	}

	public TreeSet getMissingDependenciesInSet1() {
		return (TreeSet)this.missingDependenciesInSet1.clone();
	}

	public void addMissingDependencyInSet1(FunctionalDependency missingDependency) {
		this.missingDependenciesInSet1.add(missingDependency);
	}

	public TreeSet getMissingDependenciesInSet2() {
		return (TreeSet)this.missingDependenciesInSet2.clone();
	}

	public void addMissingDependencyInSet2(FunctionalDependency missingDependency) {
		this.missingDependenciesInSet2.add(missingDependency);
	}
}
