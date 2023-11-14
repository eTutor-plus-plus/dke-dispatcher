package at.jku.dke.etutor.modules.nf.analysis.normalformdetermination;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.modules.nf.analysis.normalform.NormalformAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;

import java.util.Iterator;
import java.util.Vector;

public class NormalformDeterminationAnalysis extends NormalformAnalysis implements Analysis {

	private NormalformLevel submittedLevel;
	private final Vector<Object[]> wrongLeveledDependencies;

	public NormalformDeterminationAnalysis() {
		super();
		
		this.submittedLevel = NormalformLevel.FIRST;
		this.wrongLeveledDependencies = new Vector<>();
	}

	public void addWrongLeveledDependency(FunctionalDependency dependency, NormalformLevel correctLevel, NormalformLevel foundLevel){
		Object[] entry = new Object[3];
		entry[0] = dependency;
		entry[1] = correctLevel;
		entry[2] = foundLevel;

		this.wrongLeveledDependencies.add(entry);
	}
	
	public Iterator<Object[]> iterWrongLeveledDependencies(){
		return this.wrongLeveledDependencies.iterator();
	}
	
	public Vector<Object[]> getWrongLeveledDependencies(){
		return (Vector<Object[]>)this.wrongLeveledDependencies.clone();
	}

	public int wrongLeveledDependenciesCount(){
		return this.wrongLeveledDependencies.size();
	}

	public NormalformLevel getSubmittedLevel() {
		return this.submittedLevel;
	}

	public void setSubmittedLevel(NormalformLevel level) {
		this.submittedLevel = level;
	}
}
