package at.jku.dke.etutor.modules.nf;

import at.jku.dke.etutor.modules.nf.ui.IdentifiedRelation;

import java.io.Serializable;
import java.util.Collection;
import java.util.TreeSet;
import java.util.Vector;

public class RBRSpecification implements Serializable, Cloneable, RDBDSpecification{

	private static final long serialVersionUID = 2025183566330710558L;

	private Vector<String> baseAttributes;
	private IdentifiedRelation baseRelation;

	public RBRSpecification() {
		super();
		this.baseRelation = null;
		this.baseAttributes = new Vector<>();
	}

	public Object clone() throws CloneNotSupportedException {
		RBRSpecification clone = (RBRSpecification)super.clone();
		clone.baseAttributes = (Vector<String>)this.baseAttributes.clone();
		if (this.baseRelation != null) {
			clone.baseRelation = (IdentifiedRelation)this.baseRelation.clone();
		}
		return clone;
	}
	
	public Vector<String> getBaseAttributes() {
		checkAttributes();
		return (Vector<String>)this.baseAttributes.clone();
	}
	
	/**
	 * Removing duplicates and not anymore existing attributes of the base relation 
	 */
	private void checkAttributes() {
		TreeSet<String> attributeSet = new TreeSet<>();
		for (int i = 0; baseAttributes != null && i < baseAttributes.size(); i++) {
			if (this.baseRelation.getAttributes().contains(baseAttributes.get(i))) {
				attributeSet.add(baseAttributes.get(i));
			}
		}
		this.baseAttributes = new Vector<>();
		this.baseAttributes.addAll(attributeSet);
	}

	public void setBaseAttributes(Collection<String> attributeCombination) {
		this.baseAttributes.clear();
		this.baseAttributes.addAll(attributeCombination);
	}

	public void addBaseAttribute(String attribute){
		this.baseAttributes.add(attribute);
	}

	public IdentifiedRelation getBaseRelation() {
		return this.baseRelation;
	}

	public void setBaseRelation(IdentifiedRelation relation) {
		baseRelation = relation;
	}
	
	public boolean semanticallyEquals(Object obj) {
		RBRSpecification spec;
		
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof RBRSpecification)) {
			return false;
		}

		spec = (RBRSpecification)obj;
		checkAttributes();
		if (!(spec.getBaseAttributes().containsAll(this.baseAttributes))){
			return false;
		}
		
		if (!(this.baseAttributes.containsAll(spec.getBaseAttributes()))){
			return false;
		}

        return this.baseRelation.semanticallyEquals(spec.getBaseRelation());
    }

	public String toString(){
		StringBuilder buffer = new StringBuilder();
		checkAttributes();
		buffer.append("BASE ATTRIBUTES: ").append(this.baseAttributes).append("\n");
		buffer.append("BASE RELATION:\n").append(this.baseRelation).append("\n");
		
		return buffer.toString();
	}
}
