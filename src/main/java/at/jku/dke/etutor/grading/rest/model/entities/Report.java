package at.jku.dke.etutor.grading.rest.model.entities;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultReport;
import at.jku.dke.etutor.core.evaluation.Grading;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * The DTO representing a report generated in the course of the evaluation see {@link at.jku.dke.etutor.core.evaluation.Evaluator#report(Analysis, Grading, Map, Map, Locale)}
 */
@Entity
@Table(name= "report")
public class Report {
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

    public Report(){

    }

    public Report(String submissionId, DefaultReport report){
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Report report = (Report) o;
        return submissionId != null && Objects.equals(submissionId, report.submissionId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
