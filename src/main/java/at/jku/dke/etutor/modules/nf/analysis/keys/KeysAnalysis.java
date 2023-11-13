package at.jku.dke.etutor.modules.nf.analysis.keys;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.KeyComparator;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class KeysAnalysis extends DefaultAnalysis implements Analysis {

	private final TreeSet<Key> missingKeys;
	private final TreeSet<Key> additionalKeys;

	public KeysAnalysis() {
		super();
		this.missingKeys = new TreeSet<Key>(new KeyComparator());
		this.additionalKeys = new TreeSet<Key>(new KeyComparator());
	}

	public void addMissingKey(Key key){
		this.missingKeys.add(key);
	}
	
	public Iterator<Key> iterMissingKeys(){
		return this.missingKeys.iterator();
	}
	
	public TreeSet<Key> getMissingKeys(){
		return (TreeSet<Key>)this.missingKeys.clone();
	}
	
	public void setMissingKeys(Collection<Key> missingKeys){
		this.missingKeys.clear();
		this.missingKeys.addAll(missingKeys);
	}
	
	public void removeAllMissingKeys(Collection<Key> keys){
		this.missingKeys.removeAll(keys);
	}
	
	public void addAdditionalKey(Key key){
		this.additionalKeys.add(key);
	}
	
	public Iterator<Key> iterAdditionalKeys(){
		return this.additionalKeys.iterator();
	}
	
	public TreeSet<Key> getAdditionalKeys(){
		return (TreeSet<Key>)this.additionalKeys.clone();
	}

	public void setAdditionalKeys(Collection<Key> additionalKeys){
		this.additionalKeys.clear();
		this.additionalKeys.addAll(additionalKeys);
	}
	
	public void removeAllAdditionalKeys(Collection<Key> keys){
		this.additionalKeys.removeAll(keys);
	}
}
