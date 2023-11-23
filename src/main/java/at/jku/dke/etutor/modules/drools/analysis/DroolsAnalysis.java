package at.jku.dke.etutor.modules.drools.analysis;

import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.rt.analysis.RTSemanticsAnalysis;
import org.kie.api.runtime.KieSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DroolsAnalysis extends DefaultAnalysis {
    private int id;
    private Boolean cepMode;
    private String inputRules;
    private List<SourceFileModel> inputClasses = new ArrayList<>();
    private List<List<Object>> inputFacts = new ArrayList<>();
    private List<List<Object>> inputTestData = new ArrayList<>();
    private String syntaxErrorMessage;
    private KieSession kieSession;


    public DroolsAnalysis(int id, Boolean cepMode, String inputRules, List<SourceFileModel> inputClasses, List<List<Object>> inputFacts, List<List<Object>> inputTestData) throws IOException {
        super();
        this.id = id;
        this.cepMode = cepMode;
        this.inputRules = inputRules;
        this.inputClasses = inputClasses;
        this.inputFacts = inputFacts;
        this.inputTestData = inputTestData;
        this.kieSession = new DynamicDroolsBuilder(inputClasses).newKieSession(inputRules, cepMode);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInputRules() {
        return inputRules;
    }

    public void setInputRules(String inputRules) {
        this.inputRules = inputRules;
    }

    public boolean hasSyntaxError() {
        return false;
    }

    public boolean hasSemantikError() {
        return false;
    }

    public Boolean getCepMode() {
        return cepMode;
    }

    public void setCepMode(Boolean cepMode) {
        this.cepMode = cepMode;
    }

    public List<SourceFileModel> getInputClasses() {
        return inputClasses;
    }

    public void setInputClasses(List<SourceFileModel> inputClasses) {
        this.inputClasses = inputClasses;
    }

    public List<List<Object>> getInputFacts() {
        return inputFacts;
    }

    public void setInputFacts(List<List<Object>> inputFacts) {
        this.inputFacts = inputFacts;
    }

    public List<List<Object>> getInputTestData() {
        return inputTestData;
    }

    public void setInputTestData(List<List<Object>> inputTestData) {
        this.inputTestData = inputTestData;
    }

    public String getSyntaxErrorMessage() {
        return syntaxErrorMessage;
    }

    public void setSyntaxErrorMessage(String syntaxErrorMessage) {
        this.syntaxErrorMessage = syntaxErrorMessage;
    }

    public KieSession getKieSession() {
        return kieSession;
    }

    public void setKieSession(KieSession kieSession) {
        this.kieSession = kieSession;
    }
}
