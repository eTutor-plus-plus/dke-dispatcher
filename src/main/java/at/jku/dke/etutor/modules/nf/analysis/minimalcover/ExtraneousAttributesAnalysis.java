package at.jku.dke.etutor.modules.nf.analysis.minimalcover;

import at.jku.dke.etutor.modules.nf.analysis.NFAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExtraneousAttributesAnalysis extends NFAnalysis {

	private final Map<FunctionalDependency, List<String>> extraneousAttributes;

	public ExtraneousAttributesAnalysis() {
		super();
		this.extraneousAttributes = new HashMap<>();
	}

	public Map<FunctionalDependency, List<String>> getExtraneousAttributes() {
		return new HashMap<>(this.extraneousAttributes);
	}

	public void addExtraneousAttribute(FunctionalDependency dependency, String attribute){
		if (!this.extraneousAttributes.containsKey(dependency)){
			this.extraneousAttributes.put(dependency, new LinkedList<>());
		}

		this.extraneousAttributes.get(dependency).add(attribute);
	}
}
