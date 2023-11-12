package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.ui.IdentifiedRelation;
import at.jku.dke.etutor.modules.nf.ui.IdentifiedRelationComparator;

import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Level;

public class DecomposeAnalyzer {

	public DecomposeAnalyzer() {
		super();
	}
	
	public static DecomposeAnalysis analyze(DecomposeAnalyzerConfig config){
		TreeSet allRelations;
		TreeSet leafRelations;
		DecomposeAnalysis analysis;
		Iterator relationsIterator;
		IdentifiedRelation relation;
		NormalizationAnalysis decomposeStepAnalysis;
		DecomposeStepAnalyzerConfig decomposeStepAnalyzerConfig;
		
		RDBDHelper.getLogger().log(Level.INFO, "Analyze Decompose.");
		
		//INIT DECOMPOSE ANALYSIS
		analysis = new DecomposeAnalysis();
		analysis.setSubmissionSuitsSolution(true);
		analysis.setTargetLevel(config.getDesiredNormalformLevel());
		analysis.setMaxLostDependencies(config.getMaxLostDependencies());
		analysis.setDecomposedRelations(config.getDecomposedRelations());

		//INIT ALL RELATIONS SET
		allRelations = new TreeSet(new IdentifiedRelationComparator());
		allRelations.add(config.getBaseRelation());
		allRelations.addAll(config.getDecomposedRelations());
		
		//ANALYZE DECOMPOSE STEPS
		relationsIterator = allRelations.iterator();
		while((relationsIterator.hasNext()) && (analysis.submissionSuitsSolution())){
			relation = (IdentifiedRelation)relationsIterator.next();
			
			if (RDBDHelper.isInnerNode(relation.getID(), allRelations)){
				decomposeStepAnalyzerConfig = new DecomposeStepAnalyzerConfig();
				decomposeStepAnalyzerConfig.setBaseRelation(relation);
				decomposeStepAnalyzerConfig.setAllRelations(allRelations);
				decomposeStepAnalyzerConfig.setDesiredNormalformLevel(config.getDesiredNormalformLevel());
				decomposeStepAnalyzerConfig.addNormalizedRelation(RDBDHelper.findRelation(relation.getID() + ".1", allRelations));
				decomposeStepAnalyzerConfig.addNormalizedRelation(RDBDHelper.findRelation(relation.getID() + ".2", allRelations));
				decomposeStepAnalysis = DecomposeStepAnalyzer.analyze(decomposeStepAnalyzerConfig);
				analysis.addDecomposeStepAnalysis(relation.getID(),decomposeStepAnalysis);
				RDBDHelper.getLogger().log(Level.INFO, "Add DecomposeStepAnalysis for Relation '" + relation.getID() + "'.");
				if (!decomposeStepAnalysis.submissionSuitsSolution()){
					analysis.setSubmissionSuitsSolution(false);
					RDBDHelper.getLogger().log(Level.INFO, "DecomposeStepAnalysis for Relation '" + relation.getID() + "' does not suit the solution.");
				}
			}
		}

		//ANALYZE DEPENDENCIES PRESERVATION FOR LEAF-RELATIONS
		leafRelations = new TreeSet(new IdentifiedRelationComparator());
		relationsIterator = allRelations.iterator();
		while(relationsIterator.hasNext()){
			relation = (IdentifiedRelation)relationsIterator.next();
			if (!RDBDHelper.isInnerNode(relation.getID(), allRelations)){
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
