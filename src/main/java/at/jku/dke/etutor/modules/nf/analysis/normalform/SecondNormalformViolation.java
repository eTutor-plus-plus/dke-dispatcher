package at.jku.dke.etutor.modules.nf.analysis.normalform;

import java.util.Iterator;
import java.util.Vector;

public class SecondNormalformViolation extends NormalformViolation {
	
	private final Vector<String> nonPrimRHSAttributes;
	//private Vector comprisedPartialKeys;

	public SecondNormalformViolation() {
		this.nonPrimRHSAttributes = new Vector<>();
		//this.comprisedPartialKeys = new Vector();
	}

	public void addNonPrimRHSAttribute(String attribute){
		this.nonPrimRHSAttributes.add(attribute);
	}

	public int nonPrimRHSAttributesCount(){
		return this.nonPrimRHSAttributes.size();
	}

	public Vector<String> getNonPrimRHSAttributes(){
		return (Vector<String>)this.nonPrimRHSAttributes.clone();
	}

	/*
	public void addComprisedPartialKey(Key partialKey){
		this.comprisedPartialKeys.add(partialKey);
	}
	
	public Iterator iterComprisedPartialKeys(){
		return this.comprisedPartialKeys.iterator();
	}
	
	public Vector getComprisedPartialKeys(){
		return (Vector)this.comprisedPartialKeys.clone();
	}
	
	public int comprisedPartialKeysCount(){
		return this.comprisedPartialKeys.size();
	}*/
}
