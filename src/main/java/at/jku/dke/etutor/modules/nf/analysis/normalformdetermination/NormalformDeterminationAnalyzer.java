package at.jku.dke.etutor.modules.nf.analysis.normalformdetermination;

import at.jku.dke.etutor.modules.nf.NormalformDeterminationSubmission;
import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.analysis.normalform.NormalformAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.NormalformAnalyzerConfig;
import at.jku.dke.etutor.modules.nf.model.*;

import java.util.Iterator;
import java.util.logging.Level;

public class NormalformDeterminationAnalyzer {

	public static NormalformDeterminationAnalysis analyze(NormalformDeterminationSubmission submission, NormalformAnalyzerConfig config){
		NormalformDeterminationAnalysis analysis = new NormalformDeterminationAnalysis();
		analysis.setSubmissionSuitsSolution(true);
		
		StringBuilder temp = new StringBuilder();
        for (Key key : config.getCorrectMinimalKeys()) {
            temp.append(key).append("; ");
        }
		RDBDHelper.getLogger().log(Level.INFO, "Correct Minimal Keys: " + temp);
		
		//CHECK DEPENDENCIES
		for (FunctionalDependency currDependency : config.getRelation().getFunctionalDependencies()){
			//RDBDHelper.getLogger().log(Level.INFO, "Check Dependency: " + currDependency);

			if (NormalformAnalyzer.satisfiesFirstNormalform(analysis, currDependency, config)){
                if (NormalformAnalyzer.satisfiesSecondNormalform(analysis, currDependency, config)){
					if (NormalformAnalyzer.satisfiesThirdNormalform(analysis, currDependency, config)){
						NormalformAnalyzer.satisfiesBoyceCoddNormalform(analysis, currDependency, config);
					}
				}
			}
		}
		
		//COMPUTE OVERALL NORMALFORM LEVEL
		if (config.getRelation().getFunctionalDependencies().isEmpty()){
			if (config.getCorrectMinimalKeys().containsAll(config.getRelation().getMinimalKeys())) {
				analysis.setOverallNormalformLevel(NormalformLevel.BOYCE_CODD);
			} else {
				analysis.setOverallNormalformLevel(NormalformLevel.FIRST);
				analysis.setSubmissionSuitsSolution(false);
			}
		} else {
			analysis.setOverallNormalformLevel(NormalformLevel.BOYCE_CODD);
			if (!analysis.getBoyceCoddNormalformViolations().isEmpty()){
				analysis.setOverallNormalformLevel(NormalformLevel.THIRD);
			}
			if (!analysis.getThirdNormalformViolations().isEmpty()){
				analysis.setOverallNormalformLevel(NormalformLevel.SECOND);
			}
			if (!analysis.getSecondNormalformViolations().isEmpty()){
				analysis.setOverallNormalformLevel(NormalformLevel.FIRST);
			}
		}

		//DETERMINE WRONG LEVELED DEPENDENCIES
		Integer currID;
		NormalformLevel foundViolatedLevel;
		NormalformLevel correctViolatedLevel;
		for (FunctionalDependency currDependency : config.getRelation().getFunctionalDependencies()){
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
