package at.jku.dke.etutor.modules.ddl.analysis;

import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ForeignKeysAnalysis extends AbstractDDLCriterionAnalysis implements DDLCriterionAnalysis {
    //region Fields
    private List<ErrorTupel> missingForeignKeys;
    private List<ErrorTupel> surplusForeignKeys;
    private List<ErrorTupel> wrongUpdateForeignKeys;
    private List<ErrorTupel> wrongDeleteForeignKeys;
    //endregion

    public ForeignKeysAnalysis() {
        missingForeignKeys = new ArrayList<>();
        surplusForeignKeys = new ArrayList<>();
        wrongUpdateForeignKeys = new ArrayList<>();
        wrongDeleteForeignKeys = new ArrayList<>();
    }

    public boolean isMissingForeignKeysEmpty() {
        return this.missingForeignKeys.isEmpty();
    }

    public boolean isSurplusForeignKeysEmpty() {
        return this.surplusForeignKeys.isEmpty();
    }

    public boolean isWrongUpdateForeignKeysEmpty() {
        return this.wrongUpdateForeignKeys.isEmpty();
    }

    public boolean isWrongDeleteForeignKeysEmpty() {
        return this.wrongDeleteForeignKeys.isEmpty();
    }

    public Iterator<ErrorTupel> iterMissingForeignKeys() {
        return this.missingForeignKeys.iterator();
    }

    public Iterator<ErrorTupel> iterSurplusForeignKeys() {
        return this.surplusForeignKeys.iterator();
    }

    public Iterator<ErrorTupel> iterWrongUpdateForeignKeys() {
        return this.wrongUpdateForeignKeys.iterator();
    }

    public Iterator<ErrorTupel> iterWrongDeleteForeignKeys() {
        return this.wrongDeleteForeignKeys.iterator();
    }

    @Override
    public DDLEvaluationCriterion getEvaluationCriterion() {
        return null;
    }

    //region Getter/Setter
    public List<ErrorTupel> getMissingForeignKeys() {
        return missingForeignKeys;
    }

    public void setMissingForeignKeys(List<ErrorTupel> missingForeignKeys) {
        this.missingForeignKeys = missingForeignKeys;
    }

    public void addMissingForeignKey(ErrorTupel foreignKey) {
        this.missingForeignKeys.add(foreignKey);
    }

    public List<ErrorTupel> getSurplusForeignKeys() {
        return surplusForeignKeys;
    }

    public void setSurplusForeignKeys(List<ErrorTupel> surplusForeignKeys) {
        this.surplusForeignKeys = surplusForeignKeys;
    }

    public void addSurplusForeignKey(ErrorTupel foreignKey) {
        this.surplusForeignKeys.add(foreignKey);
    }

    public List<ErrorTupel> getWrongUpdateForeignKeys() {
        return wrongUpdateForeignKeys;
    }

    public void setWrongUpdateForeignKeys(List<ErrorTupel> wrongUpdateForeignKeys) {
        this.wrongUpdateForeignKeys = wrongUpdateForeignKeys;
    }

    public void addWrongUpdateForeignKey(ErrorTupel foreignKey) {
        this.wrongUpdateForeignKeys.add(foreignKey);
    }

    public List<ErrorTupel> getWrongDeleteForeignKeys() {
        return wrongDeleteForeignKeys;
    }

    public void setWrongDeleteForeignKeys(List<ErrorTupel> wrongDeleteForeignKeys) {
        this.wrongDeleteForeignKeys = wrongDeleteForeignKeys;
    }

    public void addWrongDeleteForeignKey(ErrorTupel foreignKey) {
        this.wrongDeleteForeignKeys.add(foreignKey);
    }
    //endregion
}
