package at.jku.dke.etutor.modules.rt.report;

import at.jku.dke.etutor.core.evaluation.DefaultReport;
import at.jku.dke.etutor.modules.rt.analysis.RTAnalysis;

import java.io.Serializable;

public class rtReport extends DefaultReport implements Serializable {
    RTAnalysis rtAnalysis;
    public rtReport(RTAnalysis rtAnalysis) {
        super();
        this.rtAnalysis = rtAnalysis;
    }

    public void getReport(){
        if (this.rtAnalysis.getHasSyntaxError() || this.rtAnalysis.getHasSemantikError()){
            this.setShowErrorDescription(true);
            if (this.rtAnalysis.getHasSemantikError()){
                this.setError("Die Lösung hat semantische Fehler!");
            }
            if (this.rtAnalysis.getHasSyntaxError()) {
                this.setError("Die Lösung hat Syntax Fehler!");
            }
            if (this.rtAnalysis.getHasSyntaxError() && this.rtAnalysis.getHasSemantikError()) {
                this.setError("Die Lösung hat Syntax und Semantik Fehler!");
            }
        }
    }
}
