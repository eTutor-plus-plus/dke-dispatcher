package at.jku.dke.etutor.modules.nf.analysis.keys;

import at.jku.dke.etutor.modules.nf.analysis.NFAnalysis;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.KeyComparator;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class KeysAnalysis extends NFAnalysis {

	private final Set<Key> missingKeys;
	private final Set<Key> additionalKeys;

	public KeysAnalysis() {
		super();

		this.missingKeys = new TreeSet<>(new KeyComparator());
		this.additionalKeys = new TreeSet<>(new KeyComparator());
	}

	public void addMissingKey(Key key){
		this.missingKeys.add(key);
	}

	public Set<Key> getMissingKeys() {
		TreeSet<Key> ret = new TreeSet<>(new KeyComparator());
		ret.addAll(this.missingKeys);

		return ret;
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

	public Set<Key> getAdditionalKeys() {
		TreeSet<Key> ret = new TreeSet<>(new KeyComparator());
		ret.addAll(this.additionalKeys);

		return ret;
	}

	public void setAdditionalKeys(Collection<Key> additionalKeys){
		this.additionalKeys.clear();
		this.additionalKeys.addAll(additionalKeys);
	}
	
	public void removeAllAdditionalKeys(Collection<Key> keys){
		this.additionalKeys.removeAll(keys);
	}
}
