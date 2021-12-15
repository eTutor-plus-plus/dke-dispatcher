package at.jku.dke.etutor.modules.dlg.report;

import at.jku.dke.etutor.core.evaluation.DefaultReport;
import at.jku.dke.etutor.modules.dlg.ParameterException;
import at.jku.dke.etutor.modules.dlg.QuerySyntaxException;
import at.jku.dke.etutor.modules.dlg.ReportException;
import at.jku.dke.etutor.modules.dlg.analysis.DatalogAnalysis;
import at.jku.dke.etutor.modules.dlg.analysis.DatalogResult;
import at.jku.dke.etutor.modules.dlg.grading.DatalogGrading;
import at.jku.dke.etutor.modules.dlg.util.DLResources;
import at.jku.dke.etutor.modules.dlg.util.HTMLConverter;
import at.jku.dke.etutor.modules.dlg.util.XMLUtil;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * This class is used for presenting the results of an analysis that was carried out on two Datalog
 * results, the first considered as the correct query result and the second one as the submitted
 * one.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class DatalogReport extends DefaultReport implements DatalogFeedback, Serializable {

	/**
     * The logger used for logging.
     */
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(XMLReport.class);
    private String generalAnalyze;
    private String consistencyAnalyze;
    private String syntaxError;
    private ErrorCategory[] analyze;
    /**
     * @deprecated
     */
    private String defaultCSS;
    private String rawResult;
    private String renderedResult;
    private String xmlReport;
    private String grading;
    private int diagnoseLevel;

    /**
     * Constructs a new instance of <code>DatalogReport</code> with default values. This means
     * that all messages are set to empty Strings and the diagnose level is set to the lowest level.
     */
    public DatalogReport() {
        super();
        this.diagnoseLevel = DIAGNOSE_NONE;
        this.syntaxError = "";
        this.generalAnalyze = "";
        this.consistencyAnalyze = "";
        this.rawResult = "";
        this.defaultCSS = "";
        this.renderedResult = "";
        this.xmlReport = "";
        this.grading = "";
        this.analyze = new ErrorCategory[] {};
    }

    /**
     * Constructs a new instance of <code>DatalogReport</code> with the specified parameters.
     * 
     * @param analysis The analysis which provides error information that is transformed into
     *            messages, and the query results that are rendered.
     * @param grading A grading object which contains information about points which have been
     *            assigned to the submitted query of the analysis. The grading information will not
     *            be used for this <code>DatalogReport</code>, if the object is <code>null</code>
     *            or if {@link DatalogGrading#isReporting()}of the specified object returns false.
     * @param filters Using the filter option it can be specified which predicates, identified by
     *            name, are considered in the rendering of the result. If this is null, all
     *            available predicates in the submitted result will be considered.
     * @param diagnoseLevel Specifies the diagnose level, which affects how detailed the generated
     *            messages are. Basically, if a report is generated on the lowest level,
     *            {@link #getAnalysis(boolean)},{@link #getConsistencyAnalysis(boolean)}and
     *            {@link #getGeneralAnalysis(boolean)}do not return any information. Moreover,
     *            {@link #getRenderedResult()}returns the HTML rendered result without any colored
     *            elements.
     * @throws NullPointerException if any of the parameters is null or if one of the results
     *             contained in the analysis is null.
     * @throws ParameterException if the diagnose level is not one of the predefined diagnose
     *             levels.
     * @throws ReportException if any unexpected Exception occured when generating the report.
     */
    public DatalogReport (
            DatalogAnalysis analysis, DatalogGrading grading, String[] filters, int diagnoseLevel)
            throws NullPointerException, ParameterException, ReportException {
        this();
        this.diagnoseLevel = checkDiagnoseLevel(diagnoseLevel);
        this.syntaxError = getSyntaxErrors(analysis);
        this.generalAnalyze = getGeneralAnalysis(analysis, diagnoseLevel);
        this.analyze = getAnalysis(analysis, diagnoseLevel);
        this.grading = getGrading(grading);

        DatalogResult result = analysis.getResult2();
        if (result != null) {
            this.rawResult = analysis.getResult2().getRawResult();
        }
        XMLReport xmlReport = createXMLReport(analysis, grading, filters, isSupplementedResult());

        this.xmlReport = xmlReport.getXMLReport();
        if (analysis.isDebugMode()) {
            LOGGER
                    .info("Debug mode: writing the generated report as XML.");
            try {

                File tempReport = XMLUtil.generateTempFile(analysis.getReportFileParameter());
                XMLUtil.printFile(this.xmlReport, tempReport);
                LOGGER.info("Written to file " + tempReport.getAbsolutePath());

            } catch (IOException e) {
            	String msg = "";
                msg += "An internal exception was thrown when writing a file written only in debug modus: ";
                msg += e.getClass().getName() + ": " + e.getMessage();
                LOGGER.error(msg);
                throw new ReportException(msg, e);
            }
        }
        
        this.renderedResult = xmlReport.getRenderedResult();
        if (analysis.isDebugMode()) {
            LOGGER
                    .info("Debug mode: writing the result part as transformed HTML.");
            try {

                File tempHTML = XMLUtil.generateTempFile(analysis.getHTMLFileParameter());
                XMLUtil.printFile(this.getRenderedResult(), tempHTML);
                LOGGER.info("Written to file " + tempHTML.getAbsolutePath());

            } catch (IOException e) {
            	String msg = "";
                msg += "An internal exception was thrown when writing a file written only in debug modus: ";
                msg += e.getClass().getName() + ": " + e.getMessage();
                LOGGER.error(msg);
                throw new ReportException(msg, e);
            }
        }
    }

    /**
     * Creates an object which contains the XML representation of all details about the analyzed
     * Datalog result, including database, query, errors and scores.
     * 
     * @param analysis The analysis whose contents are transformed into the XML representation.
     * @param grading A grading object which contains information about points which have been
     *            assigned to the submitted query of the analysis.
     * @param filters Specifies the predicates that are considered in the rendering of the result.
     *            If this is null, all available predicates in the submitted result will be
     *            considered.
     * @param supplementedResult Specifies if the rendered result should be supplemented with HTML
     *            elements for directly showing the errors within the result (like missing
     *            predicates or facts).
     * @return The generated report object.
     * @throws ReportException if any unexpected Exception occured when generating the report.
     */
    private XMLReport createXMLReport(DatalogAnalysis analysis, DatalogGrading grading,
            String[] filters, boolean supplementedResult) throws ReportException {
        XMLReport xmlResult;
        if (supplementedResult) {
            xmlResult = new XMLReport(analysis, filters);
        } else {
            xmlResult = new XMLReport(analysis.getResult2(), filters);
        }
        xmlResult.setExerciseID(analysis.getExerciseID());        
        xmlResult.setSummary(this.getGeneralAnalysis(false));
        xmlResult.setDiagnoseLevel(this.getDiagnoseLevel());
        //xmlResult.setGrading(this.getGrading(false));
        xmlResult.setSyntaxReport(this.getSyntaxErrors(false));
        xmlResult.setConsistency(this.getConsistencyAnalysis(false));
        xmlResult.setErrorDescription(this.getAnalysis(false));
        if (grading != null) {
	        xmlResult.setMaxScore(grading.getMaxPoints());
	        xmlResult.setScore(grading.getPoints());
	        if (grading.isReporting()) {
	        	xmlResult.setMode("");
	        }
        }
        return xmlResult;
    }

    /**
     * Generates an overall message which summarizes single messages as returned by methods of this
     * class. By default, the messages are generated on the highest diagnose level.
     * 
     * @param analysis The analysis whose contents are transformed into messages.
     * @return A summary of messages.
     */
    private String getAnalysisSummary(DatalogAnalysis analysis) {
        String summary = "";
        String generalAnalysis = this.getGeneralAnalysis(analysis, DIAGNOSE_HIGH);
        String detailedAnalysis = "";
        ErrorCategory[] analysisCategories = this.getAnalysis(analysis, DIAGNOSE_HIGH);
        for (int i = 0; i < analysisCategories.length; i++) {
            if (i > 0) {
                detailedAnalysis += "\n";
            }
            detailedAnalysis += analysisCategories[i].getDescription();
        }
        if (generalAnalysis.length() > 0) {
            summary += generalAnalysis + "\n";
        }

        if (detailedAnalysis.length() > 0) {
            summary += detailedAnalysis + "\n";
        }
        return summary;
    }

    /**
     * Returns the detailed message about syntax errors in the submitted query, as it was returned
     * by the query processor.
     * 
     * @param analysis The analysis object which holds all information about the submitted query.
     * @return The syntax errors, if any were detected, otherwise an empty String.
     */
    private String getSyntaxErrors(DatalogAnalysis analysis) {
        QuerySyntaxException syntaxException = null;
        DatalogResult result = analysis.getResult1();
        if (result != null) {
            syntaxException = (QuerySyntaxException) result.getSyntaxException();
        }
        result = analysis.getResult2();
        if (result != null) {
            syntaxException = (QuerySyntaxException) result.getSyntaxException();
        }
        if (syntaxException != null) {
            this.setError(syntaxException.getDescription());
            return syntaxException.getDescription();
        }
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.modules.datalog.DatalogFeedback#getSyntaxErrors(boolean)
     */
    public String getSyntaxErrors(boolean rendered) {
        return this.renderText(syntaxError, rendered);
    }

    /**
     * Returns a message concerning timeout errors, if any occured when evaluating the submitted
     * query.
     * 
     * @param analysis The analysis object which holds all information about the submitted query.
     * @return The timeout message if an timeout error occured, otherwise an empty String.
     */
    private String getTimeoutErrors(DatalogAnalysis analysis) {
        Exception timeoutException = null;
        DatalogResult result = analysis.getResult2();
        if (timeoutException != null) {
            return DLResources.getString(DLResources.ERROR_TIMEOUT);
        }
        return "";
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
    private String getGeneralAnalysis(DatalogAnalysis analysis, int diagnoseLevel) {
        DatalogResult result = analysis.getResult2();
        if (result != null && result.getSyntaxException() != null) {
            return DLResources.getString(DLResources.ERROR_RESULT_SYNTAX);
        } else if (diagnoseLevel > DIAGNOSE_NONE) {
            if (analysis.isCorrect()) {
                return DLResources.getString(DLResources.SOLUTION_CORRECT);
            } else {
                return DLResources.getString(DLResources.SOLUTION_INCORRECT);
            }
        }
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.modules.datalog.DatalogFeedback#getGeneralAnalysis(boolean)
     */
    public String getGeneralAnalysis(boolean rendered) {
        return this.renderText(this.generalAnalyze, rendered);
    }


    /*
     * (non-Javadoc)
     * 
     * @see etutor.modules.datalog.DatalogFeedback#getConsistencyAnalysis(boolean)
     */
    public String getConsistencyAnalysis(boolean rendered) {
        return this.renderText(consistencyAnalyze, rendered);
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
    private ErrorCategory[] getAnalysis(DatalogAnalysis analysis, int diagnoseLevel) {
        StringBuilder error = new StringBuilder();
        StringBuilder description = new StringBuilder();
        if (diagnoseLevel != DIAGNOSE_MEDIUM && diagnoseLevel != DIAGNOSE_HIGH) {
            return new ErrorCategory[] {};
        }
        //TODO: textual output on the highest level is actually not needed any more as errors are depicted graphically
        /*
        if (diagnoseLevel == DIAGNOSE_HIGH) {
            diagnoseLevel = DIAGNOSE_MEDIUM;
        }
        */
        ArrayList errorList = new ArrayList();
        //-------------- Medium and detailed analyze -----------------

      if(!analysis.getMissingPredicates().isEmpty())  error.append(DLResources.getString(DLResources.PREDICATES_MISSING_SI));
        ArrayList missingPredicates = analysis.getMissingPredicates();
        if (missingPredicates.size() > 0) {
            StringBuffer detailedAnalyze = new StringBuffer();
            if (diagnoseLevel == DIAGNOSE_MEDIUM) {
                detailedAnalyze.append(DLResources.getString(DLResources.PREDICATES_MISSING) + "\n");
            } else {
                // ---------- prepare the message --------------- //
                String result;
                if (missingPredicates.size() == 1) {
                    result = DLResources.getString(DLResources.PREDICATES_MISSING_SI);
                } else {
                    MessageFormat messageForm = new MessageFormat("");
                    messageForm.applyPattern(DLResources.getString(DLResources.PREDICATES_MISSING_PL));
                    Format[] formats = new Format[] {NumberFormat.getInstance()};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = new Object[] {missingPredicates.size()};
                    result = messageForm.format(messageArguments);
                }

                // ---------- apply the message --------------- //
                detailedAnalyze.append(result + ":\n");
                /*
                Iterator it = missingPredicates.iterator();
                while (it.hasNext()) {
                    WrappedPredicate next = (WrappedPredicate)it.next();
                    detailedAnalyze.append("\t" + next.getName() + "\n");
                }
                */
            }
            int category = ErrorCategory.P_MISSING;
            String title = DLResources.getString(DLResources.PREDICATES_MISSING_TITLE);
            String errorDescription = detailedAnalyze.toString();
            ErrorCategory errorCategory = new ErrorCategory(category, title);
            errorCategory.setDescription(errorDescription);
            errorList.add(errorCategory);
        }

      ArrayList missingFacts = analysis.getMissingFacts();
        if (missingFacts.size() > 0) {
            StringBuffer detailedAnalyze = new StringBuffer();
            if (diagnoseLevel == DIAGNOSE_MEDIUM) {
                detailedAnalyze.append(DLResources.getString(DLResources.FACTS_MISSING) + "\n");
                description.append(DLResources.getString(DLResources.FACTS_MISSING) + "\n");
            } else {
                // ---------- prepare the message --------------- //
                String result;
                if (missingFacts.size() == 1) {
                    result = DLResources.getString(DLResources.FACTS_MISSING_SI);
                    description.append(DLResources.getString(DLResources.FACTS_MISSING)+"\n");
                } else {
                    MessageFormat messageForm = new MessageFormat("");
                    messageForm.applyPattern(DLResources.getString(DLResources.FACTS_MISSING_PL));
                    Format[] formats = new Format[] {NumberFormat.getInstance()};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = new Object[] {missingFacts.size()};
                    result = messageForm.format(messageArguments);
                    description.append(result+"\n");
                }
                // ---------- apply the message --------------- //

                detailedAnalyze.append(result + ":\n");
                /*                
                Iterator it = missingFacts.iterator();
                while (it.hasNext()) {
                    WrappedFact next = (WrappedFact)it.next();
                    detailedAnalyze.append("\t" + next.toString() + "\n");
                }
                */
            }
            int category = ErrorCategory.F_MISSING;
            String title = DLResources.getString(DLResources.FACTS_MISSING_TITLE);
            String errorDescription = detailedAnalyze.toString();
            ErrorCategory errorCategory = new ErrorCategory(category, title);
            errorCategory.setDescription(errorDescription);
            errorList.add(errorCategory);

        }
        ArrayList redundantFacts = analysis.getRedundantFacts();
        if (redundantFacts.size() > 0) {
            StringBuffer detailedAnalyze = new StringBuffer();
            if (diagnoseLevel == DIAGNOSE_MEDIUM) {
                detailedAnalyze.append(DLResources.getString(DLResources.FACTS_REDUNDANT) + "\n");
                description.append(DLResources.getString(DLResources.FACTS_REDUNDANT) + "\n");
            } else {
                // ---------- prepare the message --------------- //
                String result;
                if (redundantFacts.size() == 1) {
                    result = DLResources.getString(DLResources.FACTS_REDUNDANT_SI);
                } else {
                    MessageFormat messageForm = new MessageFormat("");
                    messageForm.applyPattern(DLResources.getString(DLResources.FACTS_REDUNDANT_PL));
                    Format[] formats = new Format[] {NumberFormat.getInstance()};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = new Object[] {redundantFacts.size()};
                    result = messageForm.format(messageArguments);
                    description.append(result+"\n");
                }
                // ---------- apply the message --------------- //
                detailedAnalyze.append(result + ":\n");
                /*
                Iterator it = redundantFacts.iterator();
                while (it.hasNext()) {
                    WrappedFact next = (WrappedFact)it.next();
                    detailedAnalyze.append("\t" + next.toString() + "\n");
                }
                */
            }
            int category = ErrorCategory.F_REDUNDANT;
            String title = DLResources.getString(DLResources.FACTS_REDUNDANT_TITLE);
            String errorDescription = detailedAnalyze.toString();
            ErrorCategory errorCategory = new ErrorCategory(category, title);
            errorCategory.setDescription(errorDescription);
            errorList.add(errorCategory);

        }

        this.setError(error.toString());
        this.setDescription(description.toString());
        return (ErrorCategory[])errorList.toArray(new ErrorCategory[] {});
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.modules.datalog.DatalogFeedback#getAnalysis(boolean)
     */
    public ErrorCategory[] getAnalysis(boolean rendered) {
        if (!rendered) {
            return this.analyze;
        }
        ErrorCategory[] renderedAnalyze = new ErrorCategory[this.analyze.length];
        for (int i = 0; i < renderedAnalyze.length; i++) {
            ErrorCategory clone = (ErrorCategory)analyze[i].clone();
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
     * Gets the HTML rendered result out of the object, which depicts the results of a query and the
     * analysis of mistakes in an XML document.
     * 
     * @param xmlResult The object which contains the XML report.
     * @return A HTML compatible String of the rendered result.
     * @throws ReportException if any unexpected Exception was thrown when rendering the result.
     */
    private String getRenderedResult(XMLReport xmlResult) throws ReportException {
        return xmlResult.getRenderedResult();
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.modules.datalog.DatalogFeedback#getRenderedResult()
     */
    public String getRenderedResult() {
        return renderedResult;
    }

    /**
     * Returns the query result as it was returned by the query processor.
     * 
     * @param result The object to take the query result from.
     * @return The query result.
     */
    private String getRawResult(DatalogResult result) {
        return result.getRawResult();
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.modules.datalog.DatalogFeedback#getRawResult()
     */
    public String getRawResult() {
        return rawResult;
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

    /*
     * (non-Javadoc)
     * 
     * @see etutor.modules.datalog.DatalogFeedback#getDiagnoseLevel()
     */
    public int getDiagnoseLevel() {
        return this.diagnoseLevel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.modules.datalog.DatalogFeedback#getDefaultCSS()
     */
    public String getDefaultCSS() {
        return this.defaultCSS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.modules.datalog.DatalogFeedback#isSupplementedResult()
     */
    public boolean isSupplementedResult() {
        return this.diagnoseLevel == DIAGNOSE_HIGH;
    }

    /**
     * Returns any text either unchanged or rendered for HTML compatibility.
     * 
     * @param text The text to transform.
     * @param rendered Indicates if the text is to be returned unchanged or rendered.
     * @return The modified or not modified text.
     */
    private String renderText(String text, boolean rendered) {
        return rendered ? HTMLConverter.stringToHTMLString(text) : text;
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.modules.datalog.DatalogFeedback#getXMLReport()
     */
    public String getXMLReport() {
        return xmlReport;
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.modules.datalog.DatalogFeedback#getGrading()
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
     *         is <code>null</code> or if {@link DatalogGrading#isReporting()}of the specified
     *         object returns false.
     */
    private String getGrading(DatalogGrading grading) {
        if (grading != null && grading.isReporting()) {
            MessageFormat messageForm = new MessageFormat("");
            messageForm.applyPattern(DLResources.getString(DLResources.GRADING_POINTS));
            Format[] formats = new Format[] {null, null};
            messageForm.setFormats(formats);
            Double[] messageArguments = new Double[] {grading.getPoints(),
                    grading.getMaxPoints()};
            String message = messageForm.format(messageArguments);
            return message;
        }
        return "";
    }

}