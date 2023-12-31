package at.jku.dke.etutor.modules.nf.analysis.minimalcover;

import at.jku.dke.etutor.modules.nf.analysis.NFAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ExtraneousAttributesAnalysis extends NFAnalysis {

	private final Map<FunctionalDependency, Vector<String>> extraneousAttributes;

	public ExtraneousAttributesAnalysis() {
		super();
		this.extraneousAttributes = new HashMap<>();
	}

	public Map<FunctionalDependency, Vector<String>> getExtraneousAttributes() {
		return new HashMap<>(this.extraneousAttributes);
	}

	public void addExtraneousAttribute(FunctionalDependency dependency, String attribute){
		if (!this.extraneousAttributes.containsKey(dependency)){
			this.extraneousAttributes.put(dependency, new Vector<>());
		}

		this.extraneousAttributes.get(dependency).add(attribute);
	}
}
