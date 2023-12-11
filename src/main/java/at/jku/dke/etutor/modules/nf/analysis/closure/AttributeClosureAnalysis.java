package at.jku.dke.etutor.modules.nf.analysis.closure;

import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;

import java.text.Collator;
import java.util.Collection;
import java.util.TreeSet;

public class AttributeClosureAnalysis extends DefaultAnalysis {

	private final TreeSet<String> missingAttributes;
	private final TreeSet<String> additionalAttributes;

	public AttributeClosureAnalysis() {
		super();
		this.missingAttributes = new TreeSet<>(Collator.getInstance());
		this.additionalAttributes = new TreeSet<>(Collator.getInstance());
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

	public void addAdditionalAttribute(String attribute){
		this.additionalAttributes.add(attribute);
	}
	
	public void setAdditionalAttributes(Collection<String> attributes){
		this.additionalAttributes.clear();
		this.additionalAttributes.addAll(attributes);
	}

	public TreeSet<String> getAdditionalAttributes(){
		return (TreeSet<String>)this.additionalAttributes.clone();
	}
	
	public void removeAllAdditionalAttributes(Collection<String> attributes){
		this.additionalAttributes.removeAll(attributes);
	}
}
