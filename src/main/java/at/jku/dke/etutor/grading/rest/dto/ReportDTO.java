package at.jku.dke.etutor.grading.rest.dto;

import at.jku.dke.etutor.core.evaluation.DefaultReport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The report dto as entity
 */
@Entity
@Table(name= "report")
public class ReportDTO {
    /**
     * The submission id identifying the report
     */
    @Id
    private String submissionId;
    /**
     * The hint
     */
    private String hint;
    /**
     * The error message
     */
    private String error;
    /**
     * The description
     */
    @Column(length = 8192)
    private String description;

    public ReportDTO(){

    }

    public ReportDTO(String submissionId, DefaultReport report){
        this.hint = report.getHint();
        this.error = report.getError();
        this.description = report.getDescription();
        this.submissionId = submissionId;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
