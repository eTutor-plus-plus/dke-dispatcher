package at.jku.dke.etutor.modules.nf.model;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

public class KeysContainer implements Serializable {

	private Set<Key> minimalKeys;
	private Set<Key> superKeys;
	private Set<Key> partialKeys;
	

	public KeysContainer() {
		this.superKeys = null;
		this.minimalKeys = null;
		this.partialKeys = null;
	}

	public void setMinimalKeys(Set<Key> minimalKeys) {
		this.minimalKeys = minimalKeys;	
	}
	
	public Set<Key> getMinimalKeys() {
		TreeSet<Key> ret = new TreeSet<>(new KeyComparator());
		ret.addAll(minimalKeys);
		return ret;
	}

	public void setSuperKeys(Set<Key> keys) {
		this.superKeys = keys;	
	}
	
	public Set<Key> getSuperKeys() {
		TreeSet<Key> ret = new TreeSet<>(new KeyComparator());
		ret.addAll(superKeys);
		return ret;
	}
	
	public void setPartialKeys(TreeSet<Key> keys) {
		this.partialKeys = keys;	
	}
	
	public Set<Key> getPartialKeys() {
		TreeSet<Key> ret = new TreeSet<>(new KeyComparator());
		ret.addAll(partialKeys);
		return ret;
	}
}
