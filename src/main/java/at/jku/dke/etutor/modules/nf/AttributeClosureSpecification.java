package at.jku.dke.etutor.modules.nf;

import etutor.modules.rdbd.ui.IdentifiedRelation;

import java.io.Serializable;
import java.util.Collection;
import java.util.Vector;

public class AttributeClosureSpecification implements Serializable, Cloneable, RDBDSpecification{

	static final long serialVersionUID = 7948740045298387409L;

	private Vector baseAttributes;
	private IdentifiedRelation baseRelation;

	public AttributeClosureSpecification() {
		super();
		this.baseRelation = null;
		this.baseAttributes = new Vector();
	}

	public Object clone() throws CloneNotSupportedException {
		AttributeClosureSpecification clone = (AttributeClosureSpecification)super.clone();
		clone.baseAttributes = (Vector)this.baseAttributes.clone();
		if (this.baseRelation != null) {
			clone.baseRelation = (IdentifiedRelation)this.baseRelation.clone();
		}
		return clone;
	}

	public Vector getBaseAttributes() {
		return (Vector)this.baseAttributes.clone();
	}

	public void setBaseAttributes(Collection attributeCombination) {
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
		AttributeClosureSpecification spec;
		
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof AttributeClosureSpecification)) {
			return false;
		}

		spec = (AttributeClosureSpecification)obj;
		
		if (!(spec.getBaseAttributes().containsAll(this.baseAttributes))){
			return false;
		}
		
		if (!(this.baseAttributes.containsAll(spec.getBaseAttributes()))){
			return false;
		}
		
		if (!(this.baseRelation.semanticallyEquals(spec.getBaseRelation()))){
			return false;
		}

		return true;
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("BASE ATTRIBUTES: " + this.baseAttributes + "\n");
		buffer.append("BASE RELATION:\n" + this.baseRelation + "\n");
		
		return buffer.toString();
	}
	
	public static void main(String[] args) {
		AttributeClosureSpecification spec1 = new AttributeClosureSpecification();
		spec1.setBaseRelation(new IdentifiedRelation());
		try {
			AttributeClosureSpecification spec2 = (AttributeClosureSpecification)spec1.clone();
			System.out.println(spec1.getBaseRelation().equals(spec2.getBaseRelation()));
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}