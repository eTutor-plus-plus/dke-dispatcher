package at.jku.dke.etutor.modules.drools.report;

import at.jku.dke.etutor.core.evaluation.DefaultReport;
import at.jku.dke.etutor.modules.drools.analysis.DroolsAnalysis;

import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public class DroolsReport extends DefaultReport implements Serializable {
    private DroolsAnalysis droolsAnalysis;
    private StringBuilder errorMessage;
    private StringBuilder hintMessage;
    private Map<String,String> passedAttributes;
    private Locale locale;
    public DroolsReport(DroolsAnalysis droolsAnalysis, Map<String,String> passedAttributes, Locale locale) {
        super();
        this.droolsAnalysis = droolsAnalysis;
        this.locale = locale;
        this.passedAttributes = passedAttributes;
    }

    public void report(Locale locale ){
        if(this.droolsAnalysis.isHasSyntaxError()){
            if(isGermanLocale()) this.errorMessage.append("Die Regeln konnten aufgrund eines Syntaxfehlers nicht ausgeführt werden.");
            else this.errorMessage.append("Rules could not be built due to a syntax error.");
            this.setShowErrorDescription(true);
            this.errorMessage.append(droolsAnalysis.getSyntaxErrorMessage());
        }
        this.setError(errorMessage.toString());

        if((this.droolsAnalysis.getAdditionalFacts() != 0 || this.droolsAnalysis.getWrongFacts() != 0)){
            if(passedAttributes.get("diagnoseLevel").equals("0")) {
               this.setShowHint(true);
               if(isGermanLocale()) this.hintMessage.append("Die Regeln enthalten semantische Fehler.");
               else this.hintMessage.append("Rules contain semantic errors.");
            }

            if(passedAttributes.get("diagnoseLevel").equals("1")) {
                this.setShowHint(true);
                if(isGermanLocale()) this.hintMessage.append("Es wurden zu viel/zu wenig Fakten erzeugt");
                else this.hintMessage.append("Rules produce additional/less facts");
            }

            if(passedAttributes.get("diagnoseLevel").equals("2")) {
                this.setShowHint(true);
                if(isGermanLocale()) this.hintMessage.append("Es wurden um <ANZAHL> zu viel/zu wenig Fakten erzeugt");
                else this.hintMessage.append("Rules produce <NUMBER> additional/less facts");
            }

            if(passedAttributes.get("diagnoseLevel").equals("3")) {
                this.setShowHint(true);
                if(isGermanLocale()) this.hintMessage.append("Es wurden um <ANZAHL> zu viele/zu wenig Fakten, der Klasse <CLASSNAME> erzeugt");
                else this.hintMessage.append("Rules produce <NUMBER> additional/less facts of class <CLASSNAME>");
            }

            this.setHint(hintMessage.toString());
        }



    }


    /**
     * Returns whether the locale equals Locale.GERMAN
     * @return a boolean
     */
    private boolean isGermanLocale(){
        return this.locale == Locale.GERMAN;
    }



}
