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
		return new TreeSet<>(this.minimalKeys);
	}

	public void setSuperKeys(Set<Key> keys) {
		this.superKeys = keys;	
	}
	
	public Set<Key> getSuperKeys() {
		return new TreeSet<>(this.superKeys);
	}
	
	public void setPartialKeys(TreeSet<Key> keys) {
		this.partialKeys = keys;	
	}
	
	public Set<Key> getPartialKeys() {
		return new TreeSet<>(this.partialKeys);
	}
}
