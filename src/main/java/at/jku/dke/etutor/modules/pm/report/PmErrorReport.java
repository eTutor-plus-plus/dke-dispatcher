package at.jku.dke.etutor.modules.pm.report;

import java.io.Serializable;

public class PmErrorReport implements Serializable {

    private String hint;
    private String error;
    private String description;

    public PmErrorReport(){
        super();

        this.hint = "";
        this.error = "";
        this.description = "";
    }

    public String getHint() {
        return hint;
    }
    public String getError() {
        return error;
    }
    public String getDescription() {
        return description;
    }
    public void setHint(String hint) {
        this.hint = hint;
    }
    public void setError(String error) {
        this.error = error;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
