package at.jku.dke.etutor.grading.rest.dto;

import at.jku.dke.etutor.core.evaluation.DefaultReport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "report")
public class ReportDTO {
    @Id
    private String submissionId;
    private String hint;
    private String error;
    @Column(length = 4192)
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
