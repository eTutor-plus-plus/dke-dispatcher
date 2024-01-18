package at.jku.dke.etutor.modules.ddl.grading;

//todo Necessary??
public class DDLCriterionGradingConfig {
    //region Fields
    private int positivePoints;
    private int negativePoints;
    //endregion

    public DDLCriterionGradingConfig() {
        this.positivePoints = 0;
        this.negativePoints = 0;
    }

    //region Getter/Setter
    public int getPositivePoints() {
        return positivePoints;
    }

    public void setPositivePoints(int positivePoints) {
        this.positivePoints = positivePoints;
    }

    public int getNegativePoints() {
        return negativePoints;
    }

    public void setNegativePoints(int negativePoints) {
        this.negativePoints = negativePoints;
    }
    //endregion
}
