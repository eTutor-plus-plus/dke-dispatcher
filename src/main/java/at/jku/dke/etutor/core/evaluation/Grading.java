package at.jku.dke.etutor.core.evaluation;

import java.io.Serializable;

/**
 * Interface defining methods for a grading of a submission
 */
public interface Grading extends Serializable {

    /**
     * Returns the achieved points
     * @return the points
     */
    public double getPoints();

    /**
     * Sets the points
     * @param points the points
     */
    public void setPoints(double points);

    /**
     * Returns the max points
     * @return the max points
     */
    public double getMaxPoints();

    /**
     * Sets the max points
     * @param maxPoints the max points
     */
    public void setMaxPoints(double maxPoints);
}
