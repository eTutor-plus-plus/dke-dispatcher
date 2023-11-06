package at.jku.dke.etutor.modules.ddl.analysis;

import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConstraintsAnalysis extends AbstractDDLCriterionAnalysis implements DDLCriterionAnalysis {
    //region Fields
    private List<String> missingConstraints;
    private List<String> surplusConstraints;
    //endregion

    public ConstraintsAnalysis() {
        missingConstraints = new ArrayList<>();
        surplusConstraints = new ArrayList<>();
    }

    public boolean hasMissingConstraints() {
        return !this.missingConstraints.isEmpty();
    }

    public boolean hasSurplusConstraints() {
        return !this.surplusConstraints.isEmpty();
    }

    public Iterator<String> iterMissingConstraints() {
        return this.missingConstraints.iterator();
    }

    public Iterator<String> iterSurplusConstraints() {
        return this.surplusConstraints.iterator();
    }

    @Override
    public DDLEvaluationCriterion getEvaluationCriterion() {
        return null;
    }

    //region Getter/Setter
    public List<String> getMissingConstraints() {
        return missingConstraints;
    }

    public void setMissingConstraints(List<String> missingConstraints) {
        this.missingConstraints = missingConstraints;
    }

    public List<String> getSurplusConstraints() {
        return surplusConstraints;
    }

    public void setSurplusConstraints(List<String> surplusConstraints) {
        this.surplusConstraints = surplusConstraints;
    }
    //endregion
}
