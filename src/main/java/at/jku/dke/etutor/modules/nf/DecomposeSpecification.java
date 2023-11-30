package at.jku.dke.etutor.modules.nf;

import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;

import java.io.Serializable;

public class DecomposeSpecification implements Serializable, Cloneable, RDBDSpecification {

	private static final long serialVersionUID = 1411810798506094612L;

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

        return spec.getBaseRelation().semanticallyEquals(this.getBaseRelation());
    }

	@Override
	public String toString(){
        return "MAX LOST DEPENDENCIES: " + this.maxLostDependencies + "\n" +
				"TARGET LEVEL: " + this.targetLevel + "\n" +
				"BASE RELATION:\n" + this.baseRelation + "\n";
	}
}
