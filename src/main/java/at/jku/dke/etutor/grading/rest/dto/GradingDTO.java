package at.jku.dke.etutor.grading.rest.dto;

import at.jku.dke.etutor.core.evaluation.Grading;

import javax.persistence.*;

/**
 * Represents a graded submission
 */
@Entity
@Table(name="grading")
public class GradingDTO {

    /**
     * The id identifying the submission
     */
    @Id
    private String submissionId;

    /**
     * The points achieved
     */
    private double points;

    /**
     * The maxPoints
     */
    private double maxPoints;

    /**
     * The result
     */
    @Column(length = 16384)
    private String result;

    /**
     * The report containing information about potential errors
     */
    @OneToOne(cascade=CascadeType.ALL)
    private ReportDTO report;

    public GradingDTO(){}

    public GradingDTO(String submissionId, double points, double maxPoints){
        this.points = points;
        this.maxPoints= maxPoints;
        this.submissionId=submissionId;
    }

    public GradingDTO(String submissionId, Grading grading){
        this.submissionId=submissionId;
        this.points = grading.getPoints();
        this.maxPoints = grading.getMaxPoints();
    }

    public ReportDTO getReport() {
        return report;
    }

    public void setReport(ReportDTO report) {
        this.report = report;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public double getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(double maxPoints) {
        this.maxPoints = maxPoints;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
