package at.jku.dke.etutor.modules.nf;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NormalformDeterminationSubmission implements Serializable {

	private final Map<Integer, FunctionalDependency> dependencyIDs;
	private NormalformLevel overallLevel;
	private final Map<Integer, NormalformLevel> normalformViolations;

	public NormalformDeterminationSubmission() {
		super();

		this.dependencyIDs = new HashMap<>();
		this.normalformViolations = new HashMap<>();
		this.overallLevel = NormalformLevel.FIRST;
	}

	public Integer getIDForDependency(FunctionalDependency dependency) {
		Integer currID;
		FunctionalDependency currDependency;

        for (Integer integer : this.dependencyIDs.keySet()) {
            currID = integer;
            currDependency = this.dependencyIDs.get(currID);
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
		return this.dependencyIDs.get(id);
	}

	public Iterator<Integer> iterDependencyIDs() {
		return this.dependencyIDs.keySet().iterator();
	}

	public void setNormalformViolation(NormalformLevel violatedLevel, Integer dependencyID) {
		this.normalformViolations.put(dependencyID, violatedLevel);
	}

	public NormalformLevel getViolatedNormalformLevel(Integer dependencyID) {
		return this.normalformViolations.get(dependencyID);
	}

	public void setOverallLevel(NormalformLevel level) {
		this.overallLevel = level;
	}

	public NormalformLevel getOverallLevel() {
		return this.overallLevel;
	}
}