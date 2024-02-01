package at.jku.dke.etutor.modules.nf.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Key implements Serializable {

	private final Set<String> attributes;

	public Key() {
		this.attributes = new TreeSet<>(new AttributeCollator());
	}

	public Key(String ... attributesArr) {
		this.attributes = new TreeSet<>(new AttributeCollator());
		this.attributes.addAll(Arrays.stream(attributesArr).toList());
	}

	public Key(Collection<String> attributes) {
		this.attributes = new TreeSet<>(new AttributeCollator());
		this.attributes.addAll(attributes);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (!(obj instanceof Key)) {
			return false;
		}
		
		Key key = (Key)obj;

		if (!(key.getAttributes().containsAll(this.attributes))){
			return false;
		}

        return this.attributes.containsAll(key.getAttributes());
    }

	@Override
	public int hashCode() {
		return Objects.hash(attributes);
	}

	@Override
	public String toString() {
		StringJoiner toStringJoiner = new StringJoiner(" ");

        for (String attribute : this.attributes) {
            toStringJoiner.add(attribute);
        }

		return toStringJoiner.toString();
	}
	
	public Set<String> getAttributes() {
		TreeSet<String> ret = new TreeSet<>(new AttributeCollator());
		ret.addAll(this.attributes);
		return ret;
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


}