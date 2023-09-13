package at.jku.dke.etutor.modules.fd.utilities;

import java.util.List;

public class FDTaskSolveResponse {
    private String id;
    private boolean solved;
    private List<FDHint> hint;
    private float percentage;

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

    public List<FDHint> getHint() {
        return hint;
    }

    public void setHint(List<FDHint> hint) {
        this.hint = hint;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "FDTaskSolveResponse{" +
                "id='" + id + '\'' +
                ", solved=" + solved +
                ", hint=" + hint +
                ", percentage=" + percentage +
                '}';
    }
}
