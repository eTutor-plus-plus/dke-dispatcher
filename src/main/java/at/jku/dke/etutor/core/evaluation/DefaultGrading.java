package at.jku.dke.etutor.core.evaluation;

public class DefaultGrading implements Grading{

	private double points;
	private double maxPoints;

	public DefaultGrading() {
		super();
		this.points = 0;
		this.maxPoints = 1;
	}

	public double getPoints() {
		return this.points;
	}
	
	public void setPoints(double points){
		this.points = points;	
	}

	public double getMaxPoints() {
		return this.maxPoints;
	}
	
	public void setMaxPoints(double maxPoints){
		this.maxPoints = maxPoints;	
	}
}
