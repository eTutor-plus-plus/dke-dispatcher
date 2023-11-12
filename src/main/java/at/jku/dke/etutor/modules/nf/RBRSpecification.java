package at.jku.dke.etutor.modules.nf;

import etutor.modules.rdbd.ui.IdentifiedRelation;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

public class RBRSpecification implements Serializable, Cloneable, RDBDSpecification{

	static final long serialVersionUID = 2025183566330710558L;

	private Vector baseAttributes;
	private IdentifiedRelation baseRelation;

	public RBRSpecification() {
		super();
		this.baseRelation = null;
		this.baseAttributes = new Vector();
	}

	public Object clone() throws CloneNotSupportedException {
		RBRSpecification clone = (RBRSpecification)super.clone();
		clone.baseAttributes = (Vector)this.baseAttributes.clone();
		if (this.baseRelation != null) {
			clone.baseRelation = (IdentifiedRelation)this.baseRelation.clone();
		}
		return clone;
	}
	
	public Vector getBaseAttributes() {
		checkAttributes();
		return (Vector)this.baseAttributes.clone();
	}
	
	/**
	 * Removing duplicates and not anymore existing attributes of the base relation 
	 */
	private void checkAttributes() {
		TreeSet set = new TreeSet();
		Iterator it;
		for (int i = 0; baseAttributes != null && i < baseAttributes.size(); i++) {
			if (this.baseRelation.getAttributes().contains(baseAttributes.get(i))) {
				set.add(baseAttributes.get(i));
			}
		}
		this.baseAttributes = new Vector();
		it = set.iterator();
		while (it.hasNext()) {
			this.baseAttributes.add(it.next());
		}
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
		
		if (!(this.baseRelation.semanticallyEquals(spec.getBaseRelation()))){
			return false;
		}

		return true;
	}

	public String toString(){
		StringBuffer buffer = new StringBuffer();
		checkAttributes();
		buffer.append("BASE ATTRIBUTES: " + this.baseAttributes + "\n");
		buffer.append("BASE RELATION:\n" + this.baseRelation + "\n");
		
		return buffer.toString();
	}
}
