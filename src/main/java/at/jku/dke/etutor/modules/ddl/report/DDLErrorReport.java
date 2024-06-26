package at.jku.dke.etutor.modules.ddl.report;

import java.io.Serializable;

public class DDLErrorReport implements Serializable {
    //region Fields
    private String error;
    private String description;
    //endregion

    public DDLErrorReport() {
        this.error = "";
        this.description = "";
    }

    //region Getter/Setter
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
    //endregion
}
