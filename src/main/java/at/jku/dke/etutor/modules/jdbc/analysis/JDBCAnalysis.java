package at.jku.dke.etutor.modules.jdbc.analysis;


import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import java.io.Serializable;


public class JDBCAnalysis extends DefaultAnalysis implements Serializable {
    private DBAnalysis dbAnalysis;
    private JDBCRuntimeAnalysis runtimeAnalysis;
    private CompilationAnalysis compilationAnalysis;

    public JDBCAnalysis() {
        super();
        this.dbAnalysis = null;
        this.runtimeAnalysis = null;
        this.compilationAnalysis = null;
    }

    public CompilationAnalysis getCompilationAnalysis() {
        return compilationAnalysis;
    }

    public DBAnalysis getDBAnalysis() {
        return dbAnalysis;
    }

    public JDBCRuntimeAnalysis getRuntimeAnalysis() {
        return runtimeAnalysis;
    }

    public void setCompilationAnalysis(CompilationAnalysis analysis) {
        compilationAnalysis = analysis;
    }

    public void setDBAnalysis(DBAnalysis analysis) {
        dbAnalysis = analysis;
    }

    public void setRuntimeAnalysis(JDBCRuntimeAnalysis analysis) {
        runtimeAnalysis = analysis;
    }
}
