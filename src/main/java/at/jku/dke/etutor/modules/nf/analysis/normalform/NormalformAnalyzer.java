package at.jku.dke.etutor.modules.nf.analysis.normalform;

import at.jku.dke.etutor.modules.nf.NFHelper;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.model.NormalformLevelComparator;

import java.util.Collection;
import java.util.logging.Level;

public class NormalformAnalyzer {

	private NormalformAnalyzer() {
		// This class is not meant to be instantiated. (Gerald Wimmer, 2023-12-01)
	}

	/**
	 * Tests which normal form a relation conforms to, and whether that is greater or equal to the desired normal form.
	 * @param config The configuration for this analysis
	 * @return A <code>NormalformAnalysis</code> for the given configuration
	 */
	public static NormalformAnalysis analyze(NormalformAnalyzerConfig config){
		NormalformAnalysis analysis = new NormalformAnalysis();
		analysis.setSubmissionSuitsSolution(false);

		StringBuilder temp = new StringBuilder();
        for (Key key : config.getCorrectMinimalKeys()) {
            temp.append(key).append("; ");
        }

		NFHelper.getLogger().log(Level.INFO, "Correct Minimal Keys: " + temp);
		
		//CHECK DEPENDENCIES
		for (FunctionalDependency currDependency : config.getRelation().getFunctionalDependencies()){
			NFHelper.getLogger().log(Level.INFO, "Check Dependency: " + currDependency);

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

			// Note: config.getDesiredNormalformLevel() could be null. (Gerald Wimmer, 2023-12-02)
			// Note: When would that NOT be an error? (Gerald Wimmer, 2024-01-12)
			NormalformLevelComparator comparator = new NormalformLevelComparator();
			if (analysis.getOverallNormalformLevel().compareTo(config.getDesiredNormalformLevel()) >= 0) {
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

	/**
	 * Checks whether all attributes in the relation are atomic (i.e., contain only one value).
	 * @param analysis ignored.
	 * @param dependency FunctionalDependency which is to be examined for violating the first normal form
	 * @param config Ignored
	 * @return whether all attributes in the relation are atomic (i.e., contain only one value).
	 */
	public static boolean satisfiesFirstNormalform(NormalformAnalysis analysis, FunctionalDependency dependency, NormalformAnalyzerConfig config) {
		FirstNormalformViolation violation = getFirstNormalformViolation(dependency, config);
		boolean isViolated = violation != null;
		
		return !isViolated;
	}

	/**
	 * Checks if the right-hand-side of the supplied dependency is non-prime AND the left-hand-side is a partial key.
	 * @param dependency FunctionalDependency which is to be examined for violating the second normal form
	 * @param config
	 * @return A <code>SecondNormalFormViolation</code> if dependency violates the second normal form, <code>null</code> otherwise.
	 */

	/**
	 * Checks whether the right-hand-side of the supplied dependency is prime or the left-hand-side is not a partial key.
	 * @param analysis The analysis to be modified according to the result of this check
	 * @param dependency
	 * @param config
	 * @return
	 */
	public static boolean satisfiesSecondNormalform(NormalformAnalysis analysis, FunctionalDependency dependency, NormalformAnalyzerConfig config) {
		SecondNormalformViolation violation = getSecondNormalformViolation(dependency, config);
		boolean isViolated = violation != null;
		
		if (isViolated){
			analysis.addSecondNormalformViolation(violation);
		} 

		NFHelper.getLogger().log(Level.INFO, "FINISHED SECOND NF CHECK. Is violated: " + isViolated);
		return !isViolated;
	}
	
	public static boolean satisfiesThirdNormalform(NormalformAnalysis analysis, FunctionalDependency dependency, NormalformAnalyzerConfig config) {
		ThirdNormalformViolation violation = getThirdNormalformViolation(dependency, config);
		boolean isViolated = violation != null;
		
		if (isViolated){
			analysis.addThirdNormalformViolation(violation);
		}

		NFHelper.getLogger().log(Level.INFO, "FINISHED THIRD NF CHECK. Is violated: " + isViolated);
		return !isViolated;
	}

	public static boolean satisfiesBoyceCoddNormalform(NormalformAnalysis analysis, FunctionalDependency dependency, NormalformAnalyzerConfig config) {
		BoyceCoddNormalformViolation violation = getBoyceCoddNormalformViolation(dependency, config);
		boolean isViolated = violation != null;
		
		if (isViolated){
			analysis.addBoyceCoddNormalformViolation(violation);
		}
		
		NFHelper.getLogger().log(Level.INFO, "FINISHED BC NF CHECK. Is violated: " + isViolated);
		return !isViolated;	
	}

	/**
	 * Checks whether any attribute in the relation is not atomic (i.e., contains more than one value).
	 * @param dependency FunctionalDependency which is to be examined for violating the first normal form
	 * @param config Ignored
	 * @return null, always, because all exercises have atomic attributes
	 */
	public static FirstNormalformViolation getFirstNormalformViolation(FunctionalDependency dependency, NormalformAnalyzerConfig config) {
		return null;
	}

	/**
	 * Checks if the right-hand-side of the supplied dependency is non-prime AND the left-hand-side is a partial key.
	 * @param dependency FunctionalDependency which is to be examined for violating the second normal form
	 * @param config
	 * @return A <code>SecondNormalFormViolation</code> if dependency violates the second normal form, <code>null</code> otherwise.
	 */
	public static SecondNormalformViolation getSecondNormalformViolation(FunctionalDependency dependency, NormalformAnalyzerConfig config) {
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

		boolean isViolated = false;

		//Deciding whether LHS is a partial key
		//Violated, if LHS is a partial key (old comment, ca. 2005)
		/*
		 * What this literally does is, it checks (in this order):
		 *  1) If the right side contains any non-prime attribute, and only if that's true
		 *  2) if the left side is contained in (i.e., part of) a key, and only if that's true
		 *  3) if there are any other attributes in said key, i.e., if the left hand side is only a partial key.
		 *
		 * If 2) is false, LHS is non-prime. If 3) is false, LHS is a "complete" key (i.e., NOT a partial key).
		 * However, if 3) is true, LHS is a partial key, violating 2NF.
		 *
		 * (Gerald Wimmer, 2023-12-01)
		 */
		if (rhsContainsNonPrimeAttribute) {
            for (Key currKey : config.getCorrectMinimalKeys()) {
                //NFHelper.getLogger().log(Level.INFO, "Check Key: " + currKey + " (Key: " + currKey.getAttributes().size() + " -  Dependency: " + dependency.getLhsAttributes().size() + ")");

                if ((currKey.getAttributes().containsAll(dependency.getLhsAttributes())) && (currKey.getAttributes().size() > dependency.getLhsAttributes().size())) {
                    isViolated = true;
                }
            }
        }
		
		if (isViolated){
			return violation;
		}
		
		return null;
	}

	/**
	 * Checks whether both sides of the supplied <code>FunctionalDependency</code> are non-prime
	 * @param dependency <code>FunctionalDependency</code> which is to be examined for violating the third normal form
	 * @param config
	 * @return A <code>ThirdNormalFormViolation</code> if dependency violates the third normal form, <code>null</code>
	 * otherwise.
	 */
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
		//Violation, if LHS is not a super key (old comment, ca. 2005)
		/*
		 * Let's elaborate: If the right side contains any non-prime attribute, it checks if the left side contains any
		 * MINIMAL key completely (i.e., the LHS is a super key, because it either CONTAINS a minimal key, or it IS
		 * a minimal key, which automatically makes it a super key). If the left-hand-side is not a super key
		 * 	a) The left-hand-side is a partial key, violating 2NF, or
		 *  b) The left-hand-side is not even a partial key, i.e., non-prime, violating 3NF.
		 *
		 * b) obviously violates 3NF, and a) violates it because 3NF requires 2NF to be fulfilled.
		 *
		 * (Gerald Wimmer, 2023-12-01)
		 */
		if (rhsContainsNonPrimeAttribute) {
			NFHelper.getLogger().log(Level.INFO, "RHS comprises non prim attribute. Is violated: " + isViolated);

			for (Key currKey : config.getCorrectMinimalKeys()) {
				//NFHelper.getLogger().log(Level.INFO, "Check Key: " + currKey + " (Key: " + currKey.getAttributes().size() + " -  Dependency: " + dependency.getLhsAttributes().size() + ")");
				if (dependency.getLhsAttributes().containsAll(currKey.getAttributes())){
					isViolated = false;
					NFHelper.getLogger().log(Level.INFO, "LHS is a super key. Is violated: " + isViolated);
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

	/**
	 * Checks
	 * @param dependency <code>FunctionalDependency</code> which is to be examined for violating the Boyce-Codd normal form
	 * @param config
	 * @return A <code>BoyceCoddNormalFormViolation</code> if dependency violates the third normal form, <code>null</code> otherwise.
	 */
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