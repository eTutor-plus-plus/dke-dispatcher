package at.jku.dke.etutor.modules.nf.model;

import java.io.Serializable;
import java.util.TreeSet;

public class KeysContainer implements Serializable{

	private TreeSet<Key> minimalKeys;
	private TreeSet<Key> superKeys;
	private TreeSet<Key> partialKeys;
	

	public KeysContainer() {
		super();

		this.superKeys = null;
		this.minimalKeys = null;
		this.partialKeys = null;
	}

	public void setMinimalKeys(TreeSet<Key> minimalKeys){
		this.minimalKeys = minimalKeys;	
	}
	
	public TreeSet<Key> getMinimalKeys(){
		return (TreeSet<Key>)this.minimalKeys.clone();
	}

	public void setSuperKeys(TreeSet<Key> keys){
		this.superKeys = keys;	
	}
	
	public TreeSet<Key> getSuperKeys(){
		return (TreeSet<Key>)this.superKeys.clone();
	}
	
	public void setPartialKeys(TreeSet<Key> keys){
		this.partialKeys = keys;	
	}
	
	public TreeSet<Key> getPartialKeys(){
		return (TreeSet<Key>)this.partialKeys.clone();
	}
}
