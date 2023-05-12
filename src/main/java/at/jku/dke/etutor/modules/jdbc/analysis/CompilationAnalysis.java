package at.jku.dke.etutor.modules.jdbc.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;

public class CompilationAnalysis extends DefaultAnalysis implements Analysis {

    private String errorMessage;

    public CompilationAnalysis() {
        super();
        this.errorMessage = null;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
