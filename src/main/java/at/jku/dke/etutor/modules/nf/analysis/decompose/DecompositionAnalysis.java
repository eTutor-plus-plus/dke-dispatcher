package at.jku.dke.etutor.modules.nf.analysis.decompose;

import at.jku.dke.etutor.modules.nf.analysis.NFAnalysis;

import java.text.Collator;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class DecompositionAnalysis extends NFAnalysis {

	private final Set<String> missingAttributes;

	public DecompositionAnalysis() {
		super();
		this.missingAttributes = new TreeSet<>(Collator.getInstance());
	}

	public void addMissingAttribute(String attribute){
		this.missingAttributes.add(attribute);
	}
	
	public void setMissingAttributes(Collection<String> attributes){
		this.missingAttributes.clear();
		this.missingAttributes.addAll(attributes);
	}

	public Set<String> getMissingAttributes(){
		return new TreeSet<>(this.missingAttributes);
	}
	
	public void removeAllMissingAttributes(Collection<String> attributes){
		this.missingAttributes.removeAll(attributes);
	}
}
