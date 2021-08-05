package at.jku.dke.etutor.core.evaluation;

import java.io.Serializable;

/**
 * Class implementing the required Analysis interface that can be extended by the modules
 */
public class DefaultAnalysis implements Analysis {
	/**
	 * The student´s submission
	 */
	private Serializable submission;
	/**
	 * Boolean determining wheter the submission suits the solution
	 */
	private boolean submissionSuitsSolution;

	/**
	 * Default Constructor
	 */
	public DefaultAnalysis(){
		this.submission = null;
		this.submissionSuitsSolution = false;
	}

	/**
	 * Sets the submission
	 * @param submission the submission
	 */
	public void setSubmission(Serializable submission){
		this.submission = submission;
	}

	/**
	 * Returns the submission
	 * @return the submission
	 */
	public Serializable getSubmission(){
		return this.submission;
	}

	/**
	 * Returns submissionSuitsSolution
	 * @return the boolean value
	 */
	public boolean submissionSuitsSolution(){
		return this.submissionSuitsSolution;
	}

	/**
	 * Sets submissionSuitsSolution
	 * @param submissionSuitsSolution the boolean value
	 */
	public void setSubmissionSuitsSolution(boolean submissionSuitsSolution){
		this.submissionSuitsSolution = submissionSuitsSolution;
	}
}
