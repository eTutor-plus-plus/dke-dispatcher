package at.jku.dke.etutor.modules.dlg.grading;

import at.jku.dke.etutor.modules.dlg.*;
import at.jku.dke.etutor.modules.dlg.util.XMLUtil;
import oracle.xml.parser.schema.XSDException;
import oracle.xml.parser.v2.XMLDocument;
import oracle.xml.parser.v2.XMLError;
import oracle.xml.parser.v2.XSLException;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Vector;

/**
 * Used for defining grading parameters.
 * <p>
 * The information includes:
 * <ul>
 * <li>The maximum points</li>
 * <li>The minus points for an error of a category</li>
 * <li>The strategy for treating a number of errors of one error category, which is one of the
 * following:</li>
 * <ul type="circle">
 * <li>The specified points are subtracted for each error level of the error</li>
 * <li>The specified points are subtracted only once for an error category</li>
 * <li>If an error occurs, all possible points are subtracted for the whole grading subject (K.O.
 * criterion)</li>
 * </ul>
 * </ul>
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class DatalogScores implements Serializable {

    /**
     * Key for defining that each error of a certain category has to be considered when grading.
     */
    public final static int ERROR_LEVEL_EACH = 1;

    /**
     * Key for defining that an error has to be considered only once for the whole error category
     * when grading.
     */
    public final static int ERROR_LEVEL_ALL = 2;

    /**
     * Key for defining that when an error of a certain category was detected, this is a K.O.
     * criterion for the whole query. The consequence are zero achieved points.
     */
    public final static int ERROR_LEVEL_KO = 3;

    /**
     * Key for the error category, which contains missing predicates.
     */
    public final static String PREDICATES_MISSING = "missing-predicate";

    /**
     * Key for the error category, which contains redundant predicates.
     */
    public final static String PREDICATES_REDUNDANT = "redundant-predicate";

    /**
     * Key for the error category, which contains predicates missing some terms.
     */
    public final static String PREDICATES_ARITY_LOW = "low-term-predicate";

    /**
     * Key for the error category, which contains predicates with too many terms.
     */
    public final static String PREDICATES_ARITY_HIGH = "high-term-predicate";

    /**
     * Key for the error category, which contains missing facts.
     */
    public final static String FACTS_MISSING = "missing-fact";

    /**
     * Key for the error category, which contains redundant facts.
     */
    public final static String FACTS_REDUNDANT = "redundant-fact";

    /**
     * Key for the error category, which contains facts, which are falsely negative.
     */
    public final static String FACTS_NEGATIVE = "negative-fact";

    /**
     * Key for the error category, which contains facts, which are falsely positive.
     */
    public final static String FACTS_POSITIVE = "positive-fact";

    /**
     * An array which contains all the keys for the error categories as defined in this class.
     */
    public final static String[] CATEGORIES = new String[] {PREDICATES_MISSING,
            PREDICATES_REDUNDANT, PREDICATES_ARITY_LOW, PREDICATES_ARITY_HIGH, FACTS_MISSING,
            FACTS_REDUNDANT, FACTS_NEGATIVE, FACTS_POSITIVE};

    private double[] scores;

    private int[] levels;

    private double maxScore;

    /**
     * Creates a new instance of this class.
     * <p>
     * The default values are 0 for all minus points of the corresponding error category and
     * {@link #ERROR_LEVEL_KO}for each error category.
     */
    public DatalogScores() {
        init();
    }

    /**
     * Creates a new instance of this class from an XML document which holds all information used
     * for grading. This document must be valid with regard to the XML Schema defined in
     * {@link DatalogCoreManager#XSD_FILE_SCORES}.
     * 
     * @param doc The XML document containing the grading information.
     * @throws InternalException if any unexpected exception occured when parsing or validating the
     *             given XML document or the XML Schema which is internally defined.
     * @throws ValidationException if the given XML document has been successfully parsed but is not
     *             valid with regard to the internally defined XML Schema.
     * @throws ParameterException if the given XML document is both, wellformed and valid, but
     *             nevertheless contains information which is not applicable for intiializing a new
     *             <code>DatalogScores</code> object.
     */
    public DatalogScores(
            XMLDocument doc) throws InternalException, ValidationException, ParameterException {
        validate(doc);
        init(doc);
    }

    /**
     * Validates the given XML document with the internally defined XML Schema for grading
     * information.
     * 
     * @param doc The XML document containing the grading information.
     * @throws InternalException if any unexpected exception occured when parsing or validating the
     *             given XML document or the XML Schema which is internally defined.
     * @throws ValidationException if the given XML document has been successfully parsed but is not
     *             valid with regard to the internally defined XML Schema.
     */
    private void validate(XMLDocument doc) throws ValidationException, InternalException {
    	URL schemaURL = null;
		try {
			schemaURL = DatalogCoreManager.getResource(DatalogCoreManager.XSD_FILE_SCORES);
		} catch (InvalidResourceException e1) {
		}
		if (schemaURL == null) {
            String message = "Resource '" + DatalogCoreManager.XSD_FILE_SCORES + "' not found.";
            throw new InternalException(message);
        }
        XMLError xmlError = null;
        try {
            xmlError = XMLUtil.validate(doc, schemaURL);
        } catch (XSDException e) {
            String message = "";
            message += "An internal exception was thrown when validating an ";
            message += "XML document with a given XML Schema document.\n" + e.getMessage();
            throw new InternalException(message);
        } catch (IOException e) {
        	String message = "";
            message += "An internal exception was thrown when validating an ";
            message += "XML document with a given XML Schema document.\n" + e.getMessage();
            throw new InternalException(message);
        } catch (SAXException e) {
        	String message = "";
            message += "An internal exception was thrown when validating an ";
            message += "XML document with a given XML Schema document.\n" + e.getMessage();
            throw new InternalException(message);
        }
        if (xmlError.getListTrees().size() > 0) {
            String errorMessages = "";
            Vector errors = xmlError.getListTrees();
            for (int i = 0; i < errors.size(); i++) {
                errorMessages += "\n\t" + xmlError.getMessage(i);
            }
            errorMessages = "XML file is not valid:"
                    + errorMessages;
            throw new ValidationException(errorMessages);
        }
    }

    /**
     * Initializes this <code>DatalogScores</code> object using an XML document which holds all
     * information used for grading. This document must be valid with regard to the XML Schema
     * defined in {@link DatalogCoreManager#XSD_FILE_SCORES}.
     * 
     * @param doc The XML document containing the grading information.
     * @throws InternalException if any unexpected exception occured when parsing or validating the
     *             given XML document or the XML Schema which is internally defined.
     * @throws ValidationException if the given XML document has been successfully parsed but is not
     *             valid with regard to the internally defined XML Schema.
     * @throws ParameterException if the given XML document is both, wellformed and valid, but
     *             nevertheless contains information which is not applicable for intiializing a new
     *             <code>DatalogScores</code> object.
     */
    private void init(XMLDocument doc) throws ValidationException, InternalException,
            ParameterException {
        init();
        try {
            Node maxScoreNode = doc.selectSingleNode("datalog-score/@max-score");
            maxScore = Double.parseDouble(maxScoreNode.getNodeValue());

            String category;
            Node scoreNode;
            String errorMessages = "";
            for (int i = 0; i < CATEGORIES.length; i++) {
                scoreNode = doc.selectSingleNode("//category[@error = '" + CATEGORIES[i] + "']");
                if (scoreNode == null) {
                    errorMessages += "\n\t" + CATEGORIES[i];
                } else {
                    scoreNode = doc.selectSingleNode("//category[@error = '" + CATEGORIES[i]
                            + "']/@less");
                    setScore(CATEGORIES[i], scoreNode.getNodeValue());
                    scoreNode = doc.selectSingleNode("//category[@error = '" + CATEGORIES[i]
                            + "']/@error-level");
                    setErrorLevel(CATEGORIES[i], scoreNode.getNodeValue());
                }
            }
            if (errorMessages.length() > 0) {
                errorMessages = "Missing score parameters:" + errorMessages;
                throw new ValidationException(errorMessages);
            }
        } catch (XSLException e) {
            throw new InternalException(e.getMessage());
        }
    }

    /**
     * Initializes this <code>DatalogScores</code> object with default values. This is 0 for all
     * minus points of the corresponding error category and {@link #ERROR_LEVEL_KO} for each error
     * category.
     */
    private void init() {
        maxScore = 0;
        levels = new int[CATEGORIES.length];
        for (int i = 0; i < CATEGORIES.length; i++) {
            levels[i] = ERROR_LEVEL_KO;
        }
        scores = new double[CATEGORIES.length];
        for (int i = 0; i < CATEGORIES.length; i++) {
            scores[i] = 0;
        }
    }

    /**
     * Sets the maximum points which can be achieved for an exercise using this grading object.
     * 
     * @param maxScore A number recommended to be equal to or greater than 0.
     */
    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    /**
     * Returns the maximum points which can be achieved for an exercise using this grading object.
     * 
     * @return The maximum points.
     */
    public double getMaxScore() {
        return maxScore;
    }

    /**
     * Sets the points which have to be subtracted from some maximum points for an error of a
     * certain category.
     * 
     * @param category One of the category keys as defined in this class.
     * @param score A String representing the number, which is tried to be parsed as
     *            <code>double</code> value. The number will always be stored as absolute number.
     * @return The index of the category, which was set, with regard to {@link #CATEGORIES}.
     * @throws ParameterException if the given score is no valid number or if the category is not
     *             one of the categories as defined in this class.
     */
    public int setScore(String category, String score) throws ParameterException {
        try {
            double value = Double.parseDouble(score);
            return setScore(category, value);
        } catch (NumberFormatException e) {
            String message = "";
            message += score + " is no valid number and can not be used as ";
            message += "score for error category '" + category + "'.";
            throw new ParameterException(message);
        }
    }

    /**
     * Sets the points which have to be subtracted from some maximum points for an error of a
     * certain category.
     * 
     * @param category One of the category keys as defined in this class.
     * @param score The minus points, which will always be stored as absolute number.
     * @return The index of the category, which was set, with regard to {@link #CATEGORIES}.
     * @throws ParameterException if the given category is not one of the categories as defined in
     *             this class.
     */
    public int setScore(String category, double score) throws ParameterException {
        int index = getCategoryIndex(category);
        if (index > -1) {
            scores[index] = Math.abs(score);
            return index;
        }
        String message = "";
        message += "Can't set minus score for error category " + category + ". The category does not exist.";
        throw new ParameterException(message);
    }

    /**
     * Gets the minus points according to a certain error category.
     * 
     * @param category One of the category keys as defined in this class.
     * @return If a number has been set for the category, this returns a number equal to or greater
     *         than 0. The default is 0. If the category is not one of the predifined keys in this
     *         class, this returns -1.
     */
    public double getScore(String category) {
        int index = getCategoryIndex(category);
        if (index > -1) {
            return scores[index];
        }
        return -1;
    }

    /**
     * Sets the error level for an error category, which indicates how often an error of the same
     * category is considered when grading.
     * 
     * @param category One of the category keys as defined in this class.
     * @param errorLevel A String representing the number, which is tried to be parsed as
     *            <code>int</code> value.
     * @return The index of the category, which was set, with regard to {@link #CATEGORIES}, -1 if
     *         the key is not one of the predefined keys in this class.
     * @throws ParameterException if the given level is no valid number, if the error level
     *             represented by the given String is not one of the error level keys as defined in
     *             this class or if the given error level key is not one of the error level keys as
     *             defined in this class.
     */
    public int setErrorLevel(String category, String errorLevel) throws ParameterException {
        try {
            int value = Integer.parseInt(errorLevel);
            return setErrorLevel(category, value);
        } catch (NumberFormatException e) {
            String message = "";
            message += "Invalid grading configuration: The error level ";
            message += errorLevel + " can not be defined for error category '" + category + "'.";
            throw new ParameterException(message);
        }
    }
    
    /**
     * Sets the error level for an error category, which indicates how often an error of the same
     * category is considered when grading.
     * 
     * @param category One of the category keys as defined in this class.
     * @param errorLevel The error level for errors of the specified category. The value must be one
     *            of the error level keys as defined in this class.
     * @return The index of the category, which was set, with regard to {@link #CATEGORIES}.
     * @throws ParameterException if the given error level key is not one of the error level keys as
     *             defined in this class or if the given category is not one of the categories as
     *             defined in this class.
     */
    public int setErrorLevel(String category, int errorLevel) throws ParameterException {
        if (errorLevel != ERROR_LEVEL_EACH && errorLevel != ERROR_LEVEL_ALL
                && errorLevel != ERROR_LEVEL_KO) {
            String message = "";
            message += "Invalid grading configuration: The error level ";
            message += errorLevel + " can not be defined for error category '" + category + "'.";
            throw new ParameterException(message);
        }
        int index = getCategoryIndex(category);
        if (index > -1) {
            levels[index] = errorLevel;
            return index;
        }
        String message = "";
        message += "Can't set minus score for error category " + category;
        message += ". The category does not exist.";
        throw new ParameterException(message);
    }

    /**
     * Gets the error level according to a certain error category.
     * 
     * @param category One of the category keys as defined in this class.
     * @return The error level which has been set for the category. The default is
     *         {@link #ERROR_LEVEL_KO}. If the specified category key is not one of the category
     *         keys as defined in this class, this returns -1.
     */
    public int getErrorLevel(String category) {
        int index = getCategoryIndex(category);
        if (index > -1) {
            return levels[index];
        }
        return -1;
    }

    /**
     * Returns the index of the specified category key in {@link #CATEGORIES}.
     * 
     * @param category One of the category keys as defined in this class.
     * @return The index of the specified category or -1 if not found.
     */
    private int getCategoryIndex(String category) {
        for (int i = 0; i < CATEGORIES.length; i++) {
            if (CATEGORIES[i].equalsIgnoreCase(category)) {
                return i;
            }
        }
        return -1;
    }

}