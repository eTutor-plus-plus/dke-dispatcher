package at.jku.dke.etutor.modules.nf.specification;

import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NormalizationSpecification extends NFSpecification implements Cloneable {

	@Serial
	private static final long serialVersionUID = -8591463798404619419L;

	// Start of point deduction variables
	/**
	 * Points deducted for every attribute of the base relation that is not present in any of the resulting relations.
	 * <br><br>
	 * (a in the grading schema V3)
	 */
	private int penaltyPerLostAttribute;

	/**
	 * Points deducted if the resulting relations cannot be re-combined into the base relation.
	 * <br><br>
	 * (b in the grading schema V3)
	 */
	private int penaltyForLossyDecomposition;

	/**
	 * Points deducted for every non-canonical functional dependency in a resulting relation.
	 * <br><br>
	 * (c in the grading schema V3)
	 */
	private int penaltyPerNonCanonicalDependency;

	/**
	 * Points deducted for every trivial functional dependency in a resulting relation.
	 * <br><br>
	 * (d in the grading schema V3)
	 */
	private int penaltyPerTrivialDependency;

	/**
	 * Points deducted for every extraneous attribute on the left-hand side of a functional dependency in a resulting
	 * relation.
	 * <br><br>
	 * (e in the grading schema V3)
	 */
	private int penaltyPerExtraneousAttributeInDependencies;

	/**
	 * Points deducted for every redundant functional dependency in a resulting relation.
	 * <br><br>
	 * (f in the grading schema V3)
	 */
	private int penaltyPerRedundantDependency;

	/**
	 * Points deducted for every functional dependency that was lost during the decomposition process and exceeds the
	 * maximum permitted number of lost functional dependencies.
	 * <br><br>
	 * (g in the grading schema V3)
	 */
	private int penaltyPerExcessiveLostDependency;

	/**
	 * Points deducted for every functional dependency that would have to exist in a resulting relation due to the
	 * decomposition process but does not.
	 * <br><br>
	 * (h in the grading schema V3)
	 */
	private int penaltyPerMissingNewDependency;

	/**
	 * Points deducted for every functional dependency that exists in a resulting relation, even though it is not
	 * supposed to (due to the decomposition process, more specifically the RBR algorithm).
	 * <br><br>
	 * (i in the grading schema V3)
	 */
	private int penaltyPerIncorrectNewDependency;

	/**
	 * Points deducted for every missing key in a resulting relation.
	 * <br><br>
	 * (j in the grading schema V3)
	 */
	private int penaltyPerMissingKey;

	/**
	 * Points deducted for every incorrect key in a resulting relation.
	 * <br><br>
	 * (k in the grading schema V3)
	 */
	private int penaltyPerIncorrectKey;

	/**
	 * Points deducted for every resulting relation that does not match or exceed the required normal form.
	 * <br><br>
	 * (l in the grading schema V3)
	 */
	private int penaltyPerIncorrectNFRelation;
	// End of point deduction variables

	/**
	 * The maximum number of functional dependencies that is permitted to be lost in the decomposition process before
	 * points are deducted.
	 */
	protected int maxLostDependencies;

	/**
	 * The minimum normal form level which the resulting relations must have.
	 */
	protected NormalformLevel targetLevel;

	public NormalizationSpecification() {
		super(new IdentifiedRelation());
		this.maxLostDependencies = 0;
		this.targetLevel = NormalformLevel.THIRD;
 	}

	public int getPenaltyPerLostAttribute() {
		return penaltyPerLostAttribute;
	}

	public void setPenaltyPerLostAttribute(int penaltyPerLostAttribute) {
		this.penaltyPerLostAttribute = penaltyPerLostAttribute;
	}

	public int getPenaltyForLossyDecomposition() {
		return penaltyForLossyDecomposition;
	}

	public void setPenaltyForLossyDecomposition(int penaltyForLossyDecomposition) {
		this.penaltyForLossyDecomposition = penaltyForLossyDecomposition;
	}

	public int getPenaltyPerNonCanonicalDependency() {
		return penaltyPerNonCanonicalDependency;
	}

	public void setPenaltyPerNonCanonicalDependency(int penaltyPerNonCanonicalDependency) {
		this.penaltyPerNonCanonicalDependency = penaltyPerNonCanonicalDependency;
	}

	public int getPenaltyPerTrivialDependency() {
		return penaltyPerTrivialDependency;
	}

	public void setPenaltyPerTrivialDependency(int penaltyPerTrivialDependency) {
		this.penaltyPerTrivialDependency = penaltyPerTrivialDependency;
	}

	public int getPenaltyPerExtraneousAttributeInDependencies() {
		return penaltyPerExtraneousAttributeInDependencies;
	}

	public void setPenaltyPerExtraneousAttributeInDependencies(int penaltyPerExtraneousAttributeInDependencies) {
		this.penaltyPerExtraneousAttributeInDependencies = penaltyPerExtraneousAttributeInDependencies;
	}

	public int getPenaltyPerRedundantDependency() {
		return penaltyPerRedundantDependency;
	}

	public void setPenaltyPerRedundantDependency(int penaltyPerRedundantDependency) {
		this.penaltyPerRedundantDependency = penaltyPerRedundantDependency;
	}

	public int getPenaltyPerExcessiveLostDependency() {
		return penaltyPerExcessiveLostDependency;
	}

	public void setPenaltyPerExcessiveLostDependency(int penaltyPerExcessiveLostDependency) {
		this.penaltyPerExcessiveLostDependency = penaltyPerExcessiveLostDependency;
	}

	public int getPenaltyPerMissingNewDependency() {
		return penaltyPerMissingNewDependency;
	}

	public void setPenaltyPerMissingNewDependency(int penaltyPerMissingNewDependency) {
		this.penaltyPerMissingNewDependency = penaltyPerMissingNewDependency;
	}

	public int getPenaltyPerIncorrectNewDependency() {
		return penaltyPerIncorrectNewDependency;
	}

	public void setPenaltyPerIncorrectNewDependency(int penaltyPerIncorrectNewDependency) {
		this.penaltyPerIncorrectNewDependency = penaltyPerIncorrectNewDependency;
	}

	public int getPenaltyPerMissingKey() {
		return penaltyPerMissingKey;
	}

	public void setPenaltyPerMissingKey(int penaltyPerMissingKey) {
		this.penaltyPerMissingKey = penaltyPerMissingKey;
	}

	public int getPenaltyPerIncorrectKey() {
		return penaltyPerIncorrectKey;
	}

	public void setPenaltyPerIncorrectKey(int penaltyPerIncorrectKey) {
		this.penaltyPerIncorrectKey = penaltyPerIncorrectKey;
	}

	public int getPenaltyPerIncorrectNFRelation() {
		return penaltyPerIncorrectNFRelation;
	}

	public void setPenaltyPerIncorrectNFRelation(int penaltyPerIncorrectNFRelation) {
		this.penaltyPerIncorrectNFRelation = penaltyPerIncorrectNFRelation;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		NormalizationSpecification clone = (NormalizationSpecification)super.clone();
		if (this.baseRelation != null) {
			clone.baseRelation = (IdentifiedRelation)this.baseRelation.clone();
		}
		return clone;
	}

	public void setMaxLostDependencies(int maxLostDependencies){
		this.maxLostDependencies = maxLostDependencies;
	}

	public int getMaxLostDependencies(){
		return this.maxLostDependencies;
	}

	public NormalformLevel getTargetLevel() {
		return targetLevel;
	}

	public void setTargetLevel(NormalformLevel level) {
		targetLevel = level;
	}

	public boolean semanticallyEquals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof NormalizationSpecification)) {
			return false;
		}

		NormalizationSpecification spec = (NormalizationSpecification)obj;
		
		if (!(spec.getTargetLevel().equals(this.targetLevel))) {
			return false;
		}
		
		if (spec.getMaxLostDependencies() != this.maxLostDependencies) {
			return false;
		}

		if(spec.getPenaltyPerLostAttribute() != this.penaltyPerLostAttribute) {
			return false;
		}

		if(spec.getPenaltyForLossyDecomposition() != this.penaltyForLossyDecomposition) {
			return false;
		}

		if(spec.getPenaltyPerNonCanonicalDependency() != this.penaltyPerNonCanonicalDependency) {
			return false;
		}

		if(spec.getPenaltyPerTrivialDependency() != this.penaltyPerTrivialDependency) {
			return false;
		}

		if(spec.getPenaltyPerExtraneousAttributeInDependencies() != this.penaltyPerExtraneousAttributeInDependencies) {
			return false;
		}

		if(spec.getPenaltyPerRedundantDependency() != this.penaltyPerRedundantDependency) {
			return false;
		}

		if(spec.getPenaltyPerExcessiveLostDependency() != this.penaltyPerExcessiveLostDependency) {
			return false;
		}

		if(spec.getPenaltyPerMissingNewDependency() != this.penaltyPerMissingNewDependency) {
			return false;
		}

		if(spec.getPenaltyPerIncorrectNewDependency() != this.penaltyPerIncorrectNewDependency) {
			return false;
		}

		if(spec.getPenaltyPerMissingKey() != this.penaltyPerMissingKey) {
			return false;
		}

		if(spec.getPenaltyPerIncorrectKey() != this.penaltyPerIncorrectKey) {
			return false;
		}

		if(spec.getPenaltyPerIncorrectNFRelation() != this.penaltyPerIncorrectNFRelation) {
			return false;
		}

        return super.semanticallyEquals(spec);
    }

	@Override
	public String toString(){
        return "MAX LOST DEPENDENCIES: " + this.maxLostDependencies + "\n" +
				"TARGET LEVEL: " + this.targetLevel + "\n" +
				"BASE RELATION:\n" + this.baseRelation + "\n";
	}
}
