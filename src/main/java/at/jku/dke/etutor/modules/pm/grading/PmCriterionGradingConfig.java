package at.jku.dke.etutor.modules.pm.grading;

/**
 * Class that defines certain attributes to each and every criterion available
 * + defines their corresponding methods
 * each criterion is defined by a maximum of positive points and a maximum of negative points if criterion is not
 * satisfied
 */
public class PmCriterionGradingConfig {

    // OBJECT FIELD
    private int positivePoints;
    private int negativePoints;

    // CONSTRUCTOR
    public PmCriterionGradingConfig(){
        super();

        positivePoints = 0;
        negativePoints = 0;
    }

    // METHODS

    public int getNegativePoints() {
        return negativePoints;
    }
    public int getPositivePoints() {
        return positivePoints;
    }
    public void setNegativePoints(int negativePoints) {
        this.negativePoints = negativePoints;
    }
    public void setPositivePoints(int positivePoints) {
        this.positivePoints = positivePoints;
    }
}
