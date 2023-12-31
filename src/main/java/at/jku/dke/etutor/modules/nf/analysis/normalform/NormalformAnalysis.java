package at.jku.dke.etutor.modules.nf.analysis.normalform;

import at.jku.dke.etutor.modules.nf.analysis.NFAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;

import java.util.Vector;

public class NormalformAnalysis extends NFAnalysis {


	private final Vector<FirstNormalformViolation> firstNormalFormViolations;
	private final Vector<SecondNormalformViolation> secondNormalFormViolations;
	private final Vector<ThirdNormalformViolation> thirdNormalFormViolations;
	private final Vector<BoyceCoddNormalformViolation> boyceCoddNormalFormViolations;

	private NormalformLevel overallNormalformLevel;

	public NormalformAnalysis() {
		super();
		this.thirdNormalFormViolations = new Vector<>();
		this.firstNormalFormViolations = new Vector<>();
		this.secondNormalFormViolations = new Vector<>();
		this.boyceCoddNormalFormViolations = new Vector<>();
		this.overallNormalformLevel = NormalformLevel.FIRST;
	}
	
	public NormalformLevel getViolatedNormalformLevel(FunctionalDependency dependency) {
		if(firstNormalFormViolations.stream().anyMatch(vio -> vio.getFunctionalDependency().equals(dependency))) {
			return NormalformLevel.FIRST;
		}

		if(secondNormalFormViolations.stream().anyMatch(vio -> vio.getFunctionalDependency().equals(dependency))) {
			return NormalformLevel.SECOND;
		}

		if(thirdNormalFormViolations.stream().anyMatch(vio -> vio.getFunctionalDependency().equals(dependency))) {
			return NormalformLevel.THIRD;
		}

		if(boyceCoddNormalFormViolations.stream().anyMatch(vio -> vio.getFunctionalDependency().equals(dependency))) {
			return NormalformLevel.BOYCE_CODD;
		}

		return null;
	}
	
	public void setOverallNormalformLevel(NormalformLevel level){
		this.overallNormalformLevel = level;
	}
	
	public NormalformLevel getOverallNormalformLevel(){
		return this.overallNormalformLevel;
	}

	public void addFirstNormalformViolation(FirstNormalformViolation violation){
		this.firstNormalFormViolations.add(violation);
	}

	public Vector<FirstNormalformViolation> getFirstNormalformViolations(){
		return (Vector<FirstNormalformViolation>)this.firstNormalFormViolations.clone();
	}

	public void addSecondNormalformViolation(SecondNormalformViolation violation){
		this.secondNormalFormViolations.add(violation);
	}

	public Vector<SecondNormalformViolation> getSecondNormalformViolations(){
		return (Vector<SecondNormalformViolation>)this.secondNormalFormViolations.clone();
	}

	public void addThirdNormalformViolation(ThirdNormalformViolation violation){
		this.thirdNormalFormViolations.add(violation);
	}

	public Vector<ThirdNormalformViolation> getThirdNormalformViolations(){
		return (Vector<ThirdNormalformViolation>)this.thirdNormalFormViolations.clone();
	}

	public void addBoyceCoddNormalformViolation(BoyceCoddNormalformViolation violation){
		this.boyceCoddNormalFormViolations.add(violation);
	}

	public Vector<BoyceCoddNormalformViolation> getBoyceCoddNormalformViolations(){
		return (Vector<BoyceCoddNormalformViolation>)this.boyceCoddNormalFormViolations.clone();
	}

}
