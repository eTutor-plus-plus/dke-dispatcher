package at.jku.dke.etutor.modules.nf.analysis.closure;

import at.jku.dke.etutor.modules.nf.analysis.NFAnalysis;

import java.text.Collator;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class AttributeClosureAnalysis extends NFAnalysis {

	private final Set<String> missingAttributes;
	private final Set<String> additionalAttributes;

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

	public Set<String> getMissingAttributes() {
		TreeSet<String> ret = new TreeSet<>(Collator.getInstance());
		ret.addAll(this.missingAttributes);

		return ret;
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

	public Set<String> getAdditionalAttributes() {
		TreeSet<String> ret = new TreeSet<>(Collator.getInstance());
		ret.addAll(this.additionalAttributes);

		return ret;
	}
	
	public void removeAllAdditionalAttributes(Collection<String> attributes){
		this.additionalAttributes.removeAll(attributes);
	}
}
