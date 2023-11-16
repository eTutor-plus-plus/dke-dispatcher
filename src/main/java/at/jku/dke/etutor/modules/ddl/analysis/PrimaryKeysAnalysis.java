package at.jku.dke.etutor.modules.ddl.analysis;

import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PrimaryKeysAnalysis extends AbstractDDLCriterionAnalysis implements DDLCriterionAnalysis {
    //region Fields
    private List<ErrorTupel> missingPrimaryKeys;
    private List<ErrorTupel> surplusPrimaryKeys;
    //endregion

    public PrimaryKeysAnalysis() {
        missingPrimaryKeys = new ArrayList<>();
        surplusPrimaryKeys = new ArrayList<>();
    }

    public boolean isMissingPrimaryKeysEmpty() {
        return this.missingPrimaryKeys.isEmpty();
    }

    public boolean isSurplusPrimaryKeysEmpty() {
        return this.surplusPrimaryKeys.isEmpty();
    }

    public Iterator<ErrorTupel> iterMissingPrimaryKeys() {
        return this.missingPrimaryKeys.iterator();
    }

    public Iterator<ErrorTupel> iterSurplusPrimaryKeys() {
        return this.surplusPrimaryKeys.iterator();
    }

    @Override
    public DDLEvaluationCriterion getEvaluationCriterion() {
        return null;
    }

    //region Getter/Setter
    public List<ErrorTupel> getMissingPrimaryKeys() {
        return missingPrimaryKeys;
    }

    public void setMissingPrimaryKeys(List<ErrorTupel> missingPrimaryKeys) {
        this.missingPrimaryKeys = missingPrimaryKeys;
    }

    public void addMissingPrimaryKey(ErrorTupel missingPrimaryKey) {
        this.missingPrimaryKeys.add(missingPrimaryKey);
    }

    public List<ErrorTupel> getSurplusPrimaryKeys() {
        return surplusPrimaryKeys;
    }

    public void setSurplusPrimaryKeys(List<ErrorTupel> surplusPrimaryKeys) {
        this.surplusPrimaryKeys = surplusPrimaryKeys;
    }

    public void addSurplusPrimaryKey(ErrorTupel surplusPrimaryKey) {
        this.surplusPrimaryKeys.add(surplusPrimaryKey);
    }
    //endregion
}
