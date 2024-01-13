package at.jku.dke.etutor.modules.nf.analysis.decompose;

import at.jku.dke.etutor.modules.nf.NFHelper;
import at.jku.dke.etutor.modules.nf.analysis.minimalcover.MinimalCoverAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.normalform.NormalformAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.normalform.NormalformAnalyzerConfig;
import at.jku.dke.etutor.modules.nf.analysis.normalization.NormalizationAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.normalization.NormalizationAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.rbr.RBRAnalyzer;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;
import at.jku.dke.etutor.modules.nf.model.KeysContainer;

import java.util.Map;
import java.util.logging.Level;

public class DecomposeStepAnalyzer {

	public static NormalizationAnalysis analyze(DecomposeStepAnalyzerConfig config) {
		NormalizationAnalysis analysis = new NormalizationAnalysis();
		analysis.setSubmissionSuitsSolution(true);
		analysis.setDesiredNormalformLevel(config.getDesiredNormalformLevel());
		
		StringBuilder temp = new StringBuilder();
		NFHelper.getLogger().log(Level.INFO, "ANALYZING Decomposition of Relation '" + config.getBaseRelation().getID() + "'.");

		for(IdentifiedRelation currDecomposedRelation : config.getNormalizedRelations()) {
			temp.append("Relation '").append(currDecomposedRelation.getID()).append("' ");
		}
		NFHelper.getLogger().log(Level.INFO, "With Sub-Relations: " + temp + ".");
		
		
		//ANALYZE DECOMPOSITION
		analysis.setDecompositionAnalysis(NormalizationAnalyzer.analyzeDecomposition(config.getBaseRelation(), config.getNormalizedRelations()));
		if (!analysis.getDecompositionAnalysis().submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			return analysis;
		}

		//ANALYZE LOSSLESS
		analysis.setLosslessAnalysis(NormalizationAnalyzer.analyzeLossless(config.getBaseRelation(), config.getNormalizedRelations()));
		if (!analysis.getLossLessAnalysis().submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			return analysis;
		}
		
		//ANALYZE CANONICAL REPRESENTATION
		for(IdentifiedRelation currRelation : config.getNormalizedRelations()) {

			analysis.addCanonicalRepresentationAnalysis(currRelation.getID(), MinimalCoverAnalyzer.analyzeCanonicalRepresentation(currRelation.getFunctionalDependencies()));
			if (!analysis.getCanonicalRepresentationAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
				return analysis;
			}
		}
	
		//ANALYZE TRIVIAL FUNCTIONAL DEPENDENCIES
		for(IdentifiedRelation currRelation : config.getNormalizedRelations()) {

			analysis.addTrivialDependenciesAnalysis(currRelation.getID(), MinimalCoverAnalyzer.analyzeTrivialDependencies(currRelation.getFunctionalDependencies()));
			if (!analysis.getTrivialDependenciesAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
				return analysis;
			}
		}
		
		//ANALYZE EXTRANEOUS ATTRIBUTES
		for(IdentifiedRelation currRelation : config.getNormalizedRelations()) {

			analysis.addExtraneousAttributesAnalysis(currRelation.getID(), MinimalCoverAnalyzer.analyzeExtraneousAttributes(currRelation.getFunctionalDependencies()));
			if (!analysis.getExtraneousAttributesAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
				return analysis;
			}
		}
		
		//ANALYZE REDUNDANT FUNCTIONAL DEPENDENCIES
		for(IdentifiedRelation currRelation : config.getNormalizedRelations()) {

			analysis.addRedundantDependenciesAnalysis(currRelation.getID(), MinimalCoverAnalyzer.analyzeRedundantDependencies(currRelation.getFunctionalDependencies()));
			if (!analysis.getRedundantDependenciesAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
				return analysis;
			}
		}
		
		//ANALYZE DEPENDENCIES PRESERVATION
		analysis.setDepPresAnalysis(NormalizationAnalyzer.analyzeDependenciesPreservation(config.getBaseRelation(), config.getNormalizedRelations()));
		
		//ANALYZE RBR DECOMPOSITION
		for(IdentifiedRelation currRelation : config.getNormalizedRelations()) {

			analysis.addRBRAnalysis(currRelation.getID(), RBRAnalyzer.analyze(config.getBaseRelation(), currRelation));
			if (!analysis.getRBRAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
			}
		}

		//ANALYZE KEYS and get correct keys
		Map<String, KeysContainer> correctKeysOfDecomposedRelations = NormalizationAnalyzer.analyzeCorrectKeys(analysis, config.getNormalizedRelations());

		//ANALYZE NORMALFORM LEVELS
		NormalformAnalyzerConfig normalformAnalyzerConfig = new NormalformAnalyzerConfig();
		for(IdentifiedRelation currRelation : config.getNormalizedRelations()) {
			normalformAnalyzerConfig.setRelation(currRelation);
			normalformAnalyzerConfig.setDesiredNormalformLevel(config.getDesiredNormalformLevel());
			normalformAnalyzerConfig.setCorrectMinimalKeys(correctKeysOfDecomposedRelations.get(currRelation.getID()).getMinimalKeys());
			normalformAnalyzerConfig.setCorrectPartialKeys(correctKeysOfDecomposedRelations.get(currRelation.getID()).getPartialKeys());

			analysis.addNormalformAnalysis(currRelation.getID(), NormalformAnalyzer.analyze(normalformAnalyzerConfig));
			if (!NFHelper.isInnerNode(currRelation.getID(), config.getAllRelations())){
				if (!analysis.getNormalformAnalysis(currRelation.getID()).submissionSuitsSolution()){
					analysis.setSubmissionSuitsSolution(false);
				}
			}
		}
				
		return analysis;
	}
}
