package at.jku.dke.etutor.modules.nf.model;

import java.io.Serializable;
import java.util.TreeSet;

public class KeysContainer implements Serializable{

	private TreeSet minimalKeys;
	private TreeSet superKeys;
	private TreeSet partialKeys;
	

	public KeysContainer() {
		super();

		this.superKeys = null;
		this.minimalKeys = null;
		this.partialKeys = null;
	}

	public void setMinimalKeys(TreeSet minimalKeys){
		this.minimalKeys = minimalKeys;	
	}
	
	public TreeSet getMinimalKeys(){
		return (TreeSet)this.minimalKeys.clone();
	}

	public void setSuperKeys(TreeSet keys){
		this.superKeys = keys;	
	}
	
	public TreeSet getSuperKeys(){
		return (TreeSet)this.superKeys.clone();
	}
	
	public void setPartialKeys(TreeSet keys){
		this.partialKeys = keys;	
	}
	
	public TreeSet getPartialKeys(){
		return (TreeSet)this.partialKeys.clone();
	}
}
