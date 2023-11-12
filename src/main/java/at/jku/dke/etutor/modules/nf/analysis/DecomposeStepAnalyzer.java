package at.jku.dke.etutor.modules.nf.analysis;

import etutor.modules.rdbd.RDBDHelper;
import etutor.modules.rdbd.model.KeysContainer;
import etutor.modules.rdbd.ui.IdentifiedRelation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

public class DecomposeStepAnalyzer {

	public static NormalizationAnalysis analyze(DecomposeStepAnalyzerConfig config){
		NormalizationAnalysis analysis;
		IdentifiedRelation currRelation;
		Iterator decomposedRelationsIterator;
		KeysAnalyzerConfig keysAnalyzerConfig;
		HashMap correctKeysOfDecomposedRelations;
		IdentifiedRelation currDecomposedRelation;
		DependenciesPreservationAnalysis depPresAnalysis;
		NormalformAnalyzerConfig normalformAnalyzerConfig;
		
		correctKeysOfDecomposedRelations = new HashMap();

		analysis = new NormalizationAnalysis();
		analysis.setSubmissionSuitsSolution(true);
		analysis.setDesiredNormalformLevel(config.getDesiredNormalformLevel());
		
		StringBuffer temp = new StringBuffer();
		RDBDHelper.getLogger().log(Level.INFO, "ANALYZING Decomposition of Relation '" + config.getBaseRelation().getID() + "'.");
		decomposedRelationsIterator = config.iterNormalizedRelations();
		while(decomposedRelationsIterator.hasNext()){
			temp.append("Relation '" + ((IdentifiedRelation)decomposedRelationsIterator.next()).getID() + "' ");
		}
		RDBDHelper.getLogger().log(Level.INFO, "With Sub-Relations: " + temp.toString() + ".");
		
		
		//ANALYZE DECOMPOSITION
		analysis.setDecompositionAnalysis(NormalizationAnalyzer.analyzeDecomposition(config.getBaseRelation(), config.getNormalizedRelations()));
		if (!analysis.getDecompositionAnalysis().submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			return analysis;
		}

		//ANALYZE LOSS LESS
		analysis.setLossLessAnalysis(NormalizationAnalyzer.analyzeLossLess(config.getBaseRelation(), config.getNormalizedRelations()));
		if (!analysis.getLossLessAnalysis().submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			return analysis;
		}

		//INITIALIZE CORRECT KEYS
		decomposedRelationsIterator = config.getNormalizedRelations().iterator();
		while (decomposedRelationsIterator.hasNext()){
			currDecomposedRelation = (IdentifiedRelation)decomposedRelationsIterator.next();
			correctKeysOfDecomposedRelations.put(currDecomposedRelation.getID(), KeysDeterminator.determineAllKeys(currDecomposedRelation));
		}
		
		//ANALYZE CANONICAL REPRESENTATION
		decomposedRelationsIterator = config.getNormalizedRelations().iterator();
		while (decomposedRelationsIterator.hasNext()){
			currRelation = (IdentifiedRelation)decomposedRelationsIterator.next();

			analysis.addCanonicalRepresentationAnalysis(currRelation.getID(), MinimalCoverAnalyzer.analyzeCanonicalRepresentation(currRelation.getFunctionalDependencies()));
			if (!analysis.getCanonicalRepresentationAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
				return analysis;
			}
		}
	
		//ANALYZE TRIVIAL FUNCTIONAL DEPENDENCIES
		decomposedRelationsIterator = config.getNormalizedRelations().iterator();
		while (decomposedRelationsIterator.hasNext()){
			currRelation = (IdentifiedRelation)decomposedRelationsIterator.next();

			analysis.addTrivialDependenciesAnalysis(currRelation.getID(), MinimalCoverAnalyzer.analyzeTrivialDependencies(currRelation.getFunctionalDependencies()));
			if (!analysis.getTrivialDependenciesAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
				return analysis;
			}
		}
		
		//ANALYZE EXTRANEOUS ATTRIBUTES
		decomposedRelationsIterator = config.getNormalizedRelations().iterator();
		while (decomposedRelationsIterator.hasNext()){
			currRelation = (IdentifiedRelation)decomposedRelationsIterator.next();

			analysis.addExtraneousAttributesAnalysis(currRelation.getID(), MinimalCoverAnalyzer.analyzeExtraneousAttributes(currRelation.getFunctionalDependencies()));
			if (!analysis.getExtraneousAttributesAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
				return analysis;
			}
		}
		
		//ANALYZE REDUNDAND FUNCTIONAL DEPENDENCIES
		decomposedRelationsIterator = config.getNormalizedRelations().iterator();
		while (decomposedRelationsIterator.hasNext()){
			currRelation = (IdentifiedRelation)decomposedRelationsIterator.next();

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
			currRelation = (IdentifiedRelation)decomposedRelationsIterator.next();

			analysis.addRBRAnalysis(currRelation.getID(), RBRAnalyzer.analyze(config.getBaseRelation(), currRelation));
			if (!analysis.getRBRAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
			}
		}

		//ANALYZE KEYS
		keysAnalyzerConfig = new KeysAnalyzerConfig();
		decomposedRelationsIterator = config.getNormalizedRelations().iterator();
		while (decomposedRelationsIterator.hasNext()){
			currDecomposedRelation = (IdentifiedRelation)decomposedRelationsIterator.next();
			keysAnalyzerConfig.setCorrectMinimalKeys(((KeysContainer)correctKeysOfDecomposedRelations.get(currDecomposedRelation.getID())).getMinimalKeys());

			analysis.addKeysAnalysis(currDecomposedRelation.getID(), KeysAnalyzer.analyze(currDecomposedRelation, keysAnalyzerConfig));
			if (!analysis.getKeysAnalysis(currDecomposedRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
			}
		}

		//ANALYZE NORMALFORM LEVELS
		normalformAnalyzerConfig = new NormalformAnalyzerConfig();
		decomposedRelationsIterator = config.getNormalizedRelations().iterator();
		while (decomposedRelationsIterator.hasNext()){
			currRelation = (IdentifiedRelation)decomposedRelationsIterator.next();
			normalformAnalyzerConfig.setRelation(currRelation);
			normalformAnalyzerConfig.setDesiredNormalformLevel(config.getDesiredNormalformLevel());
			normalformAnalyzerConfig.setCorrectMinimalKeys(((KeysContainer)correctKeysOfDecomposedRelations.get(currRelation.getID())).getMinimalKeys());
			normalformAnalyzerConfig.setCorrectPartialKeys(((KeysContainer)correctKeysOfDecomposedRelations.get(currRelation.getID())).getPartialKeys());

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
