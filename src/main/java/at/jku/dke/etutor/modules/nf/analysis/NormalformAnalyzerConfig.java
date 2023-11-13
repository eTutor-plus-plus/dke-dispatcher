package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.KeyComparator;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.model.Relation;

import java.util.TreeSet;

public class NormalformAnalyzerConfig {

	private Relation relation;
	private TreeSet<Key> correctMinimalKeys;
	private TreeSet<Key> correctPartialKeys;
	private NormalformLevel desiredLevel;
	
	public NormalformAnalyzerConfig() {
		super();
		this.relation = new Relation();
		this.desiredLevel = NormalformLevel.FIRST;
		this.correctMinimalKeys = new TreeSet<Key>(new KeyComparator());
		this.correctPartialKeys = new TreeSet<Key>(new KeyComparator());
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
	
	public void setCorrectMinimalKeys(TreeSet<Key> correctMinimalKeys){
		this.correctMinimalKeys = correctMinimalKeys;
	}
	
	public TreeSet<Key> getCorrectMinimalKeys(){
		return (TreeSet<Key>)this.correctMinimalKeys.clone();
	}

	public void setCorrectPartialKeys(TreeSet<Key> correctPartialKeys){
		this.correctPartialKeys = correctPartialKeys;
	}
	
	public TreeSet<Key> getCorrectPartialKeys(){
		return (TreeSet<Key>)this.correctPartialKeys.clone();
	}
}