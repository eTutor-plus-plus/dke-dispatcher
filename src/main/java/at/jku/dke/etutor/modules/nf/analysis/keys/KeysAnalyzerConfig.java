package at.jku.dke.etutor.modules.nf.analysis.keys;

import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.KeyComparator;

import java.util.TreeSet;

public class KeysAnalyzerConfig {

	private TreeSet<Key> correctMinimalKeys;

	public KeysAnalyzerConfig() {
		this.correctMinimalKeys = new TreeSet<>(new KeyComparator());
	}

	public void setCorrectMinimalKeys(TreeSet<Key> correctMinimalKeys){
		this.correctMinimalKeys = correctMinimalKeys;
	}
	
	public TreeSet<Key> getCorrectMinimalKeys(){
		return (TreeSet<Key>)this.correctMinimalKeys.clone();
	}
}
