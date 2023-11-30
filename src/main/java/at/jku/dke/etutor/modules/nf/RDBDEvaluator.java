package at.jku.dke.etutor.modules.nf;

import at.jku.dke.etutor.core.evaluation.Report;
import at.jku.dke.etutor.core.evaluation.*;
import at.jku.dke.etutor.modules.nf.analysis.*;
import at.jku.dke.etutor.modules.nf.analysis.closure.AttributeClosureAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.closure.AttributeClosureAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.decompose.DecomposeAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.decompose.DecomposeAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.decompose.DecomposeAnalyzerConfig;
import at.jku.dke.etutor.modules.nf.analysis.keys.KeysAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.keys.KeysAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.keys.KeysAnalyzerConfig;
import at.jku.dke.etutor.modules.nf.analysis.minimalcover.MinimalCoverAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.minimalcover.MinimalCoverAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.normalformdetermination.NormalformDeterminationAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.normalformdetermination.NormalformDeterminationAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.normalization.NormalizationAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.normalization.NormalizationAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.normalization.NormalizationAnalyzerConfig;
import at.jku.dke.etutor.modules.nf.analysis.rbr.RBRAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.rbr.RBRAnalyzer;
import at.jku.dke.etutor.modules.nf.exercises.RDBDExercisesManager;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.KeysContainer;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.model.Relation;
import at.jku.dke.etutor.modules.nf.parser.NFLexer;
import at.jku.dke.etutor.modules.nf.parser.NFParser;
import at.jku.dke.etutor.modules.nf.report.*;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;
import at.jku.dke.etutor.modules.nf.ui.IdentifiedRelationComparator;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
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

	/*
	 * TODO: In the old eTutor, the Map parameters were effectively
	 *  passedAttributes: Map<String, Serializable>
	 *  passedParameters: Map<String, String[]> (according to documentation, actually seems to be single Strings that
	 *   may contain multiple values)
	 *  This must be adapted to match the interface's Map<String, String> in both cases (Gerald Wimmer, 2023-11-12).
	 */
	@Override
	public Analysis analyze(int exerciseID, int userID, Map<String, String> passedAttributes, Map<String, String> passedParameters, Locale locale) throws Exception {
		Analysis analysis;

		RDBDHelper.getLogger().log(Level.INFO, "Start analyzing.");

		/*
		 * TODO: Receive submission as String instead of Serializable, pass it on to our new, shiny, parser, and receive
		 *  what used to be passed in from this Serializable from the Parser, instead (Gerald Wimmer, 2023-11-12).
		 */
		// Serializable submission = (Serializable)passedAttributes.get(RDBDConstants.calcSubmissionIDFor(exerciseID));
		Serializable submission = null; // NOTE: Temporary addition so IntelliJ doesn't complain about the uninitialized variable (Gerald Wimmer, 2023-11-12)

		submission = "#A,B,C#;#D,E,F#";
		CharStream lexerInput = CharStreams.fromString((String)submission);	// Source: https://datacadamia.com/antlr/getting_started (Gerald Wimmer, 2023-11-27)
		Lexer lexer = new NFLexer(lexerInput);
		TokenStream parserInput = new CommonTokenStream(lexer);
		NFParser parser = new NFParser(parserInput);

		int internalType = RDBDExercisesManager.fetchInternalType(exerciseID);
		Serializable specification = RDBDExercisesManager.fetchSpecification(exerciseID);

		/*
		 * TODO: Pass the submission string on to the appropriate method of our new, shiny, parser (method could be
		 *  selected inside the if statement) and receive the appropriate data (TreeSet, IdentifiedRelation, Vector)
		 *  from the parser (Gerald Wimmer, 2023-11-12).
		 */
		if (internalType == RDBDConstants.TYPE_KEYS_DETERMINATION) {
			//KEYS DETERMINATION
			KeysAnalyzerConfig keysAnalyzerConfig = new KeysAnalyzerConfig();
			KeysContainer correctKeys = KeysDeterminator.determineAllKeys((Relation)specification);
			keysAnalyzerConfig.setCorrectMinimalKeys(correctKeys.getMinimalKeys());

			// Relation relation = (Relation)((Collection<IdentifiedRelation>)submission).toArray()[0]; // Replaced with call to parser (Gerald Wimmer, 2023-11-27)
			// Assemble relation from input string (Gerald Wimmer, 2023-11-27)
			Relation relation = new Relation();
			Set<Key> minimalKeys = parser.keySet().keys;
			relation.setMinimalKeys(minimalKeys);

			analysis = KeysAnalyzer.analyze(relation, keysAnalyzerConfig);
			
			//Set Submission
			analysis.setSubmission(relation);

		} else if (internalType == RDBDConstants.TYPE_MINIMAL_COVER) {
			//MINIMAL COVER
			// Relation relation = (Relation)((Collection<IdentifiedRelation>)submission).toArray()[0]; // Replaced with call to parser (Gerald Wimmer, 2023-11-27)
			// Assemble relation from input string (Gerald Wimmer, 2023-11-27)
			Relation relation = new Relation();
			Set<FunctionalDependency> functionalDependencies = parser.functionalDependencySet().functionalDependencies;
			relation.setFunctionalDependencies(functionalDependencies);

			//TODO: pass specification itself instead of exerciseID? (2005-10-16, g.n.)
			analysis = MinimalCoverAnalyzer.analyze(relation, exerciseID);
				
			//Set Submission
			analysis.setSubmission(relation);

		} else if (internalType == RDBDConstants.TYPE_ATTRIBUTE_CLOSURE) {
			//ATTRIBUTE CLOSURE
			AttributeClosureSpecification attributeClosureSpecification = (AttributeClosureSpecification)specification;
			// Relation relation = (Relation)((Collection<IdentifiedRelation>)submission).toArray()[0]; // Replaced with call to parser (Gerald Wimmer, 2023-11-27)
			// Assemble relation from input string (Gerald Wimmer, 2023-11-27)
			Relation relation = new Relation();
			Set<String> attributes = parser.attributeSet().attributes;
			relation.setAttributes(attributes);

			analysis = AttributeClosureAnalyzer.analyze(
					attributeClosureSpecification.getBaseRelation().getFunctionalDependencies(),
					attributeClosureSpecification.getBaseAttributes(),
					relation.getAttributes());
				
			//Set Submission
			analysis.setSubmission(relation);

		} /*else if (internalType == RDBDConstants.TYPE_RBR) { // Note: No longer necessary as its own task type, may be required for Decompose (Gerald Wimmer, 2023-11-27)
			//RBR
			RBRSpecification rbrSpecification = (RBRSpecification)specification;
			Relation relation = (Relation)((Collection<IdentifiedRelation>)submission).toArray()[0]; // TODO: Replace with call to parser (Gerald Wimmer, 2023-11-12)
			analysis = RBRAnalyzer.analyze(rbrSpecification.getBaseRelation(), relation);
			
			//Set Submission
			analysis.setSubmission(relation);

		}*/ else if (internalType == RDBDConstants.TYPE_DECOMPOSE) {	// Todo: Maybe remove and replace with Normalize, now that steps need not be validated
			//DECOMPOSE
			StringBuilder temp;
			
			DecomposeAnalyzerConfig decomposeAnalyzerConfig = new DecomposeAnalyzerConfig();
			DecomposeSpecification decomposeSpecification = (DecomposeSpecification)specification;

			TreeSet<IdentifiedRelation> allRelations = new TreeSet<>(new IdentifiedRelationComparator());
			allRelations.add(decomposeSpecification.getBaseRelation());
			/*
			 * TODO: Replace with call to parser (Gerald Wimmer, 2023-11-12)
			 *
			 * NOTE: Generic <IdentifiedRelation> should always work, because that is the actual type passed in
			 *  by RDBDEditor.initPerformTask() (see the first if-statement there).
			 *  (Gerald Wimmer, 2023-11-12)
			 */
			TreeSet<IdentifiedRelation> submissionTreeSet = (TreeSet<IdentifiedRelation>)submission;
			allRelations.addAll(submissionTreeSet);
			
			String baseRelationID;
			if (passedParameters.get(RDBDConstants.PARAM_DIAGNOSE_RELATION) != null && !((String) passedParameters.get(RDBDConstants.PARAM_DIAGNOSE_RELATION)).isEmpty()){
				baseRelationID = ((String)passedParameters.get(RDBDConstants.PARAM_DIAGNOSE_RELATION));
			} else {
				baseRelationID = ((DecomposeSpecification)specification).getBaseRelation().getID();
			}

			decomposeAnalyzerConfig.setBaseRelation(RDBDHelper.findRelation(baseRelationID, allRelations));
			RDBDHelper.getLogger().log(Level.INFO, "BaseRelation: '" + baseRelationID + "'."); 

			Iterator<IdentifiedRelation> decomposedRelationsIterator = allRelations.iterator();
			temp = new StringBuilder();
			while(decomposedRelationsIterator.hasNext()){
				temp.append("Relation '").append(((IdentifiedRelation) decomposedRelationsIterator.next()).getID()).append("' ");
			}
			RDBDHelper.getLogger().log(Level.INFO, "All submitted Relations: " + temp + ".");

			decomposeAnalyzerConfig.setDecomposedRelations(RDBDHelper.findSubRelations(baseRelationID, allRelations));
			decomposedRelationsIterator = decomposeAnalyzerConfig.iterDecomposedRelations();
			temp = new StringBuilder();
			while(decomposedRelationsIterator.hasNext()){
				temp.append("Relation '").append(((IdentifiedRelation) decomposedRelationsIterator.next()).getID()).append("' ");
			}
			RDBDHelper.getLogger().log(Level.INFO, "Decomposed Relations: " + temp + ".");
			
			decomposeAnalyzerConfig.setDesiredNormalformLevel(decomposeSpecification.getTargetLevel());
			RDBDHelper.getLogger().log(Level.INFO, "Target NormalformLevel: '" + decomposeSpecification.getTargetLevel() + "'."); 

			decomposeAnalyzerConfig.setMaxLostDependencies(decomposeSpecification.getMaxLostDependencies());
			RDBDHelper.getLogger().log(Level.INFO, "Max Lost Dependencies: '" + decomposeSpecification.getMaxLostDependencies() + "'."); 
			
			analysis = DecomposeAnalyzer.analyze(decomposeAnalyzerConfig);
			
			//Set Submission
			analysis.setSubmission(submissionTreeSet);

		} else if (internalType == RDBDConstants.TYPE_NORMALIZATION) { // Note: Could be identical to Decompose, now that you only have to specify the end result (Gerald Wimmer, 2023-11-27)
			// TODO: Replace Decompose with THIS. (Gerald Wimmer, 2023-11-30)
			//NORMALIZATION
			NormalizationAnalyzerConfig normalizationAnalyzerConfig = new NormalizationAnalyzerConfig();
			NormalizationSpecification normalizationSpecification = (NormalizationSpecification)specification;
			
			normalizationAnalyzerConfig.setBaseRelation(normalizationSpecification.getBaseRelation());
			normalizationAnalyzerConfig.setDesiredNormalformLevel(normalizationSpecification.getTargetLevel());
			normalizationAnalyzerConfig.setMaxLostDependencies(normalizationSpecification.getMaxLostDependencies());
			/*
			 * TODO: Replace with call to parser (Gerald Wimmer, 2023-11-12)
			 *
			 * NOTE: Cast to TreeSet<IdentifiedRelation> should always work, because that is the actual type passed in
			 *  by RDBDEditor.initPerformTask(). Also, it was cast (albeit needlessly) to TreeSet (without generics
			 *  because there were none back then) for analysis.setSubmission(). (Gerald Wimmer, 2023-11-12)
			 */
			TreeSet<IdentifiedRelation> submissionTreeSet = (TreeSet<IdentifiedRelation>) submission;
			normalizationAnalyzerConfig.setNormalizedRelations(submissionTreeSet);
			
			analysis = NormalizationAnalyzer.analyze(normalizationAnalyzerConfig);
			
			//Set Submission
			analysis.setSubmission(submissionTreeSet);

		} else if (internalType == RDBDConstants.TYPE_NORMALFORM_DETERMINATION) {
			//NORMALFORM DETERMINATION
			NormalformDeterminationSubmission normalformDeterminationSubmission = (NormalformDeterminationSubmission)submission; // TODO: Replace with call to parser (Gerald Wimmer, 2023-11-12)

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
			Iterator<Integer> iter = normalformDeterminationSubmission.iterDependencyIDs();
			while (iter.hasNext()) {
				Integer currID = iter.next();
				
				if (passedParameters.get(currID + "_violation") != null){
					String violatedNF = (String)passedParameters.get(currID + "_violation");
					
					if (violatedNF.equalsIgnoreCase("1")){
						normalformDeterminationSubmission.setNormalformViolation(NormalformLevel.FIRST, currID);
						//RDBDHelper.getLogger().log(Level.INFO, "Dependency '" + normalformDeterminationSubmission.getDependency(currID) + "' violates FIRST NF.");
					} else if (violatedNF.equalsIgnoreCase("2")){
						normalformDeterminationSubmission.setNormalformViolation(NormalformLevel.SECOND, currID);
						//RDBDHelper.getLogger().log(Level.INFO, "Dependency '" + normalformDeterminationSubmission.getDependency(currID) + "' violates SECOND NF.");
					} else if (violatedNF.equalsIgnoreCase("3")){
						normalformDeterminationSubmission.setNormalformViolation(NormalformLevel.THIRD, currID);
						//RDBDHelper.getLogger().log(Level.INFO, "Dependency '" + normalformDeterminationSubmission.getDependency(currID) + "' violates THIRD NF.");
					} else if (violatedNF.equalsIgnoreCase("4")){
						normalformDeterminationSubmission.setNormalformViolation(NormalformLevel.BOYCE_CODD, currID);
						//RDBDHelper.getLogger().log(Level.INFO, "Dependency '" + normalformDeterminationSubmission.getDependency(currID) + "' violates BOYCE CODD NF.");
					} else {
						normalformDeterminationSubmission.setNormalformViolation(null, currID);
					}
				} else {
					normalformDeterminationSubmission.setNormalformViolation(null, currID);
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

	@Override
	public Grading grade(Analysis analysis, int taskID, Map<String, String> passedAttributes, Map<String, String> passedParameters) throws Exception {
		DefaultGrading grading = new DefaultGrading();
		grading.setMaxPoints(1);
		if (analysis.submissionSuitsSolution()){
			grading.setPoints(1);
		} else {
			grading.setPoints(0);
		}
		return grading;
	}

	/*
	 * TODO: In the old eTutor, the Map parameters were effectively
	 *  passedAttributes: Map<String, Serializable>
	 *  passedParameters: Map<String, String[]> (according to documentation, actually seems to be single Strings that
	 *   may contain multiple values)
	 *  This must be adapted to match the interface's Map<String, String> in both cases (Gerald Wimmer, 2023-11-12).
	 *
	 * NOTE: passedParameters was never actually used, so there was no conflict in converting it to Map<String, String>,
	 *  and passedAttribute is only ever queried for String values (see explanation below, where there isn't a cast to
	 *  String, anyway)
	 *  (Gerald Wimmer, 2023-11-12)
	 */
	@Override
	public Report report(Analysis analysis, Grading grading, Map<String, String> passedAttributes, Map<String, String> passedParameters, Locale locale) throws Exception {
		ReporterConfig config;
		
		Report report;
		String actionParam = (String)passedAttributes.get(RDBDConstants.PARAM_ACTION);
		/*
		 * NOTE: Whenever the parameter with the key RDBDConstants.ATT_EXERCISE_ID is queried, its .toString() value is
		 *  passed into Integer.parseInt(). As Integer.parseInt() can only accept Strings (and only interpret those
		 *  containing Integer values), I assume that calling toString() on the  value of get() has the same effect as
		 *  casting it to String, which, I assume, it already is if it can be parsed by Integer.parseInt()).
		 *  (Gerald Wimmer, 2023-11-12).
		 */
		int exerciseIdParam = Integer.parseInt(passedAttributes.get(RDBDConstants.ATT_EXERCISE_ID).toString());
		String diagnoseLevelParam = (String)passedAttributes.get(RDBDConstants.PARAM_LEVEL);

		int diagnoseLevel = 2;
		int internalType = RDBDExercisesManager.fetchInternalType(exerciseIdParam);

		if (!actionParam.equals(RDBDConstants.EVAL_ACTION_SUBMIT)){
			try{
				diagnoseLevel = Integer.parseInt(diagnoseLevelParam);
			} catch (Exception ignore){
				RDBDHelper.getLogger().log(Level.WARNING, "Diagnose Level '" + diagnoseLevelParam + "' is not a number! Using default Diagnose Level '0'");
			}
		}

		if (internalType == RDBDConstants.TYPE_KEYS_DETERMINATION){
			//KEYS DETERMINATION
			RDBDHelper.getLogger().log(Level.INFO, "Printing report for internal type 'KEY_DETERMINATION'");
			
			config = new ReporterConfig();
			config.setAction(actionParam);
			config.setDiagnoseLevel(diagnoseLevel);
			report = KeysReporter.report((KeysAnalysis)analysis, (DefaultGrading)grading, config, messageSource, locale);

		} else if (internalType == RDBDConstants.TYPE_MINIMAL_COVER){
			//MINIMAL COVER
			RDBDHelper.getLogger().log(Level.INFO, "Printing report for internal type 'MINIMAL_COVER'");
			
			config = new ReporterConfig();
			config.setAction(actionParam);
			config.setDiagnoseLevel(diagnoseLevel);
			report = MinimalCoverReporter.report((MinimalCoverAnalysis)analysis, (DefaultGrading)grading, config, messageSource, locale);

		} else if (internalType == RDBDConstants.TYPE_ATTRIBUTE_CLOSURE){
			//ATTRIBUTE CLOSURE
			RDBDHelper.getLogger().log(Level.INFO, "Printing report for internal type 'ATTRIBUTE_CLOSURE'");
			
			config = new ReporterConfig();
			config.setAction(actionParam);
			config.setDiagnoseLevel(diagnoseLevel);
			report = AttributeClosureReporter.report((AttributeClosureAnalysis)analysis, (DefaultGrading)grading, config, messageSource, locale);

		} else if (internalType == RDBDConstants.TYPE_RBR){
			//RBR
			RDBDHelper.getLogger().log(Level.INFO, "Printing report for internal type 'RBR'");
			
			config = new ReporterConfig();
			config.setAction(actionParam);
			config.setDiagnoseLevel(diagnoseLevel);
			report = RBRReporter.report((RBRAnalysis)analysis, (DefaultGrading)grading, config, messageSource, locale);

		} else if (internalType == RDBDConstants.TYPE_DECOMPOSE){
			//DECOMPOSE
			RDBDHelper.getLogger().log(Level.INFO, "Printing report for internal type 'DECOMPOSE'");
			
			DecomposeReporterConfig decomposeReporterConfig = new DecomposeReporterConfig();
			decomposeReporterConfig.setAction(actionParam);
			decomposeReporterConfig.setDiagnoseLevel(diagnoseLevel);
			decomposeReporterConfig.setGrading((DefaultGrading)grading);
			decomposeReporterConfig.setDecomposeAnalysis((DecomposeAnalysis)analysis);

			report = DecomposeReporter.report(decomposeReporterConfig, messageSource, locale);

		} else if (internalType == RDBDConstants.TYPE_NORMALIZATION){
			//NORMALIZATION
			RDBDHelper.getLogger().log(Level.INFO, "Printing report for internal type 'NORMALIZATION'");
			
			NormalizationReporterConfig normalizationReporterConfig = new NormalizationReporterConfig();  
			normalizationReporterConfig.setAction(actionParam);
			normalizationReporterConfig.setDiagnoseLevel(diagnoseLevel);
			normalizationReporterConfig.setDecomposedRelations((TreeSet<IdentifiedRelation>)analysis.getSubmission());
			normalizationReporterConfig.setDesiredNormalformLevel(((NormalizationAnalysis)analysis).getDesiredNormalformLevel());

			report = NormalizationReporter.report((NormalizationAnalysis)analysis, (DefaultGrading)grading, normalizationReporterConfig, messageSource, locale);
		
		} else if (internalType == RDBDConstants.TYPE_NORMALFORM_DETERMINATION){
			//NORMALFORM DETERMINATION
			RDBDHelper.getLogger().log(Level.INFO, "Printing report for internal type 'NORMALFORM DETERMINATION'");

			config = new ReporterConfig();
			config.setAction(actionParam);
			config.setDiagnoseLevel(diagnoseLevel);
			
			report = NormalformReporter.report((NormalformDeterminationAnalysis)analysis, (DefaultGrading)grading, config, messageSource, locale);

		} else {
			RDBDHelper.getLogger().log(Level.SEVERE, "RDBD internal type '" + internalType + "' is not supported.");
			throw new Exception("Unsupported RDBD type.");
		}

		return report;
	}

	@Override
	public String generateHTMLResult(Analysis analysis, Map<String, String> passedAttributes, Locale locale) {
		return null; // TODO: Implement this method (Gerald Wimmer, 2023-11-12)
	}
}