package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.modules.nf.NormalformDeterminationSubmission;
import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.model.NormalformLevelComparator;

import java.util.Iterator;
import java.util.logging.Level;

public class NormalformDeterminationAnalyzer {

	public static NormalformDeterminationAnalysis analyze(NormalformDeterminationSubmission submission, NormalformAnalyzerConfig config){
		NormalformDeterminationAnalysis analysis;
		Iterator dependenciesIterator;
		FunctionalDependency currDependency;
		
		analysis = new NormalformDeterminationAnalysis();
		analysis.setSubmissionSuitsSolution(true);
		dependenciesIterator = config.getRelation().iterFunctionalDependencies();
		
		StringBuffer temp = new StringBuffer();
		Iterator keysIterator = config.getCorrectMinimalKeys().iterator();
		while (keysIterator.hasNext()){
			temp.append(keysIterator.next() + "; ");
		}
		RDBDHelper.getLogger().log(Level.INFO, "Correct Minimal Keys: " + temp.toString());
		
		//CHECK DEPENDENCIES
		while (dependenciesIterator.hasNext()){
			currDependency = (FunctionalDependency)dependenciesIterator.next();
			//RDBDHelper.getLogger().log(Level.INFO, "Check Dependency: " + currDependency);

			if (NormalformAnalyzer.satisfiesFirstNormalform(analysis, currDependency, config)){;
				if (NormalformAnalyzer.satisfiesSecondNormalform(analysis, currDependency, config)){
					if (NormalformAnalyzer.satisfiesThirdNormalform(analysis, currDependency, config)){
						NormalformAnalyzer.satisfiesBoyceCoddNormalform(analysis, currDependency, config);
					}
				}
			}
		}
		
		//COMPUTE OVERALL NORMALFORM LEVEL
		if (config.getRelation().getFunctionalDependencies().size() == 0){
			if (config.getCorrectMinimalKeys().containsAll(config.getRelation().getMinimalKeys())) {
				analysis.setOverallNormalformLevel(NormalformLevel.BOYCE_CODD);
			} else {
				analysis.setOverallNormalformLevel(NormalformLevel.FIRST);
				analysis.setSubmissionSuitsSolution(false);
			}
		} else {
			analysis.setOverallNormalformLevel(NormalformLevel.BOYCE_CODD);
			if (analysis.getBoyceCottNormalformViolations().size() != 0){
				analysis.setOverallNormalformLevel(NormalformLevel.THIRD);
			}
			if (analysis.getThirdNormalformViolations().size() != 0){
				analysis.setOverallNormalformLevel(NormalformLevel.SECOND);
			}
			if (analysis.getSecondNormalformViolations().size() != 0){
				analysis.setOverallNormalformLevel(NormalformLevel.FIRST);
			}
		}

		//DETERMINE WRONG LEVELED DEPENDENCIES
		Integer currID;
		NormalformLevel foundViolatedLevel;
		NormalformLevel correctViolatedLevel;
		Iterator iter = config.getRelation().iterFunctionalDependencies();
		while (iter.hasNext()){
			currDependency = (FunctionalDependency)iter.next();
			currID = submission.getIDForDependency(currDependency);
			RDBDHelper.getLogger().log(Level.INFO, "Check NF-Level of dependency: " + currDependency);
			
			foundViolatedLevel = submission.getViolatedNormalformLevel(currID);
			correctViolatedLevel = analysis.getViolatedNormalformLevel(currDependency);
			RDBDHelper.getLogger().log(Level.INFO, "Found: " + foundViolatedLevel + " Correct: " + correctViolatedLevel);
			
			if (correctViolatedLevel == null){
				if (foundViolatedLevel != null){
					RDBDHelper.getLogger().log(Level.INFO, "ADD WRONG LEVELD DEPENDENCY");
					analysis.addWrongLeveledDependency(currDependency, correctViolatedLevel, foundViolatedLevel);
					analysis.setSubmissionSuitsSolution(false);							
				}
			} else {
				if (!correctViolatedLevel.equals(foundViolatedLevel)){
					RDBDHelper.getLogger().log(Level.INFO, "ADD WRONG LEVELD DEPENDENCY");
					analysis.addWrongLeveledDependency(currDependency, correctViolatedLevel, foundViolatedLevel);
					analysis.setSubmissionSuitsSolution(false);							
				}
			}
		}
		
		//DETERMINE CORRECTNESS OF OVERALL NF LEVEL
		NormalformLevelComparator comparator = new NormalformLevelComparator();
		analysis.setSubmittedLevel(submission.getOverallLevel());
		if (comparator.compare(analysis.getOverallNormalformLevel(), analysis.getSubmittedLevel()) != 0){
			analysis.setSubmissionSuitsSolution(false);			
		}
		
		return analysis;
	}
}
