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
import at.jku.dke.etutor.modules.nf.ui.IdentifiedRelation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;


public class NormalizationAnalyzer {

	public NormalizationAnalyzer() {
		super();
	}
	
	public static NormalizationAnalysis analyze(NormalizationAnalyzerConfig config){
		NormalizationAnalysis analysis;
		IdentifiedRelation currRelation;
		KeysAnalyzerConfig keysAnalyzerConfig;
		IdentifiedRelation currNormalizedRelation;
		NormalformAnalyzerConfig normalformAnalyzerConfig;
		
		HashMap<String, KeysContainer> correctKeysOfNormalizedRelations = new HashMap<>();

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
		Iterator<IdentifiedRelation> normalizedRelationsIterator = config.getNormalizedRelations().iterator();
		while (normalizedRelationsIterator.hasNext()){
			currNormalizedRelation = normalizedRelationsIterator.next();
			correctKeysOfNormalizedRelations.put(currNormalizedRelation.getID(), KeysDeterminator.determineAllKeys(currNormalizedRelation));
		}

		//ANALYZE CANONICAL REPRESENTATION
		normalizedRelationsIterator = config.getNormalizedRelations().iterator();
		while (normalizedRelationsIterator.hasNext()){
			currRelation = normalizedRelationsIterator.next();

			analysis.addCanonicalRepresentationAnalysis(currRelation.getID(), MinimalCoverAnalyzer.analyzeCanonicalRepresentation(currRelation.getFunctionalDependencies()));
			if (!analysis.getCanonicalRepresentationAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
				return analysis;
			}
		}
	
		//ANALYZE TRIVIAL FUNCTIONAL DEPENDENCIES
		normalizedRelationsIterator = config.getNormalizedRelations().iterator();
		while (normalizedRelationsIterator.hasNext()){
			currRelation = normalizedRelationsIterator.next();

			analysis.addTrivialDependenciesAnalysis(currRelation.getID(), MinimalCoverAnalyzer.analyzeTrivialDependencies(currRelation.getFunctionalDependencies()));
			if (!analysis.getTrivialDependenciesAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
				return analysis;
			}
		}
		
		//ANALYZE EXTRANEOUS ATTRIBUTES
		normalizedRelationsIterator = config.getNormalizedRelations().iterator();
		while (normalizedRelationsIterator.hasNext()){
			currRelation = normalizedRelationsIterator.next();

			analysis.addExtraneousAttributesAnalysis(currRelation.getID(), MinimalCoverAnalyzer.analyzeExtraneousAttributes(currRelation.getFunctionalDependencies()));
			if (!analysis.getExtraneousAttributesAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
				return analysis;
			}
		}
		
		//ANALYZE REDUNDAND FUNCTIONAL DEPENDENCIES
		normalizedRelationsIterator = config.getNormalizedRelations().iterator();
		while (normalizedRelationsIterator.hasNext()){
			currRelation = normalizedRelationsIterator.next();

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
			currRelation = normalizedRelationsIterator.next();

			analysis.addRBRAnalysis(currRelation.getID(), RBRAnalyzer.analyze(config.getBaseRelation(), currRelation));
			if (!analysis.getRBRAnalysis(currRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
			}
		}

		//ANALYZE KEYS
		keysAnalyzerConfig = new KeysAnalyzerConfig();
		normalizedRelationsIterator = config.getNormalizedRelations().iterator();
		while (normalizedRelationsIterator.hasNext()){
			currNormalizedRelation = normalizedRelationsIterator.next();
			keysAnalyzerConfig.setCorrectMinimalKeys(correctKeysOfNormalizedRelations.get(currNormalizedRelation.getID()).getMinimalKeys());

			analysis.addKeysAnalysis(currNormalizedRelation.getID(), KeysAnalyzer.analyze(currNormalizedRelation, keysAnalyzerConfig));
			if (!analysis.getKeysAnalysis(currNormalizedRelation.getID()).submissionSuitsSolution()){
				analysis.setSubmissionSuitsSolution(false);
			}
		}

		//ANALYZE NORMALFORM LEVELS
		normalformAnalyzerConfig = new NormalformAnalyzerConfig();
		normalizedRelationsIterator = config.getNormalizedRelations().iterator();
		while (normalizedRelationsIterator.hasNext()){
			currRelation = normalizedRelationsIterator.next();
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
		DecompositionAnalysis analysis;

		analysis = new DecompositionAnalysis();
	
		analysis.setMissingAttributes(baseRelation.getAttributes());
        for (Relation decomposedRelation : decomposedRelations) {
            analysis.removeAllMissingAttributes(decomposedRelation.getAttributes());
        }

		analysis.setSubmissionSuitsSolution((analysis.getMissingAttributes().isEmpty()));

		return analysis;
	}
	
	public static LossLessAnalysis analyzeLossLess(Relation baseRelation, Collection<? extends Relation> decomposedRelations){
		LossLessAnalysis analysis;
		
		analysis = new LossLessAnalysis();
		analysis.setSubmissionSuitsSolution(false);
		HashSet<FunctionalDependency> decomposedRelationsDependencies = new HashSet<>();

		Iterator<? extends Relation> decomposedRelationsIterator = decomposedRelations.iterator();
		while (decomposedRelationsIterator.hasNext()){
			decomposedRelationsDependencies.addAll(decomposedRelationsIterator.next().getFunctionalDependencies());
		}

		decomposedRelationsIterator = decomposedRelations.iterator();
		while (decomposedRelationsIterator.hasNext()){
			TreeSet<String> closure = Closure.execute(decomposedRelationsIterator.next().getAttributes(), decomposedRelationsDependencies);
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
		
		Iterator<FunctionalDependency> baseRelationDependenciesIterator = baseRelation.iterFunctionalDependencies();
		while(baseRelationDependenciesIterator.hasNext()){
			FunctionalDependency currBaseDependency = baseRelationDependenciesIterator.next();
			if (!Member.execute(currBaseDependency, decomposedRelationsDependencies)){
				analysis.addLostFunctionalDependency(currBaseDependency);						
			}
		}

		analysis.setSubmissionSuitsSolution(analysis.getLostFunctionalDependencies().isEmpty());
		return analysis;
	}

	public static void analyzeNormalform(Collection<? extends Relation> decomposedRelations, NormalizationAnalysis analysis, HashMap<String, KeysContainer> correctKeys){
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
