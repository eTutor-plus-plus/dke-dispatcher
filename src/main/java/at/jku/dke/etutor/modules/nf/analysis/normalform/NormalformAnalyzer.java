package at.jku.dke.etutor.modules.nf.analysis.normalform;

import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.analysis.*;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.model.NormalformLevelComparator;

import java.util.Collection;
import java.util.logging.Level;

public class NormalformAnalyzer {

	public static NormalformAnalysis analyze(NormalformAnalyzerConfig config){
		NormalformAnalysis analysis = new NormalformAnalysis();
		analysis.setSubmissionSuitsSolution(false);

		StringBuilder temp = new StringBuilder();
        for (Key key : config.getCorrectMinimalKeys()) {
            temp.append(key).append("; ");
        }

		RDBDHelper.getLogger().log(Level.INFO, "Correct Minimal Keys: " + temp);
		
		//CHECK DEPENDENCIES
		for (FunctionalDependency currDependency : config.getRelation().getFunctionalDependencies()){
			RDBDHelper.getLogger().log(Level.INFO, "Check Dependency: " + currDependency);

			if (satisfiesFirstNormalform(analysis, currDependency, config)){
                if (satisfiesSecondNormalform(analysis, currDependency, config)){
					if (satisfiesThirdNormalform(analysis, currDependency, config)){
						satisfiesBoyceCoddNormalform(analysis, currDependency, config);
					}
				}
			}
		}
		
		//COMPUTE OVERALL NORMALFORM LEVEL
		if (config.getRelation().getFunctionalDependencies().isEmpty()){
			if (config.getCorrectMinimalKeys().containsAll(config.getRelation().getMinimalKeys())) {
				analysis.setOverallNormalformLevel(NormalformLevel.BOYCE_CODD);
				analysis.setSubmissionSuitsSolution(true);			
			} else {
				analysis.setOverallNormalformLevel(NormalformLevel.FIRST);
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
		
			NormalformLevelComparator comparator = new NormalformLevelComparator();
			if (comparator.compare(analysis.getOverallNormalformLevel(), config.getDesiredNormalformLevel()) >= 0){
				analysis.setSubmissionSuitsSolution(true);			
			}
		}
		return analysis;
	}

	private static boolean isPrimeAttribute(String attribute, Collection<Key> minimalKeys) {
        for (Key currMinimalKey : minimalKeys) {
            if (currMinimalKey.getAttributes().contains(attribute)) {
                return true;
            }
        }
		return false;
	}

	public static boolean satisfiesFirstNormalform(NormalformAnalysis analysis, FunctionalDependency dependency, NormalformAnalyzerConfig config) {
		FirstNormalformViolation violation = getFirstNormalformViolation(dependency, config);
		boolean isViolated = violation != null;
		
		return !isViolated;
	}

	public static boolean satisfiesSecondNormalform(NormalformAnalysis analysis, FunctionalDependency dependency, NormalformAnalyzerConfig config) {
		SecondNormalformViolation violation = getSecondNormalformViolation(dependency, config);
		boolean isViolated = violation != null;
		
		if (isViolated){
			analysis.addSecondNormalformViolation(violation);
		} 

		RDBDHelper.getLogger().log(Level.INFO, "FINISHED SECOND NF CHECK. Is violated: " + isViolated);
		return !isViolated;
	}
	
	public static boolean satisfiesThirdNormalform(NormalformAnalysis analysis, FunctionalDependency dependency, NormalformAnalyzerConfig config) {
		ThirdNormalformViolation violation = getThirdNormalformViolation(dependency, config);
		boolean isViolated = violation != null;
		
		if (isViolated){
			analysis.addThirdNormalformViolation(violation);
		}

		RDBDHelper.getLogger().log(Level.INFO, "FINISHED THIRD NF CHECK. Is violated: " + isViolated);
		return !isViolated;
	}

	public static boolean satisfiesBoyceCoddNormalform(NormalformAnalysis analysis, FunctionalDependency dependency, NormalformAnalyzerConfig config) {
		BoyceCoddNormalformViolation violation = getBoyceCoddNormalformViolation(dependency, config);
		boolean isViolated = violation != null;
		
		if (isViolated){
			analysis.addBoyceCoddNormalformViolation(violation);
		}
		
		RDBDHelper.getLogger().log(Level.INFO, "FINISHED BC NF CHECK. Is violated: " + isViolated);
		return !isViolated;	
	}
	
	public static FirstNormalformViolation getFirstNormalformViolation(FunctionalDependency dependency, NormalformAnalyzerConfig config) {
		return null;
	}

	public static SecondNormalformViolation getSecondNormalformViolation(FunctionalDependency dependency, NormalformAnalyzerConfig config) {
		boolean isViolated;

		SecondNormalformViolation violation = new SecondNormalformViolation();
		violation.setFunctionalDependency(dependency);

		//Deciding whether RHS contains at least one non-prime attribute
		boolean rhsContainsNonPrimeAttribute = false;
		for (String currAttribute : dependency.getRhsAttributes()) {
			if (!isPrimeAttribute(currAttribute, config.getCorrectMinimalKeys())) {
				rhsContainsNonPrimeAttribute = true;
				violation.addNonPrimeRHSAttribute(currAttribute);
				break;
			}
		}

		//Deciding whether LHS is a partial key
		//Violated, if LHS is a partial key

		if (rhsContainsNonPrimeAttribute) {
			isViolated = false;

            for (Key currKey : config.getCorrectMinimalKeys()) {
                //RDBDHelper.getLogger().log(Level.INFO, "Check Key: " + currKey + " (Key: " + currKey.getAttributes().size() + " -  Dependency: " + dependency.getLhsAttributes().size() + ")");

                if ((currKey.getAttributes().containsAll(dependency.getLhsAttributes())) && (currKey.getAttributes().size() > dependency.getLhsAttributes().size())) {
                    isViolated = true;
                }
            }
        } else {
			isViolated = false;
		}
		
		if (isViolated){
			return violation;
		}
		
		return null;
	}
	
	public static ThirdNormalformViolation getThirdNormalformViolation(FunctionalDependency dependency, NormalformAnalyzerConfig config) {

		boolean isViolated = true;

		ThirdNormalformViolation violation = new ThirdNormalformViolation();
		violation.setFunctionalDependency(dependency);

		//Deciding whether RHS contains at least one non-prime attribute
		boolean rhsContainsNonPrimeAttribute = false;
		for (String currAttribute : dependency.getRhsAttributes()) {
			if (!isPrimeAttribute(currAttribute,config.getCorrectMinimalKeys())) {
				rhsContainsNonPrimeAttribute = true;
				violation.addNonPrimeRHSAttribute(currAttribute);
				break;
			}
		}

		//Deciding whether LHS is a super key
		//Violation, if LHS is not a super key
		if (rhsContainsNonPrimeAttribute) {
			RDBDHelper.getLogger().log(Level.INFO, "RHS comprises non prim attribute. Is violated: " + isViolated);

			for (Key currKey : config.getCorrectMinimalKeys()) {
				//RDBDHelper.getLogger().log(Level.INFO, "Check Key: " + currKey + " (Key: " + currKey.getAttributes().size() + " -  Dependency: " + dependency.getLhsAttributes().size() + ")");
				if (dependency.getLhsAttributes().containsAll(currKey.getAttributes())){
					isViolated = false;
					RDBDHelper.getLogger().log(Level.INFO, "LHS is a super key. Is violated: " + isViolated);
					break;
				}
			}
		} else {
			isViolated = false;
		}
		
		if (isViolated){
			return violation;
		}
		
		return null;
	}

	public static BoyceCoddNormalformViolation getBoyceCoddNormalformViolation(FunctionalDependency dependency, NormalformAnalyzerConfig config) {
		boolean isViolated = true;

        for (Key currKey : config.getCorrectMinimalKeys()) {
            if (dependency.getLhsAttributes().containsAll(currKey.getAttributes())) {
                isViolated = false;
            }
        }

		if (isViolated){
			BoyceCoddNormalformViolation violation = new BoyceCoddNormalformViolation();
			violation.setFunctionalDependency(dependency);

			return violation;
		}
		
		return null;
	}
}