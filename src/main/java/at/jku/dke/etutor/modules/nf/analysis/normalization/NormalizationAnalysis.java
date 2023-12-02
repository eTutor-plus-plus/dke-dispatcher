package at.jku.dke.etutor.modules.nf.analysis.normalization;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.*;
import at.jku.dke.etutor.modules.nf.analysis.decompose.DecompositionAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.keys.KeysAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.normalform.NormalformAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.rbr.RBRAnalysis;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;

import java.util.HashMap;

public class NormalizationAnalysis extends DefaultAnalysis implements Analysis {

	private int maxLostDependencies;

	private final HashMap<String, RBRAnalysis> rbrAnalyses;
	private final HashMap<String, KeysAnalysis> keysAnalyses;
	private final HashMap<String, NormalformAnalysis> normalformAnalyses;

	private NormalformLevel desiredNormalformLevel;

	private LosslessAnalysis lossLessAnalysis;
	private DecompositionAnalysis decompositionAnalysis;
	private DependenciesPreservationAnalysis depPresAnalysis;

	private final HashMap<String, TrivialDependenciesAnalysis> trivialDependenciesAnalyses;
	private final HashMap<String, ExtraneousAttributesAnalysis> extraneousAttributesAnalyses;
	private final HashMap<String, RedundantDependenciesAnalysis> redundantDependenciesAnalyses;
	private final HashMap<String, CanonicalRepresentationAnalysis> canonicalRepresentationAnalyses;

	public NormalizationAnalysis() {
		super();
		this.rbrAnalyses = new HashMap<>();
		this.keysAnalyses = new HashMap<>();
		this.normalformAnalyses = new HashMap<>();

		this.depPresAnalysis = null;
		this.lossLessAnalysis = null;
		this.decompositionAnalysis = null;
		
		this.trivialDependenciesAnalyses = new HashMap<>();
		this.extraneousAttributesAnalyses = new HashMap<>();
		this.redundantDependenciesAnalyses = new HashMap<>();
		this.canonicalRepresentationAnalyses = new HashMap<>();
	}

	public DecompositionAnalysis getDecompositionAnalysis() {
		return this.decompositionAnalysis;
	}

	public DependenciesPreservationAnalysis getDepPresAnalysis() {
		return this.depPresAnalysis;
	}

	public LosslessAnalysis getLossLessAnalysis() {
		return this.lossLessAnalysis;
	}

	public void setDecompositionAnalysis(DecompositionAnalysis analysis) {
		this.decompositionAnalysis = analysis;
	}

	public void setDepPresAnalysis(DependenciesPreservationAnalysis analysis) {
		this.depPresAnalysis = analysis;
	}

	public void setLosslessAnalysis(LosslessAnalysis analysis) {
		this.lossLessAnalysis = analysis;
	}
	
	public void addKeysAnalysis(String relationID, KeysAnalysis analysis){
		this.keysAnalyses.put(relationID, analysis);
	}

	public KeysAnalysis getKeysAnalysis(String relationID){
        return this.keysAnalyses.getOrDefault(relationID, null);
	}

	public void addRBRAnalysis(String relationID, RBRAnalysis analysis){
		this.rbrAnalyses.put(relationID, analysis);	
	}
	
	public RBRAnalysis getRBRAnalysis(String relationID){
        return this.rbrAnalyses.getOrDefault(relationID, null);
	}

	public void addNormalformAnalysis(String relationID, NormalformAnalysis analysis){
		this.normalformAnalyses.put(relationID, analysis);	
	}
	
	public NormalformAnalysis getNormalformAnalysis(String relationID){
        return this.normalformAnalyses.getOrDefault(relationID, null);
	}
	public NormalformLevel getDesiredNormalformLevel() {
		return this.desiredNormalformLevel;
	}

	public void setDesiredNormalformLevel(NormalformLevel level) {
		this.desiredNormalformLevel = level;
	}

	public ExtraneousAttributesAnalysis getExtraneousAttributesAnalysis(String relationID){
		return this.extraneousAttributesAnalyses.get(relationID);
	}

	public void addExtraneousAttributesAnalysis(String relationID, ExtraneousAttributesAnalysis analysis){
		this.extraneousAttributesAnalyses.put(relationID, analysis); 
	}

	public RedundantDependenciesAnalysis getRedundantDependenciesAnalysis(String relationID){
		return this.redundantDependenciesAnalyses.get(relationID);
	}
	
	public void addRedundantDependenciesAnalysis(String relationID, RedundantDependenciesAnalysis analysis){
		this.redundantDependenciesAnalyses.put(relationID, analysis);
	}

	public CanonicalRepresentationAnalysis getCanonicalRepresentationAnalysis(String relationID) {
		return this.canonicalRepresentationAnalyses.get(relationID);
	}

	public void addCanonicalRepresentationAnalysis(String relationID, CanonicalRepresentationAnalysis analysis) {
		this.canonicalRepresentationAnalyses.put(relationID, analysis);
	}
	
	public TrivialDependenciesAnalysis getTrivialDependenciesAnalysis(String relationID) {
		return this.trivialDependenciesAnalyses.get(relationID);
	}

	public void addTrivialDependenciesAnalysis(String relationID, TrivialDependenciesAnalysis analysis) {
		this.trivialDependenciesAnalyses.put(relationID, analysis);
	}

	public void setMaxLostDependencies(int maxLostDependencies){
		this.maxLostDependencies = maxLostDependencies;
	}

	public int getMaxLostDependencies(){
		return this.maxLostDependencies;
	}
}
