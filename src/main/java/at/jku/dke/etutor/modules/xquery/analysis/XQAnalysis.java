package at.jku.dke.etutor.modules.xquery.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.modules.xquery.*;
import at.jku.dke.etutor.modules.xquery.util.FileParameter;
import at.jku.dke.etutor.modules.xquery.util.XMLUtil;
import ch.qos.logback.classic.Logger;
import oracle.xml.differ.XMLDiff;
import oracle.xml.parser.v2.NodeFactory;
import oracle.xml.parser.v2.XMLDocument;
import oracle.xml.parser.v2.XMLElement;
import oracle.xml.parser.v2.XSLException;
import org.dom4j.DocumentException;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 * An instance of this class takes two queries or already the result of two XQuery queries as input,
 * analyzes them and prepares the found differences and errors for further processing (grading and
 * reporting). <br>
 * The first query is considered as the " <i>correct </i>" query and the second as the "
 * <i>submitted </i>" one, which has to be compared with the correct solution. This numbering is
 * used throughout the module to identify the correct and the submitted solution. <br>
 * The correct query must be syntactically correct and it must be possible to interpret it as XML
 * fragment, otherwise an Exception is thrown. On the other hand, if the <i>submitted </i>query is
 * either syntactically incorrect or not interpretable as XML fragment, this is part of the analysis
 * and will be treated respectively when grading and reporting.
 * <p>
 * Errors in the submitted query result are detected by generating an XSL document, which represents
 * the differences between the submitted query result and the correct query result, both taken as
 * XML document.
 * <p>
 * A query result is taken as XML fragment and embedded into a default root element  in order to use it as XML document for
 * the analysis.
 *
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class XQAnalysis implements Analysis, Serializable {

    /**
     * The logger used for logging.
     */
    private final static Logger LOGGER = (Logger) LoggerFactory.getLogger(XQAnalysis.class);

    private Serializable submission;

    private boolean different;

    private boolean debugMode;

    private XQResult result1;

    private XQResult result2;

    private transient XMLDocument xslDiff;

    private boolean validSolution;

    private boolean submissionSuitsSolution;

    private int exerciseID;

    private int userID;

    //data structures for detected errors
    private NodeErrorList redundantNodes;

    private NodeErrorList redundantInsteadNodes;

    private NodeErrorList displacedNodes;

    private NodeErrorList missingInsteadNodes;

    private NodeErrorList missingPreviousNodes;

    private NodeErrorList missingNextNodes;

    private NodeErrorList missingInnerNodes;

    private NodeErrorList redundantAttributes;

    private NodeErrorList incorrectAttributeValues;

    private NodeErrorList missingAttributes;

    private NodeErrorList incorrectTextValues;

    //parameters for files for debugging purpose or files required for analysis; keep references centrally
    private FileParameter xml1FileParameter;

    private FileParameter xml2FileParameter;

    private FileParameter dtdFileParameter;

    private FileParameter xslFileParameter;

    private FileParameter xslModifiedFileParameter;

    private FileParameter reportFileParameter;

    private FileParameter htmlFileParameter;

    /**
     * Creates a new instance. Unless the query results are not set using
     * {@link #setResults(XQResult, XQResult)}, the created object must be considered as blank
     * analysis, which can not be reported or graded.
     */
    public XQAnalysis() {
        super();
        XQAnalysisConfig config = new XQAnalysisConfig();
        config.setDebugMode(false);
        init(config);
    }

    /**
     * Creates a new analysis instance by comparing the results of two XQuery queries.
     *
     * @param result1 The "correct" query result.
     * @param result2 The "submitted" query result.
     * @param config  configuration object holding parameters like a flag which is set to know if
     *                intermediate results should be saved to files when analyzing, grading, and reporting later on.
     * @throws NullPointerException    if one of the results is <code>null</code>
     * @throws WellformednessException if the correct query result can not be interpreted as XML
     *                                 fragment.
     * @throws QuerySyntaxException    if the correct query result object was created from a query,
     *                                 which is syntactically incorrect.
     * @throws UrlContentException     if the correct query result object was created from a query,
     *                                 which contains some <code>doc</code> function statements with contents, which
     *                                 are interpreted as alias names, but the corresponding urls were not specified.
     * @throws AnalysisException       if any kind of unexpected Exception occured during analyzing the
     *                                 results.
     */
    public XQAnalysis(
            XQResult result1, XQResult result2, XQAnalysisConfig config) throws AnalysisException,
            NullPointerException, WellformednessException, QuerySyntaxException,
            UrlContentException {
        super();
        init(config);
        setResults(result1, result2);
    }

    /**
     * Creates a new analysis instance by evaluating the given queries and comparing the produced
     * results.
     *
     * @param config configuration object holding parameters like a flag which is set to know if
     *               intermediate results should be saved to files when analyzing, grading, and reporting later on.
     * @throws ParameterException      if the sorted nodes given by XPath expressions do not exist in the
     *                                 correct query result itself or if any of the expressions is no valid XPath
     *                                 expression.
     * @throws UrlContentException     if the <code>urls</code> parameter is not <code>null</code>
     *                                 and some urls within <code>doc</code> function statements of the correct query
     *                                 are detected, where the corresponding alias name has not been specified.
     * @throws QuerySyntaxException    if the "correct" query is syntactically incorrect.
     * @throws AnalysisException       if any kind of unexpected Exception occured during analyzing the
     *                                 results.
     * @throws WellformednessException if the correct query result can not be interpreted as XML
     *                                 fragment.
     */
    public XQAnalysis(XQAnalysisConfig config) throws ParameterException, QuerySyntaxException,
            AnalysisException, WellformednessException, UrlContentException {
        super();
        init(config);
        try {
            XQResult result1 = new XQResult(config.getQuery1(), config.getProcessor(), config.getUrls());
            result1.setSortedNodes(config.getSortedNodes());
            result1.setUrls(config.getUrls());

            XQResult result2 = new XQResult(config.getQuery2(), config.getProcessor(), config.getUrls());

            setResults(result1, result2);
        } catch (InternalException | IOException e) {
            throw new AnalysisException(e.getMessage());
        }
    }

    /**
     * Sets the results to be compared and analyzes the differences.
     *
     * @param result1 The "correct" query result.
     * @param result2 The "submitted" query result.
     * @throws WellformednessException if the correct query result can not be interpreted as XML
     *                                 fragment.
     * @throws QuerySyntaxException    if the correct query result object was created from a query,
     *                                 which is syntactically incorrect.
     * @throws AnalysisException       if any kind of unexpected Exception occured during analyzing the
     *                                 results.
     */
    public void setResults(XQResult result1, XQResult result2) throws AnalysisException,
            WellformednessException, QuerySyntaxException, UrlContentException {

        //Parameters for files for debugging purpose or files required for analysis
        FileParameter xml1FileParameter = getXML1FileParameter();
        FileParameter xml2FileParameter = getXML2FileParameter();
        FileParameter dtdFileParameter = getDTDFileParameter();
        FileParameter xslFileParameter = getXSLFileParameter();

        // LOGGER.debug("Start comparing correct and submitted result object");
        if (result1.getSyntaxException() != null) {
            throw result1.getSyntaxException();
        }
        if (result1.getWellformednessException() != null) {
            throw result1.getWellformednessException();
        }
        if (result1.getUrlContentException() != null) {
            throw result1.getUrlContentException();
        }
        this.result1 = result1;
        this.result2 = result2;

        this.result1.setXMLFileParameter(xml1FileParameter);
        this.result2.setXMLFileParameter(xml2FileParameter);
        this.result1.setDTDFileParameter(dtdFileParameter);

        if (isDebugMode()) {
            LOGGER.info("Debug mode: writing the correct and submitted XQuery results "
                        + "as XML files, as well as the DTD of the correct result.");
            try {
                File xml1 = this.result1.getResultFile(true);
                LOGGER.info("Written to file " + xml1.getAbsolutePath());
                File xml2 = this.result2.getResultFile(true);
                LOGGER.info("Written to file " + xml2.getAbsolutePath());
                //DTD of result2 is not needed for analysis
                File dtd = this.result1.getDTDFile(true);
                LOGGER.info("Written to file " + dtd.getAbsolutePath());
            } catch (QuerySyntaxException e) {
            } catch (UrlContentException e) {
            } catch (InternalException e) {
                String msg = "An internal exception was thrown when writing a file written only in debug modus: ";
                msg += e.getClass().getName() + ": " + e.getMessage();
                LOGGER.error(msg);
                throw new AnalysisException(msg, e);
            }
        }

        this.setSubmission(result2.getQuery());
        if (result2.isWellformed()) {
            XMLDiff xmlDiff = new XMLDiff();
            xmlDiff.setInput1(result2.getDocument());
            xmlDiff.setInput2(result1.getDocument());

            try {
                //must be called before generating XSL
                different = xmlDiff.diff();
                //System.out.println("Different? " + different);
                File tempFile = this.getXSLFileParameter().generateTempFile();
                xmlDiff.generateXSLFile(tempFile.getAbsolutePath());
                xslDiff = XMLUtil.parse(tempFile.toURL(), false);
                this.validSolution = isValid(result1, result2);
                compare(result1, result2);
            } catch (QuerySyntaxException e) {
                // Unexpected: both, result1 and result2 have already been tested on syntax errors
                String msg = "A fatal exception occured when comparing XQuery results, which were expected to be wellformed: ";
                msg += e.getClass().getName() + ": " + e.getMessage();
                throw new AnalysisException(msg, e);
            } catch (WellformednessException e) {
                // Unexpected: both, result1 and result2 have already been tested on wellformedness
                String msg = "A fatal exception occured when comparing XQuery results, which were expected to be wellformed: ";
                msg += e.getClass().getName() + ": " + e.getMessage();
                throw new AnalysisException(msg, e);
            } catch (UrlContentException e) {
                // Unexpected: both, result1 and result2 have already been tested on url contents
                String msg = "A fatal exception occured when comparing XQuery results, which were expected to be wellformed: ";
                msg += e.getClass().getName() + ": " + e.getMessage();
                throw new AnalysisException(msg, e);
            } catch (DocumentException e) {
                String msg = "A fatal exception occured when comparing XQuery results, which were expected to be wellformed: ";
                msg += e.getClass().getName() + ": " + e.getMessage();
                throw new AnalysisException(msg, e);
            } catch (SAXException e) {
                String msg = "A fatal exception occured when comparing XQuery results, which were expected to be wellformed: ";
                msg += e.getClass().getName() + ": " + e.getMessage();
                throw new AnalysisException(msg, e);
            } catch (IOException e) {
                String msg = "A fatal exception occured when comparing XQuery results, which were expected to be wellformed: ";
                msg += e.getClass().getName() + ": " + e.getMessage();
                throw new AnalysisException(msg, e);
            } catch (XSLException e) {
                String msg = "A fatal exception occured when comparing XQuery results, which were expected to be wellformed: ";
                msg += e.getClass().getName() + ": " + e.getMessage();
                throw new AnalysisException(msg, e);
            } catch (InternalException e) {
                String msg = "A fatal exception occured when comparing XQuery results, which were expected to be wellformed: ";
                msg += e.getClass().getName() + ": " + e.getMessage();
                throw new AnalysisException(msg, e);
            }
        }

        this.submissionSuitsSolution = this.isCorrect();
    }

    /**
     * Initializes the fields of this analysis object.
     *
     * @param config configuration object holding parameters like a flag which is set to know if
     *               intermediate results should be saved to files when analyzing, grading, and reporting later on.
     */
    private void init(XQAnalysisConfig config) {

        setDebugMode(config.isDebugMode());
        setExerciseID(config.getExerciseID());
        setUserID(config.getUserID());

        different = true;
        validSolution = false;
        submissionSuitsSolution = false;
        redundantNodes = new NodeErrorList();
        redundantInsteadNodes = new NodeErrorList();
        displacedNodes = new NodeErrorList();
        missingInsteadNodes = new NodeErrorList();
        missingPreviousNodes = new NodeErrorList();
        missingNextNodes = new NodeErrorList();
        missingInnerNodes = new NodeErrorList();
        redundantAttributes = new NodeErrorList();
        incorrectAttributeValues = new NodeErrorList();
        missingAttributes = new NodeErrorList();
        incorrectTextValues = new NodeErrorList();
    }

    /**
     * Checks if some node has children which are in scope of the specified namespace. This is used
     * for analyzing the templates of the XSL document, which represents the differences between the
     * submitted query result and the correct query result, both in form of an XML document.
     *
     * @param node      The XML node which is searched for children with the specified namespace URI.
     * @param namespace The URI of the namespace.
     * @return true if at least one child of the specified node is in scope of the specified
     * namespace, false if there is none.
     */
    private boolean containsNamespaceElement(Node node, String namespace) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node child = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && isNamespaceNode(child, namespace)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a node is in scope of the specified namespace. This is used for analyzing the
     * templates of the XSL document, which represents the differences between the submitted query
     * result and the correct query result, both in form of an XML document.
     *
     * @param node      The XML node to check.
     * @param namespace The URI of the namespace.
     * @return true if the node is in scope of the specified namespace, false if there is none.
     */
    private boolean isNamespaceNode(Node node, String namespace) {
        if (node.getNamespaceURI() != null && node.getNamespaceURI().equals(namespace)) {
            return true;
        }
        return false;
    }

    /**
     * Tests if an XML node has to be considered in the analysis, depending on its type. Basically,
     * comment nodes are excluded, whereas text nodes, elements and attributes are analyzed with
     * regard to differences between the correct and the submitted solution.
     *
     * @param node The XML node to test.
     * @return true if the node has a node type which implies that it should be analyzed.
     */
    private boolean hasRegardableNodeType(Node node) {
        if (node != null) {
            if (node.getNodeType() != Node.COMMENT_NODE) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests if the submitted solution is at least valid to the structure of the correct solution,
     * both interpreted as XML documents. This is accomplished by generating the DTD of the correct
     * solution and validating the submitted solution with that DTD.
     * <p>
     * This can be considered as the first step when analyzing the differences between two
     * solutions. If the submitted solution is not valid with regard to the given DTD, this means
     * that basically the structure of the result is incorrect. So any further information about
     * errors (displaced elements, missing attributess, ...) is understood as hint, how to structure
     * the result of a query, so that in could be correct at all.
     *
     * @param result1 The "correct" query result.
     * @param result2 The "submitted" query result.
     * @return true if the submitted query result conforms the DTD of the correct query result,
     * false if at least one validation error could be detected.
     * @throws IOException             if an internal IO exception occured when processing the XML document so
     *                                 that it can be validated.
     * @throws DocumentException       if an internal parsing exception occured when processing the XML
     *                                 document so that it can be validated.
     * @throws SAXException            if the generated DTD is not valid itself.
     * @throws InternalException       if an unexpected Exception occured when writing the contents of the
     *                                 XML document of the correct query result or the contents of the generated DTD to
     *                                 a file.
     * @throws QuerySyntaxException    if one of the results was built from a query, which is not
     *                                 syntactically correct.
     * @throws UrlContentException     if one of the results was built from a query, which contains some
     *                                 <code>doc</code> function statements with contents, which are interpreted as
     *                                 alias names, but the corresponding urls were not specified.
     * @throws WellformednessException if one of the query results can not be interpreted as XML
     *                                 fragment.
     */
    private boolean isValid(XQResult result1, XQResult result2) throws IOException, SAXException,
            DocumentException, QuerySyntaxException, InternalException, WellformednessException,
            UrlContentException {
        // Check syntax and wellformedness of the submitted result. If syntax or wellformedness of
        // the correct solution is incorrect, according exceptions are thrown when calling the
        // following methods.
        if (result2.getWellformednessException() != null) {
            throw result2.getWellformednessException();
        }
        if (result2.getSyntaxException() != null) {
            throw result2.getSyntaxException();
        }
        if (result2.getUrlContentException() != null) {
            throw result2.getUrlContentException();
        }
        File dtd = result1.getDTDFile(false);
        return XMLUtil.isValid(result2.getDocument(), XQResult.XML_ROOT, dtd.toURL().toString());
    }

    /**
     * Does the analysis of two XQuery query results. Found differences are stored in fields of this
     * <code>XQAnalysis</code>.
     *
     * @param result1 The "correct" query result.
     * @param result2 The "submitted" query result.
     * @throws XSLException      if any unexpected Exception was thrown when analyzing the differences
     *                           between the XML documents which represent the query results. These differences in
     *                           turn are represented by an XSL document which is used for analyzing.
     * @throws InternalException if an internal error occured when removing nodes from the XSL
     *                           stylesheet which represent incorrectly sorted nodes but are not incorrectly
     *                           sorted in the context of the exercise.
     */
    private void compare(XQResult result1, XQResult result2) throws XSLException, InternalException {

        if (result2 == null || result2.getDocument() == null
            || result2.getSyntaxException() != null
            || result2.getWellformednessException() != null || xslDiff == null) {
            return;
        }

        XMLDocument document2 = result2.getDocument();
        XMLElement xslDiff = (XMLElement) this.xslDiff.getDocumentElement();
        String namespace = xslDiff.getNamespaceURI();

        TreeMap displacedNodesTemp = new TreeMap();
        // Search for xsl:templates with 'match' values which are found in the 'select' value
        // of an 'xsl:copy-of' element somewhere else in the stylesheet. This indicates that the
        // node denoted by the 'match' value is displaced.
        String copyOfPattern = "//xsl:template[@match = //xsl:copy-of/@select]/@match";
        NodeList copyList = xslDiff.selectNodes(copyOfPattern, xslDiff);
        for (int i = 0; i < copyList.getLength(); i++) {
            Node attr = copyList.item(i);
            Node node = document2.selectSingleNode(attr.getNodeValue(), document2);
            if (hasRegardableNodeType(node)) {
                displacedNodesTemp.put(attr.getNodeValue(), node);
            }
        }
        // Search for xsl:templates with 'match' values which are found in the 'name' value
        // of an 'xsl:element' element somewhere else in the stylesheet. This also indicates that the
        // node denoted by the 'match' value is displaced.
        copyOfPattern = "//xsl:template[concat('{name(', @match, ')}') = //xsl:element/@name]/@match";
        copyList = xslDiff.selectNodes(copyOfPattern, xslDiff);
        for (int i = 0; i < copyList.getLength(); i++) {
            Node attr = copyList.item(i);
            Node node = document2.selectSingleNode(attr.getNodeValue(), document2);
            if (hasRegardableNodeType(node)) {
                displacedNodesTemp.put(attr.getNodeValue(), node);
            }
        }

        // Search for xsl:templates. If such a template has no children,
        // this means the corresponding node is redundant (denoted by an XPath in the match
        // attribute)
        String matchPattern = "//xsl:template/@match";
        NodeList matchList = xslDiff.selectNodes(matchPattern, xslDiff);
        for (int i = 0; i < matchList.getLength(); i++) {
            String matchValue = matchList.item(i).getNodeValue();

            String matchNodePattern = "//xsl:template[@match='" + matchValue + "']";
            // TODO: two patterns with same match -> Exception, Warning
            String pattern = matchNodePattern
                             + "/xsl:copy[xsl:apply-templates/@select='node()|@*']";
            Node copyNode = xslDiff.selectSingleNode(pattern, xslDiff);
            // Second chance, if no template with the specified pattern was found
            if (copyNode == null) {
                pattern = matchNodePattern + "/xsl:element[@name='{name()}']";
                copyNode = xslDiff.selectSingleNode(pattern, xslDiff);
                ArrayList innerElements = getInnerElements(copyNode, namespace);
                ArrayList innerTextNodes = getInnerTextNodes(copyNode);
                ArrayList missingAtts = getMissingAttributes(copyNode);

                // This is the case when the xsl:template matches an XPath expression which denotes
                // an attribute
                pattern = matchNodePattern + "/xsl:attribute";
                NodeList attributeNodes = xslDiff.selectNodes(pattern, xslDiff);
                boolean foundIncorrectAttribute = false;
                for (int j = 0; j < attributeNodes.getLength(); j++) {
                    String attributeOwner = matchValue.substring(0, matchValue.lastIndexOf("/@"));
                    String name = attributeNodes.item(j).getAttributes().getNamedItem("name")
                            .getNodeValue();
                    String value = "";
                    // An attribute might have an empty String as value. In the stylesheet this is
                    // represented
                    // by an empty element, which has no children.
                    if (attributeNodes.item(j).getFirstChild() != null) {
                        value = attributeNodes.item(j).getFirstChild().getNodeValue();
                    }
                    incorrectAttributeValues.addNode(attributeOwner, new NodeFactory()
                            .createAttribute(name, value));
                    foundIncorrectAttribute = true;
                }
                if (innerElements.size() > 0 || innerTextNodes.size() > 0 || missingAtts.size() > 0 || foundIncorrectAttribute) {
                    if (innerElements.size() > 0) {
                        missingInnerNodes.addNodes(matchValue, (Node[]) innerElements
                                .toArray(new Node[]{}));
                    }
                    if (innerTextNodes.size() > 0) {
                        incorrectTextValues.addNodes(matchValue, (Node[]) innerTextNodes
                                .toArray(new Node[]{}));
                    }
                    if (missingAtts.size() > 0) {
                        missingAttributes.addNodes(matchValue, (Node[]) missingAtts
                                .toArray(new Node[]{}));
                    }
                } else {
                    Node node = xslDiff.selectSingleNode(matchNodePattern, xslDiff);
                    ArrayList missingNodes = getInnerNodes(node, namespace);
                    if (missingNodes.size() > 0) {
                        //Missing nodes have been checked already, but these ones here are
                        // considered to be replaced completely by a node of the correct result.
                        // In other words, some redundant nodes are found instead of some
                        // required, missing node. This error is split up feeding the 'redundant
                        // nodes' category as well as the 'missing nodes' category.
                        Node redundantNode = document2.selectSingleNode(matchValue, document2);
                        redundantInsteadNodes.addNode(matchValue, redundantNode);
                        missingInsteadNodes.addNodes(matchValue, (Node[]) missingNodes
                                .toArray(new Node[]{}));
                    } else if (!displacedNodesTemp.containsKey(matchValue)) {
                        Node redundantNode = document2.selectSingleNode(matchValue, document2);
                        if (hasRegardableNodeType(redundantNode)) {
                            if (redundantNode.getNodeType() == Node.ATTRIBUTE_NODE) {
                                String attributeOwner = matchValue.substring(0, matchValue
                                        .lastIndexOf("/@"));
                                redundantAttributes.addNode(attributeOwner, redundantNode);
                            } else {
                                redundantNodes.addNode(matchValue, redundantNode);
                            }
                        }
                    }
                }
            }
            // TODO: applies to xsl:template/xsl:element as well as to xsl:template/xsl:copy ?
            ArrayList previousNodes = getPreviousNodes(copyNode, new ArrayList(), namespace);
            if (previousNodes.size() > 0) {
                missingPreviousNodes.addNodes(matchValue, (Node[]) previousNodes
                        .toArray(new Node[]{}));
            }

            // TODO: applies to xsl:template/xsl:element as well as to xsl:template/xsl:copy ?
            ArrayList nextNodes = getNextNodes(copyNode, new ArrayList(), namespace);
            if (nextNodes.size() > 0) {
                missingNextNodes.addNodes(matchValue, (Node[]) nextNodes.toArray(new Node[]{}));
            }

        }
        displacedNodes = getDisplacedNodes(displacedNodesTemp, result1.getSortedNodes(), xslDiff);

    }

    /**
     * Returns a list of xml nodes which are in fact displaced. On the one hand, the XSL stylesheet
     * which represents all differences between two XML documents, tells about any displaced node
     * that was detected. In terms of an XQuery result, interpreted as XML document, this does not
     * necessarily mean that this is an error, because the ordering of XML nodes does not have to be
     * the same as in the correct result by default. This is additionally specified by an array of
     * XPath expressions, denoting XML nodes that have to be in a certain order.
     * <p>
     * In this method, unnessecary nodes are excluded from the list of displaced nodes that were
     * found generally. The criterion is that a node must appear in in at least one node list which
     * results from evaluating one of the XPath expressions. Otherwise it is excluded.
     * <p>
     * Additionally, the stylesheet is adapted: The XSL elements of corresponding nodes which are
     * not displaced in the context of the exercise, are modified so that these nodes will be
     * transformed without displaying an error.
     *
     * @param allDisplacedNodes A map of key/value-pairs, whereas the key is an XPath expression,
     *                          and the corresponding value the (unique) XML node from the submitted query result
     *                          document, which results from evaluating the expression.
     * @param sortedNodes       A number of XPath expressions, which indicates that each node list that
     *                          results from evaluating one of these expressions must be in the same order in the
     *                          submitted result as it is in the correct result.
     * @param xslDoc            This is expected to be the root element of the XSL stylesheet. This document
     *                          will be modified.
     * @return An object which holds a list of all remaining displaced nodes.
     * @throws XSLException      if an unexpected Exception was thrown when analyzing the documents that
     *                           represent the results of the XQuery queries.
     * @throws InternalException if some node which is selected from the stylesheet for modification
     *                           can not be identified uniquely, or if the <code>xsl:copy-of</code> element can
     *                           not be removed, because it has no parent.
     */
    private NodeErrorList getDisplacedNodes(TreeMap allDisplacedNodes, String[] sortedNodes,
                                            XMLElement xslDoc) throws XSLException, InternalException {
        NodeErrorList displacedNodes = new NodeErrorList();
        XMLDocument document2 = result2.getDocument();
        if (document2 == null) {
            return displacedNodes;
        }

        // ignore displaced nodes which are not required to be sorted
        TreeMap sortedNodesMap = new TreeMap();
        if (sortedNodes != null) {
            for (int i = 0; i < sortedNodes.length; i++) {
                sortedNodesMap
                        .put(sortedNodes[i], document2.selectNodes(sortedNodes[i], document2));
            }
        }
        Iterator displacedNodesKeys = allDisplacedNodes.keySet().iterator();
        Set sortedNodesKeySet = sortedNodesMap.keySet();
        while (displacedNodesKeys.hasNext()) {
            String xPathKey = (String) displacedNodesKeys.next();
            Node node = (Node) allDisplacedNodes.get(xPathKey);
            Iterator sortedNodesKeys = sortedNodesKeySet.iterator();
            boolean found = false;
            while (sortedNodesKeys.hasNext() && !found) {
                String xPath = (String) sortedNodesKeys.next();
                NodeList nodeList = (NodeList) sortedNodesMap.get(xPath);
                for (int i = 0; i < nodeList.getLength() && !found; i++) {
                    if (nodeList.item(i) == node) {
                        displacedNodes.addNode(xPath, node);
                        found = true;
                    }
                }
            }
            if (!found) {
                removeDisplacedNodes(xPathKey, xslDoc);
            }
        }
        return displacedNodes;
    }

    /**
     * An XSL stylesheet as it is used for analyzing the differences between some query results,
     * displays all detected 'errors'. As the sorting of nodes is only required if specified
     * explicitly in the configuration of an XQuery exercise, the XSL stylesheet has to be adapted.
     * <p>
     * A node is considered to be displaced, if in the stylesheet there is some template without any
     * XSL element children, like <br>
     *
     * <pre>
     *
     *  &lt;xsl:template match=&quot;/employees/person[1]&quot;/&gt;
     *
     * </pre>
     * <p>
     * and somewhere else in the stylesheet there is a template with an <code>xsl:copy-of</code>
     * element child like this one:
     *
     * <pre>
     *
     *  &lt;xsl:template match=&quot;/employees/person[2]&quot;&gt;
     * 	  &lt;xsl:copy-of select=&quot;/employees/person[1]&quot;/&gt;
     * 	  &lt;xsl:copy&gt;
     * 	     &lt;xsl:apply-templates select=&quot;node()|@*&quot;/&gt;
     * 	  &lt;/xsl:copy&gt;
     *  &lt;/xsl:template&gt;
     *
     * </pre>
     * <p>
     * This means that the first <code>person</code> element should be copied before the first
     * <code>person</code> element. If the ordering of these <code>person</code> elements is not
     * required, the XSL stylesheet will be adapted here, so that the result looks like the
     * following: <p>
     * First there is the template, which will be added a <code>xsl:copy</code> element, implying
     * that if the XSL processor comes across the first <code>person</code> element, this node
     * will be simply copied where it is found:
     *
     * <pre>
     *
     *  &lt;xsl:template match=&quot;/employees/person[1]&quot;&gt;
     *    &lt;xsl:copy&gt;
     * 	    &lt;xsl:apply-templates select=&quot;node()|@*&quot;/&gt;
     * 	 &lt;/xsl:copy&gt;
     *  &lt;/xsl:template&gt;
     *
     * </pre>
     * <p>
     * The <code>xsl:copy-of</code> will be removed from the other template:
     *
     * <pre>
     *
     *  &lt;xsl:template match=&quot;/employees/person[2]&quot;&gt;
     * 	  &lt;xsl:copy&gt;
     * 	     &lt;xsl:apply-templates select=&quot;node()|@*&quot;/&gt;
     * 	  &lt;/xsl:copy&gt;
     *  &lt;/xsl:template&gt;
     *
     * </pre>
     *
     * @param matchValue This is the XPath of the displaced node. In the example above this is the
     *                   value of the <code>match</code> and <code>select</code> attributes.
     * @param xslDoc     This is expected to be the root element of the XSL stylesheet. This document
     *                   will be modified.
     * @throws XSLException      if any XSLException occured when selecting the nodes of the stylesheet,
     *                           which have to be adapted.
     * @throws InternalException if some node which is selected from the stylesheet for modification
     *                           can not be identified uniquely, or if the <code>xsl:copy-of</code> element can
     *                           not be removed, because it has no parent.
     */
    private void removeDisplacedNodes(String matchValue, XMLElement xslDoc) throws XSLException,
            InternalException {
        String unclearXPath = null;
        Node copyOfNode = null;

        String xPath = "//xsl:template[@match='" + matchValue + "']";
        NodeList templateList = xslDoc.selectNodes(xPath, xslDoc);
        Node templateNode = null;
        if (templateList.getLength() != 1) {
            unclearXPath = xPath;
        }

        // Search for displaced nodes using the first pattern, searching for 'xsl:copy-of' elements
        xPath = "//xsl:template//xsl:copy-of[@select='" + matchValue + "']";
        NodeList copyOfList = xslDoc.selectNodes(xPath, xslDoc);
        if (copyOfList.getLength() != 1) {
            //Bug of Oracle API (?): Second try -> in some cases the double slash selector fails
            xPath = "//xsl:template/xsl:copy-of[@select='" + matchValue + "']";
            copyOfList = xslDoc.selectNodes(xPath, xslDoc);
            if (copyOfList.getLength() != 1) {
                unclearXPath = xPath;
            }
        }

        // If not successfull, search for displaced nodes using the second pattern, searching for 
        // special 'xsl:element' elements
        if (unclearXPath != null) {
            xPath = "//xsl:template//xsl:element[xsl:value-of/@select and @name='{name(" + matchValue + ")}']";
            copyOfList = xslDoc.selectNodes(xPath, xslDoc);
            if (copyOfList.getLength() != 1) {
                //Bug of Oracle API (?): Second try -> in some cases the double slash selector fails
                xPath = "//xsl:template/xsl:element[xsl:value-of/@select and @name='{name(" + matchValue + ")}']";
                copyOfList = xslDoc.selectNodes(xPath, xslDoc);
            }
            if (copyOfList.getLength() != 1) {
                unclearXPath = xPath;
            } else {
                unclearXPath = null;
            }
        }

        if (unclearXPath != null) {
            String message = "An internal exception was thrown when adapting the internal XSL stylesheet, ";
            message += "which represents the differences between correct and submitted query result. ";
            message += "Evaluation of XPath expression " + unclearXPath + " was expected to return exactly one node, but did not.";
            throw new InternalException(message);
        }

        copyOfNode = copyOfList.item(0);
        templateNode = templateList.item(0);

        XMLDocument doc = (XMLDocument) xslDoc.getOwnerDocument();
        XMLElement applyTemplatesElement = (XMLElement) doc.createElementNS(templateNode
                .getNamespaceURI(), "xsl:apply-templates");
        applyTemplatesElement.setAttribute("select", "node()|@*");
        XMLElement copyElement = (XMLElement) doc.createElementNS(templateNode.getNamespaceURI(),
                "xsl:copy");
        copyElement.appendChild(applyTemplatesElement);

        Node parent = copyOfNode.getParentNode();
        templateNode.appendChild(copyElement);
        parent.removeChild(copyOfNode);
    }

    /**
     * Returns a list of attributes, that are detected to be missing in an element of the submitted
     * query result. The passed node is interpreted as XSL node, which contains elements called
     * <code>xsl:attribute</code>, indicating that an attribute should be created if the XSL
     * stylesheet was applied by an XSL processor.
     *
     * @param node The XSL node to check for missing attributes.
     * @return A list of XML attributes.
     */
    private ArrayList getMissingAttributes(Node node) {
        ArrayList list = new ArrayList();
        if (node != null) {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE
                    && child.getNodeName().equals("xsl:attribute")) {
                    String name = child.getAttributes().getNamedItem("name").getNodeValue();
                    String value = "";
                    // An attribute might have an empty String as value. In the stylesheet this is
                    // represented
                    // by an empty element, which has no children.
                    if (child.getFirstChild() != null) {
                        value = child.getFirstChild().getNodeValue();
                    }
                    list.add(new NodeFactory().createAttribute(name, value));
                }
            }
        }
        return list;
    }

    /**
     * Returns a list of text or element nodes, that are detected to be missing in an element of the
     * submitted query result. The passed node is interpreted as XSL node, which may in turn contain
     * further XSL elements as well as ordinary elements. These ordinary elements are interpreted as
     * missing in the submitted query result.
     *
     * @param node              The XSL node to check for missing nodes <i>within </i> an element of the
     *                          submitted query result.
     * @param excludedNamespace This is used for distinguishing between nodes that are considered as
     *                          XSL elements and elements that actually are part or should be part of the query
     *                          result.
     * @return Only the non-XSL-nodes <i>within </i> the specified node.
     */
    private ArrayList getInnerNodes(Node node, String excludedNamespace) {
        ArrayList list = new ArrayList();
        if (node != null) {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (hasRegardableNodeType(child) && !isNamespaceNode(child, excludedNamespace)) {
                    list.add(child);
                }
            }
        }
        return list;
    }

    /**
     * Returns a list of element nodes, that are detected to be missing in an element of the
     * submitted query result. The passed node is interpreted as XSL node, which may in turn contain
     * further XSL elements as well as ordinary elements. These ordinary elements are interpreted as
     * missing in the submitted query result.
     *
     * @param node              The XSL node to check for missing elements <i>within </i> an element of the
     *                          submitted query result.
     * @param excludedNamespace This is used for distinguishing between nodes that are considered as
     *                          XSL elements and elements that actually are part or should be part of the query
     *                          result.
     * @return Only the non-XSL-elements <i>within </i> the specified node.
     */
    private ArrayList getInnerElements(Node node, String excludedNamespace) {
        ArrayList list = new ArrayList();
        if (node != null) {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE && !isNamespaceNode(child, excludedNamespace)) {
                    list.add(child);
                }
            }
        }
        return list;
    }

    /**
     * Returns a list of text nodes, that are detected to be missing in an element of the
     * submitted query result. The passed node is interpreted as XSL node, which may in turn contain
     * further XSL elements as well as ordinary elements. These ordinary text nodes are interpreted as
     * missing in the submitted query result.
     *
     * @param node The XSL node to check for missing text nodes <i>within </i> an element of the
     *             submitted query result.
     * @return The text nodes <i>within </i> the specified node.
     */
    private ArrayList getInnerTextNodes(Node node) {
        ArrayList list = new ArrayList();
        if (node != null) {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.TEXT_NODE) {
                    list.add(child);
                }
            }
        }
        return list;
    }

    /**
     * Returns a list of text or element nodes, that are detected to be missing before an element of
     * the submitted query result. The passed node is interpreted as XSL node, which may be the
     * sibling <i>after </i> other XSL elements as well as after ordinary elements. These ordinary
     * elements are interpreted as missing in the submitted query result.
     *
     * @param node              The XSL node to check for missing nodes <i>before </i> an element of the
     *                          submitted query result.
     * @param list              A list which is returned, supplemented with found nodes.
     * @param excludedNamespace This is used for distinguishing between nodes that are considered as
     *                          XSL elements and elements that actually are part or should be part of the query
     *                          result.
     * @return Only the non-XSL-elements <i>before </i> the specified node.
     */
    private ArrayList getPreviousNodes(Node node, ArrayList list, String excludedNamespace) {
        if (node == null) {
            return list;
        }
        Node previous = node.getPreviousSibling();

        if (previous == null) {
            return list;
        } else {
            if (hasRegardableNodeType(previous) && !isNamespaceNode(previous, excludedNamespace)) {
                list.add(0, previous);
            }
            return getPreviousNodes(previous, list, excludedNamespace);
        }
    }

    /**
     * Returns a list of text or element nodes, that are detected to be missing after an element of
     * the submitted query result. The passed node is interpreted as XSL node, which may be the
     * sibling <i>before </i> other XSL elements as well as after ordinary elements. These ordinary
     * elements are interpreted as missing in the submitted query result.
     *
     * @param node              The XSL node to check for missing nodes <i>after </i> an element of the submitted
     *                          query result.
     * @param list              A list which is returned, supplemented with found nodes.
     * @param excludedNamespace This is used for distinguishing between nodes that are considered as
     *                          XSL elements and elements that actually are part or should be part of the query
     *                          result.
     * @return Only the non-XSL-elements <i>after </i> the specified node.
     */
    private ArrayList getNextNodes(Node node, ArrayList list, String excludedNamespace) {
        if (node == null) {
            return list;
        }
        Node next = node.getNextSibling();

        if (next == null) {
            return list;
        } else {
            if (hasRegardableNodeType(next) && !isNamespaceNode(next, excludedNamespace)) {
                list.add(next);
            }
            return getNextNodes(next, list, excludedNamespace);
        }
    }

    /**
     * Can be used to verify if an expression which is interpreted as XPath expression, denotes the
     * root of the XML documents used for comparing and analyzing the results of queries.
     *
     * @param expression An XPath expression.
     * @return true if the specified expression can be interpreted as valid XPath expression and
     * returns the root element when evaluated, otherwise false.
     */
    public boolean isRootNode(String expression) {
        XMLDocument document1 = result1.getDocument();
        if (document1 != null) {
            try {
                if (document1.selectSingleNode(expression, document1) == document1
                        .getDocumentElement()) {
                    return true;
                }
            } catch (XSLException e) {
            }
        }
        return false;
    }

    /**
     * Returns the description of (text or element) nodes that are in wrong order compared to the
     * correct result.
     *
     * @return An object describing the found errors.
     */
    public NodeErrorList getDisplacedNodes() {
        return displacedNodes;
    }

    /**
     * Returns the description of attributes that have a different value than the same attribute in
     * the correct result.
     *
     * @return An object describing the found errors.
     */
    public NodeErrorList getIncorrectAttributeValues() {
        return incorrectAttributeValues;
    }

    /**
     * Returns the description of attributes that are missing in the submitted result.
     *
     * @return An object describing the found errors.
     */
    public NodeErrorList getMissingAttributes() {
        return missingAttributes;
    }

    /**
     * Returns the description of (text or element) nodes that are missing <i>within </i> some
     * element of the submitted result.
     *
     * @return An object describing the found errors.
     */
    public NodeErrorList getMissingInnerNodes() {
        return missingInnerNodes;
    }

    /**
     * Returns the description of (text or element) nodes that are missing <i>after </i> some
     * element of the submitted result.
     *
     * @return An object describing the found errors.
     */
    public NodeErrorList getMissingNextNodes() {
        return missingNextNodes;
    }

    /**
     * Returns the description of (text or element) nodes that are missing <i>before </i> some
     * element of the submitted result.
     *
     * @return An object describing the found errors.
     */
    public NodeErrorList getMissingPreviousNodes() {
        return missingPreviousNodes;
    }

    /**
     * Returns the description of (text or element) nodes that are redundant in the submitted
     * result.
     *
     * @return An object describing the found errors.
     */
    public NodeErrorList getRedundantNodes() {
        return redundantNodes;
    }

    /**
     * Returns the description of (text or element) nodes that are redundant in the submitted result
     * and should be replaced by some missing nodes of the correct result.
     * <p>
     * <b>Note </b>, that the entries of this category correlate with entries of the category as
     * returned by {@link #getMissingInsteadNodes()}. If some redundant node is found in the
     * submitted result, instead of a required, missing node of the correct result, this error is
     * split up feeding both categories. The resulting {@link NodeError}objects both will have the
     * same XPath key.
     *
     * @return An object describing the found errors.
     */
    public NodeErrorList getRedundantInsteadNodes() {
        return redundantInsteadNodes;
    }

    /**
     * Returns the description of (text or element) nodes that are found in the submitted result in
     * place of a node that was rather expected.
     * <p>
     * <b>Note </b>, that the entries of this category with entries of the category as returned by
     * {@link #getRedundantInsteadNodes()}. If some redundant node is found in the submitted
     * result, instead of a required, missing node of the correct result, this error is split up
     * feeding both categories. The resulting {@link NodeError}objects both will have the same
     * XPath key.
     *
     * @return An object describing the found errors.
     */
    public NodeErrorList getMissingInsteadNodes() {
        return missingInsteadNodes;
    }

    /**
     * Returns the description of attributes that are redundant in some element of the submitted
     * result.
     *
     * @return An object describing the found errors.
     */
    public NodeErrorList getRedundantAttributes() {
        return redundantAttributes;
    }

    /**
     * Returns the description of text nodes that have an incorrect value.
     *
     * @return An object describing the found errors.
     */
    public NodeErrorList getIncorrectTextValues() {
        return incorrectTextValues;
    }

    /**
     * Gives information whether the analyzed submitted result is valid in that in conforms to the
     * DTD which was generated from the correct result.
     *
     * @return true if the submitted query result does not result from a syntactically incorrect
     * query, if the result can be interpreted as XML fragment, and if the result has the
     * same structure as the correct query result, so that not a single validation error can
     * be detected;
     */
    public boolean isValidSolution() {
        return validSolution;
    }

    /**
     * Returns the document which represents an XSL stylesheet that depicts the differences between
     * the submitted and the correct query results, interpreted as XML fragments.
     *
     * @return Returns the XML document representing the XSL stylesheet; <code>null</code>, if
     * either no analysis was done or if the submitted query result was built from a query
     * which has syntax errors, or if the submitted query result can not be interpreted as
     * XML fragment.
     */
    public XMLDocument getXSLDiff() {
        return xslDiff;
    }

    /**
     * Returns the correct query result.
     *
     * @return The correct query result, if set, otherwise <code>null</code>.
     */
    public XQResult getResult1() {
        return result1;
    }

    /**
     * Returns the submitted query result.
     *
     * @return The submitted query result, if set, otherwise <code>null</code>.
     */
    public XQResult getResult2() {
        return result2;
    }

    /**
     * Tells if the result that was analyzed is correct. The requirements are:
     * <p>
     * <li>{@link #isAnalyzable()}must return true</li>
     * <li>No error has been found in the submitted query result.</li>
     * <p>
     * Note that a call to this is only sensible if an analysis has actually been carried out.
     *
     * @return true if the submitted query is correct.
     */
    public boolean isCorrect() {
        boolean correct = isAnalyzable() && (!this.getDisplacedNodes().containsErrors())
                          && (!this.getIncorrectTextValues().containsErrors())
                          && (!this.getIncorrectAttributeValues().containsErrors())
                          && (!this.getMissingAttributes().containsErrors())
                          && (!this.getMissingInnerNodes().containsErrors())
                          && (!this.getMissingInsteadNodes().containsErrors())
                          && (!this.getMissingNextNodes().containsErrors())
                          && (!this.getMissingPreviousNodes().containsErrors())
                          && (!this.getRedundantAttributes().containsErrors())
                          && (!this.getRedundantNodes().containsErrors())
                          && (!this.getRedundantInsteadNodes().containsErrors());
        return correct;
    }

    /**
     * Provides information about whether the query results that were used for this analysis objects
     * are comparable and analyzable. The requirements are:
     * <ul>
     * <li>An analysis has been carried out before.</li>
     * <li>Both, the submitted and the correct query must be syntactically correct and the result
     * of the query must be wellformed, so that it can be interpreted as XML document for analysis.
     * </li>
     * </ul>
     *
     * @return true if the results used for this analysis objects are analyzable, otherwise false.
     */
    public boolean isAnalyzable() {
        boolean analyzable = result1 != null && result1.isWellformed() && result2 != null
                             && result2.isWellformed();
        return analyzable;
    }

    /*
     * (non-Javadoc)
     *
     * @see etutor.core.evaluation.Analysis#setSubmission(java.io.Serializable)
     */
    public void setSubmission(Serializable submission) {
        this.submission = submission;
    }

    /*
     * (non-Javadoc)
     *
     * @see etutor.core.evaluation.Analysis#getSubmission()
     */
    public Serializable getSubmission() {
        return this.submission;
    }


    /**
     * Needed for making instances of <code>XQAnalysis</code> serializable. All fields are
     * serialized by default, except for XML document objects, which are exported to a String.
     *
     * @param out The output stream.
     * @throws IOException if an IOException occured when serializing this object.
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        LOGGER.debug("Start serializing " + this.toString());
        /*
        LOGGER.debug("\t1. default serialization");
        */
        out.defaultWriteObject();

        /*
        LOGGER.debug("\t2. customized serialization of transient fields");
        */
        String xmlString = null;
        if (this.xslDiff != null) {
            try {
                xmlString = XMLUtil.getXMLString(this.xslDiff);
                /*
                LOGGER.debug("\t\tlength of serialized XML document: " + xmlString.length()
                        + " characters");
                        */
            } catch (IOException e) {
                String msg = "Fatal: An exception was thrown when serializing an XML document.\n"
                             + e.getMessage();
                throw new IOException(msg);
            }
        } else {
            /*
            LOGGER.debug("\t\t XML document is null");
            */
        }
        out.writeObject(xmlString);

        /*
        LOGGER.debug("Finished serializing " + this.toString() + "");
        */
    }

    /**
     * Needed for making instances of <code>XQAnalysis</code> serializable. All fields are
     * deserialized by default, except for XML document objects, which are reparsed from a String.
     *
     * @param in The input stream.
     * @throws IOException            if an IOException occured when deserializing this object.
     * @throws ClassNotFoundException if a ClassNotFoundException occured when deserializing this
     *                                object.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        LOGGER.debug("Start deserializing " + this.toString());
        /*
        LOGGER.debug("\t1. default deserialization");
        */
        in.defaultReadObject();

        /*
        LOGGER.debug("\t2. customized deserialization of transient fields");
        */
        String xmlString = (String) in.readObject();
        if (xmlString == null) {
            /*
            LOGGER.debug("\t\t XML document is null");
            */
        } else {
            /*
            LOGGER.debug("\t\tlength of serialized XML document: " + xmlString.length()
                    + " characters");
                    */
            try {
                this.xslDiff = XMLUtil.parse(xmlString, false);
                this.xslDiff.setEncoding("UTF-8");
                /*
                LOGGER.debug("Finished deserializing " + this.toString() + "");
                */
            } catch (SAXException e) {
                String msg = "Fatal: An exception was thrown when reparsing an XML document after serialization.\n"
                             + e.getMessage();
                throw new IOException(msg);
            } catch (IOException e) {
                String msg = "Fatal: An exception was thrown when reparsing an XML document after serialization.\n"
                             + e.getMessage();
                throw new IOException(msg);
            }
        }
    }

    /**
     * Returns the exercise id.
     *
     * @return The exercise id.
     */
    public int getExerciseID() {
        return this.exerciseID;
    }

    /**
     * Sets the exercise id.
     *
     * @param exerciseID The exercise id to set.
     */
    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    /**
     * Returns the user id.
     *
     * @return The user id.
     */
    public int getUserID() {
        return this.userID;
    }

    /**
     * Sets the user id.
     *
     * @param userID The user id to set.
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Checks if the analysis and subsequently grading and reporting is done in debug modus.
     *
     * @return true if the analysis is run in debug modus, implying that intermediate results of the
     * analysis, grading and reporting process are saved to files.
     */
    public boolean isDebugMode() {
        return this.debugMode;
    }

    /**
     * Sets the debug modus.
     *
     * @param debugMode The debugMode to set.
     */
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    /**
     * This returns an object which holds all necessary information for creating a file which is
     * included in the analysis, the grading or the reporting process. Some files are created only
     * in debug mode. By default, all necessary files are saved in a folder
     * named <code>temp</code> within a folder named <code>xquery</code>.
     *
     * @return An object holding information for the file to create.
     */
    private FileParameter createDefaultFileParameter(boolean debugMode) {
        boolean defaultOnError = !debugMode;
        boolean deleteOnExit = !debugMode;
        String folder = XQCoreManager.TEMP_FOLDER;
        String prefix = null;
        String suffix = null;

        return new FileParameter(folder, prefix, suffix, deleteOnExit, defaultOnError);
    }

    /**
     * Gets designated parameters for XML temp file of the correct query result
     *
     * @return An object holding information for the file to create.
     */
    public FileParameter getXML1FileParameter() {
        if (xml1FileParameter == null) {
            xml1FileParameter = createDefaultFileParameter(this.isDebugMode());
            xml1FileParameter.setTempPrefix("xq01a_uid" + this.getUserID() + "_");
            xml1FileParameter.setTempSuffix(".xml");
        }
        return xml1FileParameter;
    }

    /**
     * Gets designated parameter for XML temp file of the submitted query result
     *
     * @return An object holding information for the file to create.
     */
    public FileParameter getXML2FileParameter() {
        if (xml2FileParameter == null) {
            xml2FileParameter = createDefaultFileParameter(this.isDebugMode());
            xml2FileParameter.setTempPrefix("xq01b_uid" + this.getUserID() + "_");
            xml2FileParameter.setTempSuffix(".xml");
        }
        return xml2FileParameter;
    }

    /**
     * Gets designated parameters for DTD temp file of the correct query result
     *
     * @return An object holding information for the file to create.
     */
    public FileParameter getDTDFileParameter() {
        if (dtdFileParameter == null) {
            dtdFileParameter = createDefaultFileParameter(this.isDebugMode());
            dtdFileParameter.setTempPrefix("xq02_uid" + this.getUserID() + "_");
            dtdFileParameter.setTempSuffix(".dtd");
        }
        return dtdFileParameter;
    }

    /**
     * Gets designated parameters for XSL temp file expressing differences between correct and
     * submitted query
     *
     * @return An object holding information for the file to create.
     */
    public FileParameter getXSLFileParameter() {
        if (xslFileParameter == null) {
            xslFileParameter = createDefaultFileParameter(this.isDebugMode());
            xslFileParameter.setTempPrefix("xq03_uid" + this.getUserID() + "_");
            xslFileParameter.setTempSuffix(".xsl");
        }
        return xslFileParameter;
    }

    /**
     * Gets designated parameters for XSL temp file expressing differences between correct and
     * submitted query in more advanced way
     *
     * @return An object holding information for the file to create.
     */
    public FileParameter getModifiedXSLFileParameter() {
        if (xslModifiedFileParameter == null) {
            xslModifiedFileParameter = createDefaultFileParameter(this.isDebugMode());
            xslModifiedFileParameter.setTempPrefix("xq04_uid" + this.getUserID() + "_");
            xslModifiedFileParameter.setTempSuffix(".xsl");
        }
        return xslModifiedFileParameter;
    }

    /**
     * Gets designated parameters for XML temp file of the submitted query result, already
     * supplemented with error descriptions
     *
     * @return An object holding information for the file to create.
     */
    public FileParameter getReportFileParameter() {
        if (reportFileParameter == null) {
            reportFileParameter = createDefaultFileParameter(this.isDebugMode());
            reportFileParameter.setTempPrefix("xq05_uid" + this.getUserID() + "_");
            reportFileParameter.setTempSuffix(".xml");
        }
        return reportFileParameter;
    }

    /**
     * Gets designated parameters for HTML temp file of the rendered result
     *
     * @return An object holding information for the file to create.
     */
    public FileParameter getHTMLFileParameter() {
        if (htmlFileParameter == null) {
            htmlFileParameter = createDefaultFileParameter(this.isDebugMode());
            htmlFileParameter.setTempPrefix("xq06_uid" + this.getUserID() + "_");
            htmlFileParameter.setTempSuffix(".html");
        }
        return htmlFileParameter;
    }

    /**
     * Removes all files which were required to be generated when analyzing or reporting.
     * Files which were requested to be saved as debug files (debug mode) are removed as well.
     * Exceptions are not thrown but only logged.
     */
    public void deleteTempFiles() {
        this.delete(getXML1FileParameter().getFile());
        this.delete(getXML2FileParameter().getFile());
        this.delete(getXSLFileParameter().getFile());
        this.delete(getDTDFileParameter().getFile());

        this.delete(getModifiedXSLFileParameter().getFile());
        this.delete(getReportFileParameter().getFile());
        this.delete(getHTMLFileParameter().getFile());
    }

    /**
     * Attempts to delete the file which the passed object stands for.
     *
     * @param file the file to delete
     */
    private void delete(File file) {
        if (file != null) {
            String filename = file.getAbsolutePath();
            try {
                if (file.delete()) {
                    LOGGER.debug("Deleted file " + filename);
                    return;
                }
            } catch (Exception e) {
                LOGGER.warn("Exception when deleting " + filename + ".\n" + e.getMessage());
            }
            LOGGER.warn("File " + filename + " was not deleted successfully for some unknown reason.");
        }
    }

    /* (non-Javadoc)
     * @see etutor.core.evaluation.Analysis#setSubmissionSuitsSolution(boolean)
     */
    public void setSubmissionSuitsSolution(boolean b) {
    }

    /* (non-Javadoc)
     * @see etutor.core.evaluation.Analysis#submissionSuitsSolution()
     */
    public boolean submissionSuitsSolution() {
        return this.submissionSuitsSolution;
    }
}