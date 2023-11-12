package at.jku.dke.etutor.modules.nf.analysis;

import etutor.core.evaluation.Analysis;
import etutor.core.evaluation.DefaultAnalysis;

import java.text.Collator;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class AttributeClosureAnalysis extends DefaultAnalysis implements Analysis {

	private TreeSet missingAttributes;
	private TreeSet additionalAttributes;

	public AttributeClosureAnalysis() {
		super();
		this.missingAttributes = new TreeSet(Collator.getInstance());
		this.additionalAttributes = new TreeSet(Collator.getInstance());
	}

	public void addMissingAttribute(String attribute){
		this.missingAttributes.add(attribute);
	}
	
	public void setMissingAttributes(Collection attributes){
		this.missingAttributes.clear();
		this.missingAttributes.addAll(attributes);
	}
	
	public Iterator iterMissingAttributes(){
		return this.missingAttributes.iterator();
	}
	
	public TreeSet getMissingAttributes(){
		return (TreeSet)this.missingAttributes.clone();
	}
	
	public void removeAllMissingAttributes(Collection attributes){
		this.missingAttributes.removeAll(attributes);
	}

	public void addAdditionalAttribute(String attribute){
		this.additionalAttributes.add(attribute);
	}
	
	public void setAdditionalAttributes(Collection attributes){
		this.additionalAttributes.clear();
		this.additionalAttributes.addAll(attributes);
	}
	
	public Iterator iterAdditionalAttributes(){
		return this.additionalAttributes.iterator();
	}
	
	public TreeSet getAdditionalAttributes(){
		return (TreeSet)this.additionalAttributes.clone();
	}
	
	public void removeAllAdditionalAttributes(Collection attributes){
		this.additionalAttributes.removeAll(attributes);
	}
}
