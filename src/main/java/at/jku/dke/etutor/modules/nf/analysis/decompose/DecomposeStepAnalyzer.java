package at.jku.dke.etutor.modules.nf.analysis.decompose;

import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.analysis.*;
import at.jku.dke.etutor.modules.nf.analysis.keys.KeysAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.keys.KeysAnalyzerConfig;
import at.jku.dke.etutor.modules.nf.analysis.minimalcover.MinimalCoverAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.normalform.NormalformAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.normalization.NormalizationAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.normalization.NormalizationAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.rbr.RBRAnalyzer;
import at.jku.dke.etutor.modules.nf.model.KeysContainer;
import at.jku.dke.etutor.modules.nf.ui.IdentifiedRelation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

public class DecomposeStepAnalyzer {

	public static NormalizationAnalysis analyze(DecomposeStepAnalyzerConfig config){
		IdentifiedRelation currRelation;
		KeysAnalyzerConfig keysAnalyzerConfig;
		HashMap<String, KeysContainer> correctKeysOfDecomposedRelations = new HashMap<>();
		IdentifiedRelation currDecomposedRelation;
		NormalformAnalyzerConfig normalformAnalyzerConfig;

		NormalizationAnalysis analysis = new NormalizationAnalysis();
		analysis.setSubmissionSuitsSolution(true);
		analysis.setDesiredNormalformLevel(config.getDesiredNormalformLevel());
		
		StringBuilder temp = new StringBuilder();
		RDBDHelper.getLogger().log(Level.INFO, "ANALYZING Decomposition of Relation '" + config.getBaseRelation().getID() + "'.");
		Iterator<IdentifiedRelation> decomposedRelationsIterator = config.iterNormalizedRelations();
		while(decomposedRelationsIterator.hasNext()){
			temp.append("Relation '").append(decomposedRelationsIterator.next().getID()).append("' ");
		}
		RDBDHelper.getLogger().log(Level.INFO, "With Sub-Relations: " + temp + ".");
		
		
		//ANALYZE DECOMPOSITION
		analysis.setDecompositionAnalysis(NormalizationAnalyzer.analyzeDecomposition(config.getBaseRelation(), config.getNormalizedRelations()));
		if (!analysis.getDecompositionAnalysis().submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			return analysis;
		}

		//ANALYZE LOSS LESS
		analysis.setLossLessAnalysis(NormalizationAnalyzer.analyzeLossless(config.getBaseRelation(), config.getNormalizedRelations()));
		if (!analysis.getLossLessAnalysis().submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			return analysis;
		}

		//INITIALIZE CORRECT KEYS
		decomposedRelationsIterator = config.getNormalizedRelations().iterator();
		while (decomposedRelationsIterator.hasNext()){
			currDecomposedRelation = decomposedRelationsIterator.next();
			correctKeysOfDecomposedRelations.put(currDecomposedRelation.getID(), KeysDeterminator.determineAllKeys(currDecomposedRelation));
		}
		
		//ANALYZE CANONICAL REPRESENTATION
		decomposedRelationsIterator = config.getNormalizedRelations().iterator();
		while (decomposedRelationsIterator.hasNext()){
			currRelation = decomposedRelationsIterator.next();

			analysis.addCanonicalRepresentationAnalysis(currRelation.getID(), MinimalCoverAnalyzer.analyzeCanonicalRepresentation(currRelation.getFunctionalDependencies()));
			if (!analysis.getCanonicalRepresentationAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
				return analysis;
			}
		}
	
		//ANALYZE TRIVIAL FUNCTIONAL DEPENDENCIES
		decomposedRelationsIterator = config.getNormalizedRelations().iterator();
		while (decomposedRelationsIterator.hasNext()){
			currRelation = decomposedRelationsIterator.next();

			analysis.addTrivialDependenciesAnalysis(currRelation.getID(), MinimalCoverAnalyzer.analyzeTrivialDependencies(currRelation.getFunctionalDependencies()));
			if (!analysis.getTrivialDependenciesAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
				return analysis;
			}
		}
		
		//ANALYZE EXTRANEOUS ATTRIBUTES
		decomposedRelationsIterator = config.getNormalizedRelations().iterator();
		while (decomposedRelationsIterator.hasNext()){
			currRelation = decomposedRelationsIterator.next();

			analysis.addExtraneousAttributesAnalysis(currRelation.getID(), MinimalCoverAnalyzer.analyzeExtraneousAttributes(currRelation.getFunctionalDependencies()));
			if (!analysis.getExtraneousAttributesAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
				return analysis;
			}
		}
		
		//ANALYZE REDUNDAND FUNCTIONAL DEPENDENCIES
		decomposedRelationsIterator = config.getNormalizedRelations().iterator();
		while (decomposedRelationsIterator.hasNext()){
			currRelation = decomposedRelationsIterator.next();

			analysis.addRedundandDependenciesAnalysis(currRelation.getID(), MinimalCoverAnalyzer.analyzeRedundandDependencies(currRelation.getFunctionalDependencies()));
			if (!analysis.getRedundandDependenciesAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
				return analysis;
			}
		}
		
		//ANALYZE DEPENDENCIES PRESERVATION
		analysis.setDepPresAnalysis(NormalizationAnalyzer.analyzeDependenciesPreservation(config.getBaseRelation(), config.getNormalizedRelations()));
		
		//ANALYZE RBR DECOMPOSITION
		decomposedRelationsIterator = config.getNormalizedRelations().iterator();
		while (decomposedRelationsIterator.hasNext()){
			currRelation = decomposedRelationsIterator.next();

			analysis.addRBRAnalysis(currRelation.getID(), RBRAnalyzer.analyze(config.getBaseRelation(), currRelation));
			if (!analysis.getRBRAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
			}
		}

		//ANALYZE KEYS
		keysAnalyzerConfig = new KeysAnalyzerConfig();
		decomposedRelationsIterator = config.getNormalizedRelations().iterator();
		while (decomposedRelationsIterator.hasNext()){
			currDecomposedRelation = decomposedRelationsIterator.next();
			keysAnalyzerConfig.setCorrectMinimalKeys(correctKeysOfDecomposedRelations.get(currDecomposedRelation.getID()).getMinimalKeys());

			analysis.addKeysAnalysis(currDecomposedRelation.getID(), KeysAnalyzer.analyze(currDecomposedRelation, keysAnalyzerConfig));
			if (!analysis.getKeysAnalysis(currDecomposedRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
			}
		}

		//ANALYZE NORMALFORM LEVELS
		normalformAnalyzerConfig = new NormalformAnalyzerConfig();
		decomposedRelationsIterator = config.getNormalizedRelations().iterator();
		while (decomposedRelationsIterator.hasNext()){
			currRelation = decomposedRelationsIterator.next();
			normalformAnalyzerConfig.setRelation(currRelation);
			normalformAnalyzerConfig.setDesiredNormalformLevel(config.getDesiredNormalformLevel());
			normalformAnalyzerConfig.setCorrectMinimalKeys(correctKeysOfDecomposedRelations.get(currRelation.getID()).getMinimalKeys());
			normalformAnalyzerConfig.setCorrectPartialKeys(correctKeysOfDecomposedRelations.get(currRelation.getID()).getPartialKeys());

			analysis.addNormalformAnalysis(currRelation.getID(), NormalformAnalyzer.analyze(normalformAnalyzerConfig));
			if (!RDBDHelper.isInnerNode(currRelation.getID(), config.getAllRelations())){
				if (!analysis.getNormalformAnalysis(currRelation.getID()).submissionSuitsSolution()){
					analysis.setSubmissionSuitsSolution(false);
				}
			}
		}
				
		return analysis;
	}
}
