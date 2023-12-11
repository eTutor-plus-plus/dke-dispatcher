package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.HashMap;
import java.util.Vector;

public class ExtraneousAttributesAnalysis extends DefaultAnalysis {

	private final HashMap<FunctionalDependency, Vector<String>> extraneousAttributes;

	public ExtraneousAttributesAnalysis() {
		super();
		this.extraneousAttributes = new HashMap<>();
	}

	public HashMap<FunctionalDependency, Vector<String>> getExtraneousAttributes() {
		return (HashMap<FunctionalDependency, Vector<String>>)this.extraneousAttributes.clone();
	}
	
	public Vector<String> getExtraneousAttributes(FunctionalDependency dependency){
		return this.extraneousAttributes.get(dependency);
	}

	public void addExtraneousAttribute(FunctionalDependency dependency, String attribute){
		if (!this.extraneousAttributes.containsKey(dependency)){
			this.extraneousAttributes.put(dependency, new Vector<>());
		}

		this.extraneousAttributes.get(dependency).add(attribute);
	}
}
