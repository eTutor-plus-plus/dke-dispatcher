package at.jku.dke.etutor.modules.nf.specification;

import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;

import java.io.Serializable;

public abstract class NFSpecification implements Serializable, Cloneable, HasSemanticEquality {
	protected IdentifiedRelation baseRelation;

	private int totalPoints;

	protected NFSpecification() {
		this.baseRelation = null;
		this.totalPoints = 0;
	}

	protected NFSpecification(IdentifiedRelation baseRelation, int totalPoints) {
		this.baseRelation = baseRelation;
		this.totalPoints = totalPoints;
	}

	public IdentifiedRelation getBaseRelation() {
		return this.baseRelation;
	}

	public void setBaseRelation(IdentifiedRelation baseRelation) {
		this.baseRelation = baseRelation;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		NFSpecification clone = (NFSpecification) super.clone();
		if(baseRelation != null) {
			clone.baseRelation = (IdentifiedRelation) this.baseRelation.clone();
		}

		return clone;
	}
}
