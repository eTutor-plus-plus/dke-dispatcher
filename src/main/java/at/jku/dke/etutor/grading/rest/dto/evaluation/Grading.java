package at.jku.dke.etutor.grading.rest.dto.evaluation;

import java.io.Serializable;

public interface Grading extends Serializable {

    public double getPoints();

    public void setPoints(double points);


    public double getMaxPoints();

    public void setMaxPoints(double maxPoints);
}
