package at.jku.dke.etutor.modules.nf.model;

import at.jku.dke.etutor.modules.nf.RDBDSpecification;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
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

	@Override
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
		this.subKeys.clear();

		if (subKeys != null) {
			for (Key subKey : subKeys) {
				this.addSubKey(subKey);
			}
		}
	}

	public void setSuperKeys(Collection<Key> superKeys) {
		this.superKeys.clear();

		if (superKeys != null) {
			for (Key superKey : superKeys) {
				this.addSuperKey(superKey);
			}
		}
	}

	public void setAttributes(Collection<String> attributes) {
		this.attributes.clear();

		if (attributes != null) {
			for (String a : attributes) {
				this.addAttribute(a);
			}
		}
	}

	public void setMinimalKeys(Collection<Key> minimalKeys) {
		this.minimalKeys.clear();

		if (minimalKeys != null) {
			for(Key minimalKey : minimalKeys) {
				this.addMinimalKey(minimalKey);
			}
		}
	}

	public void setFunctionalDependencies(Collection<FunctionalDependency> dependencies) {
		this.dependencies.clear();

		if (dependencies != null) {
			for (FunctionalDependency fd : dependencies) {
				this.addFunctionalDependency(fd);
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
	
	public Set<Key> getSubKeys(){
		return (Set<Key>)this.subKeys.clone();
	}
	
	public TreeSet<Key> getSuperKeys(){
		return (TreeSet<Key>)this.superKeys.clone();
	}
	
	public TreeSet<String> getAttributes(){
		return (TreeSet<String>)this.attributes.clone();
	}
	
	public String[] getAttributesArray() {
		String[] attributesArray = new String[this.attributes.size()];

		int pos = 0;
        for (String attribute : this.attributes) {
            attributesArray[pos] = attribute;
            pos++;
        }
		
		return attributesArray;
	}
	
	public TreeSet<Key> getMinimalKeys(){
		return (TreeSet<Key>)this.minimalKeys.clone();
	}
	
	public Set<FunctionalDependency> getFunctionalDependencies(){
		return (Set<FunctionalDependency>)this.dependencies.clone();
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
