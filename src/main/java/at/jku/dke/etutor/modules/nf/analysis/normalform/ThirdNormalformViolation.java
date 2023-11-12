package at.jku.dke.etutor.modules.nf.analysis.normalform;

import java.util.Iterator;
import java.util.Vector;

public class ThirdNormalformViolation extends NormalformViolation {

	private Vector nonPrimRHSAttributes;

	public ThirdNormalformViolation() {
		super();
		this.nonPrimRHSAttributes = new Vector();
	}

	public void addNonPrimRHSAttribute(String attribute){
		this.nonPrimRHSAttributes.add(attribute);
	}
	
	public Iterator iterNonPrimRHSAttributes(){
		return this.nonPrimRHSAttributes.iterator();
	}
	
	public Vector getNonPrimRHSAttributes(){
		return (Vector)this.nonPrimRHSAttributes.clone();
	}
	
	public int nonPrimRHSAttributesCount(){
		return this.nonPrimRHSAttributes.size();
	}
}
