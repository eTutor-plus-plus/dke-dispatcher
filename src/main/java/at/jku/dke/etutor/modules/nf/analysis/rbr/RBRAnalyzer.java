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
		StringBuffer temp;
		RBRAnalysis analysis;
		Collection correctDependencies;
		Iterator correctDependenciesIterator;
		Iterator submittedDependenciesIterator;
		FunctionalDependency currCorrectDependency;
		FunctionalDependency currSubmittedDependency;

		RDBDHelper.getLogger().log(Level.INFO, "ANALYZE RBR for base-relation: " + baseRelation);
		RDBDHelper.getLogger().log(Level.INFO, "ANALYZE RBR for sub-relation: " + subRelation);
		
		
		analysis = new RBRAnalysis();
		analysis.setSubmissionSuitsSolution(true);
		
		correctDependencies = ReductionByResolution.execute(baseRelation, subRelation.getAttributes());

		temp = new StringBuffer();
		correctDependenciesIterator = correctDependencies.iterator();
		while (correctDependenciesIterator.hasNext()){
			temp.append(correctDependenciesIterator.next() + "; ");
		}
		RDBDHelper.getLogger().log(Level.INFO, "CORRECT DEPENDENCIES: " + temp.toString());

		correctDependenciesIterator = correctDependencies.iterator();
		while (correctDependenciesIterator.hasNext()){
			currCorrectDependency = (FunctionalDependency)correctDependenciesIterator.next();

			if (!Member.execute(currCorrectDependency, subRelation.getFunctionalDependencies())) {
				analysis.addMissingFunctionalDependency(currCorrectDependency);
				analysis.setSubmissionSuitsSolution(false);

				RDBDHelper.getLogger().log(Level.INFO, "Found missing functional dependency: " + currCorrectDependency.toString());
			}
		}

		submittedDependenciesIterator = subRelation.iterFunctionalDependencies();
		while (submittedDependenciesIterator.hasNext()){
			currSubmittedDependency = (FunctionalDependency)submittedDependenciesIterator.next();

			if (!Member.execute(currSubmittedDependency, correctDependencies)) {
				analysis.addAdditionalFunctionalDependency(currSubmittedDependency);
				analysis.setSubmissionSuitsSolution(false);

				RDBDHelper.getLogger().log(Level.INFO, "Found additional functional dependency: " + currSubmittedDependency.toString());
			}
		}

		return analysis;
	}
}
