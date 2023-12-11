package at.jku.dke.etutor.modules.nf.analysis.rbr;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.Vector;

public class RBRAnalysis extends DefaultAnalysis {

	private final Vector<FunctionalDependency> missingFunctionalDependencies;
	private final Vector<FunctionalDependency> additionalFunctionalDependencies;

	public RBRAnalysis() {
		super();
		
		this.missingFunctionalDependencies = new Vector<>();
		this.additionalFunctionalDependencies = new Vector<>();
	}

	public Vector<FunctionalDependency> getMissingFunctionalDependencies() {
		return (Vector<FunctionalDependency>)this.missingFunctionalDependencies.clone();
	}

	public void addMissingFunctionalDependency(FunctionalDependency lostDependency) {
		this.missingFunctionalDependencies.add(lostDependency);
	}

	public Vector<FunctionalDependency> getAdditionalFunctionalDependencies() {
		return (Vector<FunctionalDependency>)this.additionalFunctionalDependencies.clone();
	}

	public void addAdditionalFunctionalDependency(FunctionalDependency additionalDependency) {
		this.additionalFunctionalDependencies.add(additionalDependency);
	}
}
