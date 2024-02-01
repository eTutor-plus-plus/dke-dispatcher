package at.jku.dke.etutor.modules.nf.analysis.normalform;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

public abstract class NormalformViolation {

	private FunctionalDependency dependency;

	public void setFunctionalDependency(FunctionalDependency dependency){
		this.dependency = dependency;
	}
	
	public FunctionalDependency getFunctionalDependency(){
		return this.dependency;
	}

}
