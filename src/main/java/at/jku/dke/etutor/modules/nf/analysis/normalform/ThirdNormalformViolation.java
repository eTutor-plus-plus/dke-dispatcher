package at.jku.dke.etutor.modules.nf.analysis.normalform;

import java.util.LinkedList;
import java.util.List;

public class ThirdNormalformViolation extends NormalformViolation {

	private final List<String> nonPrimRHSAttributes;

	public ThirdNormalformViolation() {
		this.nonPrimRHSAttributes = new LinkedList<>();
	}

	public void addNonPrimeRHSAttribute(String attribute) {
		this.nonPrimRHSAttributes.add(attribute);
	}

	public List<String> getNonPrimRHSAttributes() {
		return new LinkedList<>(this.nonPrimRHSAttributes);
	}
	
	public int nonPrimRHSAttributesCount() {
		return this.nonPrimRHSAttributes.size();
	}
}
