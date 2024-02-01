package at.jku.dke.etutor.modules.nf.analysis.normalform;

import at.jku.dke.etutor.modules.nf.analysis.NFAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;

import java.util.LinkedList;
import java.util.List;

public class NormalformAnalysis extends NFAnalysis {


	private final List<FirstNormalformViolation> firstNormalFormViolations;
	private final List<SecondNormalformViolation> secondNormalFormViolations;
	private final List<ThirdNormalformViolation> thirdNormalFormViolations;
	private final List<BoyceCoddNormalformViolation> boyceCoddNormalFormViolations;

	private NormalformLevel overallNormalformLevel;

	public NormalformAnalysis() {
		super();
		this.thirdNormalFormViolations = new LinkedList<>();
		this.firstNormalFormViolations = new LinkedList<>();
		this.secondNormalFormViolations = new LinkedList<>();
		this.boyceCoddNormalFormViolations = new LinkedList<>();
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

	public List<FirstNormalformViolation> getFirstNormalformViolations(){
		return new LinkedList<>(this.firstNormalFormViolations);
	}

	public void addSecondNormalformViolation(SecondNormalformViolation violation){
		this.secondNormalFormViolations.add(violation);
	}

	public List<SecondNormalformViolation> getSecondNormalformViolations(){
		return new LinkedList<>(this.secondNormalFormViolations);
	}

	public void addThirdNormalformViolation(ThirdNormalformViolation violation){
		this.thirdNormalFormViolations.add(violation);
	}

	public List<ThirdNormalformViolation> getThirdNormalformViolations(){
		return new LinkedList<>(this.thirdNormalFormViolations);
	}

	public void addBoyceCoddNormalformViolation(BoyceCoddNormalformViolation violation){
		this.boyceCoddNormalFormViolations.add(violation);
	}

	public List<BoyceCoddNormalformViolation> getBoyceCoddNormalformViolations(){
		return new LinkedList<>(this.boyceCoddNormalFormViolations);
	}

}
