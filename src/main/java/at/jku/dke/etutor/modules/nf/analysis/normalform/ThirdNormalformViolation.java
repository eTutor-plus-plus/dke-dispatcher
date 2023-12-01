package at.jku.dke.etutor.modules.nf.analysis.normalform;

import java.util.Vector;

public class ThirdNormalformViolation extends NormalformViolation {

	private final Vector<String> nonPrimRHSAttributes;

	public ThirdNormalformViolation() {
		this.nonPrimRHSAttributes = new Vector<>();
	}

	public void addNonPrimeRHSAttribute(String attribute){
		this.nonPrimRHSAttributes.add(attribute);
	}

	public Vector<String> getNonPrimRHSAttributes(){
		return (Vector<String>)this.nonPrimRHSAttributes.clone();
	}
	
	public int nonPrimRHSAttributesCount(){
		return this.nonPrimRHSAttributes.size();
	}
}
