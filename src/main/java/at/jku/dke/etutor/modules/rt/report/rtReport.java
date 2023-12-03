package at.jku.dke.etutor.modules.rt.report;

import at.jku.dke.etutor.core.evaluation.DefaultReport;
import at.jku.dke.etutor.modules.rt.analysis.RTAnalysis;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.Serializable;
import java.util.Map;

public class rtReport extends DefaultReport implements Serializable {
    RTAnalysis rtAnalysis;
    Map<String, String> passedAttributes;
    String errorMessage;
    String hintMessage;
    public rtReport(RTAnalysis rtAnalysis,  Map<String, String> passedAttributes) {
        super();
        this.rtAnalysis = rtAnalysis;
        this.passedAttributes = passedAttributes;
    }

    public void getReport() {
        if (this.rtAnalysis.getHasSyntaxError()) {
            this.setShowErrorDescription(true);
            this.errorMessage = "Die Abfrage wurde aufgrund von Syntax-Fehlern abgebrochen!";
            if (this.rtAnalysis.getErrorLogSyntax() != null) {
                this.errorMessage = errorMessage + this.rtAnalysis.getErrorLogSyntax();
            }
            this.setError(errorMessage);
        }
        if (!this.rtAnalysis.getHasSyntaxError() && !this.passedAttributes.get("diagnoseLevel").equals("0") && this.rtAnalysis.getHasSemantikError()) {
            this.setShowHint(true);
            this.hintMessage = "Die Abgabe enthält semantische Fehler!";
            this.hintMessage = hintMessage + this.rtAnalysis.getErrorLogSemantik();
            this.setHint(hintMessage);
        }
    }
}
