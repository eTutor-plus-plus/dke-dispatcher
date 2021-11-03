package at.jku.dke.etutor.modules.xquery.analysis;

import java.io.Serializable;

/**
 * Represents the content of some <code>doc</code> function statement which usually is used within
 * an XQuery query for accessing some XML documents.
 * <p>
 * In an exercise environment a set of required documents might be predefined where the real
 * location of the documents is hidden. In order to do so, an alias name must be defined for all
 * documents which appear somewhere in the query, thus <i>encoding </i> the query documents by
 * replacing all URLs with alias names. <br>
 * These URLs in turn can be <i>decoded </i> by replacing all alias names of a query with the
 * corresponding URLs, before executing.
 * <p>
 * In this sense, an <code>UrlContent</code> can be taken for both, representing the real query
 * document or the alias name for an URL.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class UrlContent implements Serializable, Comparable {

    String content;

    int startIndex;

    int endIndex;

    int line;

    /**
     * Creates a new <code>UrlContetn</code> object with the specified parameters.
     * 
     * @param content The content, which might be interpreted as URL or the alias name of an URL.
     * @param startIndex The index of the occurence of the content String within a query or the
     *            fragment of a query.
     * @param endIndex The first index after the content String, which was found within some query
     *            or some fragment of a query.
     * @param line The line number within some query, where the specified content was found.
     */
    public UrlContent(
            String content, int startIndex, int endIndex, int line) {
        this.content = content;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.line = line;
    }

    /**
     * Returns the first index after the content String of this <code>UrlContent</code>, which
     * was found within some query or some fragment of a query.
     * 
     * @return Returns the ending index of the content String.
     */
    public int getEndIndex() {
        return this.endIndex;
    }

    /**
     * Returns the starting index of the content String of this <code>UrlContent</code>, which
     * was found within some query or some fragment of a query.
     * 
     * @return Returns the starting index of the content String.
     */
    public int getStartIndex() {
        return this.startIndex;
    }

    /**
     * Returns the content String of this <code>UrlContent</code>, which was found within some
     * query or some fragment of a query.
     * 
     * @return Returns the content.
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Returns the line number within some query, where the specified content was found.
     * 
     * @return Returns the line number.
     */
    public int getLine() {
        return this.line;
    }

    /**
     * This is used in order to make this <code>UrlContent</code> comparable with other objects of
     * this class, which might be required if such objects are stored in ordered collections.
     * <p>
     * The ordering is done with regard to the occurence of a content String within some query. So
     * if one <code>UrlContent</code> is found in line number 1 and the other one is found in line
     * number 2, the first content is less than the second one. If they have the same line number,
     * the one with the smaller starting index is less than the other. <br>
     * This ordering is used when replacing all occurences of some contents within a query from
     * right to left and from the last line to the first line.
     * <p>
     * Note: this class has a natural ordering that is inconsistent with equals.
     * 
     * @param obj The object to be compared.
     * @return <li>1 if the specified object is an <code>UrlContent</code> object, which has a
     *         lower line number or starting index within the same line number as this
     *         <code>UrlContent</code>.</li>
     *         <li>0 if the specified object is an <code>UrlContent</code> object, which has the
     *         same line number and starting index as this <code>UrlContent</code>.</li>
     *         <li>-1 if the specified object is no <code>UrlContent</code> object or has a
     *         greater line number or starting index within the the same line number as this
     *         <code>UrlContent</code>.</li>
     */
    public int compareTo(Object obj) {
        if (obj instanceof UrlContent) {
            UrlContent alias = (UrlContent)obj;
            if (alias.getLine() < this.getLine()) {
                return 1;
            }
            if (alias.getLine() == this.getLine()) {
                if (alias.getStartIndex() < this.getStartIndex()) {
                    return 1;
                }
                if (alias.getStartIndex() == this.getStartIndex()) {
                    return 0;
                }
            }
        }
        return -1;
    }
}