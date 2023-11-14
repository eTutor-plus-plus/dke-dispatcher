package at.jku.dke.etutor.modules.nf.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class Key implements Serializable{

	private final TreeSet<String> attributes;

	public Key() {
		super();
		this.attributes = new TreeSet<String>(new AttributeCollator());
	}
	
	public boolean equals(Object obj) {
		Key key;

		if (obj == null) {
			return false;
		}
		
		if (!(obj instanceof Key)) {
			return false;
		}
		
		key = (Key)obj;
		Iterator<String> attributesIterator = key.iterAttributes();

		if (!(key.getAttributes().containsAll(this.attributes))){
			return false;
		}

		while (attributesIterator.hasNext()){
			if (!(this.attributes.contains(attributesIterator.next()))){
				return false;
			}
		}

		return true;
	}

	public int hashCode() {
		return -1;
	}

	public String toString() {
		boolean first = true;
		StringBuilder toStringBuilder = new StringBuilder();
		Iterator<String> attributesIterator = this.attributes.iterator();

		while (attributesIterator.hasNext()) {
			if (first) {
				first = false;
			} else {
				toStringBuilder.append(" ");
			}
			toStringBuilder.append(attributesIterator.next());
		}

		return toStringBuilder.toString();
	}
	
	public TreeSet<String> getAttributes(){
		return (TreeSet<String>)this.attributes.clone();
	}
	
	public Iterator<String> iterAttributes() {
		return this.attributes.iterator();
	}

	public boolean addAttribute(String attribute) {
		if (attribute != null) {
			return this.attributes.add(attribute);
		} else {
			return false;
		}
	}
	
	public void addAllAttributes(Collection<String> attributes){
		if (attributes != null){
			Iterator<String> attributesIterator = attributes.iterator();
			while (attributesIterator.hasNext()){
				this.addAttribute(attributesIterator.next());
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