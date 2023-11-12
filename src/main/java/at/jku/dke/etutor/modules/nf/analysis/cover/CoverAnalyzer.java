package at.jku.dke.etutor.modules.nf.analysis.cover;

import at.jku.dke.etutor.modules.nf.algorithms.Member;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.Collection;
import java.util.Iterator;

public class CoverAnalyzer {

	public static CoverAnalysis analyze(Collection fd1, Collection fd2){
		CoverAnalysis analysis;
		Iterator dependenciesIterator;
		FunctionalDependency currDependency;
 
 		analysis = new CoverAnalysis();
		analysis.setSubmissionSuitsSolution(true);
 
		dependenciesIterator = fd1.iterator();
		while (dependenciesIterator.hasNext()) {
			currDependency = (FunctionalDependency)dependenciesIterator.next();
			
			if (!Member.execute(currDependency, fd2)) {
				analysis.setSubmissionSuitsSolution(false);
				analysis.addMissingDependencyInSet2(currDependency);
			}
		}

		dependenciesIterator = fd2.iterator();
		while (dependenciesIterator.hasNext()) {
			currDependency = (FunctionalDependency)dependenciesIterator.next();

			if (!Member.execute(currDependency, fd1)) {
				analysis.setSubmissionSuitsSolution(false);
				analysis.addMissingDependencyInSet2(currDependency);
			}
		}

		return analysis;		
	}
}
