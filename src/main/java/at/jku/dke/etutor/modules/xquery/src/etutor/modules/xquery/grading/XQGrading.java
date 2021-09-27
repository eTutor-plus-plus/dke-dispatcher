package etutor.modules.xquery.grading;

import java.io.Serializable;

import etutor.core.evaluation.Grading;
import etutor.modules.xquery.analysis.NodeErrorList;
import etutor.modules.xquery.analysis.XQAnalysis;

/**
 * This class represents the grading of an analyzed query.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class XQGrading implements Serializable, Grading {

    private double points;

    private double maxPoints;

    private boolean isCorrect;

    /**
     * Creates a new grading object with default values. Maximum points and actual points are set to
     * 0,{@link #solutionIsCorrect()}and {@link #isReporting()}will return <code>false</code>.
     */
    public XQGrading() {
        super();
        this.points = 0;
        this.maxPoints = 0;
        this.isCorrect = false;
    }

    /**
     * Creates a new grading object using the results of an analysis object and some grading
     * parameters.
     * 
     * @param analysis The analysis object containing information about the analyzed query and
     *            detected errors.
     * @param config Parameters used for grading, most of all the minus points for different error
     *            categories and the strategy of how often errors of the same category are
     *            considered - for each occurence, once for the whole category or as K.O. criterion.
     */
    public XQGrading(
            XQAnalysis analysis, XQGradingConfig config) {
        this.setSolutionIsCorrect(analysis.isCorrect());
        this.maxPoints = config.getMaxScore();
        if (analysis.isAnalyzable() && analysis.isValidSolution()) {
        	double minusPoints = countMinusPoints(analysis, config);
            this.points = this.maxPoints - minusPoints;
            this.points = Math.max(0, this.points);
        } else {
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
     * @param config Parameters used for grading.
     * @return The minus points counted together, which is at least 0.
     */
    private double countMinusPoints(XQAnalysis analysis, XQGradingConfig config) {
        
        //TODO: There are two categories with exactly the same entries. The first one contains
        //nodes which are missing instead of a found one, which is redundant. The second one in turn 
        //contains the redundant node, which should be replaced by some missing nodes found in the correct
        //result. In this version, the error is counted twice: One for redundant nodes, the other one
        //for missing nodes, although the semantic is the same.
        
        double totalMinusPoints = 0;
        NodeErrorList[] errorList;

        // CATEGORY: any displaced nodes
        NodeErrorList displacedNodes = analysis.getDisplacedNodes();
        errorList = new NodeErrorList[] {displacedNodes};
        int errorLevel = config.getErrorLevel(XQGradingConfig.NODES_DISPLACED);
        double minusPoints = config.getScore(XQGradingConfig.NODES_DISPLACED);
        totalMinusPoints += countMinusPoints(errorList, errorLevel, minusPoints, maxPoints);

        // CATEGORY: any redundant nodes
        NodeErrorList redundantNodes = analysis.getRedundantNodes();
        /*
        //This category is related to the one as returned by getMissingInsteadNodes()
        NodeErrorList redundantInsteadNodes = analysis.getRedundantInsteadNodes();
        errorList = new NodeErrorList[] {redundantNodes, redundantInsteadNodes};
        */
        errorList = new NodeErrorList[] {redundantNodes};
        errorLevel = config.getErrorLevel(XQGradingConfig.NODES_REDUNDANT);
        minusPoints = config.getScore(XQGradingConfig.NODES_REDUNDANT);
        totalMinusPoints += countMinusPoints(errorList, errorLevel, minusPoints, maxPoints);

        // CATEGORY: any missing nodes
        NodeErrorList missingPreviousNodes = analysis.getMissingPreviousNodes();
        NodeErrorList missingNextNodes = analysis.getMissingNextNodes();
        NodeErrorList missingInnerNodes = analysis.getMissingInnerNodes();
        //This category is related to the one as returned by getRedundantInsteadNodes()
        NodeErrorList missingInsteadNodes = analysis.getMissingInsteadNodes();
        errorList = new NodeErrorList[] {missingPreviousNodes, missingNextNodes, missingInnerNodes, missingInsteadNodes};
        errorLevel = config.getErrorLevel(XQGradingConfig.NODES_MISSING);
        minusPoints = config.getScore(XQGradingConfig.NODES_MISSING);
        totalMinusPoints += countMinusPoints(errorList, errorLevel, minusPoints, maxPoints);

        // CATEGORY: redundant attributes
        NodeErrorList redundantAttributes = analysis.getRedundantAttributes();
        errorList = new NodeErrorList[] {redundantAttributes};
        errorLevel = config.getErrorLevel(XQGradingConfig.ATTRIBUTES_REDUNDANT);
        minusPoints = config.getScore(XQGradingConfig.ATTRIBUTES_REDUNDANT);
        totalMinusPoints += countMinusPoints(errorList, errorLevel, minusPoints, maxPoints);

        // CATEGORY: missing attributes
        NodeErrorList missingAttributes = analysis.getMissingAttributes();
        errorList = new NodeErrorList[] {missingAttributes};
        errorLevel = config.getErrorLevel(XQGradingConfig.ATTRIBUTES_MISSING);
        minusPoints = config.getScore(XQGradingConfig.ATTRIBUTES_MISSING);
        totalMinusPoints += countMinusPoints(errorList, errorLevel, minusPoints, maxPoints);

        // CATEGORY: incorrect values of text nodes and attributes
        NodeErrorList incorrectAttributeValues = analysis.getIncorrectAttributeValues();
        NodeErrorList incorrectTextValues = analysis.getIncorrectTextValues();
        errorList = new NodeErrorList[] {incorrectAttributeValues, incorrectTextValues};
        errorLevel = config.getErrorLevel(XQGradingConfig.VALUE_INCORRECT);
        minusPoints = config.getScore(XQGradingConfig.VALUE_INCORRECT);
        totalMinusPoints += countMinusPoints(errorList, errorLevel, minusPoints, maxPoints);

        return totalMinusPoints;
    }

    /**
     * Calculates the minus points with regard to a certain error category.
     * 
     * @param errorList This list is expected to contain the errors according to a certain category.
     * @param errorLevel One of the error level keys defined in {@link XQGradingConfig}.
     * @param minusPoints The points, interpreted as number equal to or greater than 0, which are
     *            assigned to an error.
     * @param maxPoints The maximum points which can be generally achieved for a task. This is
     *            needed if the consequence of some errors is, that all points are subtracted at
     *            once for the whole task.
     * @return A number equal to or greater than 0, representing the points which have to be
     *         subtracted from the highest available points for an exercise.
     */
    private double countMinusPoints(NodeErrorList[] errorList, int errorLevel,
            double minusPoints, double maxPoints) {
        if (errorList != null) {
            if (errorLevel == XQGradingConfig.ERROR_LEVEL_EACH) {
                double points = 0;
                for (int i = 0; i < errorList.length; i++) {
                    points += minusPoints * errorList[i].size();
                }
                return points;
            } else if (errorLevel == XQGradingConfig.ERROR_LEVEL_ALL) {
                for (int i = 0; i < errorList.length; i++) {
                    if (errorList[i].size() > 0) {
                        return minusPoints;
                    }
                }
            } else if (errorLevel == XQGradingConfig.ERROR_LEVEL_KO) {
                for (int i = 0; i < errorList.length; i++) {
                    if (errorList[i].size() > 0) {
                        return maxPoints;
                    }
                }
            }
        }
        return 0;
    }
}