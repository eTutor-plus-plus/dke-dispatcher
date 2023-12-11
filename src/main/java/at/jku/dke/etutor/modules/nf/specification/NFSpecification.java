package at.jku.dke.etutor.modules.nf.specification;

import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;

import java.io.Serializable;

public abstract class NFSpecification implements Serializable, Cloneable, HasSemanticEquality {
	protected IdentifiedRelation baseRelation;

	/**
	 * The number of points which each subdivision of this exercise gives you (e.g, points per leaf relation, per key,
	 * per correct functional dependency ...)
	 */
	private int pointsPerSubdivision;

	protected NFSpecification(IdentifiedRelation baseRelation) {
		this.baseRelation = baseRelation;
		this.pointsPerSubdivision = 1;
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

	public abstract void getTotalPoints();
}
