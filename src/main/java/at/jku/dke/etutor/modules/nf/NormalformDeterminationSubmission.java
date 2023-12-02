package at.jku.dke.etutor.modules.nf;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class NormalformDeterminationSubmission implements Serializable {
	private NormalformLevel overallLevel;
	private final Map<FunctionalDependency, NormalformLevel> normalformViolations;

	public NormalformDeterminationSubmission() {
		this.normalformViolations = new HashMap<>();
		this.overallLevel = NormalformLevel.FIRST;
	}

	public void setNormalformViolations(Map<FunctionalDependency, NormalformLevel> normalformViolations) {
		this.normalformViolations.putAll(normalformViolations);
	}

	public void setNormalformViolation(NormalformLevel violatedLevel, FunctionalDependency dependency) {
		this.normalformViolations.put(dependency, violatedLevel);
	}

	public NormalformLevel getViolatedNormalformLevel(FunctionalDependency dependency) {
		return this.normalformViolations.get(dependency);
	}

	public void setOverallLevel(NormalformLevel level) {
		this.overallLevel = level;
	}

	public NormalformLevel getOverallLevel() {
		return this.overallLevel;
	}
}
