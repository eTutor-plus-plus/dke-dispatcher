package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.ui.IdentifiedRelation;
import at.jku.dke.etutor.modules.nf.ui.IdentifiedRelationComparator;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class NormalizationAnalyzerConfig {


	private int maxLostDependencies;
	private TreeSet normalizedRelations;
	private IdentifiedRelation baseRelation;
	private NormalformLevel desiredNormalformLevel;
	
	public NormalizationAnalyzerConfig() {
		super();
		this.baseRelation = null;
		this.maxLostDependencies = 0;
		this.desiredNormalformLevel = NormalformLevel.FIRST;
		this.normalizedRelations = new TreeSet(new IdentifiedRelationComparator());
	}

	public void setMaxLostDependencies(int maxLostDependencies){
		this.maxLostDependencies = maxLostDependencies;
	}

	public int getMaxLostDependencies(){
		return this.maxLostDependencies;
	}

	public void setNormalizedRelations(Collection normalizedRelations){
		this.normalizedRelations.clear();
		this.normalizedRelations.addAll(normalizedRelations);
	}
	
	public void addNormalizedRelation(IdentifiedRelation normalizedRelation){
		this.normalizedRelations.add(normalizedRelation);
	}

	public Iterator iterNormalizedRelations(){
		return this.normalizedRelations.iterator();
	}
	
	public TreeSet getNormalizedRelations(){
		return (TreeSet)this.normalizedRelations.clone();
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
	
}
