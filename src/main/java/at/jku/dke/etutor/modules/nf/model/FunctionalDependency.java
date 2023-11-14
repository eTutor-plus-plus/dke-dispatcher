package at.jku.dke.etutor.modules.nf.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

public class FunctionalDependency implements Serializable{

	private final TreeSet<String> lhs;
	private final TreeSet<String> rhs;

	public FunctionalDependency() {
		super();
		this.lhs = new TreeSet<>(new AttributeCollator());
		this.rhs = new TreeSet<>(new AttributeCollator());
	}

	public FunctionalDependency(Collection<String> lhs, Collection<String> rhs) {
		this();
		this.setLHSAttributes(lhs);
		this.setRHSAttributes(rhs);
	}

	public Vector<FunctionalDependency> unfold() {
		FunctionalDependency dependency;

		Vector<FunctionalDependency> unfoldedRepresentation = new Vector<>();

        for (String rh : this.rhs) {
            dependency = new FunctionalDependency(this.lhs, null);
            dependency.addRHSAttribute(rh);
            if (!dependency.isTrivial()) {
                unfoldedRepresentation.add(dependency);
            }
        }

		return unfoldedRepresentation;
	}

	public boolean isTrivial() {

		if (this.lhs.isEmpty()) {
			return true;
		}
		if (this.rhs.isEmpty()) {
			return true;
		}

		return this.lhs.containsAll(this.rhs);
	}

	public boolean equals(Object obj) {
		FunctionalDependency dependency;

		if (obj == null) {
			return false;
		}

		if (!(obj instanceof FunctionalDependency)) {
			return false;
		}

		dependency = (FunctionalDependency)obj;

		if (!(dependency.getLHSAttributes().containsAll(this.lhs))) {
			return false;
		}

		if (!(dependency.getRHSAttributes().containsAll(this.rhs))) {
			return false;
		}
		
		if (!(this.lhs.containsAll(dependency.getLHSAttributes()))) {
			return false;
		}

        return this.rhs.containsAll(dependency.getRHSAttributes());
    }

	public int hashCode() {
		return -1;
	}

	public String toString() {
		boolean first;
		String toString;
		Iterator<String> attributesIterator;

		toString = "";
		
		first = true;
		attributesIterator = this.iterLHSAttributes();
		while (attributesIterator.hasNext()){
			if (first){
				first = false;
			} else {
				toString = toString.concat(" ");
			}
			toString = toString.concat(attributesIterator.next());
		}

		toString = toString.concat(" -> ");

		first = true;
		attributesIterator = this.iterRHSAttributes();
		while (attributesIterator.hasNext()){
			if (first){
				first = false;
			} else {
				toString = toString.concat(" ");
			}
			toString = toString.concat(attributesIterator.next());
		}

		return toString;
	}

	public boolean addLHSAttribute(String attribute) {
		if (attribute != null) {
			return this.lhs.add(attribute);
		} else {
			return false;
		}
	}

	public boolean addRHSAttribute(String attribute) {
		if (attribute != null) {
			return this.rhs.add(attribute);
		} else {
			return false;
		}
	}

	public void addAllLHSAttributes(Collection<String> attributes){
        for (String attribute : attributes) {
            this.addLHSAttribute(attribute);
        }
		
	}

	public void addAllRHSAttributes(Collection<String> attributes){
        for (String attribute : attributes) {
            this.addRHSAttribute(attribute);
        }
		
	}

	public void setLHSAttributes(Collection<String> attributes) {
		this.lhs.clear();
		if (attributes != null) {
            for (String attribute : attributes) {
                this.addLHSAttribute(attribute);
            }
		}
	}

	public void setRHSAttributes(Collection<String> attributes) {
		this.rhs.clear();
		if (attributes != null) {
            for (String attribute : attributes) {
                this.addRHSAttribute(attribute);
            }
		}
	}

	public boolean removeLHSAttribute(String attribute) {
		return this.lhs.remove(attribute);
	}

	public boolean removeRHSAttribute(String attribute) {
		return this.rhs.remove(attribute);
	}

	public void removeAllLHSAttributes() {
		this.lhs.clear();
	}

	public void removeAllRHSAttributes() {
		this.rhs.clear();
	}

	public void removeLHSAttributes(Collection<String> attributes) {
		this.lhs.removeAll(attributes);
	}

	public void removeRHSAttributes(Collection<String> attributes) {
		this.rhs.removeAll(attributes);
	}

	public Iterator<String> iterLHSAttributes() {
		return this.lhs.iterator();
	}

	public Iterator<String> iterRHSAttributes() {
		return this.rhs.iterator();
	}

	public TreeSet<String> getLHSAttributes(){
		return (TreeSet<String>)this.lhs.clone();
	}

	public TreeSet<String> getRHSAttributes(){
		return (TreeSet<String>)this.rhs.clone();
	}
}
