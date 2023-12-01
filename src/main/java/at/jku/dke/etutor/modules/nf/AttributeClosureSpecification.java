package at.jku.dke.etutor.modules.nf;

import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;

import java.io.Serializable;
import java.util.Collection;
import java.util.Vector;

public class AttributeClosureSpecification implements Serializable, Cloneable, RDBDSpecification{

	private static final long serialVersionUID = 7948740045298387409L;

	private Vector<String> baseAttributes;
	private IdentifiedRelation baseRelation;

	public AttributeClosureSpecification() {
		super();
		this.baseRelation = null;
		this.baseAttributes = new Vector<>();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		AttributeClosureSpecification clone = (AttributeClosureSpecification)super.clone();
		clone.baseAttributes = (Vector<String>)this.baseAttributes.clone();
		if (this.baseRelation != null) {
			clone.baseRelation = (IdentifiedRelation)this.baseRelation.clone();
		}
		return clone;
	}

	public Vector<String> getBaseAttributes() {
		return (Vector<String>)this.baseAttributes.clone();
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

        return this.baseRelation.semanticallyEquals(spec.getBaseRelation());
    }

	@Override
	public String toString(){
        return "BASE ATTRIBUTES: " + this.baseAttributes + "\n" +
				"BASE RELATION:\n" + this.baseRelation + "\n";
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