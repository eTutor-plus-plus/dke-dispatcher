package at.jku.dke.etutor.modules.nf.analysis.rbr;

import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.algorithms.Member;
import at.jku.dke.etutor.modules.nf.algorithms.ReductionByResolution;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.Relation;

import java.util.Collection;
import java.util.logging.Level;

public class RBRAnalyzer {

	/**
	 * Tests whether a subrelation contains the correct, minimal functional dependencies with respect to the base
	 * relation.
	 * @param baseRelation The base relation
	 * @param subRelation The subrelation
	 * @return An <code>RBRAnalysis</code> for the supplied subrelation with respect to the base relation
	 */
	public static RBRAnalysis analyze(Relation baseRelation, Relation subRelation){
		RDBDHelper.getLogger().log(Level.INFO, "ANALYZE RBR for base-relation: " + baseRelation);
		RDBDHelper.getLogger().log(Level.INFO, "ANALYZE RBR for sub-relation: " + subRelation);
		
		RBRAnalysis analysis = new RBRAnalysis();
		analysis.setSubmissionSuitsSolution(true);
		
		Collection<FunctionalDependency> correctDependencies = ReductionByResolution.execute(baseRelation, subRelation.getAttributes());

		StringBuilder temp = new StringBuilder();
		for (FunctionalDependency currDependency : correctDependencies){
			temp.append(currDependency).append("; ");
		}
		RDBDHelper.getLogger().log(Level.INFO, "CORRECT DEPENDENCIES: " + temp);

		/*
		 * Check if there is an equivalent for each correct dependency in the submission (i.e., whether any
		 * dependencies are missing from the submission (Gerald Wimmer, 2024-01-01).
		 */
		for (FunctionalDependency currCorrectDependency : correctDependencies){
			if (!Member.execute(currCorrectDependency, subRelation.getFunctionalDependencies())) {
				analysis.addMissingFunctionalDependency(currCorrectDependency);
				analysis.setSubmissionSuitsSolution(false);

				RDBDHelper.getLogger().log(Level.INFO, "Found missing functional dependency: " + currCorrectDependency);
			}
		}

		/*
		 * Check if there is an equivalent for each submitted dependency in the correct solution (i.e., whether there
		 * are any superfluous dependencies in the submission) (Gerald Wimmer, 2024-01-01).
		 */
		for (FunctionalDependency currSubmittedDependency : subRelation.getFunctionalDependencies()){
			if (!Member.execute(currSubmittedDependency, correctDependencies)) {
				analysis.addAdditionalFunctionalDependency(currSubmittedDependency);
				analysis.setSubmissionSuitsSolution(false);

				RDBDHelper.getLogger().log(Level.INFO, "Found additional functional dependency: " + currSubmittedDependency);
			}
		}

		return analysis;
	}
}
