package at.jku.dke.etutor.modules.drools.report;

import at.jku.dke.etutor.core.evaluation.DefaultReport;
import at.jku.dke.etutor.modules.drools.analysis.DroolsAnalysis;

import java.io.IOException;
import java.io.Serializable;

public class DroolsReport extends DefaultReport implements Serializable {
    DroolsAnalysis droolsAnalysis;
    public DroolsReport(DroolsAnalysis droolsAnalysis) {
        super();
        this.droolsAnalysis = droolsAnalysis;
    }

//    public void getReport() throws ReflectiveOperationException, IOException {
//        if (this.droolsAnalysis.hasSyntaxError() || this.droolsAnalysis.hasSemantikError()){
//            this.setShowErrorDescription(true);
//            if (this.droolsAnalysis.hasSemantikError()){
//                this.setError("Die Lösung hat semantische Fehler!");
//            }
//            if (this.droolsAnalysis.hasSyntaxError()) {
//                this.setError("Die Lösung hat Syntax Fehler!");
//            }
//            if (this.droolsAnalysis.hasSyntaxError() && this.droolsAnalysis.hasSemantikError()) {
//                this.setError("Die Lösung hat Syntax und Semantik Fehler!");
//            }
//        }
//    }
}
