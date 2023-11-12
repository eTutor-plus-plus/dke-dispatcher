package at.jku.dke.etutor.modules.nf.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class Key implements Serializable{

	private TreeSet attributes;

	public Key() {
		super();
		this.attributes = new TreeSet(new AttributeCollator());
	}
	
	public boolean equals(Object obj) {
		Key key;
		Iterator attributesIterator;

		if (obj == null) {
			return false;
		}
		
		if (!(obj instanceof Key)) {
			return false;
		}
		
		key = (Key)obj;
		attributesIterator = key.iterAttributes();

		if (!(key.getAttributes().containsAll(this.attributes))){
			return false;
		}

		while (attributesIterator.hasNext()){
			if (!(this.attributes.contains((String)attributesIterator.next()))){
				return false;
			}
		}

		return true;
	}

	public int hashCode() {
		return -1;
	}

	public String toString() {
		boolean first;
		String toString;
		Iterator attributesIterator;

		first = true;
		toString = new String();
		attributesIterator = this.attributes.iterator();

		while (attributesIterator.hasNext()) {
			if (first) {
				first = false;
			} else {
				toString = toString.concat(" ");
			}
			toString = toString + attributesIterator.next();
		}

		return toString;
	}
	
	public TreeSet getAttributes(){
		return (TreeSet)this.attributes.clone();
	}
	
	public Iterator iterAttributes() {
		return this.attributes.iterator();
	}

	public boolean addAttribute(String attribute) {
		if (attribute != null) {
			return this.attributes.add(attribute);
		} else {
			return false;
		}
	}
	
	public void addAllAttributes(Collection attributes){
		Iterator attributesIterator;
		
		if (attributes != null){
			attributesIterator = attributes.iterator();
			while (attributesIterator.hasNext()){
				this.addAttribute((String)attributesIterator.next());
			}
		}
	}

	public boolean removeAttribute(String attribute) {
		return this.attributes.remove(attribute);
	}

	public void removeAllAttributes() {
		this.attributes.clear();
	}
	
	
}