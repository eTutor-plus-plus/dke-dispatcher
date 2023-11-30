package at.jku.dke.etutor.modules.nf.analysis.normalization;

import at.jku.dke.etutor.modules.nf.algorithms.Closure;
import at.jku.dke.etutor.modules.nf.algorithms.Member;
import at.jku.dke.etutor.modules.nf.analysis.DependenciesPreservationAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.KeysDeterminator;
import at.jku.dke.etutor.modules.nf.analysis.NormalformAnalyzerConfig;
import at.jku.dke.etutor.modules.nf.analysis.decompose.DecompositionAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.keys.KeysAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.keys.KeysAnalyzerConfig;
import at.jku.dke.etutor.modules.nf.analysis.minimalcover.MinimalCoverAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.normalform.NormalformAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.rbr.RBRAnalyzer;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.KeysContainer;
import at.jku.dke.etutor.modules.nf.model.Relation;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;


public class NormalizationAnalyzer {

	private NormalizationAnalyzer() {
		// This class is not meant to be instantiated.
	}
	
	public static NormalizationAnalysis analyze(NormalizationAnalyzerConfig config) {
		NormalizationAnalysis analysis = new NormalizationAnalysis();
		analysis.setSubmissionSuitsSolution(true);
		analysis.setMaxLostDependencies(config.getMaxLostDependencies());
		analysis.setDesiredNormalformLevel(config.getDesiredNormalformLevel());
		
		//ANALYZE DECOMPOSITION
		analysis.setDecompositionAnalysis(analyzeDecomposition(config.getBaseRelation(), config.getNormalizedRelations()));
		if (!analysis.getDecompositionAnalysis().submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			return analysis;
		}

		//ANALYZE LOSSLESS
		analysis.setLosslessAnalysis(analyzeLossless(config.getBaseRelation(), config.getNormalizedRelations()));
		if (!analysis.getLossLessAnalysis().submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			return analysis;
		}

		//INITIALIZE CORRECT KEYS
		HashMap<String, KeysContainer> correctKeysOfNormalizedRelations = new HashMap<>();

		for (IdentifiedRelation currNormalizedRelation : config.getNormalizedRelations()) {
			correctKeysOfNormalizedRelations.put(currNormalizedRelation.getID(), KeysDeterminator.determineAllKeys(currNormalizedRelation));
		}

		//ANALYZE CANONICAL REPRESENTATION
		for (IdentifiedRelation currRelation : config.getNormalizedRelations()) {
			analysis.addCanonicalRepresentationAnalysis(currRelation.getID(), MinimalCoverAnalyzer.analyzeCanonicalRepresentation(currRelation.getFunctionalDependencies()));
			if (!analysis.getCanonicalRepresentationAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
				return analysis;
			}
		}
	
		//ANALYZE TRIVIAL FUNCTIONAL DEPENDENCIES
		for (IdentifiedRelation currRelation : config.getNormalizedRelations()) {
			analysis.addTrivialDependenciesAnalysis(currRelation.getID(), MinimalCoverAnalyzer.analyzeTrivialDependencies(currRelation.getFunctionalDependencies()));
			if (!analysis.getTrivialDependenciesAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
				return analysis;
			}
		}
		
		//ANALYZE EXTRANEOUS ATTRIBUTES
		for (IdentifiedRelation currRelation : config.getNormalizedRelations()) {
			analysis.addExtraneousAttributesAnalysis(currRelation.getID(), MinimalCoverAnalyzer.analyzeExtraneousAttributes(currRelation.getFunctionalDependencies()));
			if (!analysis.getExtraneousAttributesAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
				return analysis;
			}
		}
		
		//ANALYZE REDUNDAND FUNCTIONAL DEPENDENCIES
		for (IdentifiedRelation currRelation : config.getNormalizedRelations()) {
			analysis.addRedundandDependenciesAnalysis(currRelation.getID(), MinimalCoverAnalyzer.analyzeRedundandDependencies(currRelation.getFunctionalDependencies()));
			if (!analysis.getRedundandDependenciesAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
				return analysis;
			}
		}
		
		//ANALYZE DEPENDENCIES PRESERVATION
		analysis.setDepPresAnalysis(NormalizationAnalyzer.analyzeDependenciesPreservation(config.getBaseRelation(), config.getNormalizedRelations()));
		if (analysis.getDepPresAnalysis().lostFunctionalDependenciesCount() > config.getMaxLostDependencies()){
			analysis.setSubmissionSuitsSolution(false);
		} 
		
		//ANALYZE RBR DECOMPOSITION
		for (IdentifiedRelation currRelation : config.getNormalizedRelations()) {
			analysis.addRBRAnalysis(currRelation.getID(), RBRAnalyzer.analyze(config.getBaseRelation(), currRelation));
			if (!analysis.getRBRAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
			}
		}

		//ANALYZE KEYS
		KeysAnalyzerConfig keysAnalyzerConfig = new KeysAnalyzerConfig();
		for (IdentifiedRelation currNormalizedRelation : config.getNormalizedRelations()) {
			keysAnalyzerConfig.setCorrectMinimalKeys(correctKeysOfNormalizedRelations.get(currNormalizedRelation.getID()).getMinimalKeys());

			analysis.addKeysAnalysis(currNormalizedRelation.getID(), KeysAnalyzer.analyze(currNormalizedRelation, keysAnalyzerConfig));
			if (!analysis.getKeysAnalysis(currNormalizedRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
			}
		}

		//ANALYZE NORMALFORM LEVELS
		NormalformAnalyzerConfig normalformAnalyzerConfig = new NormalformAnalyzerConfig();
		for (IdentifiedRelation currRelation : config.getNormalizedRelations()) {
			normalformAnalyzerConfig.setRelation(currRelation);
			normalformAnalyzerConfig.setDesiredNormalformLevel(config.getDesiredNormalformLevel());
			normalformAnalyzerConfig.setCorrectMinimalKeys(correctKeysOfNormalizedRelations.get(currRelation.getID()).getMinimalKeys());
			normalformAnalyzerConfig.setCorrectPartialKeys(correctKeysOfNormalizedRelations.get(currRelation.getID()).getPartialKeys());

			analysis.addNormalformAnalysis(currRelation.getID(), NormalformAnalyzer.analyze(normalformAnalyzerConfig));
			if (!analysis.getNormalformAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
			}
		}
				
		return analysis;
	}
	
	public static DecompositionAnalysis analyzeDecomposition(Relation baseRelation, Collection<? extends Relation> decomposedRelations){
		DecompositionAnalysis analysis = new DecompositionAnalysis();
		analysis.setMissingAttributes(baseRelation.getAttributes());

        for (Relation decomposedRelation : decomposedRelations) {
            analysis.removeAllMissingAttributes(decomposedRelation.getAttributes());
        }

		analysis.setSubmissionSuitsSolution((analysis.getMissingAttributes().isEmpty()));

		return analysis;
	}
	
	public static LosslessAnalysis analyzeLossless(Relation baseRelation, Collection<? extends Relation> decomposedRelations){
		LosslessAnalysis analysis = new LosslessAnalysis();
		analysis.setSubmissionSuitsSolution(false);
		HashSet<FunctionalDependency> decomposedRelationsDependencies = new HashSet<>();

		for (Relation decomposedRelation : decomposedRelations) {
			decomposedRelationsDependencies.addAll(decomposedRelation.getFunctionalDependencies());
		}

		for (Relation decomposedRelation : decomposedRelations){
			TreeSet<String> closure = Closure.execute(decomposedRelation.getAttributes(), decomposedRelationsDependencies);
			if (closure.containsAll(baseRelation.getAttributes())){
				analysis.setSubmissionSuitsSolution(true);
			}
		}

		return analysis;
	}

	public static DependenciesPreservationAnalysis analyzeDependenciesPreservation(Relation baseRelation, Collection<? extends Relation> decomposedRelations){
		HashSet<FunctionalDependency> decomposedRelationsDependencies = new HashSet<>();
		DependenciesPreservationAnalysis analysis = new DependenciesPreservationAnalysis();

        for (Relation decomposedRelation : decomposedRelations) {
            decomposedRelationsDependencies.addAll(decomposedRelation.getFunctionalDependencies());
        }

		for (FunctionalDependency currBaseDependency : baseRelation.getFunctionalDependencies()){
			if (!Member.execute(currBaseDependency, decomposedRelationsDependencies)){
				analysis.addLostFunctionalDependency(currBaseDependency);						
			}
		}

		analysis.setSubmissionSuitsSolution(analysis.getLostFunctionalDependencies().isEmpty());
		return analysis;
	}

	public static void analyzeNormalform(Collection<? extends Relation> decomposedRelations, NormalizationAnalysis analysis, Map<String, KeysContainer> correctKeys){
		NormalformAnalyzerConfig config = new NormalformAnalyzerConfig();
        for (Relation decomposedRelation : decomposedRelations) {
            IdentifiedRelation currRelation = (IdentifiedRelation) decomposedRelation;

            config.setRelation(currRelation);
            config.setCorrectMinimalKeys(correctKeys.get(currRelation.getID()).getMinimalKeys());
            config.setCorrectPartialKeys(correctKeys.get(currRelation.getID()).getPartialKeys());
            analysis.addNormalformAnalysis(currRelation.getID(), NormalformAnalyzer.analyze(config));
        }
	}
}
