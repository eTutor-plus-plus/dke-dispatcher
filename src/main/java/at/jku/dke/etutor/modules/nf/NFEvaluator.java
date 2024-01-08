package at.jku.dke.etutor.modules.nf;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.core.evaluation.Evaluator;
import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.core.evaluation.Report;
import at.jku.dke.etutor.modules.nf.analysis.NFAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.closure.AttributeClosureAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.closure.AttributeClosureAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.keys.KeysAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.keys.KeysAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.keys.KeysAnalyzerConfig;
import at.jku.dke.etutor.modules.nf.analysis.minimalcover.MinimalCoverAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.minimalcover.MinimalCoverAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.normalform.NormalformAnalyzerConfig;
import at.jku.dke.etutor.modules.nf.analysis.normalformdetermination.NormalformDeterminationAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.normalformdetermination.NormalformDeterminationAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.normalization.KeysDeterminator;
import at.jku.dke.etutor.modules.nf.analysis.normalization.NormalizationAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.normalization.NormalizationAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.normalization.NormalizationAnalyzerConfig;
import at.jku.dke.etutor.modules.nf.exercises.RDBDExercisesManager;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.KeysContainer;
import at.jku.dke.etutor.modules.nf.model.Relation;
import at.jku.dke.etutor.modules.nf.parser.NFLexer;
import at.jku.dke.etutor.modules.nf.parser.NFParser;
import at.jku.dke.etutor.modules.nf.parser.NFParserErrorCollector;
import at.jku.dke.etutor.modules.nf.report.AttributeClosureReporter;
import at.jku.dke.etutor.modules.nf.report.ErrorReport;
import at.jku.dke.etutor.modules.nf.report.KeysReporter;
import at.jku.dke.etutor.modules.nf.report.MinimalCoverReporter;
import at.jku.dke.etutor.modules.nf.report.NFReport;
import at.jku.dke.etutor.modules.nf.report.NormalformReporter;
import at.jku.dke.etutor.modules.nf.report.NormalizationReporter;
import at.jku.dke.etutor.modules.nf.report.NormalizationReporterConfig;
import at.jku.dke.etutor.modules.nf.report.ReporterConfig;
import at.jku.dke.etutor.modules.nf.specification.AttributeClosureSpecification;
import at.jku.dke.etutor.modules.nf.specification.KeysDeterminationSpecification;
import at.jku.dke.etutor.modules.nf.specification.MinimalCoverSpecification;
import at.jku.dke.etutor.modules.nf.specification.NormalformDeterminationSpecification;
import at.jku.dke.etutor.modules.nf.specification.NormalizationSpecification;
import at.jku.dke.etutor.modules.nf.ui.HTMLPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

@Service
public class NFEvaluator implements Evaluator {
	/*
	 * TODO: In the old eTutor, the Map parameters were effectively
	 *  passedAttributes: Map<String, Serializable>
	 *  passedParameters: Map<String, String[]> (according to documentation, actually seems to be single Strings that
	 *   may contain multiple values)
	 *  This must be adapted to match the interface's Map<String, String> in both cases (Gerald Wimmer, 2023-11-12).
	 */
	@Override
	public Analysis analyze(int exerciseID, int userID, Map<String, String> passedAttributes, Map<String, String> passedParameters, Locale locale) throws Exception {
		NFHelper.getLogger().log(Level.INFO, "Start analyzing.");

		String submissionString = passedAttributes.get("submission");

		// Source: https://datacadamia.com/antlr/getting_started (Gerald Wimmer, 2023-11-27)
		CharStream submissionLexerInput = CharStreams.fromString(submissionString);
		Lexer submissionLexer = new NFLexer(submissionLexerInput);
		TokenStream submissionParserInput = new CommonTokenStream(submissionLexer);
		NFParser submissionParser = new NFParser(submissionParserInput);

		NFParserErrorCollector errorCollector = new NFParserErrorCollector();
		submissionParser.addErrorListener(errorCollector);

		int internalTypeId = RDBDExercisesManager.fetchInternalType(exerciseID);
		NFConstants.Type type = NFConstants.Type.values()[internalTypeId];
		String specificationString = RDBDExercisesManager.fetchSpecification(exerciseID);

		NFAnalysis analysis;
		switch (type) {
			case KEYS_DETERMINATION -> {
				KeysDeterminationSpecification specification = null;
				try {    // Source: https://mkyong.com/java/how-to-convert-java-object-to-from-json-jackson/
					specification = new ObjectMapper().readValue(specificationString, KeysDeterminationSpecification.class);
				} catch (Exception e) {
					e.printStackTrace();
				}

				KeysAnalyzerConfig keysAnalyzerConfig = new KeysAnalyzerConfig();
				KeysContainer correctKeys = KeysDeterminator.determineAllKeys(specification.getBaseRelation());
				keysAnalyzerConfig.setCorrectMinimalKeys(correctKeys.getMinimalKeys());

				// Assemble relation from input string (Gerald Wimmer, 2023-11-27)
				Relation submission = new Relation();
				Set<Key> minimalKeys = submissionParser.keySet().keys;
				if (!errorCollector.getSyntaxErrors().isEmpty()) {
					analysis = new KeysAnalysis();

					analysis.setSyntaxError(errorCollector.getSyntaxErrors().toArray(new String[0]));
				} else {
					submission.setMinimalKeys(minimalKeys);

					analysis = KeysAnalyzer.analyze(submission, keysAnalyzerConfig);
				}

				//Set Submission
				analysis.setSubmission(submission);

			}
			case MINIMAL_COVER -> {
				MinimalCoverSpecification specification = null;
				try {
					specification = new ObjectMapper().readValue(specificationString, MinimalCoverSpecification.class);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Assemble relation from input String. (Gerald Wimmer, 2023-11-27)
				Relation submission = new Relation();
				Set<FunctionalDependency> functionalDependencies = submissionParser.functionalDependencySet().functionalDependencies;
				if (!errorCollector.getSyntaxErrors().isEmpty()) {
					analysis = new MinimalCoverAnalysis();

					analysis.setSyntaxError(errorCollector.getSyntaxErrors().toArray(new String[0]));
				} else {
					submission.setFunctionalDependencies(functionalDependencies);

					analysis = MinimalCoverAnalyzer.analyze(submission, specification.getBaseRelation());
				}

				//Set Submission
				analysis.setSubmission(submission);

			}
			case ATTRIBUTE_CLOSURE -> {
				AttributeClosureSpecification attributeClosureSpecification = null;
				try {
					attributeClosureSpecification = new ObjectMapper().readValue(specificationString, AttributeClosureSpecification.class);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Assemble relation from input String. (Gerald Wimmer, 2023-11-27)
				Relation submission = new Relation();
				Set<String> attributes = submissionParser.attributeSet().attributes;
				if (!errorCollector.getSyntaxErrors().isEmpty()) {
					analysis = new AttributeClosureAnalysis();

					analysis.setSyntaxError(errorCollector.getSyntaxErrors().toArray(new String[0]));
				} else {
					submission.setAttributes(attributes);

					analysis = AttributeClosureAnalyzer.analyze(
							attributeClosureSpecification.getBaseRelation().getFunctionalDependencies(),
							attributeClosureSpecification.getBaseAttributes(),
							submission.getAttributes());
				}

				//Set Submission
				analysis.setSubmission(submission);

			}
			/*case DECOMPOSE -> {	// Note: Replaced with Normalize, now that steps no longer need to be validated
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
				if (passedParameters.get(NFConstants.PARAM_DIAGNOSE_RELATION) != null && !passedParameters.get(NFConstants.PARAM_DIAGNOSE_RELATION).isEmpty()){
					baseRelationID = passedParameters.get(NFConstants.PARAM_DIAGNOSE_RELATION);
				} else {
					baseRelationID = decomposeSpecification.getBaseRelation().getID();
				}

				decomposeAnalyzerConfig.setBaseRelation(NFHelper.findRelation(baseRelationID, allRelations));
				NFHelper.getLogger().log(Level.INFO, "BaseRelation: '" + baseRelationID + "'.");

				temp = new StringBuilder();
				for(IdentifiedRelation ir : allRelations){
					temp.append("Relation '").append(ir.getID()).append("' ");
				}
				NFHelper.getLogger().log(Level.INFO, "All submitted Relations: " + temp + ".");

				decomposeAnalyzerConfig.setDecomposedRelations(NFHelper.findSubRelations(baseRelationID, allRelations));
				temp = new StringBuilder();
				for(IdentifiedRelation ir : decomposeAnalyzerConfig.getDecomposedRelations()){
					temp.append("Relation '").append(ir.getID()).append("' ");
				}
				NFHelper.getLogger().log(Level.INFO, "Decomposed Relations: " + temp + ".");

				decomposeAnalyzerConfig.setDesiredNormalformLevel(decomposeSpecification.getTargetLevel());
				NFHelper.getLogger().log(Level.INFO, "Target NormalformLevel: '" + decomposeSpecification.getTargetLevel() + "'.");

				decomposeAnalyzerConfig.setMaxLostDependencies(decomposeSpecification.getMaxLostDependencies());
				NFHelper.getLogger().log(Level.INFO, "Max Lost Dependencies: '" + decomposeSpecification.getMaxLostDependencies() + "'.");

				analysis = DecomposeAnalyzer.analyze(decomposeAnalyzerConfig);

				//Set Submission
				analysis.setSubmission(submissionTreeSet);

			}*/
			case NORMALIZATION -> { // Note: Could be identical to Decompose, now that you only have to specify the end result (Gerald Wimmer, 2023-11-27)
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

				// Get normalized relations from input String. (Gerald Wimmer, 2023-12-02)
				Set<IdentifiedRelation> submissionSet = submissionParser.relationSet().relations;
				if (!errorCollector.getSyntaxErrors().isEmpty()) {
					analysis = new NormalizationAnalysis();

					analysis.setSyntaxError(errorCollector.getSyntaxErrors().toArray(new String[0]));
				} else {
					normalizationAnalyzerConfig.setNormalizedRelations(submissionSet);

					analysis = NormalizationAnalyzer.analyze(normalizationAnalyzerConfig);
				}

				//Set Submission
				/*
				 * Note: This cast works because submissionSet is instantiated as a HashSet inside submissionParser.
				 *  (Gerald Wimmer, 2023-12-02)
				 */
				analysis.setSubmission((Serializable) submissionSet);

			}
			case NORMALFORM_DETERMINATION -> {
				NormalformDeterminationSpecification specification = null;
				try {
					specification = new ObjectMapper().readValue(specificationString, NormalformDeterminationSpecification.class);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Get submission from input String. (Gerald Wimmer, 2023-12-02)
				NormalformDeterminationSubmission normalformDeterminationSubmission = submissionParser.normalFormSubmission().submission;
				if (!errorCollector.getSyntaxErrors().isEmpty()) {
					analysis = new NormalformDeterminationAnalysis();

					analysis.setSyntaxError(errorCollector.getSyntaxErrors().toArray(new String[0]));
				} else {
					NormalformAnalyzerConfig normalformAnalyzerConfig = new NormalformAnalyzerConfig();

					normalformAnalyzerConfig.setCorrectMinimalKeys(KeysDeterminator.determineMinimalKeys(specification.getBaseRelation()));
					normalformAnalyzerConfig.setRelation(specification.getBaseRelation());

					analysis = NormalformDeterminationAnalyzer.analyze(normalformDeterminationSubmission, normalformAnalyzerConfig);
				}

				//Set Submission
				analysis.setSubmission(normalformDeterminationSubmission);

			}
			default ->  {
				NFHelper.getLogger().log(Level.SEVERE, "RDBD internal type '" + internalTypeId + "' is not supported.");
				throw new Exception("Unsupported RDBD type.");
			}
		}

		analysis.setExerciseId(exerciseID);

		return analysis;
	}

	@Override
	public Grading grade(Analysis analysis, int maxPoints, Map<String, String> passedAttributes, Map<String, String> passedParameters) throws Exception {
		NFAnalysis nfAnalysis = (NFAnalysis) analysis;

		if(nfAnalysis.getGrading() != null) {
			return nfAnalysis.getGrading();
		}

		Grading grading = new DefaultGrading();
		grading.setMaxPoints(maxPoints);

		if(nfAnalysis.getSyntaxError() != null) {
			grading.setPoints(0);
			return grading;
		}

		int actualPoints = maxPoints;

		int internalType = RDBDExercisesManager.fetchInternalType(nfAnalysis.getExerciseId());
		NFConstants.Type type = NFConstants.Type.values()[internalType];
		String specificationString = RDBDExercisesManager.fetchSpecification(nfAnalysis.getExerciseId());


		switch (type) {
			case KEYS_DETERMINATION -> {
				KeysDeterminationSpecification specification = null;
				try {    // Source: https://mkyong.com/java/how-to-convert-java-object-to-from-json-jackson/
					specification = new ObjectMapper().readValue(specificationString, KeysDeterminationSpecification.class);
				} catch (Exception e) {
					e.printStackTrace();
				}

				KeysAnalysis keysAnalysis = (KeysAnalysis) nfAnalysis;

				actualPoints -= keysAnalysis.getMissingKeys().size() * specification.getPenaltyPerMissingKey();
				actualPoints -= keysAnalysis.getAdditionalKeys().size() * specification.getPenaltyPerIncorrectKey();

			}
			case MINIMAL_COVER -> {
				MinimalCoverSpecification specification = null;
				try {
					specification = new ObjectMapper().readValue(specificationString, MinimalCoverSpecification.class);
				} catch (Exception e) {
					e.printStackTrace();
				}

				MinimalCoverAnalysis minimalCoverAnalysis = (MinimalCoverAnalysis) nfAnalysis;

				actualPoints -= minimalCoverAnalysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size() * specification.getPenaltyPerNonCanonicalDependency();
				actualPoints -= minimalCoverAnalysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size() * specification.getPenaltyPerTrivialDependency();
				actualPoints -= minimalCoverAnalysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().values().stream()
						.mapToInt(List::size)
						.sum() * specification.getPenaltyPerExtraneousAttribute();
				actualPoints -= minimalCoverAnalysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size() * specification.getPenaltyPerRedundantDependency();
				actualPoints -= minimalCoverAnalysis.getDependenciesCoverAnalysis().getMissingDependencies().size() * specification.getPenaltyPerMissingDependencyVsSolution();
				actualPoints -= minimalCoverAnalysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size() * specification.getPenaltyPerIncorrectDependencyVsSolution();

			}
			case ATTRIBUTE_CLOSURE -> {
				AttributeClosureSpecification specification = null;
				try {
					specification = new ObjectMapper().readValue(specificationString, AttributeClosureSpecification.class);
				} catch (Exception e) {
					e.printStackTrace();
				}

				AttributeClosureAnalysis attributeClosureAnalysis = (AttributeClosureAnalysis) nfAnalysis;

				actualPoints -= attributeClosureAnalysis.getMissingAttributes().size() * specification.getPenaltyPerMissingAttribute();
				actualPoints -= attributeClosureAnalysis.getAdditionalAttributes().size() * specification.getPenaltyPerIncorrectAttribute();

			}
			case NORMALIZATION -> {
				NormalizationSpecification specification = null;
				try {
					specification = new ObjectMapper().readValue(specificationString, NormalizationSpecification.class);
				} catch (Exception e) {
					e.printStackTrace();
				}

				NormalizationAnalysis normalizationAnalysis = (NormalizationAnalysis) nfAnalysis;

				actualPoints -= normalizationAnalysis.getDecompositionAnalysis().getMissingAttributes().size() * specification.getPenaltyPerLostAttribute();
				if (!normalizationAnalysis.getLossLessAnalysis().submissionSuitsSolution()) {
					actualPoints -= specification.getPenaltyForLossyDecomposition();
				}
				actualPoints -= normalizationAnalysis.getCanonicalRepresentationAnalyses().values().stream()
						.mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
						.sum() * specification.getPenaltyPerNonCanonicalDependency();
				actualPoints -= normalizationAnalysis.getTrivialDependenciesAnalyses().values().stream()
						.mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
						.sum() * specification.getPenaltyPerTrivialDependency();
				actualPoints -= normalizationAnalysis.getExtraneousAttributesAnalyses().values().stream()
						.mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
								.mapToInt(List::size)
								.sum())
						.sum() * specification.getPenaltyPerExtraneousAttributeInDependencies();
				actualPoints -= normalizationAnalysis.getRedundantDependenciesAnalyses().values().stream()
						.mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
						.sum() * specification.getPenaltyPerRedundantDependency();
				if (normalizationAnalysis.getDepPresAnalysis().lostFunctionalDependenciesCount() > normalizationAnalysis.getMaxLostDependencies()) { // So that if we're below the threshold, we don't accidentally ADD points (Gerald Wimmer, 2024-01-01).
					actualPoints -= (normalizationAnalysis.getDepPresAnalysis().lostFunctionalDependenciesCount() - normalizationAnalysis.getMaxLostDependencies()) * specification.getPenaltyPerExcessiveLostDependency();
				}
				actualPoints -= normalizationAnalysis.getRbrAnalyses().values().stream()
						.mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
						.sum() * specification.getPenaltyPerMissingNewDependency();
				actualPoints -= normalizationAnalysis.getRbrAnalyses().values().stream()
						.mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
						.sum() * specification.getPenaltyPerIncorrectNewDependency();
				actualPoints -= normalizationAnalysis.getKeysAnalyses().values().stream()
						.mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
						.sum() * specification.getPenaltyPerMissingKey();
				actualPoints -= normalizationAnalysis.getKeysAnalyses().values().stream()
						.mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
						.sum() * specification.getPenaltyPerIncorrectKey();
				actualPoints -= normalizationAnalysis.getNormalformAnalyses().values().stream()
						.filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
						.count() * specification.getPenaltyPerIncorrectNFRelation();

			}
			case NORMALFORM_DETERMINATION -> {
				NormalformDeterminationSpecification specification = null;
				try {
					specification = new ObjectMapper().readValue(specificationString, NormalformDeterminationSpecification.class);
				} catch (Exception e) {
					e.printStackTrace();
				}

				NormalformDeterminationAnalysis normalformDeterminationAnalysis = (NormalformDeterminationAnalysis) nfAnalysis;

				if (!normalformDeterminationAnalysis.getOverallLevelIsCorrect()) {
					actualPoints -= specification.getPenaltyForIncorrectNFOverall();
				}
				actualPoints -= normalformDeterminationAnalysis.getWrongLeveledDependencies().size() * specification.getPenaltyPerIncorrectNFDependency();

			}
			default ->  {
				NFHelper.getLogger().log(Level.SEVERE, "RDBD internal type '" + internalType + "' is not supported.");
				throw new Exception("Unsupported RDBD type.");
			}
		}

		actualPoints = Math.max(0, actualPoints);
		grading.setPoints(actualPoints);

		nfAnalysis.setGrading(grading);
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
		/*
		 * NOTE: Whenever the parameter with the key NFConstants.ATT_EXERCISE_ID was queried, its .toString() value
		 *  was passed into Integer.parseInt(). I thus assume the values of this map to be Strings.
		 *  (Gerald Wimmer, 2023-11-12).
		 */
		NFAnalysis nfAnalysis = (NFAnalysis) analysis;

		if(nfAnalysis.getReport() != null) {
			return nfAnalysis.getReport();
		}

		// int exerciseIdParam = Integer.parseInt(passedAttributes.get(NFConstants.ATT_EXERCISE_ID));
		int exerciseIdParam = nfAnalysis.getExerciseId();
		String diagnoseLevelParam = passedAttributes.get(NFConstants.PARAM_LEVEL);

		int diagnoseLevel = 2;
		int internalType = RDBDExercisesManager.fetchInternalType(exerciseIdParam);
		NFConstants.Type type = NFConstants.Type.values()[internalType];

		String actionParam = passedAttributes.get(NFConstants.PARAM_ACTION);

		NFReport report;
		ReporterConfig config;
		if (!actionParam.equals(NFConstants.EVAL_ACTION_SUBMIT)) {
			try{
				diagnoseLevel = Integer.parseInt(diagnoseLevelParam);
			} catch (Exception ignore){
				NFHelper.getLogger().log(Level.WARNING, "Diagnose Level '" + diagnoseLevelParam + "' is not a number! Using default Diagnose Level '0'");
			}
		}

		if(nfAnalysis.getSyntaxError() != null) {
			ErrorReport errorReport = new ErrorReport();
			errorReport.setError(nfAnalysis.getSyntaxError());
			errorReport.setDescription("A syntax error occurred");
			errorReport.setShowErrorDescription(true);

			return errorReport;
		}

		switch (type) {
			case KEYS_DETERMINATION -> {
				//KEYS DETERMINATION
				NFHelper.getLogger().log(Level.INFO, "Printing report for internal type 'KEY_DETERMINATION'");

				config = new ReporterConfig();
				config.setAction(actionParam);
				config.setDiagnoseLevel(diagnoseLevel);
				report = KeysReporter.report((KeysAnalysis) analysis, (DefaultGrading) grading, config, locale);

			}
			case MINIMAL_COVER -> {
				//MINIMAL COVER
				NFHelper.getLogger().log(Level.INFO, "Printing report for internal type 'MINIMAL_COVER'");

				config = new ReporterConfig();
				config.setAction(actionParam);
				config.setDiagnoseLevel(diagnoseLevel);
				report = MinimalCoverReporter.report((MinimalCoverAnalysis) analysis, grading, config, locale);

			}
			case ATTRIBUTE_CLOSURE -> {
				//ATTRIBUTE CLOSURE
				NFHelper.getLogger().log(Level.INFO, "Printing report for internal type 'ATTRIBUTE_CLOSURE'");

				config = new ReporterConfig();
				config.setAction(actionParam);
				config.setDiagnoseLevel(diagnoseLevel);
				report = AttributeClosureReporter.report((AttributeClosureAnalysis) analysis, (DefaultGrading) grading, config, locale);

			}
			/*case DECOMPOSE) {
			//DECOMPOSE
			NFHelper.getLogger().log(Level.INFO, "Printing report for internal type 'DECOMPOSE'");
			
			DecomposeReporterConfig decomposeReporterConfig = new DecomposeReporterConfig();
			decomposeReporterConfig.setAction(actionParam);
			decomposeReporterConfig.setDiagnoseLevel(diagnoseLevel);
			decomposeReporterConfig.setGrading((DefaultGrading)grading);
			decomposeReporterConfig.setDecomposeAnalysis((DecomposeAnalysis)analysis);

			report = DecomposeReporter.report(decomposeReporterConfig, messageSource, locale);

		} */
			case NORMALIZATION -> {
				//NORMALIZATION
				NFHelper.getLogger().log(Level.INFO, "Printing report for internal type 'NORMALIZATION'");

				NormalizationReporterConfig normalizationReporterConfig = new NormalizationReporterConfig();
				normalizationReporterConfig.setAction(actionParam);
				normalizationReporterConfig.setDiagnoseLevel(diagnoseLevel);
				normalizationReporterConfig.setDecomposedRelations((TreeSet<IdentifiedRelation>) analysis.getSubmission());
				normalizationReporterConfig.setDesiredNormalformLevel(((NormalizationAnalysis) analysis).getDesiredNormalformLevel());

				report = NormalizationReporter.report((NormalizationAnalysis) analysis, (DefaultGrading) grading, normalizationReporterConfig, locale);

			}
			case NORMALFORM_DETERMINATION -> {
				//NORMALFORM DETERMINATION
				NFHelper.getLogger().log(Level.INFO, "Printing report for internal type 'NORMALFORM DETERMINATION'");

				config = new ReporterConfig();
				config.setAction(actionParam);
				config.setDiagnoseLevel(diagnoseLevel);

				report = NormalformReporter.report((NormalformDeterminationAnalysis) analysis, (DefaultGrading) grading, config, locale);

			}
			default -> {
				NFHelper.getLogger().log(Level.SEVERE, "RDBD internal type '" + internalType + "' is not supported.");
				throw new Exception("Unsupported RDBD type.");
			}
		}

		nfAnalysis.setReport(report);
		return report;
	}

	@Override
	public String generateHTMLResult(Analysis analysis, Map<String, String> passedAttributes, Locale locale) {
		try {
			NFAnalysis nfAnalysis = (NFAnalysis) analysis;
			NFReport nfReport = (NFReport) report(nfAnalysis, nfAnalysis.getGrading(), passedAttributes, null, locale);
			return HTMLPrinter.printReport(nfReport, 0, 0, locale);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}