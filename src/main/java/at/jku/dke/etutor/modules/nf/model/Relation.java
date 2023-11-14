package at.jku.dke.etutor.modules.nf.model;

import at.jku.dke.etutor.modules.nf.RDBDSpecification;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

public class Relation implements Serializable, Cloneable, RDBDSpecification {

	private static final long serialVersionUID = 7982386529581622533L;

	private String name;
	private TreeSet<String> attributes;
	private HashSet<FunctionalDependency> dependencies;

	private HashSet<Key> subKeys;
	private TreeSet<Key> superKeys;
	private TreeSet<Key> minimalKeys;
	
	public Relation() {
		super();
		this.name = "";
		this.subKeys = new HashSet<>();
		this.dependencies = new HashSet<>();
		this.superKeys = new TreeSet<>(new KeyComparator());
		this.minimalKeys = new TreeSet<>(new KeyComparator());
		this.attributes = new TreeSet<>(new AttributeCollator());
	}

	public Object clone() throws CloneNotSupportedException {
		Relation clone = (Relation)super.clone();
		clone.attributes = (TreeSet<String>)this.attributes.clone();
		clone.dependencies = (HashSet<FunctionalDependency>)this.dependencies.clone();
		clone.minimalKeys = (TreeSet<Key>)this.minimalKeys.clone();
		clone.subKeys = (HashSet<Key>)this.subKeys.clone();
		clone.superKeys = (TreeSet<Key>)this.superKeys.clone();
		return clone;
	}

	public static void main(String[] args) {
		Relation r = new Relation();
	try {
		Object clone = r.clone();
		System.out.println(clone.getClass().getName() + " ... ");
	} catch (CloneNotSupportedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	public Relation(Collection<String> attributes, Collection<FunctionalDependency> dependencies) {
		this();
		this.setAttributes(attributes);
		this.setFunctionalDependencies(dependencies);
	}

	public Iterator<Key> iterSubKeys() {
		return this.subKeys.iterator();
	}

	public Iterator<Key> iterSuperKeys() {
		return this.superKeys.iterator();
	}

	public Iterator<String> iterAttributes() {
		return this.attributes.iterator();
	}

	public Iterator<Key> iterMinimalKeys() {
		return this.minimalKeys.iterator();
	}

	public Iterator<FunctionalDependency> iterFunctionalDependencies() {
		return this.dependencies.iterator();
	}

	public boolean addSubKey(Key subKey) {
		if (subKey != null) {
			return this.subKeys.add(subKey);
		} else {
			return false;
		}
	}

	public boolean addSuperKey(Key superKey) {
		if (superKey != null) {
			return this.superKeys.add(superKey);
		} else {
			return false;
		}
	}

	public boolean addAttribute(String attribute) {
		if (attribute != null) {
			return this.attributes.add(attribute);
		} else {
			return false;
		}
	}

	public boolean addMinimalKey(Key minimalKey) {
		if (minimalKey != null) {
			return this.minimalKeys.add(minimalKey);
		} else {
			return false;
		}
	}

	public boolean addFunctionalDependency(FunctionalDependency functionalDependency) {
		if (functionalDependency != null) {
			return this.dependencies.add(functionalDependency);
		} else {
			return false;
		}
	}

	public boolean setName(String name){
		if (name != null){
			this.name = name;
			return true;
		} else {
			return false;
		}
	}

	public void setSubKeys(Collection<Key> subKeys) {
		Iterator<Key> subKeysIterator;

		this.subKeys.clear();
		if (subKeys != null) {
			subKeysIterator = subKeys.iterator();
			while (subKeysIterator.hasNext()) {
				this.addSubKey(subKeysIterator.next());
			}
		}
	}

	public void setSuperKeys(Collection<Key> superKeys) {
		Iterator<Key> superKeysIterator;

		this.superKeys.clear();
		if (superKeys != null) {
			superKeysIterator = superKeys.iterator();
			while (superKeysIterator.hasNext()) {
				this.addSuperKey(superKeysIterator.next());
			}
		}
	}

	public void setAttributes(Collection<String> attributes) {
		Iterator<String> attributesIterator;

		this.attributes.clear();
		if (attributes != null) {
			attributesIterator = attributes.iterator();
			while (attributesIterator.hasNext()) {
				this.addAttribute(attributesIterator.next());
			}
		}
	}

	public void setMinimalKeys(Collection<Key> minimalKeys) {
		Iterator<Key> minimalKeysIterator;

		this.minimalKeys.clear();
		if (superKeys != null) {
			minimalKeysIterator = minimalKeys.iterator();
			while (minimalKeysIterator.hasNext()) {
				this.addMinimalKey(minimalKeysIterator.next());
			}
		}
	}

	public void setFunctionalDependencies(Collection<FunctionalDependency> dependencies) {
		Iterator<FunctionalDependency> dependenciesIterator;

		this.dependencies.clear();
		if (dependencies != null) {
			dependenciesIterator = dependencies.iterator();
			while (dependenciesIterator.hasNext()) {
				this.addFunctionalDependency(dependenciesIterator.next());
			}
		}
	}

	public boolean removeSubKey(Key subKey) {
		return this.subKeys.remove(subKey);
	}

	public boolean removeSuperKey(Key superKey) {
		return this.superKeys.remove(superKey);
	}

	public boolean removeAttribute(String attribute) {
		return this.attributes.remove(attribute);
	}

	public boolean removeMinimalKey(Key minimalKey) {
		return this.minimalKeys.remove(minimalKey);
	}

	public boolean removeFunctionalDependency(FunctionalDependency dependency) {
		return this.dependencies.remove(dependency);
	}

	public void removeAllSubKeys() {
		this.subKeys.clear();
	}

	public void removeAllSuperKeys() {
		this.superKeys.clear();
	}

	public void removeAllAttributes() {
		this.attributes.clear();
	}
	
	public void removeAllMinimalKeys() {
		this.minimalKeys.clear();
	}

	public void removeAllFunctionalDependencies() {
		this.dependencies.clear();
	}
	
	public String getName(){
		return this.name;
	}
	
	public HashSet<Key> getSubKeys(){
		return (HashSet<Key>)this.subKeys.clone();
	}
	
	public TreeSet<Key> getSuperKeys(){
		return (TreeSet<Key>)this.superKeys.clone();
	}
	
	public TreeSet<String> getAttributes(){
		return (TreeSet<String>)this.attributes.clone();
	}
	
	public String[] getAttributesArray(){
		int pos = 0;
		String[] attributes = new String[this.attributes.size()];

        for (String attribute : this.attributes) {
            attributes[pos] = attribute;
            pos = pos + 1;
        }
		
		return attributes;
	}
	
	public TreeSet<Key> getMinimalKeys(){
		return (TreeSet<Key>)this.minimalKeys.clone();
	}
	
	public HashSet<FunctionalDependency> getFunctionalDependencies(){
		return (HashSet<FunctionalDependency>)this.dependencies.clone();
	}

	/*
	 * name wird nicht ueberprueft
	 */
	public boolean semanticallyEquals(Object obj){
		Relation rel;
		
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof Relation)) {
			return false;
		}

		rel = (Relation)obj;

		if (!rel.getAttributes().containsAll(this.attributes)){
			return false;
		}
		
		if (!rel.getFunctionalDependencies().containsAll(this.dependencies)){
			return false;
		}
		
		if (!this.attributes.containsAll(rel.getAttributes())){
			return false;
		}

        return this.dependencies.containsAll(rel.getFunctionalDependencies());
    }
}
