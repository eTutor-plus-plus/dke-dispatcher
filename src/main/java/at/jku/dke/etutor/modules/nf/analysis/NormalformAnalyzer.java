package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.model.NormalformLevelComparator;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;

public class NormalformAnalyzer {

	public static NormalformAnalysis analyze(NormalformAnalyzerConfig config){
		NormalformAnalysis analysis;
		Iterator dependenciesIterator;
		FunctionalDependency currDependency;
		
		analysis = new NormalformAnalysis();
		analysis.setSubmissionSuitsSolution(false);
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
			RDBDHelper.getLogger().log(Level.INFO, "Check Dependency: " + currDependency);

			if (satisfiesFirstNormalform(analysis, currDependency, config)){;
				if (satisfiesSecondNormalform(analysis, currDependency, config)){
					if (satisfiesThirdNormalform(analysis, currDependency, config)){
						satisfiesBoyceCoddNormalform(analysis, currDependency, config);
					}
				}
			}
		}
		
		//COMPUTE OVERALL NORMALFORM LEVEL
		if (config.getRelation().getFunctionalDependencies().size() == 0){
			if (config.getCorrectMinimalKeys().containsAll(config.getRelation().getMinimalKeys())) {
				analysis.setOverallNormalformLevel(NormalformLevel.BOYCE_CODD);
				analysis.setSubmissionSuitsSolution(true);			
			} else {
				analysis.setOverallNormalformLevel(NormalformLevel.FIRST);
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
		
			NormalformLevelComparator comparator = new NormalformLevelComparator();
			if (comparator.compare(analysis.getOverallNormalformLevel(), config.getDesiredNormalformLevel()) >= 0){
				analysis.setSubmissionSuitsSolution(true);			
			}
		}
		return analysis;
	}

	private static boolean isPrimAttribute(String attribute, Collection minimalKeys) {
		Key currMinimalKey;
		Iterator minimalKeysIterator;

		minimalKeysIterator = minimalKeys.iterator();
		while (minimalKeysIterator.hasNext()) {
			currMinimalKey = (Key)minimalKeysIterator.next();
			if (currMinimalKey.getAttributes().contains(attribute)) {
				return true;
			}
		}
		return false;
	}

	public static boolean satisfiesFirstNormalform(NormalformAnalysis analysis, FunctionalDependency dependency, NormalformAnalyzerConfig config) {
		FirstNormalformViolation violation;
		boolean isViolated;
		
		violation = getFirstNormalformViolation(dependency, config);
		isViolated = violation != null;
		
		return !isViolated;
	}

	public static boolean satisfiesSecondNormalform(NormalformAnalysis analysis, FunctionalDependency dependency, NormalformAnalyzerConfig config) {
		SecondNormalformViolation violation;
		boolean isViolated;
		
		violation = getSecondNormalformViolation(dependency, config);
		isViolated = violation != null;
		
		if (isViolated){
			analysis.addSecondNormalformViolation(violation);
		} 

		RDBDHelper.getLogger().log(Level.INFO, "FINISHED SECOND NF CHECK. Is violated: " + isViolated);
		return !isViolated;
	}
	
	public static boolean satisfiesThirdNormalform(NormalformAnalysis analysis, FunctionalDependency dependency, NormalformAnalyzerConfig config) {
		ThirdNormalformViolation violation; 
		boolean isViolated;

		violation = getThirdNormalformViolation(dependency, config);
		isViolated = violation != null;
		
		if (isViolated){
			analysis.addThirdNormalformViolation(violation);
		}

		RDBDHelper.getLogger().log(Level.INFO, "FINISHED THIRD NF CHECK. Is violated: " + isViolated);
		return !isViolated;
	}

	public static boolean satisfiesBoyceCoddNormalform(NormalformAnalysis analysis, FunctionalDependency dependency, NormalformAnalyzerConfig config) {
		boolean isViolated;
		BoyceCottNormalformViolation violation;

		violation = getBoyceCoddNormalformViolation(dependency, config);
		isViolated = violation != null;
		
		if (isViolated){
			analysis.addBoyceCottNormalformViolation(violation);		
		}
		
		RDBDHelper.getLogger().log(Level.INFO, "FINISHED BC NF CHECK. Is violated: " + isViolated);
		return !isViolated;	
	}
	
	public static FirstNormalformViolation getFirstNormalformViolation(FunctionalDependency dependency, NormalformAnalyzerConfig config) {
		return null;
	}

	public static SecondNormalformViolation getSecondNormalformViolation(FunctionalDependency dependency, NormalformAnalyzerConfig config) {
		Key currKey;
		Key currPartialKey;

		String currAttribute;
		SecondNormalformViolation violation;

		Iterator keysIterator;
		Iterator attributesIterator;
		Iterator partialKeysIterator;
		

		boolean lhsIsKey;
		boolean isViolated;
		boolean rhsComprisesNonPrimAttribute;
		
		violation = new SecondNormalformViolation();
		violation.setFunctionalDependency(dependency);

		//Deciding whether RHS comprises at least one non prim attribute
		rhsComprisesNonPrimAttribute = false;
		attributesIterator = dependency.iterRHSAttributes();
		while (attributesIterator.hasNext() && !rhsComprisesNonPrimAttribute) {
			currAttribute = (String)attributesIterator.next();

			if (!isPrimAttribute(currAttribute, config.getCorrectMinimalKeys())) {
				rhsComprisesNonPrimAttribute = true;
				violation.addNonPrimRHSAttribute(currAttribute);
			}
		}

		//Deciding whether LHS is a partial key
		//Violated, if LHS is a partial key

		if (rhsComprisesNonPrimAttribute) {
			isViolated = false;

			keysIterator = config.getCorrectMinimalKeys().iterator();
			while (keysIterator.hasNext()) {
				currKey = (Key)keysIterator.next();
				//RDBDHelper.getLogger().log(Level.INFO, "Check Key: " + currKey + " (Key: " + currKey.getAttributes().size() + " -  Dependency: " + dependency.getLHSAttributes().size() + ")");

				if ((currKey.getAttributes().containsAll(dependency.getLHSAttributes())) && (currKey.getAttributes().size() > dependency.getLHSAttributes().size())) {
					isViolated = true;
				}
			}
			
			/* OLD
			lhsIsKey = false;
			keysIterator = config.getCorrectMinimalKeys().iterator();
			while (keysIterator.hasNext()) {
				currKey = (Key)keysIterator.next();
				if ((currKey.getAttributes().containsAll(dependency.getLHSAttributes()))
					&& (currKey.getAttributes().size() == dependency.getLHSAttributes().size())) {
					lhsIsKey = true;
				}
			}

			if (!lhsIsKey) {
				partialKeysIterator = config.getCorrectPartialKeys().iterator();
				while (partialKeysIterator.hasNext()) {
					currPartialKey = (Key)partialKeysIterator.next();
					if (dependency.getLHSAttributes().containsAll(currPartialKey.getAttributes())){
						isViolated = true;
						violation.addComprisedPartialKey(currPartialKey);						
					}
				}
			}*/
		} else {
			isViolated = false;
		}
		
		if (isViolated){
			return violation;
		}
		
		return null;
	}
	
	public static ThirdNormalformViolation getThirdNormalformViolation(FunctionalDependency dependency, NormalformAnalyzerConfig config) {
		Key currKey;
		String currAttribute;
		Iterator keysIterator;
		Iterator attributesIterator;
		ThirdNormalformViolation violation; 

		boolean isViolated = true;
		boolean rhsComprisesNonPrimAttribute;
		
		violation = new ThirdNormalformViolation();
		violation.setFunctionalDependency(dependency);

		//Deciding whether RHS comprises at least one non prim attribute
		rhsComprisesNonPrimAttribute = false;
		attributesIterator = dependency.iterRHSAttributes();
		while (attributesIterator.hasNext() && !rhsComprisesNonPrimAttribute) {
			currAttribute = (String)attributesIterator.next();
			if (!isPrimAttribute(currAttribute,config.getCorrectMinimalKeys())) {
				rhsComprisesNonPrimAttribute = true;
				violation.addNonPrimRHSAttribute(currAttribute);
			}
		}

		//Deciding whether LHS is a super key
		//Violation, if LHS is not a super key
		if (rhsComprisesNonPrimAttribute) {
			RDBDHelper.getLogger().log(Level.INFO, "RHS comprises non prim attribute. Is violated: " + isViolated);
			keysIterator = config.getCorrectMinimalKeys().iterator();

			while ((keysIterator.hasNext()) && (isViolated)) {
				currKey = (Key)keysIterator.next();

				//RDBDHelper.getLogger().log(Level.INFO, "Check Key: " + currKey + " (Key: " + currKey.getAttributes().size() + " -  Dependency: " + dependency.getLHSAttributes().size() + ")");
				/* OLD
				if ((currKey.getAttributes().containsAll(dependency.getLHSAttributes()))
					&& (currKey.getAttributes().size() == dependency.getLHSAttributes().size())) {
					isViolated = true;
				}*/
				
				if (dependency.getLHSAttributes().containsAll(currKey.getAttributes())){
					isViolated = false;
					RDBDHelper.getLogger().log(Level.INFO, "LHS is a super key. Is violated: " + isViolated);
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

	public static BoyceCottNormalformViolation getBoyceCoddNormalformViolation(FunctionalDependency dependency, NormalformAnalyzerConfig config) {
		Key currKey;
		boolean isViolated;
		Iterator keysIterator;
		BoyceCottNormalformViolation violation;

		violation = new BoyceCottNormalformViolation();
		violation.setFunctionalDependency(dependency);

		isViolated = true;
		keysIterator = config.getCorrectMinimalKeys().iterator();

		while (keysIterator.hasNext()) {
			currKey = (Key)keysIterator.next();
			/* OLD
			if ((currKey.getAttributes().containsAll(dependency.getLHSAttributes()))
				&& (currKey.getAttributes().size() == dependency.getLHSAttributes().size())) {
				isViolated = true;
			}*/

			if (dependency.getLHSAttributes().containsAll(currKey.getAttributes())){
				isViolated = false;
			}
		}

		if (isViolated){
			return violation;		
		}
		
		return null;
	}
}