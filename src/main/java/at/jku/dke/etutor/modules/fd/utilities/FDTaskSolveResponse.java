package at.jku.dke.etutor.modules.fd.utilities;

import java.util.List;

public class FDTaskSolveResponse {
    private String id;
    private boolean solved;
    private List<FDHint> hints;
    private double points;


    public FDTaskSolveResponse(String id) {
        this.id=id;
        this.solved=true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public List<FDHint> getHints() {
        return hints;
    }

    public void setHints(List<FDHint> hints) {
        this.hints = hints;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "FDTaskSolveResponse{" +
                "id='" + id + '\'' +
                ", solved=" + solved +
                ", hints=" + hints +
                ", points=" + points +
                '}';
    }
}
