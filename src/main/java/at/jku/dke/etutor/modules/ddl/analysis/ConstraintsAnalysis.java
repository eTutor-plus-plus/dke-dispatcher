package at.jku.dke.etutor.modules.ddl.analysis;

import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConstraintsAnalysis extends AbstractDDLCriterionAnalysis implements DDLCriterionAnalysis {
    //region Fields
    private boolean insertStatementsChecked;
    private List<ErrorTupel> missingConstraints;
    private List<ErrorTupel> surplusConstraints;
    private List<String> dmlStatementsWithMistakes;
    //endregion

    public ConstraintsAnalysis() {
        missingConstraints = new ArrayList<>();
        surplusConstraints = new ArrayList<>();
        dmlStatementsWithMistakes = new ArrayList<>();
        insertStatementsChecked = false;
    }

    public boolean isInsertStatementsChecked() {
        return insertStatementsChecked;
    }

    public void setInsertStatementsChecked(boolean insertStatementsChecked) {
        this.insertStatementsChecked = insertStatementsChecked;
    }

    public boolean isMissingConstraintsEmpty() {
        return this.missingConstraints.isEmpty();
    }

    public boolean isSurplusConstraintsEmpty() {
        return this.surplusConstraints.isEmpty();
    }

    public boolean isDmlStatementsWithMistakesEmpty() {
        return this.dmlStatementsWithMistakes.isEmpty();
    }

    public Iterator<ErrorTupel> iterMissingConstraints() {
        return this.missingConstraints.iterator();
    }

    public Iterator<ErrorTupel> iterSurplusConstraints() {
        return this.surplusConstraints.iterator();
    }

    public Iterator<String> iterDmlStatementsWithMistakes() {
        return this.dmlStatementsWithMistakes.iterator();
    }

    @Override
    public DDLEvaluationCriterion getEvaluationCriterion() {
        return null;
    }

    //region Getter/Setter
    public List<ErrorTupel> getMissingConstraints() {
        return missingConstraints;
    }

    public void setMissingConstraints(List<ErrorTupel> missingConstraints) {
        this.missingConstraints = missingConstraints;
    }

    public void addMissingConstraint(ErrorTupel missingConstraint) {
        this.missingConstraints.add(missingConstraint);
    }

    public List<ErrorTupel> getSurplusConstraints() {
        return surplusConstraints;
    }

    public void setSurplusConstraints(List<ErrorTupel> surplusConstraints) {
        this.surplusConstraints = surplusConstraints;
    }

    public void addSurplusConstraint(ErrorTupel surplusConstraint) {
        this.surplusConstraints.add(surplusConstraint);
    }

    public List<String> getDmlStatementsWithMistakes() {
        return dmlStatementsWithMistakes;
    }

    public void setDmlStatementsWithMistakes(List<String> dmlStatementsWithMistakes) {
        this.dmlStatementsWithMistakes = dmlStatementsWithMistakes;
    }

    public void addDmlStatementWithMistake(String stmt) {
        this.dmlStatementsWithMistakes.add(stmt);
    }
    //endregion
}
