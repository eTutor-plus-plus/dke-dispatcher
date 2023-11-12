package at.jku.dke.etutor.modules.nf.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

public class FunctionalDependency implements Serializable{

	private TreeSet lhs;
	private TreeSet rhs;

	public FunctionalDependency() {
		super();
		this.lhs = new TreeSet(new AttributeCollator());
		this.rhs = new TreeSet(new AttributeCollator());
	}

	public FunctionalDependency(Collection lhs, Collection rhs) {
		this();
		this.setLHSAttributes(lhs);
		this.setRHSAttributes(rhs);
	}

	public Vector unfold() {
		FunctionalDependency dependency;
		Vector unfoldedRepresentation;
		Iterator rhsAttributesIterator;

		unfoldedRepresentation = new Vector();
		rhsAttributesIterator = this.rhs.iterator();

		while (rhsAttributesIterator.hasNext()) {
			dependency = new FunctionalDependency(this.lhs, null);
			dependency.addRHSAttribute((String)rhsAttributesIterator.next());
			if (!dependency.isTrivial()) {
				unfoldedRepresentation.add(dependency);
			}
		}

		return unfoldedRepresentation;
	}

	public boolean isTrivial() {

		if (this.lhs.size() == 0) {
			return true;
		}
		if (this.rhs.size() == 0) {
			return true;
		}

		return this.lhs.containsAll(this.rhs);
	}

	public boolean equals(Object obj) {
		FunctionalDependency dependency;
		Iterator attributesIterator;

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

		if (!(this.rhs.containsAll(dependency.getRHSAttributes()))) {
			return false;
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

		toString = new String();
		
		first = true;
		attributesIterator = this.iterLHSAttributes();
		while (attributesIterator.hasNext()){
			if (first){
				first = false;
			} else {
				toString = toString.concat(" ");
			}
			toString = toString.concat(attributesIterator.next().toString());
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
			toString = toString.concat(attributesIterator.next().toString());
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

	public void addAllLHSAttributes(Collection attributes){
		Iterator attributesIterator;
		
		attributesIterator = attributes.iterator();
		while (attributesIterator.hasNext()){
			this.addLHSAttribute((String)attributesIterator.next());
		}
		
	}

	public void addAllRHSAttributes(Collection attributes){
		Iterator attributesIterator;
		
		attributesIterator = attributes.iterator();
		while (attributesIterator.hasNext()){
			this.addRHSAttribute((String)attributesIterator.next());
		}
		
	}

	public void setLHSAttributes(Collection attributes) {
		Iterator attributesIterator;

		this.lhs.clear();
		if (attributes != null) {
			attributesIterator = attributes.iterator();
			while (attributesIterator.hasNext()) {
				this.addLHSAttribute((String)attributesIterator.next());
			}
		}
	}

	public void setRHSAttributes(Collection attributes) {
		Iterator attributesIterator;

		this.rhs.clear();
		if (attributes != null) {
			attributesIterator = attributes.iterator();
			while (attributesIterator.hasNext()) {
				this.addRHSAttribute((String)attributesIterator.next());
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

	public void removeLHSAttributes(Collection attributes) {
		this.lhs.removeAll(attributes);
	}

	public void removeRHSAttributes(Collection attributes) {
		this.rhs.removeAll(attributes);
	}

	public Iterator iterLHSAttributes() {
		return this.lhs.iterator();
	}

	public Iterator iterRHSAttributes() {
		return this.rhs.iterator();
	}

	public TreeSet getLHSAttributes(){
		return (TreeSet)this.lhs.clone();
	}

	public TreeSet getRHSAttributes(){
		return (TreeSet)this.rhs.clone();
	}
}
