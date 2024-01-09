package at.jku.dke.etutor.modules.nf.analysis.decompose;

import at.jku.dke.etutor.modules.nf.analysis.normalization.NormalizationAnalyzerConfig;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DecomposeStepAnalyzerConfig extends NormalizationAnalyzerConfig {

	private final List<IdentifiedRelation> allRelations;

	public DecomposeStepAnalyzerConfig() {
		super();
		this.allRelations = new LinkedList<>();
	}

	public void setAllRelations(Collection<IdentifiedRelation> allRelations){
		this.allRelations.clear();
		this.allRelations.addAll(allRelations);
	}
	
	public void addRelationToAll(IdentifiedRelation relation){
		this.allRelations.add(relation);
	}

	public List<IdentifiedRelation> getAllRelations(){
		return new LinkedList<>(this.allRelations);
	}
}
