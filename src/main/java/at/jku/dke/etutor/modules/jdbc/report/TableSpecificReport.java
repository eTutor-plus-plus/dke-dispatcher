package at.jku.dke.etutor.modules.jdbc.report;

import at.jku.dke.etutor.core.evaluation.DefaultReport;
import at.jku.dke.etutor.core.evaluation.Report;

import java.util.Iterator;
import java.util.Vector;

public class TableSpecificReport extends DefaultReport implements Report {

    private String tableName;
    private Vector subReports;

    public TableSpecificReport() {
        super();

        this.tableName = null;
        this.subReports = new Vector();
    }

    public void setTableName(String tableName){
        this.tableName = tableName;
    }

    public String getTableName(){
        return this.tableName;
    }

    public void addSubReport(DefaultReport report) {
        this.subReports.add(report);
    }

    public Iterator iterSubReports(){
        return this.subReports.iterator();
    }

    public Vector getSubReports(){
        return (Vector)this.subReports.clone();
    }
}
