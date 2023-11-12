package at.jku.dke.etutor.modules.nf.analysis.normalform;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;

import java.util.Iterator;
import java.util.Vector;

public class NormalformAnalysis extends DefaultAnalysis implements Analysis {


	private Vector firstNormalFormViolations;
	private Vector secondNormalFormViolations;
	private Vector thirdNormalFormViolations;
	private Vector boyceCottNormalFormViolations;

	private NormalformLevel overallNormalformLevel;

	public NormalformAnalysis() {
		super();
		this.thirdNormalFormViolations = new Vector();
		this.firstNormalFormViolations = new Vector();
		this.secondNormalFormViolations = new Vector();
		this.boyceCottNormalFormViolations = new Vector();
		this.overallNormalformLevel = NormalformLevel.FIRST;
	}
	
	public NormalformLevel getViolatedNormalformLevel(FunctionalDependency dependency){
		Iterator iter;
		NormalformLevel level = null;
		NormalformViolation violation;
		
		if (level == null) {
			iter = this.firstNormalFormViolations.iterator();
			while((iter.hasNext()) && (level == null)){
				violation = (NormalformViolation)iter.next();
				if (violation.getFunctionalDependency().equals(dependency)){
					level = NormalformLevel.FIRST;
				}
			}
		}

		if (level == null) {
			iter = this.secondNormalFormViolations.iterator();
			while((iter.hasNext()) && (level == null)){
				violation = (NormalformViolation)iter.next();
				if (violation.getFunctionalDependency().equals(dependency)){
					level = NormalformLevel.SECOND;
				}
			}
		}

		if (level == null) {
			iter = this.thirdNormalFormViolations.iterator();
			while((iter.hasNext()) && (level == null)){
				violation = (NormalformViolation)iter.next();
				if (violation.getFunctionalDependency().equals(dependency)){
					level = NormalformLevel.THIRD;
				}
			}
		}
		
		if (level == null) {
			iter = this.boyceCottNormalFormViolations.iterator();
			while((iter.hasNext()) && (level == null)){
				violation = (NormalformViolation)iter.next();
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
	
	public Iterator iterFirstNormalformViolations(){
		return this.firstNormalFormViolations.iterator();
	}
	
	public Vector getFirstNormalformViolations(){
		return (Vector)this.firstNormalFormViolations.clone();
	}

	public void addSecondNormalformViolation(SecondNormalformViolation violation){
		this.secondNormalFormViolations.add(violation);
	}
	
	public Iterator iterSecondNormalformViolations(){
		return this.secondNormalFormViolations.iterator();
	}
	
	public Vector getSecondNormalformViolations(){
		return (Vector)this.secondNormalFormViolations.clone();
	}

	public void addThirdNormalformViolation(ThirdNormalformViolation violation){
		this.thirdNormalFormViolations.add(violation);
	}
	
	public Iterator iterThirdNormalformViolations(){
		return this.thirdNormalFormViolations.iterator();
	}
	
	public Vector getThirdNormalformViolations(){
		return (Vector)this.thirdNormalFormViolations.clone();
	}

	public void addBoyceCottNormalformViolation(BoyceCoddNormalformViolation violation){
		this.boyceCottNormalFormViolations.add(violation);
	}
	
	public Iterator iterBoyceCottNormalformViolations(){
		return this.boyceCottNormalFormViolations.iterator();
	}
	
	public Vector getBoyceCottNormalformViolations(){
		return (Vector)this.boyceCottNormalFormViolations.clone();
	}

}
