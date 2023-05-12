package at.jku.dke.etutor.modules.jdbc.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;

public class TableAnalysis extends DefaultAnalysis implements Analysis {

    private String tableName;
    private JDBCTuplesAnalysis tuplesAnalysis;
    private JDBCColumnsAnalysis columnsAnalysis;

    public TableAnalysis() {
        super();
        this.tableName = null;
        this.tuplesAnalysis = null;
        this.columnsAnalysis = null;
    }

    public void setTableName(String name){
        this.tableName = name;
    }

    public String getTableName(){
        return this.tableName;
    }

    public JDBCColumnsAnalysis getColumnsAnalysis() {
        return columnsAnalysis;
    }

    public JDBCTuplesAnalysis getTuplesAnalysis() {
        return tuplesAnalysis;
    }

    public void setColumnsAnalysis(JDBCColumnsAnalysis analysis) {
        columnsAnalysis = analysis;
    }

    public void setTuplesAnalysis(JDBCTuplesAnalysis analysis) {
        tuplesAnalysis = analysis;
    }
}
