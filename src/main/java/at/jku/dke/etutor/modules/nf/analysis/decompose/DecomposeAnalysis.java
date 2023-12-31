package at.jku.dke.etutor.modules.nf.analysis.decompose;

import at.jku.dke.etutor.modules.nf.analysis.NFAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.normalization.DependenciesPreservationAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.normalization.NormalizationAnalysis;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelationComparator;

import java.io.Serializable;
import java.util.*;

public class DecomposeAnalysis extends NFAnalysis implements Serializable {

	private int maxLostDependencies;
	private NormalformLevel targetLevel;
	private final TreeSet<IdentifiedRelation> decomposedRelations;
	private final HashMap<String, NormalizationAnalysis> decomposeStepAnalyses;
	private DependenciesPreservationAnalysis overallDepPresAnalysis;

	public DecomposeAnalysis() {
		super();
		
		this.maxLostDependencies = 0;
		this.overallDepPresAnalysis = null;
		this.targetLevel = NormalformLevel.FIRST;
		this.decomposeStepAnalyses = new HashMap<>();
		this.decomposedRelations = new TreeSet<>(new IdentifiedRelationComparator());
	}
	
	public void setDecomposedRelations(Collection<IdentifiedRelation> decomposedRelations){
		this.decomposedRelations.clear();
		this.decomposedRelations.addAll(decomposedRelations);
	}
	
	public TreeSet<IdentifiedRelation> getDecomposedRelations(){
		return (TreeSet<IdentifiedRelation>)this.decomposedRelations.clone();
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

	public NormalizationAnalysis getDecomposeStepAnalyses(String baseRelationID){
		return this.decomposeStepAnalyses.get(baseRelationID);
	}

	public Iterator<String> iterAnalysedDecomposeStepBaseRelations(){
		return this.decomposeStepAnalyses.keySet().iterator();	
	}
}
