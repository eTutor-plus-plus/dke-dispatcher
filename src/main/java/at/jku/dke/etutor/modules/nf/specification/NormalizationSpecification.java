package at.jku.dke.etutor.modules.nf.specification;

import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;

import java.io.Serial;

public class NormalizationSpecification extends NFSpecification implements Cloneable {

	private int pointsDeductedForMissingAttribute;

	private int pointsDeductedForAdditionalAttribute;

	private int pointsDeductedForMissingDependency;

	private int pointsDeductedForAdditionalDependency;

	private int pointsDeductedForMissingKey;

	private int pointsDeductedForAdditionalKey;

	@Serial
	private static final long serialVersionUID = -8591463798404619419L;
	
	protected int maxLostDependencies;
	protected NormalformLevel targetLevel;

	public NormalizationSpecification() {
		super(new IdentifiedRelation());
		this.maxLostDependencies = 0;
		this.targetLevel = NormalformLevel.THIRD;
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

	public int getPointsDeductedForMissingAttribute() {
		return pointsDeductedForMissingAttribute;
	}

	public void setPointsDeductedForMissingAttribute(int pointsDeductedForMissingAttribute) {
		this.pointsDeductedForMissingAttribute = pointsDeductedForMissingAttribute;
	}

	public int getPointsDeductedForAdditionalAttribute() {
		return pointsDeductedForAdditionalAttribute;
	}

	public void setPointsDeductedForAdditionalAttribute(int pointsDeductedForAdditionalAttribute) {
		this.pointsDeductedForAdditionalAttribute = pointsDeductedForAdditionalAttribute;
	}

	public int getPointsDeductedForMissingDependency() {
		return pointsDeductedForMissingDependency;
	}

	public void setPointsDeductedForMissingDependency(int pointsDeductedForMissingDependency) {
		this.pointsDeductedForMissingDependency = pointsDeductedForMissingDependency;
	}

	public int getPointsDeductedForAdditionalDependency() {
		return pointsDeductedForAdditionalDependency;
	}

	public void setPointsDeductedForAdditionalDependency(int pointsDeductedForAdditionalDependency) {
		this.pointsDeductedForAdditionalDependency = pointsDeductedForAdditionalDependency;
	}

	public int getPointsDeductedForMissingKey() {
		return pointsDeductedForMissingKey;
	}

	public void setPointsDeductedForMissingKey(int pointsDeductedForMissingKey) {
		this.pointsDeductedForMissingKey = pointsDeductedForMissingKey;
	}

	public int getPointsDeductedForAdditionalKey() {
		return pointsDeductedForAdditionalKey;
	}

	public void setPointsDeductedForAdditionalKey(int pointsDeductedForAdditionalKey) {
		this.pointsDeductedForAdditionalKey = pointsDeductedForAdditionalKey;
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
