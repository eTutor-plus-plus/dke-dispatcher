package at.jku.dke.etutor.modules.nf;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.core.evaluation.Evaluator;
import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.core.evaluation.Report;
import at.jku.dke.etutor.modules.nf.analysis.KeysDeterminator;
import at.jku.dke.etutor.modules.nf.analysis.NormalformAnalyzerConfig;
import at.jku.dke.etutor.modules.nf.analysis.closure.AttributeClosureAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.closure.AttributeClosureAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.decompose.DecomposeAnalysis;
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
import at.jku.dke.etutor.modules.nf.exercises.RDBDExercisesManager;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.KeysContainer;
import at.jku.dke.etutor.modules.nf.model.Relation;
import at.jku.dke.etutor.modules.nf.parser.NFLexer;
import at.jku.dke.etutor.modules.nf.parser.NFParser;
import at.jku.dke.etutor.modules.nf.report.AttributeClosureReporter;
import at.jku.dke.etutor.modules.nf.report.DecomposeReporter;
import at.jku.dke.etutor.modules.nf.report.DecomposeReporterConfig;
import at.jku.dke.etutor.modules.nf.report.KeysReporter;
import at.jku.dke.etutor.modules.nf.report.MinimalCoverReporter;
import at.jku.dke.etutor.modules.nf.report.NormalformReporter;
import at.jku.dke.etutor.modules.nf.report.NormalizationReporter;
import at.jku.dke.etutor.modules.nf.report.NormalizationReporterConfig;
import at.jku.dke.etutor.modules.nf.report.RBRReporter;
import at.jku.dke.etutor.modules.nf.report.ReporterConfig;
import at.jku.dke.etutor.modules.nf.specification.AttributeClosureSpecification;
import at.jku.dke.etutor.modules.nf.specification.NormalizationSpecification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
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
		RDBDHelper.getLogger().log(Level.INFO, "Start analyzing.");

		String submissionString = passedAttributes.get(RDBDConstants.calcSubmissionIDFor(exerciseID));

		// Source: https://datacadamia.com/antlr/getting_started (Gerald Wimmer, 2023-11-27)
		CharStream submissionLexerInput = CharStreams.fromString(submissionString);
		Lexer submissionLexer = new NFLexer(submissionLexerInput);
		TokenStream submissionParserInput = new CommonTokenStream(submissionLexer);
		NFParser submissionParser = new NFParser(submissionParserInput);

		int internalType = RDBDExercisesManager.fetchInternalType(exerciseID);
		String specificationString = RDBDExercisesManager.fetchSpecification(exerciseID);

		Analysis analysis;
		if (internalType == RDBDConstants.TYPE_KEYS_DETERMINATION) {
			Relation specification = null;
			try {	// Source: https://mkyong.com/java/how-to-convert-java-object-to-from-json-jackson/
				specification = new ObjectMapper().readValue(specificationString, Relation.class);
			} catch (Exception e) {
				e.printStackTrace();
			}

			KeysAnalyzerConfig keysAnalyzerConfig = new KeysAnalyzerConfig();
			KeysContainer correctKeys = KeysDeterminator.determineAllKeys(specification);
			keysAnalyzerConfig.setCorrectMinimalKeys(correctKeys.getMinimalKeys());

			// Assemble relation from input string (Gerald Wimmer, 2023-11-27)
			Relation submission = new Relation();
			Set<Key> minimalKeys = submissionParser.keySet().keys;
			submission.setMinimalKeys(minimalKeys);

			analysis = KeysAnalyzer.analyze(submission, keysAnalyzerConfig);
			
			//Set Submission
			analysis.setSubmission(submission);

		} else if (internalType == RDBDConstants.TYPE_MINIMAL_COVER) {
			Relation specification = null;
			try {
				specification = new ObjectMapper().readValue(specificationString, Relation.class);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Assemble relation from input String. (Gerald Wimmer, 2023-11-27)
			Relation submission = new Relation();
			Set<FunctionalDependency> functionalDependencies = submissionParser.functionalDependencySet().functionalDependencies;
			submission.setFunctionalDependencies(functionalDependencies);

			analysis = MinimalCoverAnalyzer.analyze(submission, specification);

			//Set Submission
			analysis.setSubmission(submission);

		} else if (internalType == RDBDConstants.TYPE_ATTRIBUTE_CLOSURE) {
			AttributeClosureSpecification attributeClosureSpecification = null;
			try {
				attributeClosureSpecification = new ObjectMapper().readValue(specificationString, AttributeClosureSpecification.class);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Assemble relation from input String. (Gerald Wimmer, 2023-11-27)
			Relation submission = new Relation();
			Set<String> attributes = submissionParser.attributeSet().attributes;
			submission.setAttributes(attributes);

			analysis = AttributeClosureAnalyzer.analyze(
					attributeClosureSpecification.getBaseRelation().getFunctionalDependencies(),
					attributeClosureSpecification.getBaseAttributes(),
					submission.getAttributes());
				
			//Set Submission
			analysis.setSubmission(submission);

		} /*else if (internalType == RDBDConstants.TYPE_RBR) { // Note: No longer necessary as its own task type, may be required for Decompose (Gerald Wimmer, 2023-11-27)
			//RBR
			RBRSpecification rbrSpecification = (RBRSpecification)specification;
			Relation relation = (Relation)((Collection<IdentifiedRelation>)submission).toArray()[0]; // TODO: Replace with call to parser (Gerald Wimmer, 2023-11-12)
			analysis = RBRAnalyzer.analyze(rbrSpecification.getBaseRelation(), relation);
			
			//Set Submission
			analysis.setSubmission(relation);

		}*/ /*else if (internalType == RDBDConstants.TYPE_DECOMPOSE) {	// Note: Replaced with Normalize, now that steps no longer need to be validated
			// DecomposeSpecification decomposeSpecification = (DecomposeSpecification)specification;
			DecomposeSpecification decomposeSpecification = null;
			try {
				decomposeSpecification = new ObjectMapper().readValue(specificationString, DecomposeSpecification.class);
			} catch (Exception e) {
				e.printStackTrace();
			}

			StringBuilder temp;
			
			DecomposeAnalyzerConfig decomposeAnalyzerConfig = new DecomposeAnalyzerConfig();

			TreeSet<IdentifiedRelation> allRelations = new TreeSet<>(new IdentifiedRelationComparator());
			allRelations.add(decomposeSpecification.getBaseRelation());

			// TODO: Replace with call to parser (Gerald Wimmer, 2023-11-12)
			// NOTE: Generic <IdentifiedRelation> should always work, because that is the actual type passed in by RDBDEditor.initPerformTask() (see the first if-statement there). (Gerald Wimmer, 2023-11-12)
			TreeSet<IdentifiedRelation> submissionTreeSet = (TreeSet<IdentifiedRelation>)submissionString;
			allRelations.addAll(submissionTreeSet);
			
			String baseRelationID;
			if (passedParameters.get(RDBDConstants.PARAM_DIAGNOSE_RELATION) != null && !passedParameters.get(RDBDConstants.PARAM_DIAGNOSE_RELATION).isEmpty()){
				baseRelationID = passedParameters.get(RDBDConstants.PARAM_DIAGNOSE_RELATION);
			} else {
				baseRelationID = decomposeSpecification.getBaseRelation().getID();
			}

			decomposeAnalyzerConfig.setBaseRelation(RDBDHelper.findRelation(baseRelationID, allRelations));
			RDBDHelper.getLogger().log(Level.INFO, "BaseRelation: '" + baseRelationID + "'."); 

			temp = new StringBuilder();
			for(IdentifiedRelation ir : allRelations){
				temp.append("Relation '").append(ir.getID()).append("' ");
			}
			RDBDHelper.getLogger().log(Level.INFO, "All submitted Relations: " + temp + ".");

			decomposeAnalyzerConfig.setDecomposedRelations(RDBDHelper.findSubRelations(baseRelationID, allRelations));
			temp = new StringBuilder();
			for(IdentifiedRelation ir : decomposeAnalyzerConfig.getDecomposedRelations()){
				temp.append("Relation '").append(ir.getID()).append("' ");
			}
			RDBDHelper.getLogger().log(Level.INFO, "Decomposed Relations: " + temp + ".");
			
			decomposeAnalyzerConfig.setDesiredNormalformLevel(decomposeSpecification.getTargetLevel());
			RDBDHelper.getLogger().log(Level.INFO, "Target NormalformLevel: '" + decomposeSpecification.getTargetLevel() + "'."); 

			decomposeAnalyzerConfig.setMaxLostDependencies(decomposeSpecification.getMaxLostDependencies());
			RDBDHelper.getLogger().log(Level.INFO, "Max Lost Dependencies: '" + decomposeSpecification.getMaxLostDependencies() + "'."); 
			
			analysis = DecomposeAnalyzer.analyze(decomposeAnalyzerConfig);
			
			//Set Submission
			analysis.setSubmission(submissionTreeSet);

		}*/ else if (internalType == RDBDConstants.TYPE_NORMALIZATION) { // Note: Could be identical to Decompose, now that you only have to specify the end result (Gerald Wimmer, 2023-11-27)
			NormalizationSpecification normalizationSpecification = null;
			try {
				normalizationSpecification = new ObjectMapper().readValue(specificationString, NormalizationSpecification.class);
			} catch (Exception e) {
				e.printStackTrace();
			}

			NormalizationAnalyzerConfig normalizationAnalyzerConfig = new NormalizationAnalyzerConfig();

			normalizationAnalyzerConfig.setBaseRelation(normalizationSpecification.getBaseRelation());
			normalizationAnalyzerConfig.setDesiredNormalformLevel(normalizationSpecification.getTargetLevel());
			normalizationAnalyzerConfig.setMaxLostDependencies(normalizationSpecification.getMaxLostDependencies());
			/*
			 * TODO: Replaced with call to parser (Gerald Wimmer, 2023-12-02)
			 *
			 * NOTE: Cast to TreeSet<IdentifiedRelation> should always work, because that is the actual type passed in
			 *  by RDBDEditor.initPerformTask(). Also, it was cast (albeit needlessly) to TreeSet (without generics
			 *  because there were none back then) for analysis.setSubmission(). (Gerald Wimmer, 2023-11-12)
			 */
			// TreeSet<IdentifiedRelation> submissionTreeSet = (TreeSet<IdentifiedRelation>) submission;
			// Get normalized relations from input String. (Gerald Wimmer, 2023-12-02)
			Set<IdentifiedRelation> submissionSet = submissionParser.relationSet().relations;
			normalizationAnalyzerConfig.setNormalizedRelations(submissionSet);
			
			analysis = NormalizationAnalyzer.analyze(normalizationAnalyzerConfig);
			
			//Set Submission
			/*
			 * Note: This cast works because submissionSet is instantiated as a HashSet inside submissionParser.
			 *  (Gerald Wimmer, 2023-12-02)
			 */
			analysis.setSubmission((Serializable)submissionSet);

		} else if (internalType == RDBDConstants.TYPE_NORMALFORM_DETERMINATION) {
			Relation specification = null;
			try {
				specification = new ObjectMapper().readValue(specificationString, Relation.class);
			} catch (Exception e) {
				e.printStackTrace();
			}

            // Get submission from input String. (Gerald Wimmer, 2023-12-02)
			NormalformDeterminationSubmission normalformDeterminationSubmission = submissionParser.normalFormSubmission().submission;

			NormalformAnalyzerConfig normalformAnalyzerConfig = new NormalformAnalyzerConfig();

			normalformAnalyzerConfig.setCorrectMinimalKeys(KeysDeterminator.determineMinimalKeys(specification));
			normalformAnalyzerConfig.setRelation(specification);
			
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
		String actionParam = passedAttributes.get(RDBDConstants.PARAM_ACTION);
		/*
		 * NOTE: Whenever the parameter with the key RDBDConstants.ATT_EXERCISE_ID was queried, its .toString() value
		 *  was passed into Integer.parseInt(). I thus assume the values of this map to be Strings.
		 *  (Gerald Wimmer, 2023-11-12).
		 */
		int exerciseIdParam = Integer.parseInt(passedAttributes.get(RDBDConstants.ATT_EXERCISE_ID));
		String diagnoseLevelParam = passedAttributes.get(RDBDConstants.PARAM_LEVEL);

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
			report = MinimalCoverReporter.report((MinimalCoverAnalysis)analysis, grading, config, messageSource, locale);

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