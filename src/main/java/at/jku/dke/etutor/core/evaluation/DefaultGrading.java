package at.jku.dke.etutor.core.evaluation;

/**
 * Class that implements the required Grading interface and can be extended by the modules
 */
public class DefaultGrading implements Grading{

	/**
	 * The achieved points
	 */
	private double points;
	/**
	 * The max points
	 */
	private double maxPoints;

	/**
	 * Constructor
	 */
	public DefaultGrading() {
		super();
		this.points = 0;
		this.maxPoints = 1;
	}

	/**
	 * Returns the points
	 * @return the points
	 */
	public double getPoints() {
		return this.points;
	}

	/**
	 * Sets the points
	 * @param points the points
	 */
	public void setPoints(double points){
		this.points = points;	
	}

	/**
	 * Returns the max points
	 * @return the max points
	 */
	public double getMaxPoints() {
		return this.maxPoints;
	}

	/**
	 * Sets the max points
	 * @param maxPoints the max points
	 */
	public void setMaxPoints(double maxPoints){
		this.maxPoints = maxPoints;	
	}
}
