package at.jku.dke.etutor.modules.nf.analysis.normalform;

import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.KeyComparator;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.model.Relation;

import java.util.Set;
import java.util.TreeSet;

public class NormalformAnalyzerConfig {

	private Relation relation;
	private Set<Key> correctMinimalKeys;
	private Set<Key> correctPartialKeys;
	private NormalformLevel desiredLevel;
	
	public NormalformAnalyzerConfig() {
		this.relation = new Relation();
		this.desiredLevel = NormalformLevel.FIRST;
		this.correctMinimalKeys = new TreeSet<>(new KeyComparator());
		this.correctPartialKeys = new TreeSet<>(new KeyComparator());
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
	
	public void setCorrectMinimalKeys(Set<Key> correctMinimalKeys){
		this.correctMinimalKeys = correctMinimalKeys;
	}
	
	public Set<Key> getCorrectMinimalKeys() {
		TreeSet<Key> ret = new TreeSet<>(new KeyComparator());
		ret.addAll(correctMinimalKeys);
		return ret;
	}

	public void setCorrectPartialKeys(Set<Key> correctPartialKeys){
		this.correctPartialKeys = correctPartialKeys;
	}
	
	public Set<Key> getCorrectPartialKeys(){
		TreeSet<Key> ret = new TreeSet<>(new KeyComparator());
		ret.addAll(correctPartialKeys);
		return ret;
	}
}