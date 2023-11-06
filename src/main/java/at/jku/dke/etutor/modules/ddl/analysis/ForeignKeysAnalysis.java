package at.jku.dke.etutor.modules.ddl.analysis;

import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ForeignKeysAnalysis extends AbstractDDLCriterionAnalysis implements DDLCriterionAnalysis {
    //region Fields
    private List<String> missingForeignKeys;
    private List<String> surplusForeignKeys;
    //endregion

    public ForeignKeysAnalysis() {
        missingForeignKeys = new ArrayList<>();
        surplusForeignKeys = new ArrayList<>();
    }

    public boolean hasMissingForeignKeys() {
        return !this.missingForeignKeys.isEmpty();
    }

    public boolean hasSurplusForeignKeys() {
        return !this.surplusForeignKeys.isEmpty();
    }

    public Iterator<String> iterMissingForeignKeys() {
        return this.missingForeignKeys.iterator();
    }

    public Iterator<String> iterSurplusForeignKeys() {
        return this.surplusForeignKeys.iterator();
    }

    @Override
    public DDLEvaluationCriterion getEvaluationCriterion() {
        return null;
    }

    //region Getter/Setter
    public List<String> getMissingForeignKeys() {
        return missingForeignKeys;
    }

    public void setMissingForeignKeys(List<String> missingForeignKeys) {
        this.missingForeignKeys = missingForeignKeys;
    }

    public List<String> getSurplusForeignKeys() {
        return surplusForeignKeys;
    }

    public void setSurplusForeignKeys(List<String> surplusForeignKeys) {
        this.surplusForeignKeys = surplusForeignKeys;
    }
    //endregion
}
