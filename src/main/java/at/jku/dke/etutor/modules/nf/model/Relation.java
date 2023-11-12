package at.jku.dke.etutor.modules.nf.model;

import etutor.modules.rdbd.RDBDSpecification;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

public class Relation implements Serializable, Cloneable, RDBDSpecification {

	static final long serialVersionUID = 7982386529581622533L;

	private String name;
	private TreeSet attributes;
	private HashSet dependencies;

	private HashSet subKeys;
	private TreeSet superKeys;
	private TreeSet minimalKeys;
	
	public Relation() {
		super();
		this.name = new String();
		this.subKeys = new HashSet();
		this.dependencies = new HashSet();
		this.superKeys = new TreeSet(new KeyComparator());
		this.minimalKeys = new TreeSet(new KeyComparator());
		this.attributes = new TreeSet(new AttributeCollator());
	}

	public Object clone() throws CloneNotSupportedException {
		Relation clone = (Relation)super.clone();
		clone.attributes = (TreeSet)this.attributes.clone();
		clone.dependencies = (HashSet)this.dependencies.clone();
		clone.minimalKeys = (TreeSet)this.minimalKeys.clone();
		clone.subKeys = (HashSet)this.subKeys.clone();
		clone.superKeys = (TreeSet)this.superKeys.clone();
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
	public Relation(Collection attributes, Collection dependencies) {
		this();
		this.setAttributes(attributes);
		this.setFunctionalDependencies(dependencies);
	}

	public Iterator iterSubKeys() {
		return this.subKeys.iterator();
	}

	public Iterator iterSuperKeys() {
		return this.superKeys.iterator();
	}

	public Iterator iterAttributes() {
		return this.attributes.iterator();
	}

	public Iterator iterMinimalKeys() {
		return this.minimalKeys.iterator();
	}

	public Iterator iterFunctionalDependencies() {
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

	public void setSubKeys(Collection subKeys) {
		Iterator subKeysIterator;

		this.subKeys.clear();
		if (subKeys != null) {
			subKeysIterator = subKeys.iterator();
			while (subKeysIterator.hasNext()) {
				this.addSubKey((Key)subKeysIterator.next());
			}
		}
	}

	public void setSuperKeys(Collection superKeys) {
		Iterator superKeysIterator;

		this.superKeys.clear();
		if (superKeys != null) {
			superKeysIterator = superKeys.iterator();
			while (superKeysIterator.hasNext()) {
				this.addSuperKey((Key)superKeysIterator.next());
			}
		}
	}

	public void setAttributes(Collection attributes) {
		Iterator attributesIterator;

		this.attributes.clear();
		if (attributes != null) {
			attributesIterator = attributes.iterator();
			while (attributesIterator.hasNext()) {
				this.addAttribute((String)attributesIterator.next());
			}
		}
	}

	public void setMinimalKeys(Collection minimalKeys) {
		Iterator minimalKeysIterator;

		this.minimalKeys.clear();
		if (superKeys != null) {
			minimalKeysIterator = minimalKeys.iterator();
			while (minimalKeysIterator.hasNext()) {
				this.addMinimalKey((Key)minimalKeysIterator.next());
			}
		}
	}

	public void setFunctionalDependencies(Collection dependencies) {
		Iterator dependenciesIterator;

		this.dependencies.clear();
		if (dependencies != null) {
			dependenciesIterator = dependencies.iterator();
			while (dependenciesIterator.hasNext()) {
				this.addFunctionalDependency((FunctionalDependency)dependenciesIterator.next());
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
	
	public HashSet getSubKeys(){
		return (HashSet)this.subKeys.clone();
	}
	
	public TreeSet getSuperKeys(){
		return (TreeSet)this.superKeys.clone();
	}
	
	public TreeSet getAttributes(){
		return (TreeSet)this.attributes.clone();
	}
	
	public String[] getAttributesArray(){
		int pos;
		String[] attributes;
		Iterator attributesIterator;
		
		pos = 0;
		attributes = new String[this.attributes.size()];
		attributesIterator = this.attributes.iterator();
		while (attributesIterator.hasNext()){
			attributes[pos] = attributesIterator.next().toString();
			pos = pos + 1;
		}
		
		return attributes;
	}
	
	public TreeSet getMinimalKeys(){
		return (TreeSet)this.minimalKeys.clone();
	}
	
	public HashSet getFunctionalDependencies(){
		return (HashSet)this.dependencies.clone();
	}

	/*
	 * name wird nicht �berpr�ft
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
		
		if (!this.dependencies.containsAll(rel.getFunctionalDependencies())){
			return false;
		}
		
		return true;
	}
}
