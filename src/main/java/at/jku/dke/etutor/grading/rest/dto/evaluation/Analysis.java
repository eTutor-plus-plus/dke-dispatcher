package at.jku.dke.etutor.grading.rest.dto.evaluation;

import java.io.Serializable;

public interface Analysis extends Serializable{
    public void setSubmission(Serializable submission);

    public Serializable getSubmission();

    public boolean submissionSuitsSolution();

    public void setSubmissionSuitsSolution(boolean b);
}
