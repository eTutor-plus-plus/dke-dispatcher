package at.jku.dke.etutor.modules.nf.analysis.keys;

import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.KeyComparator;

import java.util.Set;
import java.util.TreeSet;

public class KeysAnalyzerConfig {

	private Set<Key> correctMinimalKeys;

	public KeysAnalyzerConfig() {
		this.correctMinimalKeys = new TreeSet<>(new KeyComparator());
	}

	public void setCorrectMinimalKeys(Set<Key> correctMinimalKeys){
		this.correctMinimalKeys = correctMinimalKeys;
	}
	
	public Set<Key> getCorrectMinimalKeys() {
		TreeSet<Key> ret = new TreeSet<>(new KeyComparator());
		ret.addAll(correctMinimalKeys);
		return ret;
	}
}
