package at.jku.dke.etutor.modules.nf.analysis.decompose;

import at.jku.dke.etutor.modules.nf.analysis.normalization.NormalizationAnalyzerConfig;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelationComparator;

import java.util.TreeSet;

public class DecomposeAnalyzerConfig extends NormalizationAnalyzerConfig {

	private final TreeSet<IdentifiedRelation> decomposedRelations;

	public DecomposeAnalyzerConfig() {
		super();

		this.decomposedRelations = new TreeSet<>(new IdentifiedRelationComparator());
	}

	public void setDecomposedRelations(TreeSet<IdentifiedRelation> decomposedRelations){
		this.decomposedRelations.clear();
		this.decomposedRelations.addAll(decomposedRelations);
	}
	
	public void addDecomposedRelation(IdentifiedRelation decomposedRelation){
		this.decomposedRelations.add(decomposedRelation);
	}

	public TreeSet<IdentifiedRelation> getDecomposedRelations(){
		return (TreeSet<IdentifiedRelation>)this.decomposedRelations.clone();
	}
}
