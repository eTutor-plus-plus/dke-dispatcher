package at.jku.dke.etutor.modules.jdbc.report;

import at.jku.dke.etutor.core.evaluation.DefaultReport;
import at.jku.dke.etutor.core.evaluation.Report;

import java.util.Iterator;
import java.util.Vector;


public class JDBCReport extends DefaultReport implements Report {

    private String prologue;
    private String messages;
    private Vector generalErrorReports;
    private Vector tableSpecificErrorReports;

    public JDBCReport() {
        super();

        this.prologue = null;
        this.messages = new String();
        this.generalErrorReports = new Vector();
        this.tableSpecificErrorReports = new Vector();
    }

    public Vector getGeneralErrorReports(){
        return (Vector)this.generalErrorReports.clone();
    }

    public Vector getTableSpecificErrorReports(){
        return (Vector)this.tableSpecificErrorReports.clone();
    }

    public boolean hasTableSpecificErrorReports(){
        return this.tableSpecificErrorReports.size() > 0;
    }

    public void addTableSpecficReport(TableSpecificReport report){
        this.tableSpecificErrorReports.add(report);
    }

    public Iterator iterTableSpecificReports(){
        return this.tableSpecificErrorReports.iterator();
    }

    public void addGeneralErrorReport(DefaultReport report){
        this.generalErrorReports.add(report);
    }

    public Iterator iterGeneralErrorReports(){
        return this.generalErrorReports.iterator();
    }

    public void setPrologue(String prologue){
        this.prologue = prologue;
    }

    public String getPrologue(){
        return this.prologue;
    }

    public boolean foundErrors(){
        return ((this.generalErrorReports.size() > 0) || (this.tableSpecificErrorReports.size() > 0));
    }
    public boolean hasGeneralErrorReports(){
        return this.generalErrorReports.size() > 0;
    }

    public String getMessages() {
        return this.messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

}
