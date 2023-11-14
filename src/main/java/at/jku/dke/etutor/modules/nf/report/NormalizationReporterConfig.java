package at.jku.dke.etutor.modules.nf.report;

import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.ui.IdentifiedRelation;
import at.jku.dke.etutor.modules.nf.ui.IdentifiedRelationComparator;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class NormalizationReporterConfig extends ReporterConfig {

	private final TreeSet<IdentifiedRelation> decomposedRelations;
	private NormalformLevel desiredNormalformLevel;

	public NormalizationReporterConfig() {
		super();
		this.desiredNormalformLevel = NormalformLevel.FIRST;
		this.decomposedRelations = new TreeSet<IdentifiedRelation>(new IdentifiedRelationComparator());
	}
	
	public void setDesiredNormalformLevel(NormalformLevel level){
		this.desiredNormalformLevel = level;
	}
	
	public NormalformLevel getDesiredNormalformLevel(){
		return this.desiredNormalformLevel;
	}

	public TreeSet<IdentifiedRelation> getDecomposedRelations() {
		return (TreeSet<IdentifiedRelation>)this.decomposedRelations.clone();
	}

	public void setDecomposedRelations(Collection<IdentifiedRelation> decomposedRelations) {
		this.decomposedRelations.clear();
		this.decomposedRelations.addAll(decomposedRelations);
	}

	public void addDecomposedRelation(IdentifiedRelation relation){
		this.decomposedRelations.add(relation);
	}

	public Iterator<IdentifiedRelation> iterDecomposedRelations(){
		return this.decomposedRelations.iterator();
	}
}