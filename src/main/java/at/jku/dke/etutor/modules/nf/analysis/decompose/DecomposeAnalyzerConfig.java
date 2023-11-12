package at.jku.dke.etutor.modules.nf.analysis.decompose;

import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.ui.IdentifiedRelation;
import at.jku.dke.etutor.modules.nf.ui.IdentifiedRelationComparator;

import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeSet;

public class DecomposeAnalyzerConfig implements Serializable {

	private int maxLostDependencies;
	private TreeSet decomposedRelations;
	private IdentifiedRelation baseRelation;
	private NormalformLevel desiredNormalformLevel;

	public DecomposeAnalyzerConfig() {
		super();
		
		this.baseRelation = null;
		this.maxLostDependencies = 0;
		this.desiredNormalformLevel = NormalformLevel.FIRST;
		this.decomposedRelations = new TreeSet(new IdentifiedRelationComparator());
	}

	public void setBaseRelation(IdentifiedRelation baseRelation){
		this.baseRelation = baseRelation;
	}
	
	public IdentifiedRelation getBaseRelation(){
		return this.baseRelation;
	}
	
	public void setDesiredNormalformLevel(NormalformLevel level){
		this.desiredNormalformLevel = level;
	}

	public NormalformLevel getDesiredNormalformLevel(){
		return this.desiredNormalformLevel;
	}

	public void setMaxLostDependencies(int maxLostDependencies){
		this.maxLostDependencies = maxLostDependencies;
	}

	public int getMaxLostDependencies(){
		return this.maxLostDependencies;
	}

	public void setDecomposedRelations(TreeSet decomposedRelations){
		this.decomposedRelations.clear();
		this.decomposedRelations.addAll(decomposedRelations);
	}
	
	public void addDecomposedRelation(IdentifiedRelation decomposedRelation){
		this.decomposedRelations.add(decomposedRelation);
	}

	public Iterator iterDecomposedRelations(){
		return this.decomposedRelations.iterator();
	}
	
	public TreeSet getDecomposedRelations(){
		return (TreeSet)this.decomposedRelations.clone();
	}
}
