package at.jku.dke.etutor.modules.nf.analysis.decompose;

import at.jku.dke.etutor.modules.nf.analysis.NFAnalysis;

import java.text.Collator;
import java.util.Collection;
import java.util.TreeSet;

public class DecompositionAnalysis extends NFAnalysis {

	private final TreeSet<String> missingAttributes;

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

	public TreeSet<String> getMissingAttributes(){
		return (TreeSet<String>)this.missingAttributes.clone();
	}
	
	public void removeAllMissingAttributes(Collection<String> attributes){
		this.missingAttributes.removeAll(attributes);
	}
}
