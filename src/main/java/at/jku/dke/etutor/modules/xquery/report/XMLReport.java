package at.jku.dke.etutor.modules.xquery.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Date;

import at.jku.dke.etutor.modules.xquery.*;
import at.jku.dke.etutor.modules.xquery.analysis.XQAnalysis;
import at.jku.dke.etutor.modules.xquery.analysis.XQResult;
import at.jku.dke.etutor.modules.xquery.grading.XQGrading;
import at.jku.dke.etutor.modules.xquery.util.XMLUtil;
import ch.qos.logback.classic.Logger;
import oracle.xml.parser.v2.NodeFactory;
import oracle.xml.parser.v2.XMLDocument;
import oracle.xml.parser.v2.XMLDocumentFragment;
import oracle.xml.parser.v2.XMLElement;
import oracle.xml.parser.v2.XSLException;
import oracle.xml.parser.v2.XSLProcessor;
import oracle.xml.parser.v2.XSLStylesheet;

import org.slf4j.LoggerFactory;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * An object which is the XML representation of all details about an XQuery result. An instance can
 * be constructed using a XQuery result object directly, or an analysis object, which in turn
 * contains the XQuery result and all details about the analysis.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class XMLReport {

    /**
     * The logger used for logging.
     */
    private final static Logger LOGGER = (Logger) LoggerFactory.getLogger(XMLReport.class);

    private XMLDocument doc;

    private Element root;

    private Element messageElement;
    
    private Element summaryElement;
    
    private Element gradingElement;
    
    private Element resultElement;
    
    private Element queryElement;

    /**
     * Constructs a new <code>XMLReport</code> using the results of an analysis object. This will
     * transform all important information of the analysis into the XML representation of this
     * <code>XMLReport</code>, like analyzed errors.
     * 
     * @param analysis The analysis object to transform.
     * @param grading An object holding grading information, which may also be <code>null</code>
     * @param report An object holding report information
     * @param isSupplementedResult Indicates whether the rendered result should contain information
     *            which goes beyond the mere rendering of the raw result as it is output by the
     *            query processor.
     * @throws ReportException if any unexpected Exception occured when transforming contents of the
     *             analysis into an XML representation.
     */
    public XMLReport(
            XQAnalysis analysis, XQGrading grading, XQReport report, boolean isSupplementedResult) throws ReportException {
        XMLDocument modifiedDoc = createModifiedResult(analysis, isSupplementedResult);
        init(modifiedDoc);
        setData(analysis, grading, report);
        
        if (analysis.isDebugMode()) {
            LOGGER
                    .info("Debug mode: writing the generated report as XML and the result part as transformed HTML.");
            try {
                File tempReport = analysis.getReportFileParameter().generateTempFile();
                XMLUtil.printXMLFile(this.doc, tempReport);
                LOGGER.info("Written to file " + tempReport.getAbsolutePath());
                File tempHTML = analysis.getHTMLFileParameter().generateTempFile();
                XMLUtil.printFile(this.getRenderedResult(), tempHTML);
                LOGGER.info("Written to file " + tempHTML.getAbsolutePath());
            } catch (IOException e) {
            	String msg = "An internal exception was thrown when writing a file written only in debug modus: ";
                msg += e.getClass().getName() + ": " + e.getMessage();
                throw new ReportException(msg, e);
            }
        }
    }

    /**
     * Initializes the nodes of the XML tree, so that setter methods of this class can be invoced.
     * 
     * @param doc The XML document which is set as the XQuery result in the initialized XML tree.
     * @throws ReportException if any unexpected Exception occured when building the tree.
     */
    private void init(XMLDocument doc) throws ReportException {

        this.doc = doc;

        Element oldRoot = doc.getDocumentElement();
        doc.removeChild(oldRoot);
        
        this.root = doc.createElement("xquery-result");
        doc.appendChild(root);

        this.messageElement = doc.createElement("analysis");
        root.appendChild(messageElement);

        this.summaryElement = doc.createElement("summary");
        messageElement.appendChild(summaryElement);
        
        this.gradingElement = doc.createElement("grading");
        messageElement.appendChild(gradingElement);

        this.queryElement = doc.createElement("query");
        root.appendChild(queryElement);
        
        this.resultElement = oldRoot;
        root.appendChild(resultElement);
        
    }

    /**
     * Fills the nodes of this XML tree with all important information of analysis, grading and report.
     * 
     * @param analysis The analysis object to transform.
     * @param grading An object holding grading information, which may also be <code>null</code>
     * @param report An object holding report information
     */
    private void setData(XQAnalysis analysis, XQGrading grading, XQReport report) {

    	//analysis information
    	int exerciseID = 0;
    	int userID = 0;
    	
    	//XQuery result information
        XQResult result = analysis.getResult2();
    	String query = "";
        QuerySyntaxException syntaxException;
        UrlContentException urlContentException;
        WellformednessException wellformednessException;
        boolean hasSyntaxError = false;
        boolean isWellformed = true;

        //grading information
        double points = 0;
        double maxPoints = 0;

        //report information
        String summary = "";
        String gradingText = "";
        ErrorCategory[] errors = null;
        ErrorCategory syntaxError = null;
    	String mode = "";
        int diagnoseLevel = XQReport.DIAGNOSE_NONE;

        //other information
        Date date = new Date(System.currentTimeMillis());
        
        if (analysis != null) {
        	exerciseID = analysis.getExerciseID();
        	userID = analysis.getUserID();
        }
        
        if (result != null) {
            query = result.getQuery();
            syntaxException = result.getSyntaxException();
            urlContentException = result.getUrlContentException();
            wellformednessException = result.getWellformednessException();
            hasSyntaxError = syntaxException != null || urlContentException != null;
            isWellformed = wellformednessException == null;
        }

        if (grading != null) {
	        points = grading.getPoints();
	        maxPoints = grading.getMaxPoints();
        }
        
        if (report != null) {
        	summary = report.getGeneralAnalysis(false);
        	gradingText = report.getGrading(false);
        	errors = report.getSemanticErrors(false);
           	syntaxError = report.getSyntaxError(false);
           	mode = report.getMode();
        	diagnoseLevel = report.getDiagnoseLevel();
        }

        setExerciseID(exerciseID);
        setUserID(userID);
        
        setQuery(query);
        setSyntaxError(hasSyntaxError);
        setWellformedness(isWellformed);
        
        setMaxScore(maxPoints);
        setScore(points);

        setSummary(summary, "");
        setGrading(gradingText, "");
        setSemanticError(errors);
        if (!syntaxError.getDescription().trim().equals("")) {
        	setSyntaxError(syntaxError);
        }
        setMode(mode);
        setDiagnoseLevel(diagnoseLevel);
        
        setDate(date);        
    }
    
    /**
     * Sets the <code>date</code> attribute for the XML document of this <code>XMLReport</code>.
     * 
     * @param date The date to set.
     */
    public void setDate(Date date) {
        root.setAttribute("date", date.toString());
    }

    /**
     * Sets the <code>syntax-error</code> attribute to the specified boolean value, telling
     * whether the analyzed XQuery result has syntax errors or not.
     * 
     * @param hasSyntaxErrors Indicating whether the analyzed XQuery result has syntax errors or
     *            not.
     */
    public void setSyntaxError(boolean hasSyntaxErrors) {
        resultElement.setAttribute("syntax-error", Boolean.toString(hasSyntaxErrors));
    }

    /**
     * Sets the <code>wellformed</code> attribute to the specified boolean value, telling whether
     * the analyzed result can be interpreted as XML fragment, which is necessary if the result
     * should be compared with another result and analyzed it in detail.
     * 
     * @param isWellformed Indicating whether the analyzed XQuery result is wellformed with regard
     *            to XML.
     */
    public void setWellformedness(boolean isWellformed) {
        resultElement.setAttribute("wellformed", Boolean.toString(isWellformed));
    }

    /**
     * Sets the <code>diagnose-level</code> attribute for the XML document of this
     * <code>XMLReport</code> to the specified value.
     * 
     * @param diagnoseLevel The level which was used for generating a report.
     */
    public void setDiagnoseLevel(int diagnoseLevel) {
        this.root.setAttribute("diagnose-level", Integer.toString(diagnoseLevel));
    }

    /**
     * Sets the <code>score</code> attribute for the XML document of this <code>XMLReport</code>
     * to the specified value.
     * 
     * @param score Information about the score that was reached for an XQuery result, which was
     *            analyzed and compared to some "correct" result.
     */
    public void setScore(double score) {
        root.setAttribute("score", Double.toString(score));
    }

    /**
     * Sets the <code>max-score</code> attribute for the XML document of this
     * <code>XMLReport</code> to the specified value.
     * 
     * @param score Information about the highest score which can be reached for an XQuery result,
     *            which is analyzed and compared to some "correct" result.
     */
    public void setMaxScore(double score) {
        root.setAttribute("max-score", Double.toString(score));
    }

    /**
     * Sets the <code>mode</code> attribute for the XML document of this
     * <code>XMLReport</code> to the specified value.
     * 
     * @param mode The submission mode (e.g. submission, check or run).
     */
    public void setMode(String mode) {
        root.setAttribute("mode", mode);
    }
    
    /**
     * Sets a text, which possibly is the summary of the results of analyzing an XQuery result.
     * The last text set as summary is overwritten.
     * 
     * @param analysis The text to set.
     * @param header Some header to set. If this is <code>null</code> or an empty String, no header element
     * 			will be appended.
     */
    public void setSummary(String analysis, String header) {
        // TODO: documentation: CDATA section, escaping not reliable

    	NodeList childs = this.summaryElement.getChildNodes();
    	for (int i = 0; i < childs.getLength(); i++) {
    		summaryElement.removeChild(childs.item(i));
    	}

    	if (header != null && !header.trim().equals("")) {
	    	Element headerElement = this.doc.createElement("header");
	    	headerElement.appendChild(this.doc.createTextNode(header));
	    	this.summaryElement.appendChild(headerElement);
    	}

    	if (analysis == null) {
    		analysis = "";
    	}

    	Element analysisElement = this.doc.createElement("detail");
    	analysisElement.appendChild(this.doc.createTextNode(analysis));
    	this.summaryElement.appendChild(analysisElement);
    }

    /**
     * Sets a text, which possibly is the summary of the results of grading an XQuery result.
     * The last text set as grading is overwritten.
     * 
     * @param grading The text to set.
     * @param header Some header to set. If this is <code>null</code> or an empty String, no header element
     * 			will be appended.
     */
    public void setGrading(String grading, String header) {
    	    	
    	NodeList childs = this.gradingElement.getChildNodes();
    	for (int i = 0; i < childs.getLength(); i++) {
    		gradingElement.removeChild(childs.item(i));
    	}

    	if (header != null && !header.trim().equals("")) {
	    	Element headerElement = this.doc.createElement("header");
	    	headerElement.appendChild(this.doc.createTextNode(header));
	    	this.gradingElement.appendChild(headerElement);
    	}
    	
    	if (grading == null) {
    		grading = "";
    	}
    	
    	Element analysisElement = this.doc.createElement("detail");
    	analysisElement.appendChild(this.doc.createTextNode(grading));
    	this.gradingElement.appendChild(analysisElement);
    }

    /**
     * Sets the submitted XQuery statement.
     * 
     */
    public void setQuery(String query) {
    	this.queryElement.appendChild(this.doc.createCDATASection(query));
    }
    
    /**
     * Sets the <code>exercise</code> attribute for the XML document of this
     * <code>XMLReport</code> to the specified value.
     * 
     * @param exerciceID The id to set.
     */
    public void setExerciseID(int exerciceID) {
        root.setAttribute("exercice", Integer.toString(exerciceID));
    }

    /**
     * Sets the <code>user-id</code> attribute for the XML document of this
     * <code>XMLReport</code> to the specified value.
     * 
     * @param userID The id to set.
     */
    public void setUserID(int userID) {
        root.setAttribute("user", Integer.toString(userID));
    }
    
    /**
     * Sets an error message for the given error categories.
     * 
     * @param categories a number of errors.
     */
    public void setSemanticError(ErrorCategory[] categories) {
        for (int i = 0; i < categories.length; i++) {
            setSemanticError(categories[i]);
        }
    }
    
    /**
     * Sets an error message for the given error category.
     * 
     * @param category the error.
     */
    public void setSemanticError(ErrorCategory category) {
    	if (category == null) {
    		return;
    	}
    	setSemanticError(category.getDescription(), category.getCssTitle(), category.getTitle());
    }
    
    /**
     * Sets a simple error message not assigned to a certain error category.
     * 
     * @param message The text to set.
     * @param cssTitle A text for setting the <code>code</code> attribute. Ignored if <code>null</code> or an empty String.
     * @param header Some text for the <code>header</code> element. Ignored if <code>null</code> or an empty String.
     */
    public void setSemanticError(String message, String cssTitle, String header) {
        
    	Element errorElement = doc.createElement("error");
    	
    	if (cssTitle != null && !cssTitle.trim().equals("")) {
        	errorElement.setAttribute("code", cssTitle);
        }
    	
    	if (header != null && !header.trim().equals("")) {
	    	Element headerElement = this.doc.createElement("header");
	    	headerElement.appendChild(this.doc.createTextNode(header));
	    	errorElement.appendChild(headerElement);
    	}

    	Element analysisElement = this.doc.createElement("detail");
    	analysisElement.appendChild(this.doc.createTextNode(message));
        errorElement.appendChild(analysisElement);

        messageElement.appendChild(errorElement);
    }

    /**
     * Appends elements holding textually information about some syntax error report.
     * 
     * @param category The object holding error information.
     */
    public void setSyntaxError(ErrorCategory category) {
    	if (category == null) {
    		return;
    	}
    	setSyntaxError(category.getDescription(), category.getCssTitle(), category.getTitle());
    }

    /**
     * Sets an error message for the given error categories.
     * 
     * @param categories a number of errors.
     */
    public void setSyntaxError(ErrorCategory[] categories) {
        for (int i = 0; i < categories.length; i++) {
            setSyntaxError(categories[i]);
        }
    }
    
    /**
     * Sets a simple error message not assigned to a certain error category.
     * A new element is created and appended, other elements are kept unchanged.
     * 
     * @param message The text to set.
     * @param cssTitle A text for setting the <code>code</code> attribute. Ignored if <code>null</code> or an empty String.
     * @param header Some text for the <code>header</code> element. Ignored if <code>null</code> or an empty String.
     */
    public void setSyntaxError(String message, String cssTitle, String header) {

        if (message == null) {
    		message = "";
    	}

    	Element errorElement = doc.createElement("syntax");
    	
    	if (cssTitle != null && !cssTitle.trim().equals("")) {
        	errorElement.setAttribute("code", cssTitle);
        }
    	
    	if (header != null && !header.trim().equals("")) {
	    	Element headerElement = this.doc.createElement("header");
	    	headerElement.appendChild(this.doc.createTextNode(header));
	    	errorElement.appendChild(headerElement);
    	}

    	Element analysisElement = this.doc.createElement("detail");
    	analysisElement.appendChild(this.doc.createCDATASection(message));
        errorElement.appendChild(analysisElement);

        messageElement.appendChild(errorElement);
    }
    
    /**
     * Returns a String which contains the HTML compatible rendered result of the contents of this
     * <code>XMLReport</code>. The XML representation of an XQuery result or the analysis of this
     * XQuery result is transformed using an XSL stylesheet.
     * 
     * @return The rendered result of the XQuery result or of the analysis.
     * @throws ReportException if an internal Exception was thrown when applying the XSL stylesheet
     *             used for rendering the result.
     */
    public String getRenderedResult() throws ReportException {
        try {
            return getRenderedResult(this.doc);
        } catch (XSLException e) {
            String message = "An internal exception was thrown when processing XSL stylesheets.\n"
                + e.getMessage();        	
            throw new ReportException(message, e);
        } catch (IOException e) {
            String message = "An internal exception was thrown when processing XSL stylesheets.\n"
                + e.getMessage();        	
            throw new ReportException(message, e);
        } catch (InvalidResourceException e) {
            String message = "An internal exception was thrown when processing XSL stylesheets.\n"
                + e.getMessage();        	
            throw new ReportException(message, e);
        }
    }

    /**
     * Returns a String which contains the HTML compatible rendered result of the specified XML
     * document, which is expected to be the XML document representing this <code>XMLReport</code>.
     * 
     * @param document The document to transform.
     * @return The rendered result of the document, where the transformation is done using an XSL
     *         stylesheet.
     * @throws XSLException if an internal Exception was thrown when applying the XSL stylesheet
     *             used for rendering the result.
     * @throws IOException if an internal <code>IOException</code> was thrown when applying the
     *             XSL stylesheet used for rendering the result.
     * @throws InvalidResourceException if accessing required XSL stylesheet files causes any exception 
     */
    private String getRenderedResult(XMLDocument document) throws XSLException, IOException, InvalidResourceException {

    	URL xslURL = null;
    	
    	if (document == null) {
    		return null;
    	}
    	
		xslURL = XQCoreManager.getInstance().getResource(XQCoreManager.XSL_RENDER_XQ);

        XSLProcessor xslProc = new XSLProcessor();
        xslProc.setXSLTVersion(XSLProcessor.XSLT10);
        xslProc.showWarnings(true);
        xslProc.setErrorStream(System.out);

        XSLStylesheet stylesheet = xslProc.newXSLStylesheet(xslURL);
        StringWriter resultWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(resultWriter);

        xslProc.processXSL(stylesheet, document, writer);
        return resultWriter.toString();
    }

    /**
     * Returns the whole XML document which represents this <code>XMLReport</code>, including all
     * transformed information from a XQuery result or the analysis of a XQuery result.
     * 
     * @return The String which represents the XML document.
     * @throws ReportException if an internal IOException was thrown when converting the XML
     *             document to a String.
     */
    public String getXMLReport() throws ReportException {
        try {
            return XMLUtil.getXMLString(this.doc);
        } catch (IOException e) {
            String message = "Fatal: An exception was thrown when converting an XML Document to a String.\n"
                    + e.getMessage();
            throw new ReportException(message, e);
        }
    }

    /**
     * Returns an XML document which will be used as the XML document of this <code>XMLReport</code>.
     * 
     * @param xqResult The XQuery result which provides the document, which will be cloned, if
     *            possible.
     * @return An XML document, that is the clone of the XML document, provided by the
     *         <code>XQResult</code>. If this XML document has not been built, due to problems
     *         with syntax exceptions or wellformedness of the result, a new document is returned.
     */
    private XMLDocument createModifiedResult(XQResult xqResult) throws ReportException {

        XMLDocument document = xqResult.getDocument();
        if (document == null || xqResult.getWellformednessException() != null
                || xqResult.getUrlContentException() != null
                || xqResult.getSyntaxException() != null) {
            NodeFactory factory = new NodeFactory();
            XMLDocument doc = factory.createDocument();
            XMLElement element = factory.createElement(XQResult.XML_ROOT);
            CDATASection cdata = factory.createCDATASection(xqResult.getRawResult());
            element.appendChild(cdata);
            doc.appendChild(element);
            return doc;
        }
        return (XMLDocument)document.cloneNode(true);
    }

    /**
     * Returns an XML document which will be used as the XML document of this <code>XMLReport</code>.
     * <br>
     * Generally, the returned document results from a set of transformations: <br>
     * <li>First, the XSL stylesheet, which represents the differences between two XML documents,
     * must be modified. These XML documents represent the results of two queries, which are
     * compared after they each have been embeddeded into an XML root element. The generated
     * stylesheet can be requested from the <code>XQAnalysis</code> as XML document. This
     * stylesheet itself will be transformed using a
     * information of the XQuery result.</li>
     * <li>Second, this modified XSL stylesheet is applied to the XML document of the "submitted
     * result", which has been compared to the "correct result". The result of this transformation
     * is returned.</li>
     * 
     * @param analysis The analysis to get the XML documents of the query results and the XSL
     *            stylesheet of the analysis from.
     * @param isSupplementedResult Indicates whether the rendered result should contain information
     *            as described above, at all. If not, the exact XML document of the "submitted"
     *            result is cloned and returned.
     * @return The modified result of the document or, if XML documents are not available due to
     *         syntax errors or wellformedness errors of the query results, a newly created
     *         document. If the query result is a valid XML document and the returned document is
     *         requested not to be <i>supplemented </i>, a clone of the result document is simply
     *         returned.
     * @throws ReportException if an internal Exception was thrown when applying the XSL stylesheet
     *             used for rendering the result.
     */
    private XMLDocument createModifiedResult(XQAnalysis analysis, boolean isSupplementedResult)
            throws ReportException {

        XQResult xqResult = analysis.getResult2();
        XMLDocument document = xqResult.getDocument();
        if (document == null || xqResult.getWellformednessException() != null
                || xqResult.getUrlContentException() != null
                || xqResult.getSyntaxException() != null) {
            NodeFactory factory = new NodeFactory();
            XMLDocument doc = factory.createDocument();
            XMLElement element = factory.createElement(XQResult.XML_ROOT);
            CDATASection cdata = factory.createCDATASection(xqResult.getRawResult());
            element.appendChild(cdata);
            doc.appendChild(element);
            return doc;
        }
        if (!isSupplementedResult) {
            return (XMLDocument)document.cloneNode(true);
        }

        try {
            XMLDocument xslDiff = analysis.getXSLDiff();
            XSLProcessor xslProc = new XSLProcessor();
            URL xslURL = XQCoreManager.getInstance().getResource(XQCoreManager.XSL_MODIFY);
    		
            xslProc.setXSLTVersion(XSLProcessor.XSLT10);
            xslProc.showWarnings(true);
            xslProc.setErrorStream(System.out);
            StringWriter resultWriter;
            PrintWriter writer;

            XSLStylesheet stylesheet;

            // First step: modify the XSL stylesheet produced by the XMLDiff class (xslDiff)

            stylesheet = xslProc.newXSLStylesheet(xslURL);
            resultWriter = new StringWriter();
            writer = new PrintWriter(resultWriter);
            xslProc.processXSL(stylesheet, xslDiff, writer);

            if (analysis.isDebugMode()) {
                LOGGER
                        .info("Debug mode: writing the differences of the results including error report elements as modified XSL.");
                try {
                    File tempFile = analysis.getModifiedXSLFileParameter().generateTempFile();
                    XMLUtil.printFile(resultWriter.toString(), tempFile);
                    LOGGER.info("Written to file " + tempFile.getAbsolutePath());
                } catch (IOException e) {
                	String msg = "An internal exception was thrown when writing a file written only in debug modus: ";
                    msg += e.getClass().getName() + ": " + e.getMessage();
                    LOGGER.error(msg);
                    throw new ReportException(msg, e);
                }
            }

            // Second step: apply the modified XSL stylesheet to the result of the suggested XQuery
            // statement (document2)

            stylesheet = xslProc.newXSLStylesheet(new StringReader(resultWriter.toString()));
            //Use processXSL method without PrintWriter parameter, as support of xsl:output
            // elements is not needed (see oracle xdk api)
            XMLDocumentFragment result = xslProc.processXSL(stylesheet, document);

            // Converts the fragment to a document and next returns the document
            String xmlString = XMLUtil.getXMLString(result);
            XMLDocument xmlDoc = XMLUtil.parse(xmlString, false);
            return (XMLDocument)xmlDoc.cloneNode(true);
        } catch (SAXException e) {
            String message = "An internal exception was thrown when processing XSL stylesheets.\n"
                    + e.getMessage();
            throw new ReportException(message, e);
        } catch (XSLException e) {
            String message = "An internal exception was thrown when processing XSL stylesheets.\n"
                + e.getMessage();
            throw new ReportException(message, e);
        } catch (IOException e) {
            String message = "An internal exception was thrown when processing XSL stylesheets.\n"
                + e.getMessage();
            throw new ReportException(message, e);
        } catch (InvalidResourceException e) {
            String message = "An internal exception was thrown when processing XSL stylesheets.\n"
                + e.getMessage();
            throw new ReportException(message, e);
        }
    }
}