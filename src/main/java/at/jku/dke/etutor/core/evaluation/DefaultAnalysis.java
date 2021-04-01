package at.jku.dke.etutor.core.evaluation;

import java.io.Serializable;

public class DefaultAnalysis implements Analysis {

	private Serializable submission;
	private boolean submissionSuitsSolution;

	public DefaultAnalysis(){
		this.submission = null;
		this.submissionSuitsSolution = false;
	}

	public void setSubmission(Serializable submission){
		this.submission = submission;
	}
	
	public Serializable getSubmission(){
		return this.submission;
	}
	
	public boolean submissionSuitsSolution(){
		return this.submissionSuitsSolution;
	}
	
	public void setSubmissionSuitsSolution(boolean submissionSuitsSolution){
		this.submissionSuitsSolution = submissionSuitsSolution;
	}
}
