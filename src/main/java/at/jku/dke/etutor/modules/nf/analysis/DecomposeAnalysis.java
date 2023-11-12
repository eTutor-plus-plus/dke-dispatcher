package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.ui.IdentifiedRelationComparator;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

public class DecomposeAnalysis extends DefaultAnalysis implements Serializable, Analysis{

	private int maxLostDependencies;
	private NormalformLevel targetLevel;
	private TreeSet decomposedRelations;
	private HashMap decomposeStepAnalyses;
	private DependenciesPreservationAnalysis overallDepPresAnalysis;

	public DecomposeAnalysis() {
		super();
		
		this.maxLostDependencies = 0;
		this.overallDepPresAnalysis = null;
		this.targetLevel = NormalformLevel.FIRST;
		this.decomposeStepAnalyses = new HashMap();
		this.decomposedRelations = new TreeSet(new IdentifiedRelationComparator());
	}
	
	public void setDecomposedRelations(Collection decomposedRelations){
		this.decomposedRelations.clear();
		this.decomposedRelations.addAll(decomposedRelations);
	}
	
	public TreeSet getDecomposedRelations(){
		return (TreeSet)this.decomposedRelations.clone();
	}
	
	public NormalformLevel getTargetLevel() {
		return this.targetLevel;
	}

	public void setTargetLevel(NormalformLevel level) {
		this.targetLevel = level;
	}

	public void setMaxLostDependencies(int maxLostDependencies){
		this.maxLostDependencies = maxLostDependencies;
	}

	public int getMaxLostDependencies(){
		return this.maxLostDependencies;
	}

	public void setOverallDependenciesPreservationAnalysis(DependenciesPreservationAnalysis analysis){
		this.overallDepPresAnalysis = analysis;
	}
	
	public DependenciesPreservationAnalysis getOverallDependenciesPreservationAnalysis(){
		return this.overallDepPresAnalysis;
	}

	public void addDecomposeStepAnalysis(String baseRelationID, NormalizationAnalysis analysis){
		this.decomposeStepAnalyses.put(baseRelationID, analysis);
	}
	
	public Iterator iterDecomposeStepAnalyses(){
		return this.decomposeStepAnalyses.entrySet().iterator();
	}
	
	public NormalizationAnalysis getDecomposeStepAnalyses(String baseRelationID){
		return (NormalizationAnalysis)this.decomposeStepAnalyses.get(baseRelationID);
	}

	public Iterator iterAnalysedDecomposeStepBaseRelations(){
		return this.decomposeStepAnalyses.keySet().iterator();	
	}
}
