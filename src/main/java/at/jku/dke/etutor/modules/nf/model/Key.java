package at.jku.dke.etutor.modules.nf.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Collection;
import java.util.StringJoiner;
import java.util.TreeSet;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
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
		StringJoiner toStringJoiner = new StringJoiner(" ");

        for (String attribute : this.attributes) {
            toStringJoiner.add(attribute);
        }

		return toStringJoiner.toString();
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