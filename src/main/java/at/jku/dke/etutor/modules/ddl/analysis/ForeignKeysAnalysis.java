package at.jku.dke.etutor.modules.ddl.analysis;

import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ForeignKeysAnalysis extends AbstractDDLCriterionAnalysis implements DDLCriterionAnalysis {
    //region Fields
    private List<ErrorTupel> missingForeignKeys;
    private List<ErrorTupel> surplusForeignKeys;
    //endregion

    public ForeignKeysAnalysis() {
        missingForeignKeys = new ArrayList<>();
        surplusForeignKeys = new ArrayList<>();
    }

    public boolean isMissingForeignKeysEmpty() {
        return this.missingForeignKeys.isEmpty();
    }

    public boolean isSurplusForeignKeysEmpty() {
        return this.surplusForeignKeys.isEmpty();
    }

    public Iterator<ErrorTupel> iterMissingForeignKeys() {
        return this.missingForeignKeys.iterator();
    }

    public Iterator<ErrorTupel> iterSurplusForeignKeys() {
        return this.surplusForeignKeys.iterator();
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
    //endregion
}
