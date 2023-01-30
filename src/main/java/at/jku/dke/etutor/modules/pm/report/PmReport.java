package at.jku.dke.etutor.modules.pm.report;

import at.jku.dke.etutor.core.evaluation.DefaultReport;
import at.jku.dke.etutor.core.evaluation.Report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PmReport extends DefaultReport implements Report, Serializable {

    private String prologue;
    private String exceptionText;

    private List<PmErrorReport> errorReports;
    // Elements == pairs, arcs, events
    // private List<Collection<String>> missingElements;
    // private List<Collection<String>> surplusElements;

    private boolean showHints;
    private boolean showPrologue;
    private boolean showException;
    private boolean showErrorReports;
    private boolean showErrorDescriptions;

    public PmReport(){
        super();

        this.showHints = false;
        this.showException = false;
        this.showErrorReports = false;
        this.showErrorDescriptions = false;

        this.exceptionText = "";
        this.prologue = "";

        this.errorReports = new ArrayList<>();
        // this.missingElements = new ArrayList<>();
        // this.surplusElements = new ArrayList<>();
    }

    // error report:
    public List<PmErrorReport> getErrorReports() {
        return errorReports;
    }
    public void setShowErrorReports(boolean showErrorReports) {
        this.showErrorReports = showErrorReports;
    }
    public void addErrorReport(PmErrorReport errorReport){
        this.errorReports.add(errorReport);
    }
    public Iterator<PmErrorReport> iterErrorReports(){
        return this.errorReports.iterator();
    }

/*    public void addMissingElements(Collection<String> missingElements){
        this.missingElements.add(missingElements);
    }
    public void addSurplusElements(Collection<String> surplusElements){
        this.surplusElements.add(surplusElements);
    }*/

    public void setShowErrorDescriptions(boolean b){
        this.showErrorDescriptions = b;
    }

    public void setShowHints(boolean b){
        this.showHints = b;
    }

    public String getExceptionText() {
        return exceptionText;
    }

    public boolean showErrorDescriptions(){
        return this.showErrorDescriptions;
    }
    public boolean showErrorReport(){
        return this.showErrorReports;
    }


}
