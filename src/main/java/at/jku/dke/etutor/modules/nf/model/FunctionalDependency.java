package at.jku.dke.etutor.modules.nf.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

public class FunctionalDependency implements Serializable{

	private final TreeSet<String> lhs;
	private final TreeSet<String> rhs;

	public FunctionalDependency() {
		this.lhs = new TreeSet<>(new AttributeCollator());
		this.rhs = new TreeSet<>(new AttributeCollator());
	}

	public FunctionalDependency(Collection<String> lhs, Collection<String> rhs) {
		this();
		this.setLHSAttributes(lhs);
		this.setRHSAttributes(rhs);
	}

	/**
	 * This method splits this <code>FunctionalDependency</code> up into multiple <code>FunctionalDependencies</code>,
	 * each with the same left hand side and a single attribute on the right hand side. (Gerald Wimmer, 2023-11-30)
	 * @return A <code>List</code> of unfolded functional dependencies, derived from this one
	 */
	public List<FunctionalDependency> unfold() {
		/*
		 * As the only usage of this method's return value is to add all values to another collection, I replaced the
		 * Vector with a LinkedList for faster insertion here.
		 */
		List<FunctionalDependency> unfoldedRepresentation = new LinkedList<>();

        for (String rh : this.rhs) {
            FunctionalDependency dependency = new FunctionalDependency(this.lhs, null);
            dependency.addRHSAttribute(rh);
            if (!dependency.isTrivial()) {
                unfoldedRepresentation.add(dependency);
            }
        }

		return unfoldedRepresentation;
	}

	public boolean isTrivial() {
		return this.lhs.isEmpty() || this.rhs.isEmpty() || this.lhs.containsAll(this.rhs);
	}

	@Override
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

	@Override
	public int hashCode() {
		return Objects.hash(lhs, rhs);
	}

	@Override
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
