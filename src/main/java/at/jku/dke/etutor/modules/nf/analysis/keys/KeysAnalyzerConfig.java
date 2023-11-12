package at.jku.dke.etutor.modules.nf.analysis.keys;

import at.jku.dke.etutor.modules.nf.model.KeyComparator;

import java.util.TreeSet;

public class KeysAnalyzerConfig {

	private TreeSet correctMinimalKeys;	

	public KeysAnalyzerConfig() {
		super();
		this.correctMinimalKeys = new TreeSet(new KeyComparator());
	}

	public void setCorrectMinimalKeys(TreeSet correctMinimalKeys){
		this.correctMinimalKeys = correctMinimalKeys;
	}
	
	public TreeSet getCorrectMinimalKeys(){
		return (TreeSet)this.correctMinimalKeys.clone();
	}
}
