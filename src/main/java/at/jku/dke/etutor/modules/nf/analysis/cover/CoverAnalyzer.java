package at.jku.dke.etutor.modules.nf.analysis.cover;

import at.jku.dke.etutor.modules.nf.algorithms.Member;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.Collection;
import java.util.Iterator;

public class CoverAnalyzer {

	public static CoverAnalysis analyze(Collection<FunctionalDependency> fd1, Collection<FunctionalDependency> fd2) {
		CoverAnalysis analysis = new CoverAnalysis();
		analysis.setSubmissionSuitsSolution(true);

		for (FunctionalDependency currDependency : fd1) {
			if (!Member.execute(currDependency, fd2)) {
				analysis.setSubmissionSuitsSolution(false);
				analysis.addMissingDependencyInSet2(currDependency);
			}
		}

		for (FunctionalDependency currDependency : fd2) {
			if (!Member.execute(currDependency, fd1)) {
				analysis.setSubmissionSuitsSolution(false);
				analysis.addMissingDependencyInSet2(currDependency);
			}
		}

		return analysis;		
	}
}
