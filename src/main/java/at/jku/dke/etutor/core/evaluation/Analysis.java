package at.jku.dke.etutor.core.evaluation;

import java.io.Serializable;

/**
 * Defines methods with regards to an analysis of a submission
 */
public interface Analysis extends Serializable{
    /**
     * Sets the submission
     * @param submission the submission
     */
    public void setSubmission(Serializable submission);

    /**
     * Returns the submission
     * @return the submission
     */
    public Serializable getSubmission();

    /**
     * Returns wheter the submission suits the solution
     * @return a boolean value
     */
    public boolean submissionSuitsSolution();

    /**
     * Sets wheter the submission suits the solution
     * @param b the boolean value
     */
    public void setSubmissionSuitsSolution(boolean b);
}
