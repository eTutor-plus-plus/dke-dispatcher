package at.jku.dke.etutor.modules.nf.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.TreeSet;

public class Key implements Serializable {

	private final TreeSet<String> attributes;

	public Key() {
		this.attributes = new TreeSet<>(new AttributeCollator());
	}

	@Override
	public boolean equals(Object obj) {
		Key key;

		if (obj == null) {
			return false;
		}
		
		if (!(obj instanceof Key)) {
			return false;
		}
		
		key = (Key)obj;

		if (!(key.getAttributes().containsAll(this.attributes))){
			return false;
		}

        return this.attributes.containsAll(key.getAttributes());
    }

	@Override
	public int hashCode() {
		return -1;
	}

	@Override
	public String toString() {
		boolean first = true;
		StringBuilder toStringBuilder = new StringBuilder();

        for (String attribute : this.attributes) {
            if (first) {
                first = false;
            } else {
                toStringBuilder.append(" ");
            }
            toStringBuilder.append(attribute);
        }

		return toStringBuilder.toString();
	}
	
	public TreeSet<String> getAttributes(){
		return (TreeSet<String>)this.attributes.clone();
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
            for (String attribute : attributes) {
                this.addAttribute(attribute);
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