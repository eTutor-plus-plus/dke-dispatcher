package at.jku.dke.etutor.modules.ddl.analysis;

import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PrimaryKeysAnalysis extends AbstractDDLCriterionAnalysis implements DDLCriterionAnalysis {
    //region Fields
    private List<String> missingPrimaryKeys;
    private List<String> surplusPrimaryKeys;
    //endregion

    public PrimaryKeysAnalysis() {
        missingPrimaryKeys = new ArrayList<>();
        surplusPrimaryKeys = new ArrayList<>();
    }

    public boolean hasMissingPrimaryKeys() {
        return !this.missingPrimaryKeys.isEmpty();
    }

    public boolean hasSurplusPrimaryKeys() {
        return !this.surplusPrimaryKeys.isEmpty();
    }

    public Iterator<String> iterMissingPrimaryKeys() {
        return this.missingPrimaryKeys.iterator();
    }

    public Iterator<String> iterSurplusPrimaryKeys() {
        return this.surplusPrimaryKeys.iterator();
    }

    @Override
    public DDLEvaluationCriterion getEvaluationCriterion() {
        return null;
    }

    //region Getter/Setter
    public List<String> getMissingPrimaryKeys() {
        return missingPrimaryKeys;
    }

    public void setMissingPrimaryKeys(List<String> missingPrimaryKeys) {
        this.missingPrimaryKeys = missingPrimaryKeys;
    }

    public List<String> getSurplusPrimaryKeys() {
        return surplusPrimaryKeys;
    }

    public void setSurplusPrimaryKeys(List<String> surplusPrimaryKeys) {
        this.surplusPrimaryKeys = surplusPrimaryKeys;
    }
    //endregion
}
