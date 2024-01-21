package at.jku.dke.etutor.modules.nf.report;

import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelationComparator;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class NormalizationReporterConfig extends ReporterConfig {

	private final Set<IdentifiedRelation> decomposedRelations;
	private NormalformLevel desiredNormalformLevel;

	public NormalizationReporterConfig() {
		super();
		this.desiredNormalformLevel = NormalformLevel.FIRST;
		this.decomposedRelations = new TreeSet<>(new IdentifiedRelationComparator());
	}
	
	public void setDesiredNormalformLevel(NormalformLevel level){
		this.desiredNormalformLevel = level;
	}
	
	public NormalformLevel getDesiredNormalformLevel(){
		return this.desiredNormalformLevel;
	}

	public Set<IdentifiedRelation> getDecomposedRelations() {
		TreeSet<IdentifiedRelation> ret = new TreeSet<>(new IdentifiedRelationComparator());
		ret.addAll(this.decomposedRelations);

		return ret;
	}

	public void setDecomposedRelations(Collection<IdentifiedRelation> decomposedRelations) {
		this.decomposedRelations.clear();
		this.decomposedRelations.addAll(decomposedRelations);
	}

	public void addDecomposedRelation(IdentifiedRelation relation){
		this.decomposedRelations.add(relation);
	}

}