package at.jku.dke.etutor.modules.nf.specification;

import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;

import java.io.Serial;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class RBRSpecification extends NFSpecification implements Cloneable {

	@Serial
	private static final long serialVersionUID = 2025183566330710558L;

	private List<String> baseAttributes;
	private IdentifiedRelation baseRelation;

	public RBRSpecification() {
		super();
		this.baseAttributes = new LinkedList<>();
	}

	public Object clone() throws CloneNotSupportedException {
		RBRSpecification clone = (RBRSpecification)super.clone();
		clone.baseAttributes = new LinkedList<>(this.baseAttributes);
		if (this.baseRelation != null) {
			clone.baseRelation = (IdentifiedRelation)this.baseRelation.clone();
		}
		return clone;
	}
	
	public List<String> getBaseAttributes() {
		checkAttributes();
		return new LinkedList<>(this.baseAttributes);
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
		this.baseAttributes = new LinkedList<>();
		this.baseAttributes.addAll(attributeSet);
	}

	public void setBaseAttributes(Collection<String> attributeCombination) {
		this.baseAttributes.clear();
		this.baseAttributes.addAll(attributeCombination);
	}

	public void addBaseAttribute(String attribute){
		this.baseAttributes.add(attribute);
	}

	@Override
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
