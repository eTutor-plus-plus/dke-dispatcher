package at.jku.dke.etutor.modules.drools.report;

import at.jku.dke.etutor.core.evaluation.DefaultReport;
import at.jku.dke.etutor.modules.drools.analysis.DroolsAnalysis;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public class DroolsReport extends DefaultReport implements Serializable {
    private final DroolsAnalysis droolsAnalysis;
    private final StringBuilder errorMessage;
    private final StringBuilder hintMessage;
    private final Map<String, String> passedAttributes;
    private final Locale locale;

    public DroolsReport(DroolsAnalysis droolsAnalysis, Map<String, String> passedAttributes, Locale locale) {
        super();
        this.droolsAnalysis = droolsAnalysis;
        this.locale = locale;
        this.passedAttributes = passedAttributes;
        errorMessage = new StringBuilder();
        hintMessage = new StringBuilder();
        report();
    }

    public void report() {
        if (this.droolsAnalysis.isHasSyntaxError()) {
            if (isGermanLocale())
                this.errorMessage.append("Die Regeln konnten aufgrund eines Syntaxfehlers nicht ausgeführt werden. ");
            else this.errorMessage.append("Rules could not be built due to a syntax error. ");
            this.setShowErrorDescription(true);
            this.errorMessage.append(droolsAnalysis.getSyntaxErrorMessage());
            this.setError(errorMessage.toString());
            return;
        }

        long additionalFacts = droolsAnalysis.getAdditionalFacts();
        int wrongFacts = droolsAnalysis.getWrongFactList().size();

        if(wrongFacts != 0){
            this.setShowHint(true);
            if (isGermanLocale()) {
                this.hintMessage.append("Es wurden falsche Fakten erzeugt. ");
            } else {
                this.hintMessage.append("Wrong facts generated. ");
            }
        }

        if (additionalFacts != 0) {
            if (isGermanLocale()) {
                this.hintMessage.append("Die Lösung ist nicht vollständig korrekt. ");
            } else {
                this.hintMessage.append("The solution is not entirely correct. ");
            }

            if (passedAttributes.get("diagnoseLevel").equals("0")) {
                this.setShowHint(true);
                if (isGermanLocale()) {
                    this.hintMessage.append("Die Regeln enthalten semantische Fehler. ");
                } else {
                    this.hintMessage.append("Rules contain semantic errors. ");
                }
            }

            if (passedAttributes.get("diagnoseLevel").equals("1")) {
                this.setShowHint(true);
                if (droolsAnalysis.getAdditionalFacts() > 0) {
                    if (isGermanLocale()) {
                        this.hintMessage.append("Es wurden zu viele Fakten erzeugt. ");
                    } else {
                        this.hintMessage.append("Rules produces additional facts. ");
                    }
                } else {
                    if (isGermanLocale()) {
                        this.hintMessage.append("Es wurden zu wenig Fakten erzeugt. ");
                    } else {
                        this.hintMessage.append("Rules produces too few facts. ");
                    }
                }
            }

            if (passedAttributes.get("diagnoseLevel").equals("2")) {
                this.setShowHint(true);
                if (droolsAnalysis.getAdditionalFacts() > 0) {
                    if (isGermanLocale()) {
                        this.hintMessage.append("Es wurden um ")
                                .append(droolsAnalysis.getAdditionalFacts())
                                .append(" zu viele Fakten erzeugt. ");
                    } else {
                        this.hintMessage.append("Rules are generating ")
                                .append(droolsAnalysis.getAdditionalFacts())
                                .append(" facts too many.");
                    }
                } else {
                    if (isGermanLocale()) {
                        this.hintMessage.append("Es wurden um ")
                                .append(Math.abs(droolsAnalysis.getAdditionalFacts()))
                                .append(" zu wenig Fakten erzeugt. ");
                    } else {
                        this.hintMessage.append("Rules are generating ")
                                .append(droolsAnalysis.getAdditionalFacts())
                                .append(" facts too few.");
                    }
                }
            }

            if (passedAttributes.get("diagnoseLevel").equals("3")) {
                this.setShowHint(true);
                if (droolsAnalysis.getAdditionalFacts() > 0) {
                    if (isGermanLocale()) {
                        this.hintMessage.append("Es wurden zu viel Fakten erzeugt. ");
                    } else {
                        this.hintMessage.append("Rules produces additional facts. ");
                    }
                } else {
                    if (isGermanLocale()) {
                        this.hintMessage.append("Es wurden zu wenig Fakten erzeugt. ");
                    } else {
                        this.hintMessage.append("Rules produces too few facts. ");
                    }
                }

                for (Map.Entry<String, Long> entry : droolsAnalysis.getAdditionalFactInformation().entrySet()) {
                    String className = entry.getKey();
                    Long difference = entry.getValue();

                    String message;
                    if (difference > 0) {
                        message = difference + " facts too many of class " + className;
                    } else if (difference < 0) {
                        message = Math.abs(difference) + " facts too few of class " + className;
                    } else {
                        message = "Correct number of facts of class " + className;
                    }

                    this.hintMessage.append(message);
                }

            }
        }
        this.setHint(hintMessage.toString());

    }


    /**
     * Returns whether the locale equals Locale.GERMAN
     *
     * @return a boolean
     */
    private boolean isGermanLocale() {
        return this.locale == Locale.GERMAN;
    }


}
