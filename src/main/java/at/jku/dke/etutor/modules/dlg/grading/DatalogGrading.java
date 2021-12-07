package at.jku.dke.etutor.modules.dlg.grading;

import java.io.Serializable;
import java.util.ArrayList;

import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.modules.dlg.analysis.DatalogAnalysis;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents the grading of an analyzed query.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class DatalogGrading implements Serializable, Grading {

    private double points;

    private double maxPoints;

    private boolean isCorrect;

    private boolean isReporting;
    
    /**
     * The logger used for logging.
     */
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(DatalogGrading.class);

    /**
     * Creates a new grading object with default values. Maximum points and actual points are set to
     * 0,{@link #solutionIsCorrect()}and {@link #isReporting()}will return <code>false</code>.
     */
    public DatalogGrading() {
        super();
        this.points = 0;
        this.maxPoints = 0;
        this.isCorrect = false;
        this.isReporting = false;
    }

    /**
     * Creates a new grading object using the results of an analysis object and some grading
     * parameters.
     * 
     * @param analysis The analysis object containing information about the analyzed query and
     *            detected errors.
     * @param scores Parameters used for grading, most of all the minus points for different error
     *            categories and the strategy of how often errors of the same category are
     *            considered - for each occurence, once for the whole category or as K.O. criterion.
     */
    public DatalogGrading(
            DatalogAnalysis analysis, DatalogScores scores) {
        this.setSolutionIsCorrect(analysis.isCorrect());
        this.maxPoints = scores.getMaxScore();
        if (analysis != null && analysis.isAnalyzable() && analysis.getResult2().hasConsistentModel()) {
            double minusPoints = countMinusPoints(analysis, scores);
        	this.points = this.maxPoints - minusPoints;
            this.points = Math.max(0, this.points);
            LOGGER.debug("Grading result: max(" + maxPoints + " - " + minusPoints + ", 0) = " + points);
        } else {
        	LOGGER.debug("Grading result: 0 points (no valid solution)");
            this.points = 0;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.core.evaluation.Grading#getPoints()
     */
    public double getPoints() {
        return this.points;
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.core.evaluation.Grading#setPoints(int)
     */
    public void setPoints(double points) {
        this.points = points;
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.core.evaluation.Grading#getMaxPoints()
     */
    public double getMaxPoints() {
        return this.maxPoints;
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.core.evaluation.Grading#setMaxPoints(int)
     */
    public void setMaxPoints(double maxPoints) {
        this.maxPoints = maxPoints;
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.core.evaluation.Grading#setSolutionIsCorrect(boolean)
     */
    public void setSolutionIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.core.evaluation.Grading#solutionIsCorrect()
     */
    public boolean solutionIsCorrect() {
        return this.isCorrect;
    }

    /**
     * Calculates all minus points with regard to the error categories contained in the analysis
     * object.
     * 
     * @param analysis The analysis object containing information about the analyzed query and
     *            detected errors.
     * @param scores Parameters used for grading.
     * @return The minus points counted together, which is at least 0.
     */
    private double countMinusPoints(DatalogAnalysis analysis, DatalogScores scores) {
        double totalMinusPoints = 0;
        ArrayList errorList;

        errorList = analysis.getHighArityPredicates();
        int errorLevel = scores.getErrorLevel(DatalogScores.PREDICATES_ARITY_HIGH);
        double minusPoints = scores.getScore(DatalogScores.PREDICATES_ARITY_HIGH);
        totalMinusPoints += countMinusPoints(errorList, errorLevel, minusPoints, maxPoints);

        errorList = analysis.getLowArityPredicates();
        errorLevel = scores.getErrorLevel(DatalogScores.PREDICATES_ARITY_LOW);
        minusPoints = scores.getScore(DatalogScores.PREDICATES_ARITY_LOW);
        totalMinusPoints += countMinusPoints(errorList, errorLevel, minusPoints, maxPoints);

        errorList = analysis.getMissingPredicates();
        errorLevel = scores.getErrorLevel(DatalogScores.PREDICATES_MISSING);
        minusPoints = scores.getScore(DatalogScores.PREDICATES_MISSING);
        totalMinusPoints += countMinusPoints(errorList, errorLevel, minusPoints, maxPoints);

        errorList = analysis.getRedundantPredicates();
        errorLevel = scores.getErrorLevel(DatalogScores.PREDICATES_REDUNDANT);
        minusPoints = scores.getScore(DatalogScores.PREDICATES_REDUNDANT);
        totalMinusPoints += countMinusPoints(errorList, errorLevel, minusPoints, maxPoints);

        errorList = analysis.getMissingFacts();
        errorLevel = scores.getErrorLevel(DatalogScores.FACTS_MISSING);
        minusPoints = scores.getScore(DatalogScores.FACTS_MISSING);
        totalMinusPoints += countMinusPoints(errorList, errorLevel, minusPoints, maxPoints);

        errorList = analysis.getRedundantFacts();
        errorLevel = scores.getErrorLevel(DatalogScores.FACTS_REDUNDANT);
        minusPoints = scores.getScore(DatalogScores.FACTS_REDUNDANT);
        totalMinusPoints += countMinusPoints(errorList, errorLevel, minusPoints, maxPoints);

        errorList = analysis.getPositiveFacts();
        errorLevel = scores.getErrorLevel(DatalogScores.FACTS_POSITIVE);
        minusPoints = scores.getScore(DatalogScores.FACTS_POSITIVE);
        totalMinusPoints += countMinusPoints(errorList, errorLevel, minusPoints, maxPoints);

        errorList = analysis.getNegativeFacts();
        errorLevel = scores.getErrorLevel(DatalogScores.FACTS_NEGATIVE);
        minusPoints = scores.getScore(DatalogScores.FACTS_NEGATIVE);
        totalMinusPoints += countMinusPoints(errorList, errorLevel, minusPoints, maxPoints);

        return totalMinusPoints;
    }

    /**
     * Calculates the minus points with regard to a certain error category.
     * 
     * @param errorList This list is expected to contain the errors according to a certain category.
     *            The list is only iterated, so the focus is on the list size but not on the types
     *            of the objects contained in the list.
     * @param errorLevel One of the error level keys as defined in {@link DatalogScores}.
     * @param minusPoints The points, interpreted as number equal to or greater than 0, which are
     *            assigned to an error.
     * @param maxPoints The maximum points which can be generally achieved for a task. This is
     *            needed if the consequence of some errors is, that all points are subtracted at
     *            once for the whole task.
     * @return A number equal to or greater than 0, representing the points which have to be
     *         subtracted from the highest available points for an exercise.
     */
    private double countMinusPoints(ArrayList errorList, int errorLevel, double minusPoints,
            double maxPoints) {
        if (errorList != null) {
            if (errorLevel == DatalogScores.ERROR_LEVEL_EACH) {
            	double points = minusPoints * errorList.size();
            	String msg = "Gradings configuration ... ";
            	msg += "error level: " + errorLevel + " ... " + minusPoints;
            	msg += " are subtracted for each of " + errorList.size() + " occurence(s) ... => ";
            	msg += points + " subtracted points";
            	LOGGER.debug(msg);
                return points;
            } else if (errorLevel == DatalogScores.ERROR_LEVEL_ALL) {
                if (errorList.size() > 0) {
                    String msg = "Gradings configuration ... ";
                	msg += "error level: " + errorLevel + " ... " + minusPoints;
                	msg += " are subtracted for the whole error category (" + errorList.size();
                	msg += " occurence(s)) ... => " + minusPoints + " subtracted points";
                	LOGGER.debug(msg);
                	return minusPoints;
                }
            } else if (errorLevel == DatalogScores.ERROR_LEVEL_KO) {
                if (errorList.size() > 0) {
                	String msg = "Gradings configuration ... ";
                	msg += "error level: " + errorLevel + " ... all points (" + maxPoints;
                	msg += ") are subtracted for the whole exercise (" + errorList.size();
                	msg += " occurence(s)) ... => " + maxPoints + " subtracted points";
                	LOGGER.debug(msg);
                    return maxPoints;
                }
            }
        }
        return 0;
    }

    /**
     * A flag used when using this <code>DatalogGrading</code> for reporting later on. This tells,
     * whether the information of the grading should be included in the report.
     * 
     * @return true if the results of this grading object will be included when reporting, otherwise
     *         false.
     */
    public boolean isReporting() {
        return this.isReporting;
    }

    /**
     * Sets a flag which is used when using this <code>DatalogGrading</code> for reporting later
     * on. This tells, whether the information of the grading should be included in the report.
     * 
     * @param isReporting if the results of this grading object should be included when reporting,
     *            otherwise false.
     */
    public void setReporting(boolean isReporting) {
        this.isReporting = isReporting;
    }
}