package at.jku.dke.etutor.modules.nf;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

public class NormalformDeterminationSubmission implements Serializable {

	private final HashMap<Integer, FunctionalDependency> dependencyIDs;
	private NormalformLevel overallLevel;
	private final HashMap normalformViolations;

	public NormalformDeterminationSubmission() {
		super();

		this.dependencyIDs = new HashMap<>();
		this.normalformViolations = new HashMap<>();
		this.overallLevel = NormalformLevel.FIRST;
	}

	public Integer getIDForDependency(FunctionalDependency dependency) {
		Iterator iter;
		Integer currID;
		FunctionalDependency currDependency;

		iter = this.dependencyIDs.keySet().iterator();
		while (iter.hasNext()) {
			currID = (Integer) iter.next();
			currDependency = (FunctionalDependency) this.dependencyIDs
					.get(currID);
			if (currDependency.equals(dependency)) {
				return currID;
			}
		}

		return null;
	}

	public void setDependencyID(FunctionalDependency dependency, Integer id) {
		this.dependencyIDs.put(id, dependency);
	}

	public FunctionalDependency getDependency(Integer id) {
		return (FunctionalDependency) this.dependencyIDs.get(id);
	}

	public Iterator<Integer> iterDependencyIDs() {
		return this.dependencyIDs.keySet().iterator();
	}

	public void setNormalformVioaltion(NormalformLevel violatedLevel,
			Integer dependencyID) {
		this.normalformViolations.put(dependencyID, violatedLevel);
	}

	public NormalformLevel getViolatedNormalformLevel(Integer dependencyID) {
		if (this.normalformViolations.containsKey(dependencyID)) {
			if (this.normalformViolations.get(dependencyID) != null) {
				return (NormalformLevel) this.normalformViolations
						.get(dependencyID);
			}
		}
		return null;
	}

	public void setOverallLevel(NormalformLevel level) {
		this.overallLevel = level;
	}

	public NormalformLevel getOverallLevel() {
		return this.overallLevel;
	}
}
