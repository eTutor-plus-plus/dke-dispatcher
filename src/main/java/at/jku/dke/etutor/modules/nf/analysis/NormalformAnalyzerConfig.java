package at.jku.dke.etutor.modules.nf.analysis;

import etutor.modules.rdbd.model.KeyComparator;
import etutor.modules.rdbd.model.NormalformLevel;
import etutor.modules.rdbd.model.Relation;

import java.util.TreeSet;

public class NormalformAnalyzerConfig {

	private Relation relation;
	private TreeSet correctMinimalKeys;
	private TreeSet correctPartialKeys;
	private NormalformLevel desiredLevel;
	
	public NormalformAnalyzerConfig() {
		super();
		this.relation = new Relation();
		this.desiredLevel = NormalformLevel.FIRST;
		this.correctMinimalKeys = new TreeSet(new KeyComparator());
		this.correctPartialKeys = new TreeSet(new KeyComparator());
	}

	public void setDesiredNormalformLevel(NormalformLevel level){
		this.desiredLevel = level;
	}

	public NormalformLevel getDesiredNormalformLevel(){
		return this.desiredLevel;
	}

	public void setRelation(Relation relation){
		this.relation = relation;
	}
	
	public Relation getRelation(){
		return this.relation;
	}
	
	public void setCorrectMinimalKeys(TreeSet correctMinimalKeys){
		this.correctMinimalKeys = correctMinimalKeys;
	}
	
	public TreeSet getCorrectMinimalKeys(){
		return (TreeSet)this.correctMinimalKeys.clone();
	}

	public void setCorrectPartialKeys(TreeSet correctPartialKeys){
		this.correctPartialKeys = correctPartialKeys;
	}
	
	public TreeSet getCorrectPartialKeys(){
		return (TreeSet)this.correctPartialKeys.clone();
	}
}