package at.jku.dke.etutor.modules.nf.analysis.rbr;

import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.algorithms.Member;
import at.jku.dke.etutor.modules.nf.algorithms.ReductionByResolution;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.Relation;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;

public class RBRAnalyzer {

	public static RBRAnalysis analyze(Relation baseRelation, Relation subRelation){
		RDBDHelper.getLogger().log(Level.INFO, "ANALYZE RBR for base-relation: " + baseRelation);
		RDBDHelper.getLogger().log(Level.INFO, "ANALYZE RBR for sub-relation: " + subRelation);
		
		
		RBRAnalysis analysis = new RBRAnalysis();
		analysis.setSubmissionSuitsSolution(true);
		
		Collection<FunctionalDependency> correctDependencies = ReductionByResolution.execute(baseRelation, subRelation.getAttributes());

		StringBuilder temp = new StringBuilder();
		Iterator<FunctionalDependency> correctDependenciesIterator = correctDependencies.iterator();
		while (correctDependenciesIterator.hasNext()){
			temp.append(correctDependenciesIterator.next()).append("; ");
		}
		RDBDHelper.getLogger().log(Level.INFO, "CORRECT DEPENDENCIES: " + temp);

		correctDependenciesIterator = correctDependencies.iterator();
		while (correctDependenciesIterator.hasNext()){
			FunctionalDependency currCorrectDependency = correctDependenciesIterator.next();

			if (!Member.execute(currCorrectDependency, subRelation.getFunctionalDependencies())) {
				analysis.addMissingFunctionalDependency(currCorrectDependency);
				analysis.setSubmissionSuitsSolution(false);

				RDBDHelper.getLogger().log(Level.INFO, "Found missing functional dependency: " + currCorrectDependency);
			}
		}

		Iterator<FunctionalDependency> submittedDependenciesIterator = subRelation.iterFunctionalDependencies();
		while (submittedDependenciesIterator.hasNext()){
			FunctionalDependency currSubmittedDependency = submittedDependenciesIterator.next();

			if (!Member.execute(currSubmittedDependency, correctDependencies)) {
				analysis.addAdditionalFunctionalDependency(currSubmittedDependency);
				analysis.setSubmissionSuitsSolution(false);

				RDBDHelper.getLogger().log(Level.INFO, "Found additional functional dependency: " + currSubmittedDependency);
			}
		}

		return analysis;
	}
}
