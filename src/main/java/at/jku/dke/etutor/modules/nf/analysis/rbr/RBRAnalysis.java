package at.jku.dke.etutor.modules.nf.analysis.rbr;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.Iterator;
import java.util.Vector;

public class RBRAnalysis extends DefaultAnalysis implements Analysis {

	private Vector missingFunctionalDependencies;
	private Vector additionalFunctionalDependencies;

	public RBRAnalysis() {
		super();
		
		this.missingFunctionalDependencies = new Vector();
		this.additionalFunctionalDependencies = new Vector();		
	}

	public Vector getMissingFunctionalDependencies() {
		return (Vector)this.missingFunctionalDependencies.clone();
	}

	public Iterator iterMissingFunctionalDependencies(){
		return this.missingFunctionalDependencies.iterator();
	}

	public void addMissingFunctionalDependency(FunctionalDependency lostDependency) {
		this.missingFunctionalDependencies.add(lostDependency);
	}

	public Vector getAdditionalFunctionalDependencies() {
		return (Vector)this.additionalFunctionalDependencies.clone();
	}

	public Iterator iterAdditionalFunctionalDependencies(){
		return this.additionalFunctionalDependencies.iterator();
	}

	public void addAdditionalFunctionalDependency(FunctionalDependency additionalDependency) {
		this.additionalFunctionalDependencies.add(additionalDependency);
	}
}
