package at.jku.dke.etutor.modules.nf.analysis.decompose;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;

import java.text.Collator;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class DecompositionAnalysis extends DefaultAnalysis implements Analysis {

	private TreeSet missingAttributes; 

	public DecompositionAnalysis() {
		super();
		this.missingAttributes = new TreeSet(Collator.getInstance());
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
}
