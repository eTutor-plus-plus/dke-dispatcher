package at.jku.dke.etutor.modules.nf.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FunctionalDependency implements Serializable{

	private final Set<String> lhsAttributes;
	private final Set<String> rhsAttributes;

	public FunctionalDependency() {
		this.lhsAttributes = new TreeSet<>(new AttributeCollator());
		this.rhsAttributes = new TreeSet<>(new AttributeCollator());
	}

	public FunctionalDependency(Collection<String> lhsAttributes, Collection<String> rhsAttributes) {
		this();
		this.setLhsAttributes(lhsAttributes);
		this.setRhsAttributes(rhsAttributes);
	}

	/**
	 * This method splits this <code>FunctionalDependency</code> up into multiple <code>FunctionalDependencies</code>,
	 * each with the same left hand side and a single attribute on the right hand side. (Gerald Wimmer, 2023-11-30)
	 * @return A <code>List</code> of unfolded functional dependencies, derived from this one
	 */
	public List<FunctionalDependency> unfold() {
		List<FunctionalDependency> unfoldedRepresentation = new LinkedList<>();

        for (String rh : this.rhsAttributes) {
            FunctionalDependency dependency = new FunctionalDependency(this.lhsAttributes, null);
            dependency.addRHSAttribute(rh);
            if (!dependency.isTrivial()) {
                unfoldedRepresentation.add(dependency);
            }
        }

		return unfoldedRepresentation;
	}

	/**
	 * Tests whether either side of this <code>FunctionalDependency</code> is empty or if the left-hand side contains
	 * the right-hand side (Gerald Wimmer, 2023-12-31)
	 * @return whether either side of this <code>FunctionalDependency</code> is empty or if the left-hand side contains
	 * the right-hand side
	 */
	@JsonIgnore
	public boolean isTrivial() {
		return this.lhsAttributes.isEmpty() || this.rhsAttributes.isEmpty() || this.lhsAttributes.containsAll(this.rhsAttributes);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof FunctionalDependency)) {
			return false;
		}

		FunctionalDependency dependency = (FunctionalDependency)obj;

		if (!(dependency.getLhsAttributes().containsAll(this.lhsAttributes))) {
			return false;
		}

		if (!(dependency.getRhsAttributes().containsAll(this.rhsAttributes))) {
			return false;
		}
		
		if (!(this.lhsAttributes.containsAll(dependency.getLhsAttributes()))) {
			return false;
		}

        return this.rhsAttributes.containsAll(dependency.getRhsAttributes());
    }

	@Override
	public int hashCode() {
		return Objects.hash(lhsAttributes, rhsAttributes);
	}

	@Override
	public String toString() {
		StringJoiner leftSide = new StringJoiner(" ");
		for (String a : lhsAttributes){
			leftSide.add(a);
		}

		StringJoiner rightSide = new StringJoiner(" ");
		for (String a : rhsAttributes){
			rightSide.add(a);
		}

		return leftSide + " -> " + rightSide;
	}

	public boolean addLHSAttribute(String attribute) {
		if (attribute != null) {
			return this.lhsAttributes.add(attribute);
		} else {
			return false;
		}
	}

	public boolean addRHSAttribute(String attribute) {
		if (attribute != null) {
			return this.rhsAttributes.add(attribute);
		} else {
			return false;
		}
	}

	public void addAllLhsAttributes(Collection<String> attributes){
        for (String attribute : attributes) {
            this.addLHSAttribute(attribute);
        }
	}

	public void addAllRhsAttributes(Collection<String> attributes){
        for (String attribute : attributes) {
            this.addRHSAttribute(attribute);
        }
	}

	public void setLhsAttributes(Collection<String> attributes) {
		this.lhsAttributes.clear();
		if (attributes != null) {
            for (String attribute : attributes) {
                this.addLHSAttribute(attribute);
            }
		}
	}

	public void setRhsAttributes(Collection<String> attributes) {
		this.rhsAttributes.clear();
		if (attributes != null) {
            for (String attribute : attributes) {
                this.addRHSAttribute(attribute);
            }
		}
	}

	public boolean removeLhsAttribute(String attribute) {
		return this.lhsAttributes.remove(attribute);
	}

	public boolean removeRhsAttribute(String attribute) {
		return this.rhsAttributes.remove(attribute);
	}

	public void removeAllLhsAttributes() {
		this.lhsAttributes.clear();
	}

	public void removeAllRhsAttributes() {
		this.rhsAttributes.clear();
	}

	public void removeLhsAttributes(Collection<String> attributes) {
		this.lhsAttributes.removeAll(attributes);
	}

	public void removeRhsAttributes(Collection<String> attributes) {
		this.rhsAttributes.removeAll(attributes);
	}

	public Set<String> getLhsAttributes() {
		TreeSet<String> ret = new TreeSet<>(new AttributeCollator());
		ret.addAll(lhsAttributes);
		return ret;
	}

	public Set<String> getRhsAttributes() {
		TreeSet<String> ret = new TreeSet<>(new AttributeCollator());
		ret.addAll(rhsAttributes);
		return ret;
	}
}
