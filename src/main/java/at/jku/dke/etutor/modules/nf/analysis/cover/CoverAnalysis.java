package at.jku.dke.etutor.modules.nf.analysis.cover;

import at.jku.dke.etutor.modules.nf.analysis.NFAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.TreeSet;

public class CoverAnalysis extends NFAnalysis {

	private final TreeSet<FunctionalDependency> missingDependenciesInSet1;
	private final TreeSet<FunctionalDependency> missingDependenciesInSet2;

	public CoverAnalysis() {
		super();
		
		this.missingDependenciesInSet1 = new TreeSet<>();
		this.missingDependenciesInSet2 = new TreeSet<>();
	}

	public TreeSet<FunctionalDependency> getMissingDependenciesInSet1() {
		return (TreeSet<FunctionalDependency>)this.missingDependenciesInSet1.clone();
	}

	public void addMissingDependencyInSet1(FunctionalDependency missingDependency) {
		this.missingDependenciesInSet1.add(missingDependency);
	}

	public TreeSet<FunctionalDependency> getMissingDependenciesInSet2() {
		return (TreeSet<FunctionalDependency>)this.missingDependenciesInSet2.clone();
	}

	public void addMissingDependencyInSet2(FunctionalDependency missingDependency) {
		this.missingDependenciesInSet2.add(missingDependency);
	}
}
