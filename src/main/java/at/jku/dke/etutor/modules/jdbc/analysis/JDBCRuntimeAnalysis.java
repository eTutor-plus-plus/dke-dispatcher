package at.jku.dke.etutor.modules.jdbc.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;

public class JDBCRuntimeAnalysis extends DefaultAnalysis implements Analysis {

    private String messages;
    private String runtimeException;

    public JDBCRuntimeAnalysis() {
        super();
        this.messages = null;
        this.runtimeException = null;
    }

    public String getRuntimeException() {
        return this.runtimeException;
    }

    public void setRuntimeException(String runtimeException) {
        this.runtimeException = runtimeException;
    }
    public String getMessages() {
        return this.messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }
}
