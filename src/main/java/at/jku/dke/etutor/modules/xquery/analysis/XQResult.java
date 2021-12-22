package at.jku.dke.etutor.modules.xquery.analysis;

import at.jku.dke.etutor.modules.xquery.*;
import at.jku.dke.etutor.modules.xquery.util.FileParameter;
import at.jku.dke.etutor.modules.xquery.util.XMLUtil;
import ch.qos.logback.classic.Logger;
import com.ibm.DDbEv2.Interfaces.Constants;
import com.ibm.DDbEv2.Models.DDModel;
import com.ibm.DDbEv2.Utilities.Parameter;
import oracle.xml.parser.v2.XMLDocument;
import oracle.xml.parser.v2.XSLException;
import org.basex.core.BaseXException;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.Set;

/**
 * Represents the evaluation result of an XQuery query as it is returned by a XQuery query
 * processor. The results, which are produced by the underlying query processor, are customized and
 * transformed into an XML representation if possible, so that analysis, grading and reporting can
 * be carried out.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class XQResult implements Serializable {

    /**
     * Used as the name of the XML root of an XQuery result. Results are interpreted as XML
     * fragments, which have to be embedded into a root element of an XML document in order to make
     * it analyzable and comparable with another result. The root element should never be part of
     * the analysis result.
     */
    public static final String XML_ROOT = "result";

    /**
     * The logger used for logging.
     */
    private final static Logger LOGGER = (Logger) LoggerFactory.getLogger(XQResult.class);
    private transient XMLDocument document;
    private String schema;
    private File xmlResultFile;
    private File dtdFile;
    private String query;
    private FileParameter xmlTempParameter;
    private FileParameter dtdTempParameter;
    private String rawResult;
    private String htmlResult;
    private String[] sortedNodes;
    private String[] sortedNodes_rootExcluded;
    private int exerciceID;
    private UrlContentMap urls;
    private WellformednessException wellformednessException;
    private QuerySyntaxException syntaxException;
    private UrlContentException urlContentException;

    /**
     * Constructs a new object that represents the evaluation result of the specified query,
     * evaluated by the specified processor.
     * <p>
     * The query may contain syntax errors or there may be <code>doc</code> function statements within the query,
     * containing contents, which are interpreted as alias names, where the corresponding url can
     * not be found. These errors can be checked subsequently.
     * <p>
     * It is also possible that the query result can not be interpreted as XML fragment. The nodes
     * which this fragment consists of, are normally added as child nodes to a default root for
     * further analysis. If the resulting document can not be constructed because it would not be a
     * valid XML document, this does not mean that the query is incorrect. The point is, that this
     * result can not be used for a detailed analysis.
     * 
     * @param query The XQuery query.
     * @param xqProcessor The processor, which is used for evaluating the query.
     * @param urls A map object, which may be <code>null</code> if the query is executed, as
     *            given. Otherwise all <code>doc</code> function statements within the query will be interpreted as
     *            containing alias names and replaced by their corresponding real document paths, taken from
     *            the <code>UrlContentMap</code>.
     * @throws InternalException if any unexpected Exception occured when evaluating the query.
     * @see #setUrls(UrlContentMap)
     */
    public XQResult(
            String query, XQProcessor xqProcessor, UrlContentMap urls) throws InternalException, IOException {
        String rawResult = "";
        try {
            if (urls != null) {
                Set urlSet = urls.aliasSet();
                String decodedQuery = urls.getEncodedQuery(query);
                rawResult = xqProcessor.executeQuery(decodedQuery, urlSet);
                //LOGGER.debug("EXECUTING DECODED QUERY: " + decodedQuery);
            } else {
                rawResult = xqProcessor.executeQuery(query, null);
                //LOGGER.debug("EXECUTING NOT DECODED QUERY: " + query);
            }
            //LOGGER.debug("RAW RESULT: " + rawResult);
        }catch(BaseXException e){
            this.syntaxException = createQuerySyntaxException(e.getMessage());
        } catch (UrlContentException e) {
            this.urlContentException = e;
        }
        init(query, rawResult);
    }

    /**
     * Initializes all necessary fields, most of all using the query and the result of that query.
     * 
     * @param query The XQuery query.
     * @param result The evaluation result of the query.
     * @throws InternalException if any unexpected Exception occured when evaluating the query.
     */
    private void init(String query, String result) throws InternalException {
    	String msg;
        this.dtdTempParameter = createDefaultFileParameter();
        this.xmlTempParameter = createDefaultFileParameter();
        this.query = query;
        this.rawResult = result;
        this.sortedNodes = new String[] {};
        this.sortedNodes_rootExcluded = new String[] {};
        this.exerciceID = 0;
        if (result == null) {
            return;
        }
        try {
            this.document = getComparableDocument(result);
            // TODO: encoding (properties?)
            this.document.setEncoding("UTF-8");
        } catch (SAXException e) {
            // rawResult is not applicable for XML processing
            wellformednessException = createWellformednessException(e.getMessage());
        } catch (IOException e) {
        	msg = new String();
            msg += "Fatal: An IO exception was thrown when generating ";
            msg += "an XML Document from an XQuery result String.";
            throw new InternalException(msg, e);
        }
    }

    /**
     * Verifies if the result, which this <code>XQResult</code> represents, is wellformed in that
     * in can be interpreted as XML fragment and used for detailed analysis.
     * 
     * @return true if the XML document, that was built from the query result is wellformed, false
     *         otherwise.
     */
    public boolean isWellformed() {
        return this.getSyntaxException() == null && this.getUrlContentException() == null
                && this.getWellformednessException() == null;
    }

    /**
     * Generates file containing the DTD according to the XML document that was built from the query
     * result. This DTD is used when analyzing the differences between two instances of
     * <code>XQResult</code>.
     * 
     * @param recreate Indicates if the file should be regenerated if it is already existing. The
     *            file will be generated in any case if it is the file does not exist already or if
     *            an already created one can not be found any more.
     * @return The generated file.
     * @throws InternalException if any unexpected exception occurs when transforming the XML
     *             document to a DTD.
     * @throws QuerySyntaxException if the query which this <code>XQResult</code> was generated
     *             from, has syntax errors.
     * @throws UrlContentException, if the query of this <code>XQResult</code> contains some
     *             <code>doc</code> function statements with contents, which are interpreted as alias names, but the
     *             corresponding urls were not specified.
     * @throws WellformednessException if the correct query result can not be interpreted as XML
     *             fragment.
     */
    public File getDTDFile(boolean recreate) throws InternalException, QuerySyntaxException,
            WellformednessException, UrlContentException {
    	String msg;
        if (this.getWellformednessException() != null) {
            throw this.getWellformednessException();
        }
        if (dtdFile == null || !dtdFile.exists()) {
            try {
                dtdFile = generateDTD(getResultFile(recreate), this.dtdTempParameter);
            } catch (IOException e) {
            	msg = new String();
            	msg += "An internal exception was thrown when generating ";
            	msg += "a DTD file from an XML document.";
                throw new InternalException(msg, e);
            }
        }
        return dtdFile;
    }

    /**
     * Generates the DTD file according to an XML document, given as file, too. The file is
     * generated as temp file, which can be influenced using the <code>FileParameter</code>
     * parameter.
     * 
     * @param xmlFile The file which holds the XML document, from which the DTD is to be generated.
     * @param tempParameter An object which holds required meta information about the file which is
     *            to be generated.
     * @return The generated file.
     * @throws InternalException if any unexpected exception occurs when transforming the XML
     *             document to a DTD.
     * @throws IOException if any exception occurs in connection with IO actions concerning the XML
     *             file and the DTD file.
     */
    private File generateDTD(File xmlFile, FileParameter tempParameter) throws InternalException,
            IOException {
        //String dtd_String = new SchemaGenerator().generateDTD(xmlFile.getAbsolutePath());
        Parameter param = new Parameter();
        param.setContinueOnError(false);
        param.addSource(xmlFile.getAbsolutePath());
        param.setDtdRequested(true);

        DDModel model = new DDModel(param);
        model.process();
        String dtd_String= model.printAs(Constants.DTD);

        File dtd = tempParameter.generateTempFile();
        XMLUtil.printFile(dtd_String, dtd);
        return dtd;
    }

    /**
     * Returns a file, which contains the contents of the XML document that was built from the query
     * result. If this is not possible due to wellformedness errors, the file contains the result of
     * the query as it is returned by the query processor.
     * 
     * @param recreate Indicates if the file should be regenerated if it is already existing. The
     *            file will be generated in any case if it is the file does not exist already or if
     *            an already created one can not be found any more.
     * @return The generated file.
     * @throws InternalException if any unexpected exception occured when writing the contents to
     *             the file.
     * @throws QuerySyntaxException if the query which this <code>XQResult</code> was generated
     *             from, has syntax errors.
     * @throws UrlContentException, if the query of this <code>XQResult</code> contains some
     *             <code>doc</code> function statements with contents, which are interpreted as alias names, but the
     *             corresponding urls were not specified.
     */
    public File getResultFile(boolean recreate) throws InternalException, QuerySyntaxException, UrlContentException {
    	String msg;
        if (syntaxException != null) {
            throw syntaxException;
        }
        if (urlContentException != null) {
            throw urlContentException;
        }
        try {
            if (xmlResultFile == null || !xmlResultFile.exists()) {
                xmlResultFile = this.xmlTempParameter.generateTempFile();
                if (this.isWellformed()) {
                    XMLUtil.printXMLFile(document, xmlResultFile);
                } else {
                    XMLUtil.printFile(rawResult, xmlResultFile);
                }
            }
            return xmlResultFile;
        } catch (IOException e) {
        	msg = new String();
            msg += "Fatal: An IO exception was thrown when writing ";
            msg += "contents of an XML Document to a file.";
            throw new InternalException(msg, e);
        }
    }

    /**
     * Sets the XML nodes of this query result which will be required to be in certain order, if
     * this <code>XQResult</code> is compared with another <code>XQResult</code> when analyzing,
     * thus serving as "correct" solution. The <code>XQResult</code>, which is compared with this
     * result object will not only have to contain the same nodes, but also in the same order as the
     * correct result. Any node which is not affected by the specified nodes will not be analyzed in
     * terms of correct order.
     * 
     * @param sortedNodes A number of XPath expressions which denote certain XML nodes, which have
     *            to be in the exact order as they appear in the returned list of nodes when
     *            evaluating the expression on the analyzed <code>XQResult</code>, in order to be
     *            correct.
     * @throws ParameterException if one of the specified expressions returns no nodes when
     *             evaluated on the XML document of this <code>XQResult</code>.
     * @throws QuerySyntaxException if the query which this <code>XQResult</code> was generated
     *             from, has syntax errors.
     * @throws UrlContentException, if the query of this <code>XQResult</code> contains some
     *             <code>doc</code> function statements with contents, which are interpreted as alias names, but the
     *             corresponding urls were not specified.
     * @throws WellformednessException if the correct query result can not be interpreted as XML
     *             document.
     */
    public void setSortedNodes(String[] sortedNodes)
        throws ParameterException,
        WellformednessException,
        QuerySyntaxException,
        UrlContentException {
        String[] sortedNodesInput = sortedNodes;
        this.sortedNodes = checkSortedNodes(sortedNodesInput);
        sortedNodes_rootExcluded = sortedNodesInput;
    }

    /**
     * Sets the urls of documents which are accessed from within the query. Generally, the following
     * pattern will be recognized as the corresponding statement: <br>
     * <code>doc('someUrlOrAlias')</code><br>
     * The detailed strategy is explained in {@link UrlContentMap}.<br>
     * Calling this might be required, if in an exercise environment a set of required documents is
     * predefined and the real location of the documents is hidden. In order to do so, an alias name
     * must be defined for all documents which appear somewhere in the query. <br>The direction of the mapping is
     * exactly the opposite as in {@link #XQResult(String, XQProcessor, UrlContentMap)}. 
     * In other words, using this method, urls of this <code>XQResult</code> are <i>encoded</i>, 
     * using the <code>UrlContentMap</code> in the context of the constructor, urls are <i>decoded</i>.
     * 
     * @param urls A map object which represents pairs of String, whereas the key represents the
     *            alias name and the value the document url, which the alias name stands for. All
     *            documents of the query must be assigned to an alias name. If
     *            <code>UrlContentMap</code> is set to <code>null</code> (which is the default),
     *            no aliasing will be done. Otherwise, when this <code>XQResult</code> is used as
     *            the reference solution when analyzing a submitted solution, these urls will be
     *            used in the following way: The contents of all <code>doc</code> function statements of the submitted
     *            query will be interpreted as the alias names and replaced by the real document
     *            paths before executing the query.
     * @throws UrlContentException if <code>urls</code> is not <code>null</code> and the query
     *             set for this <code>XQResult</code> contains <code>doc</code> function statements with urls,
     *             which are not defined in the <code>UrlContentMap</code>.
     */
    public void setUrls(UrlContentMap urls) throws UrlContentException {
        if (urls != null) {
            urls.getEncodedQuery(this.query);
        }
        this.urls = urls;
    }


    /**
     * Does the transformation of a String, which represents the result of an XQuery query, to an
     * XML document, which can be processed when analyzing and comparing it with another query
     * result.
     * 
     * @param resultString The result of a query, as it is returned by a query processor.
     * @return The XML document, if successfully built.
     * @throws IOException if an IOException occured when parsing the document.
     * @throws SAXException if building an XML document from the result is not possible due to
     *             wellformedness errors.
     */
    private XMLDocument getComparableDocument(String resultString) throws IOException, SAXException {
        //TODO: root mit dke-NS
        return XMLUtil.parse("<" + XML_ROOT + ">" + resultString + "</" + XML_ROOT + ">", false);
    }

    /**
     * Verifies if a number of nodes are contained in the XML document which was built from the
     * query result. The input is interpreted as a set of XPath expressions and the checked nodes
     * are retrieved by evaluating these expressions on the XML document of this
     * <code>XQResult</code>.
     * <p>
     * The following strategy is followed:
     * <p>
     * <li>First, the expressions must be modified. When initializing an <code>XQResult</code>,
     * an implicit root node is constructed internally. The nodes of the XQuery query result are
     * appended to this root as children (if possible with regard to wellformedness of XML
     * documents). Therefor, the given XPath expressions must be appended by the name of the
     * implicit root element.</li>
     * <li>Next it is checked if the expressions are valid XPath expressions.</li>
     * <li>Finally, the XPath expressions are evaluated, if valid, on the XML document of this
     * <code>XQResult</code>. A <code>ParameterException</code> is thrown, if the returned node
     * list is empty.</li>
     * 
     * @param nodes XPath expressions of nodes, which should be in exactly the same order as in the
     *            XML document of this <code>XQResult</code> when analyzing.
     * @return Basically the same array, but all XPath expressions appended with the name of the
     *         implicit root.
     * @throws ParameterException if at least one expression is no valid XPath expression or
     *             evaluating one of the expression does not return any node.
     * @throws QuerySyntaxException if the query which this <code>XQResult</code> was generated
     *             from, has syntax errors.
     * @throws UrlContentException, if the query of this <code>XQResult</code> contains some
     *             <code>doc</code> function statements with contents, which are interpreted as alias names, but the
     *             corresponding urls were not specified.
     * @throws WellformednessException if the correct query result can not be interpreted as XML
     *             document.
     * @see #setSortedNodes(String[])
     */
    private String[] checkSortedNodes(String[] nodes) throws ParameterException,
            WellformednessException, QuerySyntaxException, UrlContentException {
    	String msg;
        if (nodes == null) {
            return new String[] {};
        }
        if (wellformednessException != null) {
            throw wellformednessException;
        }
        if (urlContentException != null) {
            throw urlContentException;
        }
        if (syntaxException != null) {
            throw syntaxException;
        }
        String[] nodes_rootIncluded = null;
        String emptyExpressions = null;
        String invalidExpressions = null;
        nodes_rootIncluded = new String[nodes.length];

        for (int i = 0; i < nodes.length; i++) {
            try {
                NodeList nodeList;
                if ((nodeList = document.selectNodes(XML_ROOT + nodes[i])) == null
                        || nodeList.getLength() == 0) {
                    if (emptyExpressions == null) {
                        emptyExpressions = "";
                    }
                    emptyExpressions += "\n\t" + nodes[i];
                } else {
                    nodes_rootIncluded[i] = XML_ROOT + nodes[i];
                }
            } catch (XSLException e) {
                if (invalidExpressions == null) {
                    invalidExpressions = "";
                }
                invalidExpressions += "\n\t" + nodes[i];
            } catch (NullPointerException e) {
                if (invalidExpressions == null) {
                    invalidExpressions = "";
                }
                invalidExpressions += "\n\t" + nodes[i];
            }

        }
        msg = null;
        if (emptyExpressions != null) {
        	msg = new String();
            msg += "Nodes have been chosen to be sorted in the query result, ";
            msg += "but no nodes where found based on the following XPath expressions:" + emptyExpressions;
        }
        if (invalidExpressions != null) {
            if (msg == null) {
                msg = new String();
            } else {
                msg += "\n";
            }
            msg += "Invalid XPath expressions for nodes which have to be sorted in the query result:";
            msg += invalidExpressions;
        }
        if (msg != null) {
            throw new ParameterException(msg);
        }
        return nodes_rootIncluded;

    }

    /**
     * Returns the XPath expressions, that were set to indicate which XML nodes in the adapted
     * result have to be in exactly the same order when comparing it to another
     * <code>XQResult</code>.
     * 
     * @return An array of XPath expressions.
     */
    public String[] getSortedNodes() {
        return sortedNodes_rootExcluded;
    }

    /**
     * Returns an object which represents pairs, where the key is an alias name and the
     * corresponding value is the url of a document, which is assigned to an alias name. If this is
     * not set to <code>null</code> and this <code>XQResult</code> is used as reference solution
     * when analyzing a submitted solution, the document contents of the submitted query will be
     * considered as alias names and replaced by the corresponding urls before executing it.
     * 
     * @return The map of urls and alias names for these urls, if set, otherwise <code>null</code>.
     */
    public UrlContentMap getUrls() {
        return urls;
    }

    /**
     * Returns the XML document that was built from the result of the query, if interpreting it as
     * XML fragment is possible.
     * 
     * @return The generated document or null, if either the query contains syntax or the result can
     *         not be interpreted as XML fragment.
     */
    public XMLDocument getDocument() {
        return document;
    }

    /**
     * Returns the evaluation result of the query.
     * 
     * @return The evaluation result of the query. This might not only be an empty String if there
     *         are no facts returned, but also if the query contains syntax errors.
     */
    public String getRawResult() {
        return rawResult;
    }

    /**
     * Returns the query that this <code>XQResult</code> was built from.
     * 
     * @return Returns the query.
     */
    public String getQuery() {
        return query;
    }


    /**
     * Returns the exercise id.
     * 
     * @return The exercise id.
     */
    public int getExerciseID() {
        return this.exerciceID;
    }

    /**
     * Sets the exercise id.
     * 
     * @param exerciseID The exercise id to set.
     */
    public void setExerciseID(int exerciseID) {
        this.exerciceID = exerciseID;
    }

    /**
     * Returns the syntax exceptions that were detected when evaluating the query, that this
     * <code>XQResult</code> was built from.
     * 
     * @return The QuerySyntaxException, which contains information about the syntax exceptions that
     *         were detected, or <code>null</code> if the query is correct and was evaluated
     *         without syntax problems.
     * @see QuerySyntaxException#getDescription()
     */
    public QuerySyntaxException getSyntaxException() {
        return syntaxException;
    }

    /**
     * Returns the exceptions that occured when the query of this <code>XQResult</code> contains
     * some <code>doc</code> function statements with contents, which are interpreted as alias names, but the
     * corresponding urls were not specified.
     * 
     * @return The UrlContentException, which contains information about the line and row index of
     *         the occurence of an alias name which can not be decoded. If no such exception
     *         occured, this returns <code>null</code>.
     */
    public UrlContentException getUrlContentException() {
        return urlContentException;
    }

    /**
     * Returns the wellformedness exceptions that were detected when trying to build an XML document
     * from the query result.
     * 
     * @return The WellformednessException, which contains information about the exceptions that
     *         were detected, or <code>null</code> if the query is correct and the XML document
     *         was built without problems.
     * @see WellformednessException#getDescription()
     */
    public WellformednessException getWellformednessException() {
        return wellformednessException;
    }

    /**
     * Needed for making instances of <code>XQResult</code> serializable. All fields are
     * serialized by default, except for XML document objects, which are exported to a String.
     * 
     * @param out The output stream.
     * @throws IOException if an IOException occured when serializing this object.
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
    	String msg;
    	
    	msg = "Start serializing " + this.toString() + ": ";
        LOGGER.debug(msg);
        msg = "\t1. default serialization";
        LOGGER.debug(msg);
        out.defaultWriteObject();
        msg = "\t2. customized serialization of transient fields";
        LOGGER.debug(msg);
        String xmlString = null;
        if (this.document != null) {
            try {
                xmlString = XMLUtil.getXMLString(this.document);
                msg = new String();
                msg += "\t\tlength of serialized XML document: ";
                msg += xmlString.length() + " characters";
                LOGGER.debug(msg);
            } catch (IOException e) {
            	msg = "An exception was thrown when serializing an XML document.";
            	LOGGER.error(msg, e);
                throw e;
            }
        } else {
        	msg = "\t\t XML document is null";
            LOGGER.debug(msg);
        }
        out.writeObject(xmlString);
        msg = "Finished serializing " + this.toString();
        LOGGER.debug(msg);
    }

    /**
     * Needed for making instances of <code>XQResult</code> serializable. All fields are
     * deserialized by default, except for XML document objects, which are reparsed from a String.
     * 
     * @param in The input stream.
     * @throws IOException if an IOException occured when deserializing this object.
     * @throws ClassNotFoundException if a ClassNotFoundException occured when deserializing this
     *             object.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    	String msg;
    	msg = "Start deserializing " + this.toString() + ": ";
        LOGGER.debug(msg);
        msg = "\t1. default deserialization";
        LOGGER.debug(msg);
        in.defaultReadObject();
        msg = "\t2. customized deserialization of transient fields";
        LOGGER.debug(msg);
        String xmlString = (String)in.readObject();
        if (xmlString == null) {
        	msg = "\t\t XML document is null";
            LOGGER.debug(msg);
        } else {
        	msg = new String();
        	msg += "\t\tlength of serialized XML document: ";
        	msg += xmlString.length() + " characters";
            LOGGER.debug(msg);
            try {
                this.document = XMLUtil.parse(xmlString, false);
                this.document.setEncoding("UTF-8");
                LOGGER.debug(msg = "Finished deserializing " + this.toString() + "");
            } catch (SAXException e) {
            	msg = new String();
            	msg += "An exception was thrown when reparsing an ";
            	msg += "XML document after serialization.";
            	LOGGER.error(msg, e);
                throw new IOException(msg);
            } catch (IOException e) {
            	msg = new String();
            	msg += "An exception was thrown when reparsing an ";
            	msg += "XML document after serialization.";
                throw new IOException(msg);
            }
        }
    }

    /**
     * Creates an Exception used for indicating wellformedness errors. The specified message is used
     * for setting up the message of the returned Exception, as well as the description.
     * 
     * @param msg The message which is used for the message and the description of the returned
     *            Exception.
     * @return The created Exception.
     */
    private WellformednessException createWellformednessException(String msg) {
    	String newMsg;
    	WellformednessException exception;
    	
    	newMsg = new String();
        newMsg += "Query result is not wellformed and therefor cannot ";
        newMsg += "be processed as XML tree.\n" + msg;
        exception = new WellformednessException(newMsg);
        exception.setDescription(msg);
        return exception;
    }

    /**
     * Creates an Exception used for indicating syntax errors. The specified message is used for
     * setting up the message of the returned Exception, as well as the description.
     * 
     * @param msg The message which is used for the message and the description of the returned
     *            Exception.
     * @return The created Exception.
     */
    private QuerySyntaxException createQuerySyntaxException(String msg) {
    	String newMsg;
    	QuerySyntaxException exception;
    	
    	newMsg = new String();
        newMsg += "Query result cannot be processed due to ";
        newMsg += "syntax errors.\n" + msg;
        exception = new QuerySyntaxException(newMsg);
        exception.setDescription(msg);
        return exception;
    }

    /**
     * Sets the parameters which are used for generating the file that contains the contents of the
     * query result, either the result as it is returned by the query processor, or the adapted
     * result which is built as XML document.
     * 
     * @param tempParameter The parameters used for creating a temp file containing the query result
     *            when needed during the analysis process.
     */
    public void setXMLFileParameter(FileParameter tempParameter) {
        if (tempParameter == null) {
            tempParameter = new FileParameter();
        }
        this.xmlTempParameter = tempParameter;
    }

    /**
     * Sets the parameters which are used for generating the file that contains the DTD of the query
     * result.
     * 
     * @param tempParameter The parameters used for creating a temp file containing the DTD of the
     *            query result when needed during the analysis process.
     */
    public void setDTDFileParameter(FileParameter tempParameter) {
        if (tempParameter == null) {
            tempParameter = new FileParameter();
        }
        this.dtdTempParameter = tempParameter;
    }

    /**
     * Creates default parameters for creating files that are needed for analyzing two results.
     * 
     * @return Default parameters for internally used files.
     */
    private FileParameter createDefaultFileParameter() {
        FileParameter tempParameter = new FileParameter();
        tempParameter.setDefaultOnError(true);
        tempParameter.setDeleteOnExit(true);
        tempParameter.setTempFolder("temp");
        tempParameter.setTempPrefix("xquery");
        return tempParameter;
    }
}