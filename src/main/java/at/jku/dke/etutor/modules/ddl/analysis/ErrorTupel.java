package at.jku.dke.etutor.modules.ddl.analysis;

public class ErrorTupel {
    //region Fields
    String source;
    String error;
    //endregion

    public ErrorTupel(String source, String error) {
        this.source = source;
        this.error = error;
    }

    //region Getter/Setter

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    //endregion
}
