package at.jku.dke.etutor.modules.nf.analysis;

import etutor.core.evaluation.Analysis;
import etutor.core.evaluation.DefaultAnalysis;
import etutor.modules.rdbd.model.FunctionalDependency;

import java.util.HashMap;
import java.util.Vector;

public class ExtraneousAttributesAnalysis extends DefaultAnalysis implements Analysis {

	private HashMap extraneousAttributes;

	public ExtraneousAttributesAnalysis() {
		super();
		this.extraneousAttributes = new HashMap();
	}

	public HashMap getExtraneousAttributes() {
		return (HashMap)this.extraneousAttributes.clone();
	}
	
	public Vector getExtraneousAttributes(FunctionalDependency dependency){
		return (Vector)this.extraneousAttributes.get(dependency);
	}

	public void addExtraneousAttribute(FunctionalDependency dependency, String attribute){
		if (!this.extraneousAttributes.containsKey(dependency)){
			this.extraneousAttributes.put(dependency, new Vector());
		}

		((Vector)this.extraneousAttributes.get(dependency)).add(attribute);
	}
}
