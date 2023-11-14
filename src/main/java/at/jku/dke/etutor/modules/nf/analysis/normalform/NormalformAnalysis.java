package at.jku.dke.etutor.modules.nf.analysis.normalform;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;

import java.util.Iterator;
import java.util.Vector;

public class NormalformAnalysis extends DefaultAnalysis implements Analysis {


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
	
	public NormalformLevel getViolatedNormalformLevel(FunctionalDependency dependency){
		Iterator<? extends NormalformViolation> iter;
		NormalformLevel level = null;
		NormalformViolation violation;
		
		if (level == null) {
			iter = this.firstNormalFormViolations.iterator();
			while((iter.hasNext()) && (level == null)){
				violation = iter.next();
				if (violation.getFunctionalDependency().equals(dependency)){
					level = NormalformLevel.FIRST;
				}
			}
		}

		if (level == null) {
			iter = this.secondNormalFormViolations.iterator();
			while((iter.hasNext()) && (level == null)){
				violation = iter.next();
				if (violation.getFunctionalDependency().equals(dependency)){
					level = NormalformLevel.SECOND;
				}
			}
		}

		if (level == null) {
			iter = this.thirdNormalFormViolations.iterator();
			while((iter.hasNext()) && (level == null)){
				violation = iter.next();
				if (violation.getFunctionalDependency().equals(dependency)){
					level = NormalformLevel.THIRD;
				}
			}
		}
		
		if (level == null) {
			iter = this.boyceCoddNormalFormViolations.iterator();
			while((iter.hasNext()) && (level == null)){
				violation = iter.next();
				if (violation.getFunctionalDependency().equals(dependency)){
					level = NormalformLevel.BOYCE_CODD;
				}
			}
		}
		
		return level;
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
	
	public Iterator<FirstNormalformViolation> iterFirstNormalformViolations(){
		return this.firstNormalFormViolations.iterator();
	}
	
	public Vector<FirstNormalformViolation> getFirstNormalformViolations(){
		return (Vector<FirstNormalformViolation>)this.firstNormalFormViolations.clone();
	}

	public void addSecondNormalformViolation(SecondNormalformViolation violation){
		this.secondNormalFormViolations.add(violation);
	}
	
	public Iterator<SecondNormalformViolation> iterSecondNormalformViolations(){
		return this.secondNormalFormViolations.iterator();
	}
	
	public Vector<SecondNormalformViolation> getSecondNormalformViolations(){
		return (Vector<SecondNormalformViolation>)this.secondNormalFormViolations.clone();
	}

	public void addThirdNormalformViolation(ThirdNormalformViolation violation){
		this.thirdNormalFormViolations.add(violation);
	}
	
	public Iterator<ThirdNormalformViolation> iterThirdNormalformViolations(){
		return this.thirdNormalFormViolations.iterator();
	}
	
	public Vector<ThirdNormalformViolation> getThirdNormalformViolations(){
		return (Vector<ThirdNormalformViolation>)this.thirdNormalFormViolations.clone();
	}

	public void addBoyceCottNormalformViolation(BoyceCoddNormalformViolation violation){
		this.boyceCoddNormalFormViolations.add(violation);
	}
	
	public Iterator<BoyceCoddNormalformViolation> iterBoyceCoddNormalformViolations(){
		return this.boyceCoddNormalFormViolations.iterator();
	}
	
	public Vector<BoyceCoddNormalformViolation> getBoyceCoddNormalformViolations(){
		return (Vector<BoyceCoddNormalformViolation>)this.boyceCoddNormalFormViolations.clone();
	}

}