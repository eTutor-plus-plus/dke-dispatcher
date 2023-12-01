package at.jku.dke.etutor.modules.nf.analysis.decompose;

import at.jku.dke.etutor.modules.nf.analysis.normalization.NormalizationAnalyzerConfig;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class DecomposeStepAnalyzerConfig extends NormalizationAnalyzerConfig {

	private final Vector<IdentifiedRelation> allRelations;

	public DecomposeStepAnalyzerConfig() {
		super();
		this.allRelations = new Vector<>();
	}

	public void setAllRelations(Collection<IdentifiedRelation> allRelations){
		this.allRelations.clear();
		this.allRelations.addAll(allRelations);
	}
	
	public void addRelationToAll(IdentifiedRelation relation){
		this.allRelations.add(relation);
	}

	public Vector<IdentifiedRelation> getAllRelations(){
		return (Vector<IdentifiedRelation>)this.allRelations.clone();
	}
}
