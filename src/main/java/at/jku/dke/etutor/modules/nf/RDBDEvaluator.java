package at.jku.dke.etutor.modules.nf;

import etutor.core.evaluation.*;
import etutor.modules.rdbd.analysis.*;
import etutor.modules.rdbd.exercises.RDBDExercisesManager;
import etutor.modules.rdbd.model.KeysContainer;
import etutor.modules.rdbd.model.NormalformLevel;
import etutor.modules.rdbd.model.Relation;
import etutor.modules.rdbd.report.*;
import etutor.modules.rdbd.ui.IdentifiedRelation;
import etutor.modules.rdbd.ui.IdentifiedRelationComparator;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;

public class RDBDEvaluator implements Evaluator, MessageSourceAware {
	private MessageSource messageSource;

	@Required
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public RDBDEvaluator() {
		super();
	}

	public Analysis analyze(int exerciseID, int userID, Map passedAttributes, Map passedParameters) throws Exception {
		int internalType;

		Analysis analysis;

		Serializable submission;
		Serializable specification;
		NormalizationAnalyzerConfig config;		
		NormalizationSpecification normSpec;
		
		RDBDHelper.getLogger().log(Level.INFO, "Start analyzing.");

		normSpec = null;
		analysis = null;

		submission = (Serializable)passedAttributes.get(RDBDConstants.calcSubmissionIDFor(exerciseID));								
		internalType = RDBDExercisesManager.fetchInternalType(exerciseID);
		specification = RDBDExercisesManager.fetchSpecification(exerciseID);

		if (internalType == RDBDConstants.TYPE_KEYS_DETERMINATION){
			//KEYS DETERMINATION
			KeysAnalyzerConfig keysAnalyzerConfig = new KeysAnalyzerConfig();
			KeysContainer correctKeys = KeysDeterminator.determineAllKeys((Relation)specification);
			keysAnalyzerConfig.setCorrectMinimalKeys(correctKeys.getMinimalKeys());
			analysis = KeysAnalyzer.analyze((Relation)((Collection)submission).toArray()[0], keysAnalyzerConfig);
			
			//Set Submission
			analysis.setSubmission((Relation)((Collection)submission).toArray()[0]);

		} else if (internalType == RDBDConstants.TYPE_MINIMAL_COVER){
			//MINIMAL COVER
			//TODO: pass specificatin itself instead of exerciseID? (2005-10-16, g.n.)
			analysis = MinimalCoverAnalyzer.analyze((Relation)((Collection)submission).toArray()[0], exerciseID);
				
			//Set Submission
			analysis.setSubmission((Relation)((Collection)submission).toArray()[0]);

		} else if (internalType == RDBDConstants.TYPE_ATTRIBUTE_CLOSURE){
			//ATTRIBUTE CLOSURE
			AttributeClosureSpecification attributeClosureSpecification = (AttributeClosureSpecification)specification;
			analysis = AttributeClosureAnalyzer.analyze(attributeClosureSpecification.getBaseRelation().getFunctionalDependencies(), attributeClosureSpecification.getBaseAttributes(), ((Relation)((Collection)submission).toArray()[0]).getAttributes());
				
			//Set Submission
			analysis.setSubmission((Relation)((Collection)submission).toArray()[0]);

		} else if (internalType == RDBDConstants.TYPE_RBR){
			//RBR
			RBRSpecification rbrSpecification = (RBRSpecification)specification;
			analysis = RBRAnalyzer.analyze(rbrSpecification.getBaseRelation(), (Relation)((Collection)submission).toArray()[0]);
			
			//Set Submission
			analysis.setSubmission((Relation)((Collection)submission).toArray()[0]);

		} else if (internalType == RDBDConstants.TYPE_DECOMPOSE){
			//DECOMPOSE
			StringBuffer temp;
			Iterator decomposedRelationsIterator;
			
			DecomposeAnalyzerConfig decomposeAnalyzerConfig = new DecomposeAnalyzerConfig(); 
			DecomposeSpecification decomposeSpecification = (DecomposeSpecification)specification;

			TreeSet allRelations = new TreeSet(new IdentifiedRelationComparator());
			allRelations.add(decomposeSpecification.getBaseRelation());
			allRelations.addAll((TreeSet)submission);
			
			String baseRelationID;
			if ((passedParameters.get(RDBDConstants.PARAM_DIAGNOSE_RELATION) != null) && ((((String)passedParameters.get(RDBDConstants.PARAM_DIAGNOSE_RELATION)).length() > 0))){
				baseRelationID = ((String)passedParameters.get(RDBDConstants.PARAM_DIAGNOSE_RELATION));
			} else {
				baseRelationID = ((DecomposeSpecification)specification).getBaseRelation().getID();
			}

			decomposeAnalyzerConfig.setBaseRelation(RDBDHelper.findRelation(baseRelationID, allRelations));
			RDBDHelper.getLogger().log(Level.INFO, "BaseRelation: '" + baseRelationID + "'."); 

			decomposedRelationsIterator = allRelations.iterator();
			temp = new StringBuffer();
			while(decomposedRelationsIterator.hasNext()){
				temp.append("Relation '" + ((IdentifiedRelation)decomposedRelationsIterator.next()).getID() + "' ");
			}
			RDBDHelper.getLogger().log(Level.INFO, "All submitted Relations: " + temp.toString() + ".");

			decomposeAnalyzerConfig.setDecomposedRelations(RDBDHelper.findSubRelations(baseRelationID, allRelations));
			decomposedRelationsIterator = decomposeAnalyzerConfig.iterDecomposedRelations();
			temp = new StringBuffer();
			while(decomposedRelationsIterator.hasNext()){
				temp.append("Relation '" + ((IdentifiedRelation)decomposedRelationsIterator.next()).getID() + "' ");
			}
			RDBDHelper.getLogger().log(Level.INFO, "Decomposed Relations: " + temp.toString() + ".");
			
			decomposeAnalyzerConfig.setDesiredNormalformLevel(decomposeSpecification.getTargetLevel());
			RDBDHelper.getLogger().log(Level.INFO, "Target NormalformLevel: '" + decomposeSpecification.getTargetLevel() + "'."); 

			decomposeAnalyzerConfig.setMaxLostDependencies(decomposeSpecification.getMaxLostDependencies());
			RDBDHelper.getLogger().log(Level.INFO, "Max Lost Dependencies: '" + decomposeSpecification.getMaxLostDependencies() + "'."); 
			
			analysis = DecomposeAnalyzer.analyze(decomposeAnalyzerConfig);
			
			//Set Submission
			analysis.setSubmission((TreeSet)submission);

		} else if (internalType == RDBDConstants.TYPE_NORMALIZATION){
			//NORMALIZATION
			NormalizationAnalyzerConfig normalizationAnalyzerConfig = new NormalizationAnalyzerConfig();
			NormalizationSpecification normalizationSpecification = (NormalizationSpecification)specification;
			
			normalizationAnalyzerConfig.setBaseRelation(normalizationSpecification.getBaseRelation());
			normalizationAnalyzerConfig.setDesiredNormalformLevel(normalizationSpecification.getTargetLevel());
			normalizationAnalyzerConfig.setMaxLostDependencies(normalizationSpecification.getMaxLostDependencies());
			normalizationAnalyzerConfig.setNormalizedRelations((Collection)submission);
			
			analysis = NormalizationAnalyzer.analyze(normalizationAnalyzerConfig);
			
			//Set Submission
			analysis.setSubmission((TreeSet)submission);

		} else if (internalType == RDBDConstants.TYPE_NORMALFORM_DETERMINATION){
			//NORMALFORM DETERMINATION
			NormalformDeterminationSubmission normalformDeterminationSubmission = (NormalformDeterminationSubmission)submission;

			//Set overall level
			String overallLevel = (String)passedParameters.get(RDBDConstants.PARAM_NORMALFORM_LEVEL);
			if (overallLevel.equalsIgnoreCase("1")){
				normalformDeterminationSubmission.setOverallLevel(NormalformLevel.FIRST);
			} else if (overallLevel.equalsIgnoreCase("2")){
				normalformDeterminationSubmission.setOverallLevel(NormalformLevel.SECOND);
			} else if (overallLevel.equalsIgnoreCase("3")){
				normalformDeterminationSubmission.setOverallLevel(NormalformLevel.THIRD);
			} else if (overallLevel.equalsIgnoreCase("4")){
				normalformDeterminationSubmission.setOverallLevel(NormalformLevel.BOYCE_CODD);
			}
			RDBDHelper.getLogger().log(Level.INFO, "OVERALL LEVEL: " + normalformDeterminationSubmission.getOverallLevel());

			//Set normalform violations
			Integer currID;
			String violatedNF;
			Iterator iter = normalformDeterminationSubmission.iterDependencyIDs();
			while (iter.hasNext()){
				currID = (Integer)iter.next();
				
				if (passedParameters.get(currID + "_violation") != null){
					violatedNF = (String)passedParameters.get(currID + "_violation");
					
					if (violatedNF.equalsIgnoreCase("1")){
						normalformDeterminationSubmission.setNormalformVioaltion(NormalformLevel.FIRST, currID);
						//RDBDHelper.getLogger().log(Level.INFO, "Dependency '" + normalformDeterminationSubmission.getDependency(currID) + "' violates FIRST NF.");
					} else if (violatedNF.equalsIgnoreCase("2")){
						normalformDeterminationSubmission.setNormalformVioaltion(NormalformLevel.SECOND, currID);
						//RDBDHelper.getLogger().log(Level.INFO, "Dependency '" + normalformDeterminationSubmission.getDependency(currID) + "' violates SECOND NF.");
					} else if (violatedNF.equalsIgnoreCase("3")){
						normalformDeterminationSubmission.setNormalformVioaltion(NormalformLevel.THIRD, currID);
						//RDBDHelper.getLogger().log(Level.INFO, "Dependency '" + normalformDeterminationSubmission.getDependency(currID) + "' violates THIRD NF.");
					} else if (violatedNF.equalsIgnoreCase("4")){
						normalformDeterminationSubmission.setNormalformVioaltion(NormalformLevel.BOYCE_CODD, currID);
						//RDBDHelper.getLogger().log(Level.INFO, "Dependency '" + normalformDeterminationSubmission.getDependency(currID) + "' violates BOYCE CODD NF.");
					} else {
						normalformDeterminationSubmission.setNormalformVioaltion(null, currID);
					}
				} else {
					normalformDeterminationSubmission.setNormalformVioaltion(null, currID);
				}
			}

			NormalformAnalyzerConfig normalformAnalyzerConfig = new NormalformAnalyzerConfig();
			normalformAnalyzerConfig.setCorrectMinimalKeys(KeysDeterminator.determineMinimalKeys((Relation)specification));
			normalformAnalyzerConfig.setRelation((Relation)specification);
			
			analysis = NormalformDeterminationAnalyzer.analyze(normalformDeterminationSubmission, normalformAnalyzerConfig);

			//Set Submission
			analysis.setSubmission(normalformDeterminationSubmission);

		} else {
			RDBDHelper.getLogger().log(Level.SEVERE, "RDBD internal type '" + internalType + "' is not supported.");
			throw new Exception("Unsupported RDBD type.");
		}

		return analysis;
	}

	public Grading grade(Analysis analysis, int taskID, Map passedAttributes, Map passedParameters) throws Exception{
		DefaultGrading grading = new DefaultGrading();
		grading.setMaxPoints(1);
		if (analysis.submissionSuitsSolution()){
			grading.setPoints(1);
		} else {
			grading.setPoints(0);
		}
		return grading;
	}

	public Report report(Analysis analysis, Grading grading, Map passedAttributes, Map passedParameters, Locale locale) throws Exception {
		ReporterConfig config;
		
		Report report = null;
		String action_PARAM = (String)passedAttributes.get(RDBDConstants.PARAM_ACTION);
		int exerciseID_PARAM = Integer.parseInt(passedAttributes.get(RDBDConstants.ATT_EXERCISE_ID).toString());
		String diagnoseLevel_PARAM = (String)passedAttributes.get(RDBDConstants.PARAM_LEVEL);

		int diagnoseLevel = 2;
		int internalType = RDBDExercisesManager.fetchInternalType(exerciseID_PARAM);

		if (!action_PARAM.equals(RDBDConstants.EVAL_ACTION_SUBMIT)){
			try{
				diagnoseLevel = Integer.parseInt(diagnoseLevel_PARAM);			
			} catch (Exception ignore){
				RDBDHelper.getLogger().log(Level.WARNING, "Diagnose Level '" + diagnoseLevel_PARAM + "' is not a number! Using default Diagnose Level '0'");
			}
		}

		if (internalType == RDBDConstants.TYPE_KEYS_DETERMINATION){
			//KEYS DETERMINATION
			RDBDHelper.getLogger().log(Level.INFO, "Printing report for internal type 'KEY_DETERMINATION'");
			
			config = new ReporterConfig();
			config.setAction(action_PARAM);
			config.setDiagnoseLevel(diagnoseLevel);
			report = KeysReporter.report((KeysAnalysis)analysis, (DefaultGrading)grading, config, messageSource, locale);

		} else if (internalType == RDBDConstants.TYPE_MINIMAL_COVER){
			//MINIMAL COVER
			RDBDHelper.getLogger().log(Level.INFO, "Printing report for internal type 'MINIMAL_COVER'");
			
			config = new ReporterConfig();
			config.setAction(action_PARAM);
			config.setDiagnoseLevel(diagnoseLevel);
			report = MinimalCoverReporter.report((MinimalCoverAnalysis)analysis, (DefaultGrading)grading, config, messageSource, locale);

		} else if (internalType == RDBDConstants.TYPE_ATTRIBUTE_CLOSURE){
			//ATTRIBUTE CLOSURE
			RDBDHelper.getLogger().log(Level.INFO, "Printing report for internal type 'ATTRIBUTE_CLOSURE'");
			
			config = new ReporterConfig();
			config.setAction(action_PARAM);
			config.setDiagnoseLevel(diagnoseLevel);
			report = AttributeClosureReporter.report((AttributeClosureAnalysis)analysis, (DefaultGrading)grading, config, messageSource, locale);

		} else if (internalType == RDBDConstants.TYPE_RBR){
			//RBR
			RDBDHelper.getLogger().log(Level.INFO, "Printing report for internal type 'RBR'");
			
			config = new ReporterConfig();
			config.setAction(action_PARAM);
			config.setDiagnoseLevel(diagnoseLevel);
			report = RBRReporter.report((RBRAnalysis)analysis, (DefaultGrading)grading, config, messageSource, locale);

		} else if (internalType == RDBDConstants.TYPE_DECOMPOSE){
			//DECOMPOSE
			RDBDHelper.getLogger().log(Level.INFO, "Printing report for internal type 'DECOMPOSE'");
			
			DecomposeReporterConfig decomposeReporterConfig = new DecomposeReporterConfig();
			decomposeReporterConfig.setAction(action_PARAM);
			decomposeReporterConfig.setDiagnoseLevel(diagnoseLevel);
			decomposeReporterConfig.setGrading((DefaultGrading)grading);
			decomposeReporterConfig.setDecomposeAnalysis((DecomposeAnalysis)analysis);

			report = DecomposeReporter.report(decomposeReporterConfig, messageSource, locale);

		} else if (internalType == RDBDConstants.TYPE_NORMALIZATION){
			//NORMALIZATION
			RDBDHelper.getLogger().log(Level.INFO, "Printing report for internal type 'NORMALIZATION'");
			
			NormalizationReporterConfig normalizationReporterConfig = new NormalizationReporterConfig();  
			normalizationReporterConfig.setAction(action_PARAM);
			normalizationReporterConfig.setDiagnoseLevel(diagnoseLevel);
			normalizationReporterConfig.setDecomposedRelations((Collection)analysis.getSubmission());
			normalizationReporterConfig.setDesiredNormalformLevel(((NormalizationAnalysis)analysis).getDesiredNormalformLevel());

			report = NormalizationReporter.report((NormalizationAnalysis)analysis, (DefaultGrading)grading, normalizationReporterConfig, messageSource, locale);
		
		} else if (internalType == RDBDConstants.TYPE_NORMALFORM_DETERMINATION){
			//NORMALFORM DETERMINATION
			RDBDHelper.getLogger().log(Level.INFO, "Printing report for internal type 'NORMALFORM DETERMINATION'");

			config = new ReporterConfig();
			config.setAction(action_PARAM);
			config.setDiagnoseLevel(diagnoseLevel);
			
			report = NormalformReporter.report((NormalformDeterminationAnalysis)analysis, (DefaultGrading)grading, config, messageSource, locale);

		} else {
			RDBDHelper.getLogger().log(Level.SEVERE, "RDBD internal type '" + internalType + "' is not supported.");
			throw new Exception("Unsupported RDBD type.");
		}

		return report;
	}
}