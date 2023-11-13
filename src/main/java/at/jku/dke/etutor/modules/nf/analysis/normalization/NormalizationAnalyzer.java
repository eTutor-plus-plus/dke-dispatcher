package at.jku.dke.etutor.modules.nf.analysis.normalization;

import at.jku.dke.etutor.modules.nf.algorithms.Closure;
import at.jku.dke.etutor.modules.nf.algorithms.Member;
import at.jku.dke.etutor.modules.nf.analysis.*;
import at.jku.dke.etutor.modules.nf.analysis.decompose.DecompositionAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.keys.KeysAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.keys.KeysAnalyzerConfig;
import at.jku.dke.etutor.modules.nf.analysis.minimalcover.MinimalCoverAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.normalform.NormalformAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.rbr.RBRAnalyzer;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.KeysContainer;
import at.jku.dke.etutor.modules.nf.model.Relation;
import at.jku.dke.etutor.modules.nf.ui.IdentifiedRelation;

import java.util.*;


public class NormalizationAnalyzer {

	public NormalizationAnalyzer() {
		super();
	}
	
	public static NormalizationAnalysis analyze(NormalizationAnalyzerConfig config){
		NormalizationAnalysis analysis;
		IdentifiedRelation currRelation;
		Iterator normalizedRelationsIterator;
		KeysAnalyzerConfig keysAnalyzerConfig;
		HashMap correctKeysOfNormalizedRelations;
		IdentifiedRelation currNormalizedRelation;
		NormalformAnalyzerConfig normalformAnalyzerConfig;
		
		correctKeysOfNormalizedRelations = new HashMap();

		analysis = new NormalizationAnalysis();
		analysis.setSubmissionSuitsSolution(true);
		analysis.setMaxLostDependencies(config.getMaxLostDependencies());
		analysis.setDesiredNormalformLevel(config.getDesiredNormalformLevel());
		
		//ANALYZE DECOMPOSITION
		analysis.setDecompositionAnalysis(analyzeDecomposition(config.getBaseRelation(), config.getNormalizedRelations()));
		if (!analysis.getDecompositionAnalysis().submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			return analysis;
		}

		//ANALYZE LOSS LESS
		analysis.setLossLessAnalysis(analyzeLossLess(config.getBaseRelation(), config.getNormalizedRelations()));
		if (!analysis.getLossLessAnalysis().submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			return analysis;
		}

		//INITIALIZE CORRECT KEYS
		normalizedRelationsIterator = config.getNormalizedRelations().iterator();
		while (normalizedRelationsIterator.hasNext()){
			currNormalizedRelation = (IdentifiedRelation)normalizedRelationsIterator.next();
			correctKeysOfNormalizedRelations.put(currNormalizedRelation.getID(), KeysDeterminator.determineAllKeys(currNormalizedRelation));
		}

		//ANALYZE CANONICAL REPRESENTATION
		normalizedRelationsIterator = config.getNormalizedRelations().iterator();
		while (normalizedRelationsIterator.hasNext()){
			currRelation = (IdentifiedRelation)normalizedRelationsIterator.next();

			analysis.addCanonicalRepresentationAnalysis(currRelation.getID(), MinimalCoverAnalyzer.analyzeCanonicalRepresentation(currRelation.getFunctionalDependencies()));
			if (!analysis.getCanonicalRepresentationAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
				return analysis;
			}
		}
	
		//ANALYZE TRIVIAL FUNCTIONAL DEPENDENCIES
		normalizedRelationsIterator = config.getNormalizedRelations().iterator();
		while (normalizedRelationsIterator.hasNext()){
			currRelation = (IdentifiedRelation)normalizedRelationsIterator.next();

			analysis.addTrivialDependenciesAnalysis(currRelation.getID(), MinimalCoverAnalyzer.analyzeTrivialDependencies(currRelation.getFunctionalDependencies()));
			if (!analysis.getTrivialDependenciesAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
				return analysis;
			}
		}
		
		//ANALYZE EXTRANEOUS ATTRIBUTES
		normalizedRelationsIterator = config.getNormalizedRelations().iterator();
		while (normalizedRelationsIterator.hasNext()){
			currRelation = (IdentifiedRelation)normalizedRelationsIterator.next();

			analysis.addExtraneousAttributesAnalysis(currRelation.getID(), MinimalCoverAnalyzer.analyzeExtraneousAttributes(currRelation.getFunctionalDependencies()));
			if (!analysis.getExtraneousAttributesAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
				return analysis;
			}
		}
		
		//ANALYZE REDUNDAND FUNCTIONAL DEPENDENCIES
		normalizedRelationsIterator = config.getNormalizedRelations().iterator();
		while (normalizedRelationsIterator.hasNext()){
			currRelation = (IdentifiedRelation)normalizedRelationsIterator.next();

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
		normalizedRelationsIterator = config.getNormalizedRelations().iterator();
		while (normalizedRelationsIterator.hasNext()){
			currRelation = (IdentifiedRelation)normalizedRelationsIterator.next();

			analysis.addRBRAnalysis(currRelation.getID(), RBRAnalyzer.analyze(config.getBaseRelation(), currRelation));
			if (!analysis.getRBRAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
			}
		}

		//ANALYZE KEYS
		keysAnalyzerConfig = new KeysAnalyzerConfig();
		normalizedRelationsIterator = config.getNormalizedRelations().iterator();
		while (normalizedRelationsIterator.hasNext()){
			currNormalizedRelation = (IdentifiedRelation)normalizedRelationsIterator.next();
			keysAnalyzerConfig.setCorrectMinimalKeys(((KeysContainer)correctKeysOfNormalizedRelations.get(currNormalizedRelation.getID())).getMinimalKeys());

			analysis.addKeysAnalysis(currNormalizedRelation.getID(), KeysAnalyzer.analyze(currNormalizedRelation, keysAnalyzerConfig));
			if (!analysis.getKeysAnalysis(currNormalizedRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
			}
		}

		//ANALYZE NORMALFORM LEVELS
		normalformAnalyzerConfig = new NormalformAnalyzerConfig();
		normalizedRelationsIterator = config.getNormalizedRelations().iterator();
		while (normalizedRelationsIterator.hasNext()){
			currRelation = (IdentifiedRelation)normalizedRelationsIterator.next();
			normalformAnalyzerConfig.setRelation(currRelation);
			normalformAnalyzerConfig.setDesiredNormalformLevel(config.getDesiredNormalformLevel());
			normalformAnalyzerConfig.setCorrectMinimalKeys(((KeysContainer)correctKeysOfNormalizedRelations.get(currRelation.getID())).getMinimalKeys());
			normalformAnalyzerConfig.setCorrectPartialKeys(((KeysContainer)correctKeysOfNormalizedRelations.get(currRelation.getID())).getPartialKeys());

			analysis.addNormalformAnalysis(currRelation.getID(), NormalformAnalyzer.analyze(normalformAnalyzerConfig));
			if (!analysis.getNormalformAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
			}
		}
				
		return analysis;
	}
	
	public static DecompositionAnalysis analyzeDecomposition(Relation baseRelation, Collection decomposedRelations){
		DecompositionAnalysis analysis;
		Iterator decomposedRelationsIterator;

		analysis = new DecompositionAnalysis();
	
		analysis.setMissingAttributes(baseRelation.getAttributes());
		decomposedRelationsIterator = decomposedRelations.iterator();
		while (decomposedRelationsIterator.hasNext()){
			analysis.removeAllMissingAttributes(((Relation)decomposedRelationsIterator.next()).getAttributes());
		}

		analysis.setSubmissionSuitsSolution((analysis.getMissingAttributes().isEmpty()));

		return analysis;
	}
	
	public static LossLessAnalysis analyzeLossLess(Relation baseRelation, Collection decomposedRelations){
		TreeSet closure;
		LossLessAnalysis analysis;
		Iterator decomposedRelationsIterator;
		HashSet decomposedRelationsDependencies;
		
		analysis = new LossLessAnalysis();
		analysis.setSubmissionSuitsSolution(false);
		decomposedRelationsDependencies = new HashSet();

		decomposedRelationsIterator = decomposedRelations.iterator();
		while (decomposedRelationsIterator.hasNext()){
			decomposedRelationsDependencies.addAll(((Relation)decomposedRelationsIterator.next()).getFunctionalDependencies());
		}

		decomposedRelationsIterator = decomposedRelations.iterator();
		while (decomposedRelationsIterator.hasNext()){
			closure = Closure.execute(((Relation)decomposedRelationsIterator.next()).getAttributes(), decomposedRelationsDependencies);
			if (closure.containsAll(baseRelation.getAttributes())){
				analysis.setSubmissionSuitsSolution(true);
			}
		}

		return analysis;
	}

	public static DependenciesPreservationAnalysis analyzeDependenciesPreservation(Relation baseRelation, Collection decomposedRelations){
		Iterator decomposedRelationsIterator;
		FunctionalDependency currBaseDependency;
		HashSet decomposedRelationsDependencies;
		Iterator baseRelationDependenciesIterator;
		DependenciesPreservationAnalysis analysis;
		
		decomposedRelationsDependencies = new HashSet();
		analysis = new DependenciesPreservationAnalysis();

		decomposedRelationsIterator = decomposedRelations.iterator();
		while (decomposedRelationsIterator.hasNext()){
			decomposedRelationsDependencies.addAll(((Relation)decomposedRelationsIterator.next()).getFunctionalDependencies());
		}
		
		baseRelationDependenciesIterator = baseRelation.iterFunctionalDependencies();
		while(baseRelationDependenciesIterator.hasNext()){
			currBaseDependency = (FunctionalDependency)baseRelationDependenciesIterator.next();
			if (!Member.execute(currBaseDependency, decomposedRelationsDependencies)){
				analysis.addLostFunctionalDependency(currBaseDependency);						
			}
		}

		analysis.setSubmissionSuitsSolution(analysis.getLostFunctionalDependencies().isEmpty());
		return analysis;
	}

	public static void analyzeNormalform(Collection decomposedRelations, NormalizationAnalysis analysis, HashMap correctKeys){
		NormalformAnalyzerConfig config;
		IdentifiedRelation currRelation;
		Iterator decomposedRelationsIterator;

		config = new NormalformAnalyzerConfig();
		decomposedRelationsIterator = decomposedRelations.iterator();
		while (decomposedRelationsIterator.hasNext()){
			currRelation = (IdentifiedRelation)decomposedRelationsIterator.next();

			config.setRelation(currRelation);
			config.setCorrectMinimalKeys(((KeysContainer)correctKeys.get(currRelation.getID())).getMinimalKeys());
			config.setCorrectPartialKeys(((KeysContainer)correctKeys.get(currRelation.getID())).getPartialKeys());
			analysis.addNormalformAnalysis(currRelation.getID(), NormalformAnalyzer.analyze(config));
		}
	}
}
