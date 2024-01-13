package at.jku.dke.etutor.modules.nf.analysis.decompose;

import at.jku.dke.etutor.modules.nf.NFHelper;
import at.jku.dke.etutor.modules.nf.analysis.normalization.NormalizationAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.normalization.NormalizationAnalyzer;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelationComparator;

import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Level;

public class DecomposeAnalyzer {

	public DecomposeAnalyzer() {
		super();
	}
	
	public static DecomposeAnalysis analyze(DecomposeAnalyzerConfig config) {
		NFHelper.getLogger().log(Level.INFO, "Analyze Decompose.");
		
		//INIT DECOMPOSE ANALYSIS
		DecomposeAnalysis analysis = new DecomposeAnalysis();
		analysis.setSubmissionSuitsSolution(true);
		analysis.setTargetLevel(config.getDesiredNormalformLevel());
		analysis.setMaxLostDependencies(config.getMaxLostDependencies());
		analysis.setDecomposedRelations(config.getDecomposedRelations());

		//INIT ALL RELATIONS SET
		TreeSet<IdentifiedRelation> allRelations = new TreeSet<>(new IdentifiedRelationComparator());
		allRelations.add(config.getBaseRelation());
		allRelations.addAll(config.getDecomposedRelations());
		
		//ANALYZE DECOMPOSE STEPS
        for (IdentifiedRelation relation : allRelations) {
            if (NFHelper.isInnerNode(relation.getID(), allRelations)) {
                DecomposeStepAnalyzerConfig decomposeStepAnalyzerConfig = new DecomposeStepAnalyzerConfig();
                decomposeStepAnalyzerConfig.setBaseRelation(relation);
                decomposeStepAnalyzerConfig.setAllRelations(allRelations);
                decomposeStepAnalyzerConfig.setDesiredNormalformLevel(config.getDesiredNormalformLevel());
                decomposeStepAnalyzerConfig.addNormalizedRelation(NFHelper.findRelation(relation.getID() + ".1", allRelations));
                decomposeStepAnalyzerConfig.addNormalizedRelation(NFHelper.findRelation(relation.getID() + ".2", allRelations));

                NormalizationAnalysis decomposeStepAnalysis = DecomposeStepAnalyzer.analyze(decomposeStepAnalyzerConfig);
                analysis.addDecomposeStepAnalysis(relation.getID(), decomposeStepAnalysis);
                NFHelper.getLogger().log(Level.INFO, "Add DecomposeStepAnalysis for Relation '" + relation.getID() + "'.");
                if (!decomposeStepAnalysis.submissionSuitsSolution()) {
                    analysis.setSubmissionSuitsSolution(false);
                    NFHelper.getLogger().log(Level.INFO, "DecomposeStepAnalysis for Relation '" + relation.getID() + "' does not suit the solution.");
                    break;
                }
            }
        }

		//ANALYZE DEPENDENCIES PRESERVATION FOR LEAF-RELATIONS
		TreeSet<IdentifiedRelation> leafRelations = new TreeSet<>(new IdentifiedRelationComparator());
		for (IdentifiedRelation relation : allRelations){
			if (!NFHelper.isInnerNode(relation.getID(), allRelations)){
				leafRelations.add(relation);			
			}
		}		 
		
		analysis.setOverallDependenciesPreservationAnalysis(NormalizationAnalyzer.analyzeDependenciesPreservation(config.getBaseRelation(), leafRelations));
		if (analysis.getOverallDependenciesPreservationAnalysis().lostFunctionalDependenciesCount() > config.getMaxLostDependencies()){
			analysis.setSubmissionSuitsSolution(false);
			analysis.getOverallDependenciesPreservationAnalysis().setSubmissionSuitsSolution(false);
		} else {
			analysis.getOverallDependenciesPreservationAnalysis().setSubmissionSuitsSolution(true);
		}
		
		return analysis;
	}
}
