package at.jku.dke.etutor.grading.rest.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GradingDTO {
    @Id
    private String submissionId;
    private double points;
    private double maxPoints;

    public GradingDTO(){}

    public GradingDTO(String submissionId, double points, double maxPoints){
        this.points = points;
        this.maxPoints= maxPoints;
        this.submissionId=submissionId;
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
}
