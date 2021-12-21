package at.jku.dke.etutor.modules.dlg.report;

import at.jku.dke.etutor.modules.dlg.DatalogCoreManager;
import at.jku.dke.etutor.modules.dlg.InvalidResourceException;
import at.jku.dke.etutor.modules.dlg.ReportException;
import at.jku.dke.etutor.modules.dlg.analysis.DatalogAnalysis;
import at.jku.dke.etutor.modules.dlg.analysis.DatalogResult;
import at.jku.dke.etutor.modules.dlg.analysis.WrappedModel;
import at.jku.dke.etutor.modules.dlg.analysis.WrappedPredicate;
import at.jku.dke.etutor.modules.dlg.grading.DatalogScores;
import ch.qos.logback.classic.Logger;
import oracle.xml.parser.v2.*;
import org.slf4j.LoggerFactory;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Date;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * An object which is the XML representation of all details about a Datalog result. An instance can
 * be constructed using a Datalog result object directly, or an analysis object, which in turn
 * contains the Datalog result and all details about the analysis.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class XMLReport {

    /**
     * The logger used for logging.
     */
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(XMLReport.class);
    private XMLDocument doc;
    private Element root;
    private Element messageElement;
    private Element summaryElement;
    private Element gradingElement;
    private Element syntaxElement;
    private Element consistencyElement;
    private Map xmlNodes;

    /**
     * Constructs a new <code>XMLReport</code> using the results of an analysis object. This will
     * transform all important information of the analysis into the XML representation of this
     * <code>XMLReport</code>, like analyzed errors.
     * 
     * @param analysis The analysis object to transform.
     * @param filters Specifies which predicates, identified by name, are intended to be printed out
     *            in the rendered result. <br>
     *            It must be noted that all predicates are added to this XML representation as XML
     *            nodes. The difference is that these nodes are marked with an attribute, which is
     *            <br>
     *            <code>filter = "true"</code>,<br>
     *            if the predicate is chosen in this list, otherwise it is <br>
     *            <code>filter = "false"</code>.<br>
     *            The way it is presented depends on the XSL stylesheet. <br>
     *            Similarily, XML nodes representing the predicates are marked with an attribute,
     *            which is <br>
     *            <code>required = "true"</code>,<br>
     *            if the predicate has to be in the Datalog result in order to be correct, else <br>
     *            <code>required = "false"</code>.<br>
     *            These predicates are taken from the
     *            {@link DatalogAnalysis#getResult1() correct solution}of the analysis.
     * @see DatalogCoreManager#XSL_RENDER_DATALOG
     * @throws ReportException if any unexpected Exception occured when transforming contents of the
     *             analysis into an XML representation.
     */
    public XMLReport(
            DatalogAnalysis analysis, String[] filters) throws ReportException {
        init(analysis.getResult2(), filters);
        setErrors(analysis);
        setRequPredicates(analysis);
    }

    /**
     * Constructs a new <code>XMLReport</code> using the result of a Datalog query directly. This
     * will transform all important information of the result into the XML representation of this
     * <code>XMLReport</code>.
     * 
     * @param result The object which represents the result of a Datalog query.
     * @param filters Specifies which predicates, identified by name, are intended to be printed out
     *            in the rendered result.
     * @see #XMLReport(DatalogAnalysis, String[])
     */
    public XMLReport(
            DatalogResult result, String[] filters) {
        init(result, filters);
    }

    /**
     * Transforms all important information of the result into XML nodes.
     * 
     * @param result The object which represents the result of a Datalog query.
     * @param filters Specifies which XML nodes, representing predicates and identified by name, are
     *            to be marked with the attribute <code>filter = "true"</code>. All other nodes
     *            are marked with <code>filter = "false"</code>.
     * @see #XMLReport(DatalogAnalysis, String[])
     */
    private void init(DatalogResult result, String[] filters) {
        
    	String query = "";
        Date date = new Date(System.currentTimeMillis());
        int maxInt = 0;
        boolean syntaxError = false;
        boolean timeoutError = false;
        boolean hasModel = false;

        if (result != null) {
            query = result.getQuery();
            syntaxError = result.getSyntaxException() != null;
            hasModel = result.hasConsistentModel();
        }
        
    	NodeFactory factory = new NodeFactory();

        this.xmlNodes = new Hashtable();

        this.doc = factory.createDocument();

        this.root = factory.createElement("datalog-result");
        doc.appendChild(root);

        this.messageElement = factory.createElement("analysis");
        root.appendChild(messageElement);

        this.summaryElement = factory.createElement("summary");
        messageElement.appendChild(summaryElement);
        
        this.gradingElement = factory.createElement("grading");
        messageElement.appendChild(gradingElement);

        this.syntaxElement = factory.createElement("syntax");
        messageElement.appendChild(syntaxElement);
        
        this.consistencyElement = factory.createElement("consistency");
        messageElement.appendChild(consistencyElement);
        
        CDATASection queryCDATA = factory.createCDATASection(query);
        Element queryElement = factory.createElement("query");
        queryElement.appendChild(queryCDATA);
        root.appendChild(queryElement);

        setDate(date);
        setSyntaxError(syntaxError);
        setModelConsistency(hasModel);

        initModels(root, result, filters);

    }

    /**
     * Transforms all important information of the Datalog models of the result, which represent the 
     * result of a Datalog query, into XML nodes.
     * 
     * @param resultElement The parent XML node, which all created nodes are inserted into.
     * @param result the Datalog result to transform.
     * @param filters Specifies which XML nodes to be marked with the attribute
     *            <code>filter = "true"</code>. All other nodes are marked with
     *            <code>filter = "false"</code>.
     */
    private void initModels(Element resultElement, DatalogResult result, String[] filters) {

        if (result != null && result.getSyntaxException() == null) {
            WrappedModel[] models = result.getWrappedModels();
            for (int i = 0; i < models.length; i++) {
                Element modelElement = createModelNode(models[i], filters);
                root.appendChild(modelElement);
                xmlNodes.put(models[i], modelElement);
            }
        }
    }
    
    /**
     * Creates an XML element representing a model of a Datalog result.
     * 
     * @param model The model to transform.
     * @param filters Specifies which XML nodes to be marked with the attribute
     *            <code>filter = "true"</code>. All other nodes are marked with
     *            <code>filter = "false"</code>.
     * @return The created XML element.
     */
    private Element createModelNode(WrappedModel model, String[] filters) {
        Element modelElement = doc.createElement("model");
        modelElement.setAttribute("consistent", Boolean.toString(model.isConsistent()));

        if (filters == null) {
            filters = model.getPredicateNames();
        }
        List filterList = Arrays.asList(filters);

        WrappedPredicate[] predicates = model.getPredicates();
        for (int i = 0; i < predicates.length; i++) {
            WrappedPredicate predicate = predicates[i];
            boolean filtered = filterList.contains(predicate.getName());
            Element predicateElement = createPredicateNode(predicate, null, false, filtered);
            modelElement.appendChild(predicateElement);
            xmlNodes.put(predicate, predicateElement);
        }
        return modelElement;
	}

    /**
     * Creates an XML element representing a predicate of some model within a Datalog result.
     * 
     * @param predicate The predicate to transform.
     * @param error Sets the <code>error</code> attribute of the predicate node or removes the
     *            attribute if already existing and if the specified String is <code>null</code>.
     * @param required Sets the <code>required</code> attribute of the predicate node to the
     *            boolean value of the parameter.
     * @param filter Sets the <code>filter</code> attribute of the predicate node to the boolean
     *            value of the parameter.
     * @return The created XML element.
     */
    private Element createPredicateNode(WrappedPredicate predicate, String error, boolean required,
            boolean filter) {

        Element predicateElement = doc.createElement("predicate");
        setPredicateRequired(predicateElement, required);
        setPredicateFilter(predicateElement, filter);
        setPredicateError(predicateElement, error);
        predicateElement.setAttribute("name", predicate.getName());
        predicateElement.setAttribute("arity", Integer.toString(predicate.getArity()));

        WrappedPredicate.WrappedFact[] facts = predicate.getFacts();
        for (int i = 0; i < facts.length; i++) {
            WrappedPredicate.WrappedFact fact = facts[i];
            Element factElement = createFactNode(fact, null);
            predicateElement.appendChild(factElement);
            xmlNodes.put(fact, factElement);
        }
        return predicateElement;
    }

    /**
     * Creates an XML element representing a fact of a Datalog result.
     * 
     * @param fact The fact to transform.
     * @param error Sets the <code>error</code> attribute of a fact node or removes the attribute
     *            if already existing and if the specified String is <code>null</code>.
     * @return The created XML Element.
     */
    private Element createFactNode(WrappedPredicate.WrappedFact fact, String error) {
        Element factElement = doc.createElement("fact");
        factElement.setAttribute("negated", Boolean.toString(!fact.isPositive()));
        setFactError(factElement, error);
        for (int i = 0; i < fact.getArity(); i++) {
            Element termElement = doc.createElement("term");
            termElement.setAttribute("value", fact.getTerms()[i]);
            factElement.appendChild(termElement);
        }
        return factElement;
    }

    /**
     * Returns the XML element which is related to the specified model object and has already
     * been appended to the XML document representing the report of this <code>XMLReport</code>.
     * 
     * @param model The model object.
     * @return The XML element representing the specified model object, if already appended to
     *         the XML document, else <code>null</code>. {@link WrappedModel#equals(Object)}
     *         is used to test if it has been added already. 
     */
    private Element getElement(WrappedModel model) {
        return (Element)xmlNodes.get(model);
    }

    /**
     * Returns the XML element which is related to the specified predicate object and has already
     * been appended to the XML document representing the report of this <code>XMLReport</code>.
     * 
     * @param predicate The predicate object.
     * @return The XML element representing the specified predicate object, if already appended to
     *         the XML document, else <code>null</code>.{@link WrappedPredicate#equals(Object)}
     *         is used to test if it has been added already. 
     */
    private Element getElement(WrappedPredicate predicate) {
        return (Element)xmlNodes.get(predicate);
    }

    /**
     * Returns the XML element which is related to the specified fact object and has already been
     * appended to the XML document representing the report of this <code>XMLReport</code>.
     *
     * @param fact The fact object.
     * @return The XML element representing the specified fact object, if already appended to the
     *         XML document, else <code>null</code>.
     *         {@link at.jku.dke.etutor.modules.dlg.analysis.WrappedPredicate.WrappedFact#equals(Object)}
     *         is used to test if it has been added already.
     */
    private Element getElement(WrappedPredicate.WrappedFact fact) {
        return (Element)xmlNodes.get(fact);
    }

    /**
     * Appends an XML node to the predicate XML nodes of the XML document representing the report of
     * this <code>XMLReport</code>.
     * 
     * @param model the model, which must have already been transformed and added as XML node to this 
     * 			  <code>XMLReport</code>.
     * @param predicate The new predicate to append.
     * @param error Sets the <code>error</code> attribute of the predicate node or removes the
     *            attribute if already existing and if the specified String is <code>null</code>.
     * @param required Sets the <code>required</code> attribute of the predicate node to the
     *            boolean value of the parameter.
     * @param filter Sets the <code>filter</code> attribute of the predicate node to the boolean
     *            value of the parameter.
     * @return The appended XML element of the predicate.
     */
    private Element appendPredicate(WrappedModel model, WrappedPredicate predicate, boolean required, boolean filter,
            String error) {
        Element newPredicateElement = null;
        Element modelElement = getElement(model);
        if (modelElement != null) {
            newPredicateElement = createPredicateNode(predicate, error, required, filter);
            modelElement.appendChild(newPredicateElement);
            xmlNodes.put(predicate, newPredicateElement);
        }
        return newPredicateElement;
    }

    /**
     * Appends an XML node to the fact XML nodes of the XML document representing the report of this
     * <code>XMLReport</code>.
     * 
     * @param predicate The predicate which contains the fact and which must have been added to the
     *            XML document already.
     * @param fact The new fact to append.
     * @param error Sets the <code>error</code> attribute of the predicate node or removes the
     *            attribute if already existing and if the specified String is <code>null</code>.
     * @return The appended XML element of the fact, or <code>null</code> if the predicate has not
     *         been added to the XML document already. {@link WrappedPredicate#equals(Object)}is
     *         used to test if it has been added already.
     */
    private Element appendFact(WrappedPredicate predicate, WrappedPredicate.WrappedFact fact, String error) {
        Element newFactElement = null;
        Element predicateElement = getElement(predicate);
        if (predicateElement != null) {
            newFactElement = createFactNode(fact, error);
            xmlNodes.put(fact, newFactElement);
            predicateElement.appendChild(newFactElement);
        }
        return newFactElement;
    }

    /**
     * Sets the <code>required</code> attribute of all XML nodes, representing predicates, which
     * have been transformed already.
     * 
     * @param analysis The analysis object which contains information about the predicates in the
     *            <i>submitted </i> result and the required predicates, set with the <i>correct </i>
     *            result.
     * @see DatalogAnalysis#getResult1()
     * @see DatalogAnalysis#getResult2()
     */
    private void setRequPredicates(DatalogAnalysis analysis) {
        DatalogResult result = analysis.getResult2();
        if (result != null) {
            WrappedModel[] models = result.getWrappedModels();
            for (int i = 0; i < models.length; i++) {
		        WrappedPredicate[] predicates = models[i].getPredicates();
		        if (predicates != null) {
		            for (int j = 0; j < predicates.length; j++) {
		                WrappedPredicate predicate = predicates[j];
		                boolean required = true;
		                this.setPredicateRequired(predicate, required);
		                this.setPredicateFilter(predicate, true);
		            }
		        }
            }
        }
    }

    /**
     * Sets the <code>error</code> attribute of all XML nodes, representing predicates and facts,
     * which have been transformed already.
     * 
     * @param analysis The analysis object which contains information about the predicates and facts
     *            in the <i>submitted </i> result and the analyzed errors.
     */
    private void setErrors(DatalogAnalysis analysis) {

        for (int i = 0; i < analysis.getMissingPredicates().size(); i++) {
            WrappedModel model = analysis.getResult2().getConsistentModel();
            String error = DatalogScores.PREDICATES_MISSING;
            this.appendPredicate(model, (WrappedPredicate)analysis.getMissingPredicates().get(i), true,
                    true, error);
        }
        /*
        for (int i = 0; i < analysis.getRedundantPredicates().size(); i++) {
            String error = DatalogScores.PREDICATES_REDUNDANT;
            this.setPredicateAttributes((WrappedPredicate)analysis.getRedundantPredicates().get(i),
                    true, true, error);
        }

        for (int i = 0; i < analysis.getLowArityPredicates().size(); i++) {
            String error = DatalogScores.PREDICATES_ARITY_LOW;
            this.setPredicateAttributes((WrappedPredicate)analysis.getLowArityPredicates().get(i),
                    true, true, error);
        }

        for (int i = 0; i < analysis.getHighArityPredicates().size(); i++) {
            String error = DatalogScores.PREDICATES_ARITY_HIGH;
            this.setPredicateAttributes((WrappedPredicate)analysis.getHighArityPredicates().get(i),
                    true, true, error);
        }
        */
        for (int i = 0; i < analysis.getMissingFacts().size(); i++) {
            String error = DatalogScores.FACTS_MISSING;
            WrappedPredicate.WrappedFact missingFact = (WrappedPredicate.WrappedFact)analysis.getMissingFacts().get(i);
            WrappedModel incompleteModel = analysis.getResult2().getConsistentModel();
            WrappedPredicate incompletePredicate = incompleteModel.getPredicate(missingFact
                    .getPredicate().getName());
            this.appendFact(incompletePredicate, missingFact, error);
        }

        for (int i = 0; i < analysis.getRedundantFacts().size(); i++) {
            String error = DatalogScores.FACTS_REDUNDANT;
            this.setFactError((WrappedPredicate.WrappedFact)analysis.getRedundantFacts().get(i), error);
        }

        /*
        for (int i = 0; i < analysis.getPositiveFacts().size(); i++) {
            String error = DatalogScores.FACTS_POSITIVE;
            this.setFactError((WrappedPredicate.WrappedFact)analysis.getPositiveFacts().get(i), error);
        }

        for (int i = 0; i < analysis.getNegativeFacts().size(); i++) {
            String error = DatalogScores.FACTS_NEGATIVE;
            this.setFactError((WrappedPredicate.WrappedFact)analysis.getNegativeFacts().get(i), error);
        }
        */
    }

    /**
     * Sets all attributes of XML nodes representing a Datalog predicate.
     * 
     * @param predicate The predicate to set.
     * @param error Sets the <code>error</code> attribute of the predicate node or removes the
     *            attribute if already existing and if the specified String is <code>null</code>.
     * @param required Sets the <code>required</code> attribute of the predicate node to the
     *            boolean value of the parameter.
     * @param filter Sets the <code>filter</code> attribute of the predicate node to the boolean
     *            value of the parameter.
     */
    private void setPredicateAttributes(WrappedPredicate predicate, boolean required,
            boolean filter, String error) {
        setPredicateRequired(predicate, required);
        setPredicateFilter(predicate, filter);
        setPredicateError(predicate, error);
    }

    /**
     * Sets the <code>required</code> attribute of the node related to the predicate to the
     * boolean value of the parameter.
     * 
     * @param predicate The predicate to set.
     * @param required Sets the attribute to the boolean value of the parameter.
     * @return the XML element related to the predicate.
     */
    private Element setPredicateRequired(WrappedPredicate predicate, boolean required) {
        return setPredicateRequired(getElement(predicate), required);
    }

    /**
     * Sets the <code>required</code> attribute of the predicate node to the boolean value of the
     * parameter.
     * 
     * @param predicateElement The predicate element to set.
     * @param required Sets the attribute to the boolean value of the parameter.
     * @return the set XML element.
     */
    private Element setPredicateRequired(Element predicateElement, boolean required) {
        predicateElement.setAttribute("required", Boolean.toString(required));
        return predicateElement;
    }

    /**
     * Sets the <code>filter</code> attribute of the node related to the predicate to the boolean
     * value of the parameter.
     * 
     * @param predicate The predicate to set.
     * @param filter Sets the attribute to the boolean value of the parameter.
     * @return the XML element related to the predicate.
     */
    private Element setPredicateFilter(WrappedPredicate predicate, boolean filter) {
        return setPredicateFilter(getElement(predicate), filter);
    }

    /**
     * Sets the <code>filter</code> attribute of the predicate node to the boolean value of the
     * parameter.
     * 
     * @param predicateElement The predicate element to set.
     * @param filter Sets the attribute to the boolean value of the parameter.
     * @return the set XML element.
     */
    private Element setPredicateFilter(Element predicateElement, boolean filter) {
        predicateElement.setAttribute("filter", Boolean.toString(filter));
        return predicateElement;
    }

    /**
     * Sets the <code>error</code> attribute of the node related to the predicate. If the
     * specified String is <code>null</code> and the attribute already exists for the XML node, it
     * is removed.
     * 
     * @param predicate The predicate to set.
     * @param error Sets the error value of the attribute.
     * @return the set XML element.
     */
    private Element setPredicateError(WrappedPredicate predicate, String error) {
        return setPredicateError(getElement(predicate), error);
    }

    /**
     * Sets the <code>error</code> attribute of the predicate node. If the specified String is
     * <code>null</code> and the attribute already exists for the XML node, it is removed.
     * 
     * @param predicateElement The predicate element to set.
     * @param error Sets the error value of the attribute.
     * @return the set XML element.
     */
    private Element setPredicateError(Element predicateElement, String error) {
        if (error != null) {
            predicateElement.setAttribute("error", error);
        } else {
            predicateElement.removeAttribute("error");
        }
        return predicateElement;
    }


    /**
     * Sets the <code>error</code> attribute of the node related to the fact. If the specified
     * String is <code>null</code> and the attribute already exists for the XML node, it is
     * removed.
     *
     * @param fact The fact to set.
     * @param error Sets the error value of the attribute.
     * @return the set XML element.
     */
    private Element setFactError(WrappedPredicate.WrappedFact fact, String error) {
        return setFactError(getElement(fact), error);
    }


    /**
     * Sets the <code>error</code> attribute of the fact node. If the specified String is
     * <code>null</code> and the attribute already exists for the XML node, it is removed.
     * 
     * @param factElement The predicate element to set.
     * @param error Sets the error value of the attribute.
     * @return the set XML element.
     */
    private Element setFactError(Element factElement, String error) {
        if (error != null) {
            factElement.setAttribute("error", error);
        } else {
            factElement.removeAttribute("error");
        }
        return factElement;
    }

    /**
     * Sets the <code>date</code> attribute for the XML document of this <code>XMLReport</code>.
     * 
     * @param date The date to set.
     */
    private void setDate(Date date) {
        root.setAttribute("date", date.toString());
    }

    /**
     * Sets the <code>syntax-error</code> attribute to the specified boolean value, telling
     * whether the analyzed Datalog result has syntax errors or not.
     * 
     * @param hasSyntaxErrors Indicating whether the analyzed Datalog result has syntax errors or
     *            not.
     */
    private void setSyntaxError(boolean hasSyntaxErrors) {
        root.setAttribute("syntax-error", Boolean.toString(hasSyntaxErrors));
    }

    /**
     * Sets the <code>timeout-error</code> attribute to the specified boolean value, telling
     * whether the analyzed Datalog result was built from a query, which could not be evaluated in
     * time.
     * 
     * @param hasTimeoutErrors Indicating whether the evaluated query could not be evaluated before
     *            a time limit was reached.
     */
    private void setTimeoutError(boolean hasTimeoutErrors) {
        root.setAttribute("timeout-error", Boolean.toString(hasTimeoutErrors));
    }

    /**
     * Sets the <code>analyzable</code> attribute for the XML document of this
     * <code>XMLReport</code> to the specified boolean value.
     * 
     * @param isConsistent Indicates, if there is a single and consistent model in the Datalog result meaning that
     *            there are no contradictions in the query and the result is unique.
     */
    private void setModelConsistency(boolean isConsistent) {
        root.setAttribute("analyzable", Boolean.toString(isConsistent));
    }

    /**
     * Sets the <code>max-int</code> attribute for the XML document of this <code>XMLReport</code>
     * to the specified value.
     * 
     * @param maxInt Indicates the highest number which was set for the
     *            {@link DLVWrappedDatalogProcessor}which was used to
     *            evaluate the query.
     */
    public void setMaxInt(int maxInt) {
        root.setAttribute("max-int", Integer.toString(maxInt));
    }

    /**
     * Sets the <code>score</code> attribute for the XML document of this <code>XMLReport</code>
     * to the specified value.
     * 
     * @param score Information about the score that was reached for a Datalog result, which was
     *            analyzed and compared to some "correct" result.
     */
    public void setScore(double score) {
        root.setAttribute("score", Double.toString(score));
    }

    /**
     * Sets the <code>max-score</code> attribute for the XML document of this
     * <code>XMLReport</code> to the specified value.
     * 
     * @param score Information about the highest score which can be reached for a Datalog result,
     *            which is analyzed and compared to some "correct" result.
     */
    public void setMaxScore(double score) {
        root.setAttribute("max-score", Double.toString(score));
    }

    /**
     * Sets the <code>diagnose-level</code> attribute for the XML document of this
     * <code>XMLReport</code> to the specified value.
     * 
     * @param diagnoseLevel The level which was used for generating a report.
     * @see at.jku.dke.etutor.modules.dlg.report.DatalogFeedback
     */
    public void setDiagnoseLevel(int diagnoseLevel) {
        this.root.setAttribute("diagnose-level", Integer.toString(diagnoseLevel));
    }

    /**
     * Sets the <code>mode</code> attribute for the XML document of this
     * <code>XMLReport</code> to the specified value.
     * 
     * @param mode The submission mode (e.g. submission, check or run).
     */
    public void setMode(String mode) {
        this.root.setAttribute("mode", mode);
    }
    
    /**
     * Sets a text, which possibly is the summary of the results of analyzing a Datalog result.
     * 
     * @param analysis The text to set.
     */
    public void setSummary(String analysis) {
        // TODO: documentation: CDATA section, escaping not reliable
        //this.messageCDATA.setData(analysis);
    	this.summaryElement.appendChild(this.doc.createTextNode(analysis));
    }

    /**
     * Sets a text, which possibly is the summary of the results of grading a Datalog result.
     * 
     * @param grading The text to set.
     */
    public void setGrading(String grading) {
    	this.gradingElement.appendChild(this.doc.createTextNode(grading));
    }

    /**
     * Sets a text, which possibly is the syntax error report.
     * 
     * @param syntaxReport The text to set.
     */
    public void setSyntaxReport(String syntaxReport) {
    	this.syntaxElement.appendChild(this.doc.createTextNode(syntaxReport));
    }

    /**
     * Sets a text, which gives information whether the result of a Datalog result is a consistent model or not.
     * 
     * @param consistency The text to set.
     */
    public void setConsistency(String consistency) {
    	this.gradingElement.appendChild(this.doc.createTextNode(consistency));
    }

    /**
     * Sets an error message for the given error categories.
     * 
     * @param categories The text to set.
     */
    public void setErrorDescription(ErrorCategory[] categories) {
        for (int i = 0; i < categories.length; i++) {
            Element errorElement = doc.createElement("error");
            errorElement.setAttribute("code", categories[i].getCssTitle());
            errorElement.setAttribute("title", categories[i].getTitle());
            errorElement.appendChild(this.doc.createTextNode(categories[i].getDescription()));
            messageElement.appendChild(errorElement);        	
        }
    }

    /**
     * Sets the <code>exercise-id</code> attribute for the XML document of this
     * <code>XMLReport</code> to the specified value.
     * 
     * @param exerciseID The id to set.
     */
    public void setExerciseID(int exerciseID) {
        root.setAttribute("exercise-id", Integer.toString(exerciseID));
    }

    /**
     * Returns the whole XML document which represents this <code>XMLReport</code>, including all
     * transformed information from a Datalog result or the analysis of a Datalog result.
     * 
     * @return The String which represents the XML document.
     * @throws ReportException if an internal IOException was thrown when converting the XML
     *             document to a String.
     */
    public String getXMLReport() throws ReportException {
        try {
            return at.jku.dke.etutor.modules.dlg.util.XMLUtil.getXMLString(this.doc);
        } catch (IOException e) {
        	String message = "";
            message += "Fatal: An exception was thrown when converting an ";
            message += "XML Document to a String.\n" + e.getMessage();
            LOGGER.error(message);
            throw new ReportException(message, e);
        }
    }

    /**
     * Returns a String which contains the HTML compatible rendered result of the contents of this
     * <code>XMLReport</code>. The XML representation of a Datalog result or the analysis of this
     * Datalog result is transformed using an XSL stylesheet.
     * 
     * @return The rendered result of the Datalog result or of the analysis.
     * @throws ReportException if an internal Exception was thrown when applying the XSL stylesheet
     *             used for rendering the result.
     * @see DatalogCoreManager#XSL_RENDER_DATALOG
     */
    public String getRenderedResult() throws ReportException {
        URL xslURL = null;

    	if (doc == null) {
            return null;
        }

        try {
			xslURL = DatalogCoreManager.getResource(DatalogCoreManager.XSL_RENDER_DATALOG);
		} catch (InvalidResourceException e1) {
			//TODO: handle exception
			e1.printStackTrace();
		}

        try {
            XSLProcessor xslProc = new XSLProcessor();
            xslProc.setXSLTVersion(XSLProcessor.XSLT10);
            xslProc.showWarnings(true);
            xslProc.setErrorStream(System.out);
            StringWriter resultWriter;
            PrintWriter writer;
            XSLStylesheet stylesheet;

            // render the datalog result in HTML format
            //stylesheet = xslProc.newXSLStylesheet(this.getClass().getResource(DatalogCoreManager.XSL_RENDER_DATALOG));
            stylesheet = xslProc.newXSLStylesheet(xslURL);
            resultWriter = new StringWriter();
            writer = new PrintWriter(resultWriter);

            xslProc.processXSL(stylesheet, doc, writer);
            return resultWriter.toString();
        } catch (XSLException e) {
            String message = "An internal exception was thrown when processing XSL stylesheets.\n"
                    + e.getMessage();
            LOGGER.error(message);
            throw new ReportException(message, e);
        } catch (IOException e) {
        	String message = "An internal exception was thrown when processing XSL stylesheets.\n"
                    + e.getMessage();
            LOGGER.error(message);
            throw new ReportException(message, e);
        }
    }
}