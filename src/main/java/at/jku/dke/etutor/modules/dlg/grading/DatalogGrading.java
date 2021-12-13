package at.jku.dke.etutor.modules.dlg.grading;

import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.modules.dlg.analysis.DatalogAnalysis;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

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