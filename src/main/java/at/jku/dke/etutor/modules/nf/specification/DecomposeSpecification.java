package at.jku.dke.etutor.modules.nf.specification;

import java.io.Serial;

public class DecomposeSpecification extends NormalizationSpecification {

	@Serial
	private static final long serialVersionUID = 1411810798506094612L;

	public DecomposeSpecification() {
		super();
	}

	public boolean obligatoryDependenciesPreservation() {
		return this.maxLostDependencies == 0;
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
		
		if (spec.getMaxLostDependencies() != this.maxLostDependencies){
			return false;
		}

        return spec.getBaseRelation().semanticallyEquals(this.getBaseRelation());
    }
}
