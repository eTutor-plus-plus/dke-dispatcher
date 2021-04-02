package at.jku.dke.etutor.grading.rest.dto;

public class RestGrading {
    private double points;
    private double maxPoints;

    public RestGrading(double points, double maxPoints){
        this.points = points;
        this.maxPoints= maxPoints;
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

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }
}
