package at.jku.dke.etutor.modules.nf;

import etutor.modules.rdbd.model.NormalformLevel;
import etutor.modules.rdbd.ui.IdentifiedRelation;

import java.io.Serializable;

public class NormalizationSpecification implements Serializable, Cloneable, RDBDSpecification{

	static final long serialVersionUID = -8591463798404619419L;
	
	private int maxLostDependencies;
	private NormalformLevel targetLevel;
	private IdentifiedRelation baseRelation;

	public NormalizationSpecification() {
		super();
		this.maxLostDependencies = 0;
		this.targetLevel = NormalformLevel.THIRD;
		this.baseRelation = new IdentifiedRelation();
 	}

	public Object clone() throws CloneNotSupportedException {
		NormalizationSpecification clone = (NormalizationSpecification)super.clone();
		if (this.baseRelation != null) {
			clone.baseRelation = (IdentifiedRelation)this.baseRelation.clone();
		}
		return clone;
	}
	
	public IdentifiedRelation getBaseRelation() {
		return baseRelation;
	}

	public void setBaseRelation(IdentifiedRelation relation) {
		baseRelation = relation;
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
		
		if (!(spec.getMaxLostDependencies() == this.maxLostDependencies)){
			return false;
		}
		
		if (!(spec.getBaseRelation().semanticallyEquals(this.getBaseRelation()))){
			return false;
		}

		return true;
	}

	public String toString(){
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("MAX LOST DEPENDENCIES: " + this.maxLostDependencies + "\n");
		buffer.append("TARGET LEVEL: " + this.targetLevel + "\n");
		buffer.append("BASE RELATION:\n" + this.baseRelation + "\n");
		
		return buffer.toString();
	}
}
