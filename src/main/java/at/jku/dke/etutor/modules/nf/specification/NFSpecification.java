package at.jku.dke.etutor.modules.nf.specification;

import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;

import java.io.Serializable;

public abstract class NFSpecification implements Serializable, Cloneable, HasSemanticEquality {
	protected IdentifiedRelation baseRelation;

	protected NFSpecification() {
		this.baseRelation = null;
	}

	protected NFSpecification(IdentifiedRelation baseRelation) {
		this.baseRelation = baseRelation;
	}

	public IdentifiedRelation getBaseRelation() {
		return this.baseRelation;
	}

	public void setBaseRelation(IdentifiedRelation baseRelation) {
		this.baseRelation = baseRelation;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		NFSpecification clone = (NFSpecification) super.clone();
		if(baseRelation != null) {
			clone.baseRelation = (IdentifiedRelation) this.baseRelation.clone();
		}

		return clone;
	}

	@Override
	public boolean semanticallyEquals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof NFSpecification)) {
			return false;
		}

		NFSpecification spec = (NFSpecification) obj;

		return spec.getBaseRelation().semanticallyEquals(this.getBaseRelation());
	}
}
