package at.jku.dke.etutor.modules.xquery.src.etutor.modules.xquery.report;

import java.io.Serializable;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import etutor.modules.xquery.ParameterException;
import etutor.modules.xquery.QuerySyntaxException;
import etutor.modules.xquery.ReportException;
import etutor.modules.xquery.UrlContentException;
import etutor.modules.xquery.WellformednessException;
import etutor.modules.xquery.analysis.NodeError;
import etutor.modules.xquery.analysis.NodeErrorList;
import etutor.modules.xquery.analysis.UrlContent;
import etutor.modules.xquery.analysis.XMLNodeDescription;
import etutor.modules.xquery.analysis.XQAnalysis;
import etutor.modules.xquery.analysis.XQResult;
import etutor.modules.xquery.grading.XQGrading;
import etutor.modules.xquery.util.HTMLConverter;
import etutor.modules.xquery.util.XQResources;

/**
 * This class is used for presenting the results of an analysis that was carried out on two XQuery
 * results, the first considered as the correct query result and the second one as the submitted
 * one.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class XQReport implements XQFeedback, Serializable {

	private String summary;

    private ErrorCategory syntaxError;
    
    private ErrorCategory[] errors;

    private String rawResult;

    private String renderedResult;

    private String xmlReport;

    private String grading;
    
    private String mode;

    private int diagnoseLevel;
    
    private boolean includesGrading;

    /**
     * Constructs a new instance of <code>XQReport</code> with default values. This means that all
     * messages are set to empty Strings and the diagnose level is set to the lowest level.
     */
    public XQReport() {
        super();
        this.diagnoseLevel = DIAGNOSE_NONE;
        this.summary = "";
        this.rawResult = "";
        this.renderedResult = "";
        this.xmlReport = "";
        this.grading = "";
        this.mode = "";
        this.errors = new ErrorCategory[] {};
        this.syntaxError = new ErrorCategory();
        this.includesGrading = false;
    }

    /**
     * Constructs a new instance of <code>XQReport</code> with the specified parameters.
     * 
     * @param analysis The analysis which provides error information that is transformed into
     *            messages, and the query results that are rendered.
     * @param grading A grading object which contains information about points which have been
     *            assigned to the submitted query of the analysis. The grading information will not
     *            be used for this <code>XQReport</code>, if the object is <code>null</code> or
     *            if {@link XQGrading#isReporting()}of the specified object returns false.
     * @param config Configuration parameters for reporting.
     * @throws NullPointerException if any of the parameters is null or if one of the results
     *             contained in the analysis is null.
     * @throws ParameterException if the diagnose level in config is not one of the predefined diagnose
     *             levels.
     * @throws ReportException if any unexpected Exception occured when generating the report.
     */
    public XQReport(
            XQAnalysis analysis, XQGrading grading, XQReportConfig config) throws NullPointerException,
            ParameterException, ReportException {
        this();
        if (analysis == null) {
            throw new NullPointerException();
        }

        this.mode = config.getMode();
        this.includesGrading = config.includesGrading();
        this.diagnoseLevel = checkDiagnoseLevel(config.getDiagnoseLevel());
        
        this.syntaxError = getSyntaxError(analysis);
        this.summary = getGeneralAnalysis(analysis, config.getDiagnoseLevel());
        this.errors = getSemanticErrors(analysis, config.getDiagnoseLevel());
        this.grading = getGrading(grading);
        if (analysis.getResult2() != null) {
            this.rawResult = analysis.getResult2().getRawResult();
        }
        //transform report into XML representation
        XMLReport xmlReport = new XMLReport(analysis, grading, this, isSupplementedResult());
        this.renderedResult = xmlReport.getRenderedResult();
        this.xmlReport = xmlReport.getXMLReport();

    }

    /**
     * Tests if a given level is one of the predefined diagnose levels.
     * 
     * @param level The level to test.
     * @return The level that was passed, if it is a valid diagnose level.
     * @throws ParameterException if the specified level is not one of the predefined diagnose
     *             levels.
     */
    private int checkDiagnoseLevel(int level) throws ParameterException {
        switch (level) {
            case (DIAGNOSE_NONE):
                return level;
            case (DIAGNOSE_LOW):
                return level;
            case (DIAGNOSE_MEDIUM):
                return level;
            case (DIAGNOSE_HIGH):
                return level;
            default:
                {
                    String message = "No valid diagnose level: " + level + ".";
                    throw new ParameterException(message);
                }
        }
    }

    /**
     * Returns the detailed message about syntax errors in the submitted query, as it was returned
     * by the query processor. If urls have been detected in the submitted query, which are not
     * permitted to be accessed for this exercise, this is treated as syntax error as well.
     * 
     * @param analysis The analysis object which holds all information about the submitted query.
     * @return The syntax errors, if any were detected, otherwise otherwise a category object with
     * an empty String as description.
     */
    private ErrorCategory getSyntaxError(XQAnalysis analysis) {
        QuerySyntaxException syntaxException = null;
        XQResult result = analysis.getResult2();
        if (result != null) {
            syntaxException = result.getSyntaxException();
        }
        if (syntaxException != null) {
        	String title = XQResources.getString(XQResources.ERROR_RESULT_SYNTAX);
        	String message = syntaxException.getDescription();
        	
            ErrorCategory error = new ErrorCategory(title);
            error.setDescription(message);
        	return error;
        }
        return this.getUrlContentError(analysis);
    }

    /**
     * Returns the detailed message about wellformedness errors of the submitted query.
     * 
     * @param analysis The analysis object which holds all information about the submitted query.
     * @return The wellformedness errors, if any were detected, otherwise a category object with
     * an empty String as description.new ErrorCategory().
     */
    private ErrorCategory getWellformednessErrors(XQAnalysis analysis) {
        WellformednessException wellformednessException = null;
        XQResult result = analysis.getResult2();
        if (result != null) {
            wellformednessException = result.getWellformednessException();
        }
        if (wellformednessException != null) {
        	String title = XQResources.getString(XQResources.ERROR_RESULT_WELLFORMEDNESS);
        	String message = wellformednessException.getDescription();
        	
            ErrorCategory error = new ErrorCategory(title);
            error.setDescription(message);
        	return error;
        }
        return new ErrorCategory();
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.modules.xquery.XQFeedback#getSyntaxError(boolean)
     */
    public ErrorCategory getSyntaxError(boolean rendered) {
        if (!rendered) {
            return this.syntaxError;
        } 
        ErrorCategory clone = (ErrorCategory)this.syntaxError;
        String description = clone.getDescription();
        String title = clone.getTitle();
        description = this.renderText(description, true);
        title = this.renderText(title, true);
        clone.setDescription(description);
        clone.setTitle(title);
        return clone;
    }

    /**
     * Returns the errors of the query result with regard to document urls and their alias names. Such an error describes
     * an alias name, specified in a document statement within the query, where the corresponding url was not specified
     * in the exercise settings. This error is treated like a syntax error for the report.
     * 
     * @return The url content errors, if any were detected, otherwise a category object with
     * an empty String as description.
     */
    private ErrorCategory getUrlContentError(XQAnalysis analysis) {
        String message = "";
        UrlContentException urlContentException = null;
        XQResult result = analysis.getResult2();
        if (result != null) {
            urlContentException = result.getUrlContentException();
        }
        if (urlContentException != null) {
            String title = XQResources.getString(XQResources.ERROR_RESULT_ALIAS);
            UrlContent[] contents = urlContentException.getUrlContents();
            for (int i = 0; i < contents.length; i++) {
                UrlContent content = contents[i];
                String alias = content.getContent();
                int line = content.getLine();
                int index = content.getStartIndex();
                MessageFormat messageForm = new MessageFormat("");
                messageForm.applyPattern(XQResources
                        .getString(XQResources.ERROR_RESULT_ALIAS_UNDECLARED));
                Format[] formats = new Format[] {null, null, null};
                messageForm.setFormats(formats);
                Object[] messageArguments = new Object[] {alias, new Integer(line),
                        new Integer(index)};
                String msg = messageForm.format(messageArguments);
                if (message.length() > 0) {
                    message += "\n";
                }
                message += msg;
            }
            ErrorCategory error = new ErrorCategory(title);
            error.setDescription(message);
        	return error;
        }
        return new ErrorCategory();
    }

    /**
     * Gets the general analysis text summarizing the results of the analysis.
     * 
     * @param analysis The analysis object which holds all information about the submitted query.
     * @param diagnoseLevel Specifies the diagnose level, which affects how detailed the generated
     *            message is going to be.
     * @return The general analysis text. If the diagnose is on the lowest level this returns an
     *         empty String.
     */
    private String getGeneralAnalysis(XQAnalysis analysis, int diagnoseLevel) {
        XQResult result = analysis.getResult2();
        if (diagnoseLevel > DIAGNOSE_NONE) {
            if (analysis.isCorrect()) {
                return XQResources.getString(XQResources.SOLUTION_CORRECT) + "\n";
            } else {
                String text = XQResources.getString(XQResources.SOLUTION_INCORRECT) + "\n";
                if (diagnoseLevel > DIAGNOSE_LOW) {
                    if (analysis.isValidSolution()) {
                        text += XQResources.getString(XQResources.SOLUTION_VALID) + "\n";
                    } else if (result != null 
                    		&& result.getSyntaxException() == null 
                    		&& result.getWellformednessException() == null
							&& result.getUrlContentException() == null){
                        text += XQResources.getString(XQResources.SOLUTION_NOT_VALID) + "\n";
                    }
                }
                return text;
            }
        }
        return "";

    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.modules.xquery.XQFeedback#getGeneralAnalysis(boolean)
     */
    public String getGeneralAnalysis(boolean rendered) {
        return this.renderText(this.summary, rendered);
    }

    /**
     * Returns a list of mistakes analyzed in the submitted query result.
     * 
     * @param analysis The analysis object which holds all information about the submitted query.
     * @param diagnoseLevel Specifies the diagnose level, which affects how detailed the generated
     *            message is going to be.
     * @return An array of error descriptions. If the diagnose is on the lowest level or on low
     *         level this returns an empty array.
     */
    private ErrorCategory[] getSemanticErrors(XQAnalysis analysis, int diagnoseLevel) {
        if (diagnoseLevel == DIAGNOSE_MEDIUM) {
            return getMediumAnalysis(analysis);
        }
        if (diagnoseLevel == DIAGNOSE_HIGH) {
            return getDetailedAnalysis(analysis);
        }
        return new ErrorCategory[] {};
    }

    /**
     * Returns a list of mistakes analyzed in the submitted query result on a medium diagnose level.
     * 
     * @param analysis The analysis object which holds all information about the submitted query.
     * @return An array of error descriptions.
     */
    private ErrorCategory[] getMediumAnalysis(XQAnalysis analysis) {
    	
        ArrayList errorList = new ArrayList();

        ErrorCategory wellformednessErrors = this.getWellformednessErrors(analysis);
        if (wellformednessErrors != null && !wellformednessErrors.getDescription().trim().equals("")) {
        	errorList.add(wellformednessErrors);
        }
        
        boolean displacedNodes = analysis.getDisplacedNodes().containsErrors();
        if (displacedNodes) {
            int category = ErrorCategory.NODES_DISPLACED;
            String title = XQResources.getString(XQResources.NODES_DISPLACED_TITLE);
            String description = XQResources.getString(XQResources.NODES_DISPLACED) + "\n";
            ErrorCategory displacedNodesCategory = new ErrorCategory(category, title);
            displacedNodesCategory.setDescription(description);
            errorList.add(displacedNodesCategory);
        }

        boolean redundantNodes = analysis.getRedundantNodes().containsErrors();
        boolean redundantInsteadNodes = analysis.getRedundantInsteadNodes().containsErrors();
        if (redundantNodes || redundantInsteadNodes) {
            int category = ErrorCategory.NODES_REDUNDANT;
            String title = XQResources.getString(XQResources.NODES_REDUNDANT_TITLE);
            String description = XQResources.getString(XQResources.NODES_REDUNDANT) + "\n";
            ErrorCategory redundantNodesCategory = new ErrorCategory(category, title);
            redundantNodesCategory.setDescription(description);
            errorList.add(redundantNodesCategory);
        }

        boolean missingPreviousNodes = analysis.getMissingPreviousNodes().containsErrors();
        boolean missingNextNodes = analysis.getMissingNextNodes().containsErrors();
        boolean missingInnerNodes = analysis.getMissingInnerNodes().containsErrors();
        boolean missingInsteadNodes = analysis.getMissingInsteadNodes().containsErrors();
        if (missingPreviousNodes || missingNextNodes || missingInnerNodes || missingInsteadNodes) {
            int category = ErrorCategory.NODES_MISSING;
            String title = XQResources.getString(XQResources.NODES_MISSING_TITLE);
            String description = XQResources.getString(XQResources.NODES_MISSING) + "\n";
            ErrorCategory missingNodesCategory = new ErrorCategory(category, title);
            missingNodesCategory.setDescription(description);
            errorList.add(missingNodesCategory);
        }

        boolean redundantAttributes = analysis.getRedundantAttributes().containsErrors();
        if (redundantAttributes) {
            int category = ErrorCategory.ATTRIBUTES_REDUNDANT;
            String title = XQResources.getString(XQResources.NODES_REDUNDANT_TITLE);
            String description = XQResources.getString(XQResources.ATTRIBUTES_REDUNDANT) + "\n";
            ErrorCategory redundantAttributesCategory = new ErrorCategory(category, title);
            redundantAttributesCategory.setDescription(description);
            errorList.add(redundantAttributesCategory);
        }
        boolean missingAttributes = analysis.getMissingAttributes().containsErrors();
        if (missingAttributes) {
            int category = ErrorCategory.ATTRIBUTES_MISSING;
            String title = XQResources.getString(XQResources.ATTRIBUTES_MISSING_TITLE);
            String description = XQResources.getString(XQResources.ATTRIBUTES_MISSING) + "\n";
            ErrorCategory missingAttributesCategory = new ErrorCategory(category, title);
            missingAttributesCategory.setDescription(description);
            errorList.add(missingAttributesCategory);
        }
        boolean incorrectAttributeValues = analysis.getIncorrectAttributeValues().containsErrors();
        boolean incorrectTextValues = analysis.getIncorrectTextValues().containsErrors();
        if (incorrectAttributeValues || incorrectTextValues) {
            int category = ErrorCategory.VALUES_INCORRECT;
            String title = XQResources.getString(XQResources.VALUES_INCORRECT_TITLE);
            String description = XQResources.getString(XQResources.VALUES_INCORRECT) + "\n";
            ErrorCategory incorrectValuesCategory = new ErrorCategory(category, title);
            incorrectValuesCategory.setDescription(description);
            errorList.add(incorrectValuesCategory);
        }

        return (ErrorCategory[])errorList.toArray(new ErrorCategory[] {});

    }

    /**
     * Returns a list of mistakes analyzed in the submitted query result on a high diagnose level.
     * 
     * @param analysis The analysis object which holds all information about the submitted query.
     * @return An array of error descriptions.
     */
    private ErrorCategory[] getDetailedAnalysis(XQAnalysis analysis) {

        int category;
        String title;

        ErrorCategory wellformednessError;
        ErrorCategory displacedNodesCategory;
        ErrorCategory missingNodesCategory;
        ErrorCategory redundantNodesCategory;
        ErrorCategory incorrectValuesCategory;
        ErrorCategory missingAttributesCategory;
        ErrorCategory redundantAttributesCategory;

        wellformednessError = this.getWellformednessErrors(analysis);
        
        category = ErrorCategory.NODES_DISPLACED;
        title = XQResources.getString(XQResources.NODES_DISPLACED_TITLE);
        displacedNodesCategory = new ErrorCategory(category, title);

        category = ErrorCategory.NODES_MISSING;
        title = XQResources.getString(XQResources.NODES_MISSING_TITLE);
        missingNodesCategory = new ErrorCategory(category, title);

        category = ErrorCategory.NODES_REDUNDANT;
        title = XQResources.getString(XQResources.NODES_REDUNDANT_TITLE);
        redundantNodesCategory = new ErrorCategory(category, title);

        category = ErrorCategory.VALUES_INCORRECT;
        title = XQResources.getString(XQResources.VALUES_INCORRECT_TITLE);
        incorrectValuesCategory = new ErrorCategory(category, title);

        category = ErrorCategory.ATTRIBUTES_MISSING;
        title = XQResources.getString(XQResources.ATTRIBUTES_MISSING_TITLE);
        missingAttributesCategory = new ErrorCategory(category, title);

        category = ErrorCategory.ATTRIBUTES_REDUNDANT;
        title = XQResources.getString(XQResources.NODES_REDUNDANT_TITLE);
        redundantAttributesCategory = new ErrorCategory(category, title);

        NodeErrorList displacedNodes = analysis.getDisplacedNodes();
        if (displacedNodes.containsErrors()) {
            StringBuffer detailedAnalyze = new StringBuffer();

            NodeError[] errorList = displacedNodes.getErrorList();
            for (int i = 0; i < errorList.length; i++) {
                String xPathNode = errorList[i].getXPath();
                XMLNodeDescription[] list = errorList[i].getAllNodes();

                // ---------- prepare the message --------------- //
                String result = null;
                if (list.length == 1) {
                    MessageFormat messageForm = new MessageFormat("");
                    messageForm.applyPattern(XQResources.getString(XQResources.NODES_DISPLACED_SI));
                    Format[] formats = new Format[] {null};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = new Object[] {getRootExcluded(xPathNode)};
                    result = messageForm.format(messageArguments);
                } else if (list.length > 1) {
                    MessageFormat messageForm = new MessageFormat("");
                    messageForm.applyPattern(XQResources.getString(XQResources.NODES_DISPLACED_PL));
                    Format[] formats = new Format[] {NumberFormat.getInstance(), null};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = new Object[] {new Integer(list.length),
                            getRootExcluded(xPathNode)};
                    result = messageForm.format(messageArguments);
                } else {
                    // The list of a NodeError has at least one entry
                }
                // ---------- apply the message --------------- //
                if (result != null) {
                    detailedAnalyze.append(result + ":\n");

                    for (int j = 0; j < list.length; j++) {
                        String nodeDescription = list[j].getDescription();
                        detailedAnalyze.append("\t" + nodeDescription + "\n");
                    }
                }
            }
            String description = detailedAnalyze.toString();
            displacedNodesCategory.appendDescription(description);
        }
        NodeErrorList redundantNodes = analysis.getRedundantNodes();
        if (redundantNodes.containsErrors()) {
            StringBuffer detailedAnalyze = new StringBuffer();

            // ---------- prepare the message --------------- //
            String result;
            if (redundantNodes.size() == 1) {
                result = XQResources.getString(XQResources.NODES_REDUNDANT_SI);
            } else {
                MessageFormat messageForm = new MessageFormat("");
                messageForm.applyPattern(XQResources.getString(XQResources.NODES_REDUNDANT_PL));
                Format[] formats = new Format[] {NumberFormat.getInstance()};
                messageForm.setFormats(formats);
                Object[] messageArguments = new Object[] {new Integer(redundantNodes.size())};
                result = messageForm.format(messageArguments);
            }
            // ---------- apply the message --------------- //
            detailedAnalyze.append(result + ":\n");
            NodeError[] errorList = redundantNodes.getErrorList();
            for (int i = 0; i < errorList.length; i++) {
                String xPathNode = errorList[i].getXPath();
                detailedAnalyze.append("\t" + getRootExcluded(xPathNode) + "\n");
            }
            String description = detailedAnalyze.toString();
            redundantNodesCategory.appendDescription(description);
        }
        //This one is related to the category as returned by getMissingInsteadNodes(),
        NodeErrorList redundantInsteadNodes = analysis.getRedundantInsteadNodes();
        if (redundantInsteadNodes.containsErrors()) {
            StringBuffer detailedAnalyze = new StringBuffer();

            NodeError[] errorList = redundantInsteadNodes.getErrorList();
            for (int i = 0; i < errorList.length; i++) {
                String xPathNode = errorList[i].getXPath();
                XMLNodeDescription[] list = errorList[i].getAllNodes();

                // ---------- prepare the message --------------- //
                String result = null;
                if (list.length == 1) {
                    MessageFormat messageForm = new MessageFormat("");
                    messageForm.applyPattern(XQResources
                            .getString(XQResources.NODES_REDUNDANT_SI));
                    Format[] formats = new Format[] {null};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = new Object[] {getRootExcluded(xPathNode)};
                    result = messageForm.format(messageArguments);
                } else if (list.length > 1) {
                    MessageFormat messageForm = new MessageFormat("");
                    messageForm.applyPattern(XQResources
                            .getString(XQResources.NODES_REDUNDANT_PL));
                    Format[] formats = new Format[] {NumberFormat.getInstance(), null};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = new Object[] {new Integer(list.length),
                            getRootExcluded(xPathNode)};
                    result = messageForm.format(messageArguments);
                } else {
                    // The list of a NodeError has at least one entry
                }
                // ---------- apply the message --------------- //
                if (result != null) {
                    detailedAnalyze.append(result + ":\n");
                    for (int j = 0; j < list.length; j++) {
                        String nodeDescription = list[j].getDescription();
                        detailedAnalyze.append("\t" + nodeDescription + "\n");
                    }

                }
            }
            //TODO: verify if error is comparable between textual and graphical representation,
            // equal to redundant/missing nodes?
            String description = detailedAnalyze.toString();
            redundantNodesCategory.appendDescription(description);
        }
        //This one is related to the category as returned by getRedundantInsteadNodes(),
        NodeErrorList missingInsteadNodes = analysis.getMissingInsteadNodes();
        if (missingInsteadNodes.containsErrors()) {
            StringBuffer detailedAnalyze = new StringBuffer();

            NodeError[] errorList = missingInsteadNodes.getErrorList();
            for (int i = 0; i < errorList.length; i++) {
                String xPathNode = errorList[i].getXPath();
                XMLNodeDescription[] list = errorList[i].getAllNodes();

                // ---------- prepare the message --------------- //
                String result = null;
                if (list.length == 1) {
                    MessageFormat messageForm = new MessageFormat("");
                    messageForm.applyPattern(XQResources
                            .getString(XQResources.NODES_MISSING_INSTEAD_SI));
                    Format[] formats = new Format[] {null};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = new Object[] {getRootExcluded(xPathNode)};
                    result = messageForm.format(messageArguments);
                } else if (list.length > 1) {
                    MessageFormat messageForm = new MessageFormat("");
                    messageForm.applyPattern(XQResources
                            .getString(XQResources.NODES_MISSING_INSTEAD_PL));
                    Format[] formats = new Format[] {NumberFormat.getInstance(), null};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = new Object[] {new Integer(list.length),
                            getRootExcluded(xPathNode)};
                    result = messageForm.format(messageArguments);
                } else {
                    // The list of a NodeError has at least one entry
                }
                // ---------- apply the message --------------- //
                if (result != null) {
                    detailedAnalyze.append(result + ":\n");
                    for (int j = 0; j < list.length; j++) {
                        String nodeDescription = list[j].getDescription();
                        detailedAnalyze.append("\t" + nodeDescription + "\n");
                    }

                }
            }
            //TODO: verify if error is comparable between textual and graphical representation,
            // equal to redundant/missing nodes?
            String description = detailedAnalyze.toString();
            missingNodesCategory.appendDescription(description);
        }
        NodeErrorList missingPreviousNodes = analysis.getMissingPreviousNodes();
        if (missingPreviousNodes.containsErrors()) {
            StringBuffer detailedAnalyze = new StringBuffer();

            NodeError[] errorList = missingPreviousNodes.getErrorList();
            for (int i = 0; i < errorList.length; i++) {
                String xPathNode = errorList[i].getXPath();
                XMLNodeDescription[] list = errorList[i].getAllNodes();

                // ---------- prepare the message --------------- //
                String result = null;
                if (list.length == 1) {
                    MessageFormat messageForm = new MessageFormat("");
                    messageForm
                            .applyPattern(XQResources.getString(XQResources.NODES_MISSING_BEFORE_SI));
                    Format[] formats = new Format[] {null};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = new Object[] {getRootExcluded(xPathNode)};
                    result = messageForm.format(messageArguments);
                } else if (list.length > 1) {
                    MessageFormat messageForm = new MessageFormat("");
                    messageForm
                            .applyPattern(XQResources.getString(XQResources.NODES_MISSING_BEFORE_PL));
                    Format[] formats = new Format[] {NumberFormat.getInstance(), null};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = new Object[] {new Integer(list.length),
                            getRootExcluded(xPathNode)};
                    result = messageForm.format(messageArguments);
                } else {
                    // The list of a NodeError has at least one entry
                }
                // ---------- apply the message --------------- //
                if (result != null) {
                    detailedAnalyze.append(result + ":\n");
                    for (int j = 0; j < list.length; j++) {
                        String nodeDescription = list[j].getDescription();
                        detailedAnalyze.append("\t" + nodeDescription + "\n");
                    }

                }
            }
            String description = detailedAnalyze.toString();
            missingNodesCategory.appendDescription(description);
        }
        NodeErrorList missingNextNodes = analysis.getMissingNextNodes();
        if (missingNextNodes.containsErrors()) {
            StringBuffer detailedAnalyze = new StringBuffer();

            NodeError[] errorList = missingNextNodes.getErrorList();
            for (int i = 0; i < errorList.length; i++) {
                String xPathNode = errorList[i].getXPath();
                XMLNodeDescription[] list = errorList[i].getAllNodes();

                // ---------- prepare the message --------------- //
                String result = null;
                if (list.length == 1) {
                    MessageFormat messageForm = new MessageFormat("");
                    messageForm.applyPattern(XQResources.getString(XQResources.NODES_MISSING_AFTER_SI));
                    Format[] formats = new Format[] {null};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = new Object[] {getRootExcluded(xPathNode)};
                    result = messageForm.format(messageArguments);
                } else if (list.length > 1) {
                    MessageFormat messageForm = new MessageFormat("");
                    messageForm.applyPattern(XQResources.getString(XQResources.NODES_MISSING_AFTER_PL));
                    Format[] formats = new Format[] {NumberFormat.getInstance(), null};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = new Object[] {new Integer(list.length),
                            getRootExcluded(xPathNode)};
                    result = messageForm.format(messageArguments);
                } else {
                    // The list of a NodeError has at least one entry
                }
                // ---------- apply the message --------------- //
                if (result != null) {
                    detailedAnalyze.append(result + ":\n");
                    for (int j = 0; j < list.length; j++) {
                        String nodeDescription = list[j].getDescription();
                        detailedAnalyze.append("\t" + nodeDescription + "\n");
                    }

                }
            }
            String description = detailedAnalyze.toString();
            missingNodesCategory.appendDescription(description);
        }
        NodeErrorList missingInnerNodes = analysis.getMissingInnerNodes();
        if (missingInnerNodes.containsErrors()) {
            StringBuffer detailedAnalyze = new StringBuffer();

            NodeError[] errorList = missingInnerNodes.getErrorList();
            for (int i = 0; i < errorList.length; i++) {
                String xPathNode = errorList[i].getXPath();
                XMLNodeDescription[] list = errorList[i].getAllNodes();

                // ---------- prepare the message --------------- //
                String result = null;
                if (list.length == 1) {
                    MessageFormat messageForm = new MessageFormat("");
                    if (analysis.isRootNode(xPathNode)) {
                        result = XQResources.getString(XQResources.NODES_MISSING_ROOT_SI);
                    } else {
                        messageForm
                                .applyPattern(XQResources.getString(XQResources.NODES_MISSING_IN_SI));
                        Format[] formats = new Format[] {null};
                        messageForm.setFormats(formats);
                        Object[] messageArguments = new Object[] {getRootExcluded(xPathNode)};
                        result = messageForm.format(messageArguments);
                    }
                } else if (list.length > 1) {
                    MessageFormat messageForm = new MessageFormat("");
                    if (analysis.isRootNode(xPathNode)) {
                        messageForm.applyPattern(XQResources
                                .getString(XQResources.NODES_MISSING_ROOT_PL));
                        Format[] formats = new Format[] {NumberFormat.getInstance()};
                        messageForm.setFormats(formats);
                        Object[] messageArguments = new Object[] {new Integer(list.length)};
                        result = messageForm.format(messageArguments);
                    } else {
                        messageForm
                                .applyPattern(XQResources.getString(XQResources.NODES_MISSING_IN_PL));
                        Format[] formats = new Format[] {NumberFormat.getInstance(), null};
                        messageForm.setFormats(formats);
                        Object[] messageArguments = new Object[] {new Integer(list.length),
                                getRootExcluded(xPathNode)};
                        result = messageForm.format(messageArguments);
                    }
                } else {
                    // The list of a NodeError has at least one entry
                }
                // ---------- apply the message --------------- //
                if (result != null) {
                    detailedAnalyze.append(result + ":\n");
                    for (int j = 0; j < list.length; j++) {
                        String nodeDescription = list[j].getDescription();
                        detailedAnalyze.append("\t" + nodeDescription + "\n");
                    }

                }
            }
            String description = detailedAnalyze.toString();
            missingNodesCategory.appendDescription(description);
        }
        NodeErrorList redundantAttributes = analysis.getRedundantAttributes();
        if (redundantAttributes.containsErrors()) {
            StringBuffer detailedAnalyze = new StringBuffer();

            NodeError[] errorList = redundantAttributes.getErrorList();
            for (int i = 0; i < errorList.length; i++) {
                String xPathNode = errorList[i].getXPath();
                XMLNodeDescription[] list = errorList[i].getAllNodes();

                // ---------- prepare the message --------------- //
                String result = null;
                if (list.length == 1) {
                    MessageFormat messageForm = new MessageFormat("");
                    messageForm
                            .applyPattern(XQResources.getString(XQResources.ATTRIBUTES_REDUNDANT_SI));
                    Format[] formats = new Format[] {null};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = new Object[] {getRootExcluded(xPathNode)};
                    result = messageForm.format(messageArguments);
                } else if (list.length > 1) {
                    MessageFormat messageForm = new MessageFormat("");
                    messageForm
                            .applyPattern(XQResources.getString(XQResources.ATTRIBUTES_REDUNDANT_PL));
                    Format[] formats = new Format[] {NumberFormat.getInstance(), null};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = new Object[] {new Integer(list.length),
                            getRootExcluded(xPathNode)};
                    result = messageForm.format(messageArguments);
                } else {
                    // The list of a NodeError has at least one entry
                }
                // ---------- apply the message --------------- //
                if (result != null) {
                    detailedAnalyze.append(result + ":\n");
                    for (int j = 0; j < list.length; j++) {
                        String nodeDescription = list[j].getDescription();
                        detailedAnalyze.append("\t" + nodeDescription + "\n");
                    }

                }
            }
            String description = detailedAnalyze.toString();
            redundantAttributesCategory.appendDescription(description);
        }
        NodeErrorList missingAttributes = analysis.getMissingAttributes();
        if (missingAttributes.containsErrors()) {
            StringBuffer detailedAnalyze = new StringBuffer();

            NodeError[] errorList = missingAttributes.getErrorList();
            for (int i = 0; i < errorList.length; i++) {
                String xPathNode = errorList[i].getXPath();
                XMLNodeDescription[] list = errorList[i].getAllNodes();

                // ---------- prepare the message --------------- //
                String result = null;
                if (list.length == 1) {
                    MessageFormat messageForm = new MessageFormat("");
                    messageForm.applyPattern(XQResources.getString(XQResources.ATTRIBUTES_MISSING_SI));
                    Format[] formats = new Format[] {null};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = new Object[] {getRootExcluded(xPathNode)};
                    result = messageForm.format(messageArguments);
                } else if (list.length > 1) {
                    MessageFormat messageForm = new MessageFormat("");
                    messageForm.applyPattern(XQResources.getString(XQResources.ATTRIBUTES_MISSING_PL));
                    Format[] formats = new Format[] {NumberFormat.getInstance(), null};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = new Object[] {new Integer(list.length),
                            getRootExcluded(xPathNode)};
                    result = messageForm.format(messageArguments);
                } else {
                    // The list of a NodeError has at least one entry
                }
                // ---------- apply the message --------------- //
                if (result != null) {
                    detailedAnalyze.append(result + ":\n");
                    for (int j = 0; j < list.length; j++) {
                        String nodeDescription = list[j].getDescription();
                        detailedAnalyze.append("\t" + nodeDescription + "\n");
                    }

                }
            }
            String description = detailedAnalyze.toString();
            missingAttributesCategory.appendDescription(description);
        }
        NodeErrorList incorrectAttributeValues = analysis.getIncorrectAttributeValues();
        if (incorrectAttributeValues.containsErrors()) {
            StringBuffer detailedAnalyze = new StringBuffer();

            NodeError[] errorList = incorrectAttributeValues.getErrorList();
            for (int i = 0; i < errorList.length; i++) {
                String xPathNode = errorList[i].getXPath();
                XMLNodeDescription[] list = errorList[i].getAllNodes();

                // ---------- prepare the message --------------- //
                String result = null;
                if (list.length == 1) {
                    MessageFormat messageForm = new MessageFormat("");
                    messageForm.applyPattern(XQResources
                            .getString(XQResources.VALUES_INCORRECT_SI));
                    Format[] formats = new Format[] {null};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = new Object[] {getRootExcluded(xPathNode)};
                    result = messageForm.format(messageArguments);
                } else if (list.length > 1) {
                    MessageFormat messageForm = new MessageFormat("");
                    messageForm.applyPattern(XQResources
                            .getString(XQResources.VALUES_INCORRECT_PL));
                    Format[] formats = new Format[] {NumberFormat.getInstance(), null};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = new Object[] {new Integer(list.length),
                            getRootExcluded(xPathNode)};
                    result = messageForm.format(messageArguments);
                } else {
                    // The list of a NodeError has at least one entry
                }
                // ---------- apply the message --------------- //
                if (result != null) {
                    detailedAnalyze.append(result + ":\n");
                    for (int j = 0; j < list.length; j++) {
                        String nodeDescription = list[j].getDescription();
                        detailedAnalyze.append("\t" + nodeDescription + "\n");
                    }

                }
            }
            String description = detailedAnalyze.toString();
            incorrectValuesCategory.appendDescription(description);
        }
        NodeErrorList incorrectTextValues = analysis.getIncorrectTextValues();
        if (incorrectTextValues.containsErrors()) {
            StringBuffer detailedAnalyze = new StringBuffer();

            NodeError[] errorList = incorrectTextValues.getErrorList();
            for (int i = 0; i < errorList.length; i++) {
                String xPathNode = errorList[i].getXPath();
                XMLNodeDescription[] list = errorList[i].getAllNodes();

                // ---------- prepare the message --------------- //
                String result = null;
                if (list.length == 1) {
                    MessageFormat messageForm = new MessageFormat("");
                    messageForm.applyPattern(XQResources
                            .getString(XQResources.VALUES_INCORRECT_SI));
                    Format[] formats = new Format[] {null};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = new Object[] {getRootExcluded(xPathNode)};
                    result = messageForm.format(messageArguments);
                } else if (list.length > 1) {
                    MessageFormat messageForm = new MessageFormat("");
                    messageForm.applyPattern(XQResources
                            .getString(XQResources.VALUES_INCORRECT_PL));
                    Format[] formats = new Format[] {NumberFormat.getInstance(), null};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = new Object[] {new Integer(list.length),
                            getRootExcluded(xPathNode)};
                    result = messageForm.format(messageArguments);
                } else {
                    // The list of a NodeError has at least one entry
                }
                // ---------- apply the message --------------- //
                if (result != null) {
                    detailedAnalyze.append(result + ":\n");
                    for (int j = 0; j < list.length; j++) {
                        String nodeDescription = list[j].getDescription();
                        detailedAnalyze.append("\t" + nodeDescription + "\n");
                    }

                }
            }
            String description = detailedAnalyze.toString();
            incorrectValuesCategory.appendDescription(description);
        }
        
        ArrayList errorList = new ArrayList();

        if (wellformednessError.getDescription().length() > 0) {
            errorList.add(wellformednessError);
        }
        if (displacedNodesCategory.getDescription().length() > 0) {
            errorList.add(displacedNodesCategory);
        }
        if (missingNodesCategory.getDescription().length() > 0) {
            errorList.add(missingNodesCategory);
        }
        if (redundantNodesCategory.getDescription().length() > 0) {
            errorList.add(redundantNodesCategory);
        }
        if (incorrectValuesCategory.getDescription().length() > 0) {
            errorList.add(incorrectValuesCategory);
        }
        if (missingAttributesCategory.getDescription().length() > 0) {
            errorList.add(missingAttributesCategory);
        }
        if (redundantAttributesCategory.getDescription().length() > 0) {
            errorList.add(redundantAttributesCategory);
        }
        return (ErrorCategory[])errorList.toArray(new ErrorCategory[] {});

    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.modules.xquery.XQFeedback#getAnalysis(boolean)
     */
    public ErrorCategory[] getSemanticErrors(boolean rendered) {
        if (!rendered) {
            return this.errors;
        }
        ErrorCategory[] renderedAnalyze = new ErrorCategory[this.errors.length];
        for (int i = 0; i < renderedAnalyze.length; i++) {
            ErrorCategory clone = (ErrorCategory)errors[i].clone();
            String description = clone.getDescription();
            String title = clone.getTitle();
            description = this.renderText(description, true);
            title = this.renderText(title, true);
            clone.setDescription(description);
            clone.setTitle(title);
            renderedAnalyze[i] = clone;
        }
        return renderedAnalyze;
    }

    /**
     * Generates an overall message which summarizes single messages as returned by methods of this
     * class. By default, the messages are generated on the highest diagnose level.
     * 
     * @param analysis The analysis whose contents are transformed into messages.
     * @return A summary of messages.
     */
    private String getAnalysisSummary(XQAnalysis analysis) {
        String summary = "";
        String generalAnalysis = this.getGeneralAnalysis(analysis, DIAGNOSE_HIGH);
        String detailedAnalysis = "";
        ErrorCategory[] errors = this.getSemanticErrors(analysis, DIAGNOSE_HIGH);
        for (int i = 0; i < errors.length; i++) {
            if (i > 0) {
                detailedAnalysis += "\n";
            }
            detailedAnalysis += errors[i].getDescription();
        }
        if (generalAnalysis.length() > 0) {
            summary += generalAnalysis + "\n";
        }
        if (detailedAnalysis.length() > 0) {
            summary += detailedAnalysis + "\n";
        }
        return summary;
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.modules.xquery.XQFeedback#getRenderedResult()
     */
    public String getRenderedResult() {
        return renderedResult;
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.modules.xquery.XQFeedback#getRawResult()
     */
    public String getRawResult() {
        return rawResult;
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.modules.xquery.XQFeedback#getDiagnoseLevel()
     */
    public int getDiagnoseLevel() {
        return this.diagnoseLevel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.modules.xquery.XQFeedback#getMode()
     */
    public String getMode() {
        return this.mode;
    }
    
    /**
     * Cuts off the first name identifier from an expression, which is interpreted as XPath
     * expression. This is used for making the first identifier of the XPath expressions invisible
     * when reporting, as this is the implicit root element of an XQuery query result.
     * 
     * @param path The expression.
     * @return The reduced String.
     */
    private String getRootExcluded(String path) {
        int pos;
        if (path == null || path.startsWith("//") || (pos = path.indexOf("/")) < 0) {
            return path;
        }
        if (pos == 0) {
            pos = path.indexOf('/', pos + 1);
        }
        if (pos < 0) {
            return path;
        }
        return path.substring(pos);
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.modules.xquery.XQFeedback#isSupplementedResult()
     */
    public boolean isSupplementedResult() {
        return this.diagnoseLevel == DIAGNOSE_HIGH;
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.modules.xquery.XQFeedback#getXMLReport()
     */
    public String getXMLReport() {
        return xmlReport;
    }

    /**
     * Returns any text either unchanged or rendered for HTML compatibility.
     * 
     * @param text The text to transform.
     * @param render Indicates if the text is to be returned unchanged or rendered.
     * @return The modified or not modified text.
     */
    private String renderText(String text, boolean render) {
        if (render) {
            return HTMLConverter.stringToHTMLString(text);
        }
        return text;
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.modules.datalog.XQFeedback#getGrading()
     */
    public String getGrading(boolean rendered) {
        return renderText(grading, rendered);
    }

    /**
     * Returns a text which was generated using the information of the grading object.
     * 
     * @param grading An object which contains information about the maximum of points which can be
     *            achieved for a datalog exercise, and the points actually assigned for a submitted
     *            query.
     * @return The text generated from the grading object or an empty String if the grading object
     *         is <code>null</code> or if {@link XQGrading#isReporting()}of the specified object
     *         returns false.
     */
    private String getGrading(XQGrading grading) {
        if (grading != null && this.includesGrading) {
            MessageFormat messageForm = new MessageFormat("");
            messageForm.applyPattern(XQResources.getString(XQResources.GRADING_POINTS));
            Format[] formats = new Format[] {null, null};
            messageForm.setFormats(formats);
            Object[] messageArguments = new Object[] {new Double(grading.getPoints()),
                    new Double(grading.getMaxPoints())};
            String message = messageForm.format(messageArguments);
            return message;
        }
        return "";
    }

}