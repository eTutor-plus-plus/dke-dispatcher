package at.jku.dke.etutor.modules.xquery.src.etutor.modules.xquery.analysis;

/**
 * Interface for description objects of XML nodes, which are transformed
 * for internal use.
 * 
 * @author  Georg Nitsche
 * @version 1.0
 * @since   1.0
 */

public interface XMLNodeDescription {

    /**
     * Provides all important information about a certain XML node, which is used for
     * analysis, grading or reporting of XQuery query results.
     * 
     * @return The description of the XML node.
     */
    public String getDescription();

}
