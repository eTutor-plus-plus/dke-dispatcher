package at.jku.dke.etutor.modules.nf.specification;

import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AttributeClosureSpecification extends NFSpecification implements Cloneable {
	/**
	 * Points deducted for every missing key, as compared to the correct solution.
	 * <br><br>
	 * (a in the grading schema V3)
	 */
	private int penaltyPerMissingAttribute;

	/**
	 * Points deducted for every incorrect key, as compared to the correct solution.
	 * <br><br>
	 * (b in the grading schema V3)
	 */
	private int penaltyPerIncorrectAttribute;

	@Serial
	private static final long serialVersionUID = 7948740045298387409L;

	private List<String> baseAttributes;

	public AttributeClosureSpecification() {
		super();
		this.baseAttributes = new LinkedList<>();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		AttributeClosureSpecification clone = (AttributeClosureSpecification)super.clone();
		clone.baseAttributes = new LinkedList<> (this.baseAttributes);
		if (this.baseRelation != null) {
			clone.baseRelation = (IdentifiedRelation)this.baseRelation.clone();
		}
		return clone;
	}

	public List<String> getBaseAttributes() {
		return new LinkedList<>(this.baseAttributes);
	}

	public void setBaseAttributes(Collection<String> attributeCombination) {
		this.baseAttributes.clear();
		this.baseAttributes.addAll(attributeCombination);
	}

	public int getPenaltyPerMissingAttribute() {
		return penaltyPerMissingAttribute;
	}

	public void setPenaltyPerMissingAttribute(int penaltyPerMissingAttribute) {
		this.penaltyPerMissingAttribute = penaltyPerMissingAttribute;
	}

	public int getPenaltyPerIncorrectAttribute() {
		return penaltyPerIncorrectAttribute;
	}

	public void setPenaltyPerIncorrectAttribute(int penaltyPerIncorrectAttribute) {
		this.penaltyPerIncorrectAttribute = penaltyPerIncorrectAttribute;
	}

	public void addBaseAttribute(String attribute){
		this.baseAttributes.add(attribute);
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

		if(spec.getPenaltyPerMissingAttribute() != this.penaltyPerMissingAttribute) {
			return false;
		}

		if(spec.getPenaltyPerIncorrectAttribute() != this.penaltyPerIncorrectAttribute) {
			return false;
		}

        return super.semanticallyEquals(spec);
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