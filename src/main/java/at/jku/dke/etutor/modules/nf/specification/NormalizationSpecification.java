package at.jku.dke.etutor.modules.nf.specification;

import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;

import java.io.Serial;

public class NormalizationSpecification extends NFSpecification implements Cloneable {

	@Serial
	private static final long serialVersionUID = -8591463798404619419L;

	// Start of point deduction variables
	/**
	 * (a in the grading schema V3)
	 */
	private int penaltyPerLostAttribute;

	/**
	 * (b in the grading schema V3)
	 */
	private int penaltyForLossyDecomposition;

	/**
	 * (c in the grading schema V3)
	 */
	private int penaltyPerNonCanonicalDependency;

	/**
	 * (d in the grading schema V3)
	 */
	private int penaltyPerTrivialDependency;

	/**
	 * (e in the grading schema V3)
	 */
	private int penaltyPerExtraneousAttributeInDependencies;

	/**
	 * (f in the grading schema V3)
	 */
	private int penaltyPerRedundantDependency;

	/**
	 * (g in the grading schema V3)
	 */
	private int penaltyPerExcessiveLostDependency;

	/**
	 * Points deducted for every functional dependency that would have to exist in a resulting relation due to the
	 * decomposition process but does not
	 *
	 * (h in the grading schema V3)
	 */
	private int penaltyPerMissedNewDependency;

	/**
	 * Points deducted for every functional dependency that exists in a resulting relation, even though it is not
	 * supposed to (due to the decomposition process, specifically the RBR algorithm).
	 *
	 * (i in the grading schema V3)
	 */
	private int penaltyPerWrongNewDependency;

	/**
	 * (j in the grading schema V3)
	 */
	private int penaltyPerMissingKey;

	/**
	 * (k in the grading schema V3)
	 */
	private int penaltyPerWrongKey;

	/**
	 * (l in the grading schema V3)
	 */
	private int penaltyPerWrongNFRelation;
	// End of point deduction variables

	protected int maxLostDependencies;
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

	public int getPenaltyPerMissedNewDependency() {
		return penaltyPerMissedNewDependency;
	}

	public void setPenaltyPerMissedNewDependency(int penaltyPerMissedNewDependency) {
		this.penaltyPerMissedNewDependency = penaltyPerMissedNewDependency;
	}

	public int getPenaltyPerWrongNewDependency() {
		return penaltyPerWrongNewDependency;
	}

	public void setPenaltyPerWrongNewDependency(int penaltyPerWrongNewDependency) {
		this.penaltyPerWrongNewDependency = penaltyPerWrongNewDependency;
	}

	public int getPenaltyPerMissingKey() {
		return penaltyPerMissingKey;
	}

	public void setPenaltyPerMissingKey(int penaltyPerMissingKey) {
		this.penaltyPerMissingKey = penaltyPerMissingKey;
	}

	public int getPenaltyPerWrongKey() {
		return penaltyPerWrongKey;
	}

	public void setPenaltyPerWrongKey(int penaltyPerWrongKey) {
		this.penaltyPerWrongKey = penaltyPerWrongKey;
	}

	public int getPenaltyPerWrongNFRelation() {
		return penaltyPerWrongNFRelation;
	}

	public void setPenaltyPerWrongNFRelation(int penaltyPerWrongNFRelation) {
		this.penaltyPerWrongNFRelation = penaltyPerWrongNFRelation;
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
		NormalizationSpecification spec;
		
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof NormalizationSpecification)) {
			return false;
		}

		spec = (NormalizationSpecification)obj;
		
		if (!(spec.getTargetLevel().equals(this.targetLevel))){
			return false;
		}
		
		if (spec.getMaxLostDependencies() != this.maxLostDependencies){
			return false;
		}

        return spec.getBaseRelation().semanticallyEquals(this.getBaseRelation());
    }

	@Override
	public String toString(){
        return "MAX LOST DEPENDENCIES: " + this.maxLostDependencies + "\n" +
				"TARGET LEVEL: " + this.targetLevel + "\n" +
				"BASE RELATION:\n" + this.baseRelation + "\n";
	}
}
