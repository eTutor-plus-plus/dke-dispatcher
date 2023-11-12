package at.jku.dke.etutor.modules.nf.analysis;

import etutor.core.evaluation.Analysis;
import etutor.core.evaluation.DefaultAnalysis;
import etutor.modules.rdbd.model.Key;
import etutor.modules.rdbd.model.KeyComparator;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class KeysAnalysis extends DefaultAnalysis implements Analysis {

	private TreeSet missingKeys;
	private TreeSet additionalKeys;

	public KeysAnalysis() {
		super();
		this.missingKeys = new TreeSet(new KeyComparator());
		this.additionalKeys = new TreeSet(new KeyComparator());
	}

	public void addMissingKey(Key key){
		this.missingKeys.add(key);
	}
	
	public Iterator iterMissingKeys(){
		return this.missingKeys.iterator();
	}
	
	public TreeSet getMissingKeys(){
		return (TreeSet)this.missingKeys.clone();
	}
	
	public void setMissingKeys(Collection missingKeys){
		this.missingKeys.clear();
		this.missingKeys.addAll(missingKeys);
	}
	
	public void removeAllMissingKeys(Collection keys){
		this.missingKeys.removeAll(keys);
	}
	
	public void addAdditionalKey(Key key){
		this.additionalKeys.add(key);
	}
	
	public Iterator iterAdditionalKeys(){
		return this.additionalKeys.iterator();
	}
	
	public TreeSet getAdditionalKeys(){
		return (TreeSet)this.additionalKeys.clone();
	}

	public void setAdditionalKeys(Collection additionalKeys){
		this.additionalKeys.clear();
		this.additionalKeys.addAll(additionalKeys);
	}
	
	public void removeAllAdditionalKeys(Collection keys){
		this.additionalKeys.removeAll(keys);
	}
}
