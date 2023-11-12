package at.jku.dke.etutor.modules.nf.report;

import etutor.modules.rdbd.model.NormalformLevel;
import etutor.modules.rdbd.ui.IdentifiedRelation;
import etutor.modules.rdbd.ui.IdentifiedRelationComparator;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class NormalizationReporterConfig extends ReporterConfig {

	private TreeSet decomposedRelations;
	private NormalformLevel desiredNormalformLevel;

	public NormalizationReporterConfig() {
		super();
		this.desiredNormalformLevel = NormalformLevel.FIRST;
		this.decomposedRelations = new TreeSet(new IdentifiedRelationComparator());
	}
	
	public void setDesiredNormalformLevel(NormalformLevel level){
		this.desiredNormalformLevel = level;
	}
	
	public NormalformLevel getDesiredNormalformLevel(){
		return this.desiredNormalformLevel;
	}

	public TreeSet getDecomposedRelations() {
		return (TreeSet)this.decomposedRelations.clone();
	}

	public void setDecomposedRelations(Collection decomposedRelations) {
		this.decomposedRelations.clear();
		this.decomposedRelations.addAll(decomposedRelations);
	}

	public void addDecomposedRelation(IdentifiedRelation relation){
		this.decomposedRelations.add(relation);
	}

	public Iterator iterDecomposedRelations(){
		return this.decomposedRelations.iterator();
	}
}