package at.jku.dke.etutor.modules.nf;

import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.ui.IdentifiedRelation;

import java.io.Serializable;

public class DecomposeSpecification implements Serializable, Cloneable, RDBDSpecification {

	static final long serialVersionUID = 1411810798506094612L;	

	private int maxLostDependencies;
	private NormalformLevel targetLevel;
	private IdentifiedRelation baseRelation;

	public DecomposeSpecification() {
		super();

		this.maxLostDependencies = 0;
		this.targetLevel = NormalformLevel.THIRD;
		this.baseRelation = new IdentifiedRelation();
	}

	public Object clone() throws CloneNotSupportedException {
		DecomposeSpecification clone = (DecomposeSpecification)super.clone();
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

	public boolean obligatoryDependenciesPreservation() {
		return this.maxLostDependencies == 0;
	}

	public IdentifiedRelation getBaseRelation() {
		return baseRelation;
	}

	public void setBaseRelation(IdentifiedRelation relation) {
		baseRelation = relation;
	}

	public NormalformLevel getTargetLevel() {
		return targetLevel;
	}

	public void setTargetLevel(NormalformLevel level) {
		targetLevel = level;
	}
	
	public boolean semanticallyEquals(Object obj) {
		DecomposeSpecification spec;
		
		if (obj == null) {
			return false;
		}
		
		if (!(obj instanceof DecomposeSpecification)) {
			return false;
		}

		spec = (DecomposeSpecification)obj;
		
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
