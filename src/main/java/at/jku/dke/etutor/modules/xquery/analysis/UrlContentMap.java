package at.jku.dke.etutor.modules.xquery.analysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import at.jku.dke.etutor.modules.xquery.ParameterException;
import at.jku.dke.etutor.modules.xquery.UrlContentException;

/**
 * This class can be used for managing the mapping between some URLs and alias names for these URLs.
 * <p>
 * This might be required if an XQuery query must be transformed before execution in the following
 * way: <br>
 * All parameters of the <code>doc</code> and the <code>collection</code> function within the query 
 * are treated as alias names and not as the real urls. For example, in an exercise environment a set of
 * required documents might be predefined and the real location of the documents is hidden. The
 * alias names for these documents thus can be used when formulating a query. In order to be ready
 * for execution, all occurences of these alias names must be detected and replaced by the real
 * document path internally.
 * <p>
 * Basically, what is found within a <code>doc("some content")</code> statement, is referred to as
 * <i>content </i>. It depends on the context whether it is treated as URL or as the corresponding
 * alias name of some URL. This class can also be used if a mapping is not desired at all. URLs can 
 * also be checked to be permitted as they are detected in the query. This can be managed by 
 * specifying the same String both for the url and the alias. 
 * <p>
 * The <code>doc</code> function is detected in the following way: <br>
 * <ul>
 * <li>The query is processed line by line. A <code>doc</code> function statement which extends
 * over more than one line, is invalid. Such a statement will be ignored and left to the query
 * processor to detect as error.</li>
 * <li>Within a line every "<code>doc(</code>" character sequence is considered as signing the
 * start of a <code>doc</code> function statement.</li>
 * <li>For each found start fragment, it must be checked if it is valid and worth being analyzed
 * subsequently:</li>
 * <ul type="circle">
 * <li>If the found start fragment follows some character, this character <i>must not </i> be a
 * letter.</li>
 * <li>After the opening bracket there must be either an opening quotation mark (<code>"</code>)
 * or an apostrophe (<code>'</code>).</li>
 * <li>A sequence of white spaces between bracket and quotation mark, respectively apostrophe, is
 * ignored.</li>
 * <li>The sequence after the apostrophe or quotation mark is considered as the <i>content </i>.
 * The sequence must be finished by an apostrophe or quotation mark. If there is none, this
 * <code>doc</code> function statement is not analyzed further on. The statement will not be
 * analyzed also if the <i>content </i> is enclosed by an opening quotation mark and ending
 * apostrophe or vice versa.</li>
 * <li>After the ending apostrophe or quotation mark there must be a closing bracket.</li>
 * <li>Again, a sequence of white spaces between quotation mark, respectively apostrophe, and the
 * bracket is ignored.</li>
 * </ul>
 * </ul>.
 * <p>
 * Here are some examples, where invalid statements are ignored by the algorithm as described above,
 * and left to the query processor to detect as syntax errors: <table border='1' summary="Examples
 * for document statements which are detected">
 * <tr>
 * <th>Found character sequence</th>
 * <th>Detected as <code>doc</code> statement</th>
 * </tr>
 * <tr>
 * <td valign='top' nowrap='nowrap'><code>for $e in fn:doc('employees.xml')//employee</code>
 * </td>
 * <td valign='top'>Yes</td>
 * </tr>
 * <tr>
 * <td valign='top' nowrap='nowrap'><code>for $e in fn:doc('employees.xml<b>"</b>)//employee</code>
 * </td>
 * <td valign='top'>No, an apostrophe is expected</td>
 * </tr>
 * <tr>
 * <td valign='top' nowrap='nowrap'><code>for $e in <b>fndoc</b>("employees.xml")//employee</code>
 * </td>
 * <td valign='top'>No, some letter preceeds the <code>doc</code> statement</td>
 * </tr>
 * <tr>
 * <td valign='top' nowrap='nowrap'>
 * <code>for $e in fn:doc( &nbsp;&nbsp;&nbsp;"employees.xml" &nbsp;&nbsp;&nbsp;)//employee</code>
 * </td>
 * <td valign='top'>Yes, spaces between bracket and quotation marks, or apostrophes in case, are
 * ignored</td>
 * </tr>
 * <tr>
 * <td valign='top' nowrap='nowrap'><code>for $e in fn:doc("employees.<br>xml")//employee</code></td>
 * <td valign='top'>No, new lines before ending the <code>doc</code> function statement is
 * invalid</td>
 * </tr>
 * <tr>
 * <td valign='top' nowrap='nowrap'>
 * <code>for $e in fn:doc &nbsp;&nbsp;&nbsp;("employees.xml")//employee</code></td>
 * <td valign='top'>No, spaces before the opening bracket not allowed</td>
 * </tr>
 * </table>
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class UrlContentMap implements Serializable {

    private Map aliasMap;
    
    /**
     * Constructs a new map object, with an empty map of alias/URL pairs.
     */
    public UrlContentMap() {
        this.aliasMap = new Hashtable();
    }

    /**
     * Adds a new URL to the map of this object, together with its alias name. It is useful to pass the
     * same String as alias and as URL if queries are checked to access only the specified URL.
     * 
     * @param alias The alias name for the specified URL.
     * @param url The URL.
     * @throws ParameterException Both, alias names and URLs must be unique in the map of this
     *             <code>UrlContentMap</code>. An exception is thrown, if an entry with the
     *             specified alias as <i>key </i> already exists, or if an entry with the specified
     *             URL as <i>value </i> already exists. Equality is detected for both, URLs and
     *             alias names, ignoring cases. Additionally, white spaces are trimmed from the
     *             alias names to be compared. It is no error, if with regard to this equality check
     *             there is an entry with exactly the same pair of URL and alias name. This entry
     *             will be replaced by the new one.
     */
    public void addUrlAlias(String alias, String url) throws ParameterException {
    	String msg;
        Map.Entry duplicateEntry = getCaseIgnoredKeyEntry(alias);
        if (duplicateEntry != null) {
            String duplicateUrl = (String)duplicateEntry.getValue();
            if (duplicateUrl.equalsIgnoreCase(url)) {
                // Removing the old case ignored alias entry, new alias/url pair will be inserted
                aliasMap.remove(duplicateEntry.getKey());
            } else {
            	msg = new String();
                msg += alias + " can not be used as alias for URL " + url;
                msg += ", it is already used for URL " + duplicateUrl + ".";
                throw new ParameterException(msg);
            }
        }
        duplicateEntry = getCaseIgnoredValueEntry(url);
        if (duplicateEntry != null) {
            String duplicateAlias = (String)duplicateEntry.getKey();
            if (duplicateAlias.equalsIgnoreCase(alias)) {
                // Removing the old case-ignored alias entry, new alias/url pair will be inserted
                aliasMap.remove(duplicateAlias);
            } else {
            	msg = new String();
                msg += "URL "+ url + " can not be assigned to alias " + alias;
                msg += ", it is already assigned to alias " + duplicateAlias + ".";
                throw new ParameterException(msg);
            }
        }
        aliasMap.put(alias, url);
    }

    public boolean removeAlias(String alias) {
    	return aliasMap.remove(alias) != null;
    }

    public boolean removeUrl(String url) {
    	String alias;
    	alias = getAlias(url);
    	return removeAlias(alias);
    }
    
    /**
     * Returns the entry with the specified key in the managed map. White spaces are trimmed from
     * both, the specified key and the key of each entry before comparing. Cases are ignored.
     * <p>
     * In the context of this class, <i>key </i> stands for an <i>alias name </i>.
     * 
     * @param key The key of the entry to search for.
     * @return The found entry of the map or <code>null</code>, if not found.
     */
    private Map.Entry getCaseIgnoredKeyEntry(String key) {
        Iterator it = aliasMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            Object keyObject = entry.getKey();
            if (keyObject instanceof String
                    && ((String)keyObject).trim().equalsIgnoreCase(key.trim())) {
                return entry;
            }
        }
        return null;
    }

    /**
     * Returns the entry with the specified value in the managed map. In contrast to
     * {@link #getCaseIgnoredKeyEntry(String)}, white spaces are <i>not </i> trimmed from the
     * values before comparing. Nevertheless, cases are ignored.
     * <p>
     * In the context of this class, <i>value </i> stands for an <i>URL </i>.
     * 
     * @param value The value of the entry to search for.
     * @return The found entry of the map or <code>null</code>, if not found.
     */
    private Map.Entry getCaseIgnoredValueEntry(String value) {
        Iterator it = aliasMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            Object valueObject = entry.getValue();
            if (valueObject instanceof String && ((String)valueObject).equalsIgnoreCase(value)) {
                return entry;
            }
        }
        return null;
    }

    /**
     * Returns the alias name, which is mapped to the specified URL. Character cases are ignored.
     * 
     * @param url The URL.
     * @return The alias name of the specified URL, if found, otherwise <code>null</code>.
     */
    public String getAlias(String url) {
        if (url != null) {
            Map.Entry entry = getCaseIgnoredValueEntry(url);
            if (entry != null) {
                return (String)entry.getKey();
            }
        }
        return null;
    }

    /**
     * Returns the URL, which is mapped to the specified alias name. Leading and trailing spaces of
     * the alias name are ignored when searching, as well as character cases.
     * 
     * @param alias The alias name of the requested URL.
     * @return The URL which is mapped to the specified alias name. If not found, this returns
     *         <code>null</code>.
     */
    public String getUrl(String alias) {
        if (alias != null) {
            Map.Entry entry = getCaseIgnoredKeyEntry(alias);
            if (entry != null) {
                return (String)entry.getValue();
            }
        }
        return null;
    }

    /**
     * Returns a <code>Set</code> view of the alias names, stored in this
     * <code>UrlContentMap</code>, which are considered as <i>key </i> obects for the
     * corresponding URL they stand for.
     * 
     * @return A set of String values which represent all alias names stored in this
     *         <code>UrlContentMap</code>.
     */
    public Set aliasSet() {
        return this.aliasMap.keySet();
    }

    /**
     * Returns a <code>Set</code> view of the URLs, stored in this <code>UrlContentMap</code>,
     * which are considered as <i>value </i> obects of the alias names they are mapped to.
     * 
     * @return A set of String values which represent all URLs stored in this
     *         <code>UrlContentMap</code>.
     */
    public Set urlSet() {
        Set set = new TreeSet();
        Iterator it = this.aliasMap.entrySet().iterator();
        while (it.hasNext()) {
            set.add(((Map.Entry)it.next()).getValue());
        }
        return set;
    }

    /**
     * Detects all valid occurences of <code>doc</code> and 
     * <code>collection</code> function statements of the specified query
     * and treats their contents as <i>alias names </i>. All found alias names are replaced with
     * URLs which are mapped to these alias names. The URLs, identified by the found alias names,
     * must be defined for this <code>UrlContentMap</code>.
     * 
     * @param query The query to decode.
     * @return The decoded query, where all alias names are replaced by their corresponding URL.
     * @throws UrlContentException if any alias name was found, which has not been mapped to an URL
     *             for this <code>UrlContentMap</code>.
     */
    public String getDecodedQuery(String query) throws UrlContentException {
    	String decodedQuery = query;
    	
    	decodedQuery = getDecodedQuery(decodedQuery, "doc");
    	decodedQuery = getDecodedQuery(decodedQuery, "collection");
    	
    	return decodedQuery;
    }
    
    /**
     * Detects all valid occurences of <code>doc</code> and 
     * <code>collection</code> function statements of the specified query
     * and treats their contents as <i>alias names </i>. All found alias names are replaced with
     * URLs which are mapped to these alias names. The URLs, identified by the found alias names,
     * must be defined for this <code>UrlContentMap</code>.
     * 
     * @param function name of the function (<code>doc</code> or <code>collection</code>)
     * @param query The query to decode.
     * @return The decoded query, where all alias names are replaced by their corresponding URL.
     * @throws UrlContentException if any alias name was found, which has not been mapped to an URL
     *             for this <code>UrlContentMap</code>.
     */
    public String getDecodedQuery(String query, String function) throws UrlContentException {
        String decodedQuery = "";
        ArrayList undeclaredAliases = new ArrayList();
        int indexStart = -1;
        int indexEnd = -1;
        int indexLine = 1;
        do {
            indexStart = indexEnd + 1;
            indexEnd = query.indexOf("\n", indexStart);
            String line = "";
            if (indexEnd == -1) {
                line = query.substring(indexStart, query.length());
            } else {
                line = query.substring(indexStart, indexEnd);
            }

            ArrayList foundAliases = getDocContents(line, indexLine, function);
            StringBuffer decodedLine = new StringBuffer(line);
            for (int i = foundAliases.size() - 1; i >= 0; i--) {
                UrlContent content = (UrlContent)foundAliases.get(i);
                String alias = content.getContent();
                String url = getUrl(alias);
                if (url == null) {
                    undeclaredAliases.add(content);
                }
                // Replace only if it still makes sense.
                if (undeclaredAliases.size() == 0) {
                    decodedLine = decodedLine.replace(content.startIndex, content.endIndex, url);
                }
            }
            decodedQuery += decodedLine.toString() + "\n";
            indexLine++;
        } while (indexEnd != -1);
        if (undeclaredAliases.size() > 0) {
            throw createAliasException(undeclaredAliases);
        }
        return decodedQuery;
    }

    /**
     * Detects all valid occurences of <code>doc</code> and 
     * <code>collection</code> function statements of the specified query
     * and treats their contents as <i>URLs </i>. All found URLs are replaced with alias names which
     * are mapped to these URLs. The alias names, identified by the corresponding URLs, must be
     * defined for this <code>UrlContentMap</code>.
     * 
     * @param query The query to encode.
     * @return The encoded query, where all URLs are replaced by their corresponding alias name.
     * @throws UrlContentException if any URL was found, which has not been mapped to an alias name
     *             for this <code>UrlContentMap</code>.
     */
    public String getEncodedQuery(String query) throws UrlContentException {
    	String encodedQuery = query;
    	
    	encodedQuery = getEncodedQuery(encodedQuery, "doc");
    	encodedQuery = getEncodedQuery(encodedQuery, "collection");
    	
    	return encodedQuery;
    }
    
    /**
     * Detects all valid occurences of <code>doc</code> and 
     * <code>collection</code> function statements of the specified query
     * and treats their contents as <i>URLs </i>. All found URLs are replaced with alias names which
     * are mapped to these URLs. The alias names, identified by the corresponding URLs, must be
     * defined for this <code>UrlContentMap</code>.
     * 
     * @param function name of the function (<code>doc</code> or <code>collection</code>)
     * @param query The query to encode.
     * @return The encoded query, where all URLs are replaced by their corresponding alias name.
     * @throws UrlContentException if any URL was found, which has not been mapped to an alias name
     *             for this <code>UrlContentMap</code>.
     */
    private String getEncodedQuery(String query, String function) throws UrlContentException {
        String encodedQuery = "";
        ArrayList undeclaredUrls = new ArrayList();
        int indexStart = -1;
        int indexEnd = -1;
        int indexLine = 1;
        do {
            indexStart = indexEnd + 1;
            indexEnd = query.indexOf("\n", indexStart);
            String line = "";
            if (indexEnd == -1) {
                line = query.substring(indexStart);
            } else {
                line = query.substring(indexStart, indexEnd);
            }

            ArrayList foundUrls = getDocContents(line, indexLine, function);
            StringBuffer encodedLine = new StringBuffer(line);
            for (int i = foundUrls.size() - 1; i >= 0; i--) {
                UrlContent content = (UrlContent)foundUrls.get(i);
                String url = content.getContent();
                String alias = getAlias(url);
                if (alias == null) {
                    undeclaredUrls.add(content);
                }
                // Replace only if it still makes sense.
                if (undeclaredUrls.size() == 0) {
                    encodedLine = encodedLine.replace(content.startIndex, content.endIndex, alias);
                }
            }
            
            encodedQuery += encodedLine.toString() + "\n";
            indexLine++;
        } while (indexEnd != -1);
        if (undeclaredUrls.size() > 0) {
            throw createUrlException(undeclaredUrls);
        }
        return encodedQuery;
    }

    /**
     * Detects and returns the content of all valid occurences of the specified function.
     * statements of the specified query fragment. This fragment is expected to be a single line.
     * 
     * @param function name of the function (<code>doc</code> or <code>collection</code>)
     * @param fragment A fragment of a query which is analyzed. The fragment is expected to be a
     *            single line of the analyzed query.
     * @param line The line number of the specified fragment. This information is passed to the
     *            returned objects.
     * @return A list of <code>UrlContent</code> objects, which represent all found contents of
     *         valid <code>doc</code> function statements.
     */
    private ArrayList getDocContents(String fragment, int line, String function) {
        ArrayList contentList = new ArrayList();
        String docStart = function + "(";
        int indexStart = -1;
        int indexEnd = -1;
        do {
            indexStart = indexEnd + 1;
            indexEnd = fragment.indexOf(docStart, indexStart);
            boolean followsLetter = followsLetter(fragment, indexEnd);
            boolean found = indexEnd != -1 && !followsLetter;
            if (found) {
                // Next, check if the String "doc(" is preceeding no or more space characters,
                // followed by "\"" or "'"
                int indexQuot = getSpaceIgnoredIndex(fragment, "'", indexEnd + docStart.length());
                if (indexQuot == -1) {
                    indexQuot = getSpaceIgnoredIndex(fragment, "\"", indexEnd + docStart.length());
                }
                if (indexQuot > -1) {
                    char quot = fragment.charAt(indexQuot);
                    UrlContent content = getDocContent(fragment, quot, ")", indexQuot + 1, line);
                    if (content != null) {
                        contentList.add(content);
                    }
                }
            }
        } while (indexEnd != -1);
        return contentList;

    }

    /**
     * Detects and returns the content of a single <code>doc</code> function statement, if it is
     * valid.
     * <p>
     * The start of this function is expected to have been detected as a sequence of characters
     * which starts with "<code>doc(</code>", maybe followed by some white spaces, eventually
     * either followed by an apostrophe or a quotation mark. The first index after this apostrophe
     * or quotation mark is considered as the start of the <i>content </i>.
     * 
     * @param fragment A fragment of a query which is analyzed. The fragment is expected to be a
     *            single line of the analyzed query.
     * @param quot Expected to be the apostrophe or a quotation mark which marks the beginning of
     *            the <i>content </i>. The found content will only be valid if it is ended by the
     *            very same character.
     * @param docEnd The end of the <code>doc</code> function statement, which is expected to be a
     *            closing bracket. The function statement is only considered as valid, if this is
     *            found after the ending apostrophe, respectively the quotation mark. A sequence of
     *            white spaces between this apostrophe or quotation mark and the <code>docEnd</code>
     *            is ignored.
     * @param index Marks the beginning of the <i>content </i>.
     * @param line The line number of the specified fragment. This information is passed to the
     *            returned object.
     * @return An object, which represents the content of the <code>doc</code> function statement,
     *         if valid, otherwise <code>null</code>.
     */
    private UrlContent getDocContent(String fragment, char quot, String docEnd, int index, int line) {
        if (fragment != null && index > -1 && index < fragment.length()) {
            int indexApos = fragment.indexOf("'", index);
            int indexQuot = fragment.indexOf("\"", index);
            int indexEnd = -1;
            if (indexApos == -1) {
                indexEnd = indexQuot;
            } else if (indexQuot == -1) {
                indexEnd = indexApos;
            } else {
                indexEnd = Math.min(indexQuot, indexApos);
            }
            if (indexEnd > -1 && fragment.charAt(indexEnd) == quot) {
                int indexBracket = getSpaceIgnoredIndex(fragment, docEnd, indexEnd + 1);
                if (indexBracket > -1) {
                    UrlContent content = new UrlContent(fragment.substring(index, indexEnd), index,
                            indexEnd, line);
                    return content;
                }
            }
        }
        return null;

    }

    /**
     * Checks if the specified text follows some letter character.
     * 
     * @param text The text to analyze.
     * @param index Marks the character index, which is verified to follow some letter.
     * @return true if the specified index follows any character and this character is a letter,
     *         otherwise false.
     */
    private boolean followsLetter(String text, int index) {
        if (text != null && index > 0 && index < text.length()) {
            char preceedingChar = text.charAt(index - 1);
            return Character.isLetter(preceedingChar);
        }
        return false;
    }

    /**
     * Returns the index of a String within a text, where only white spaces are allow in between.
     * 
     * @param text The text to search for the specified target.
     * @param target The target to search for within the specified text.
     * @param startIndex The index which marks the first point where only whitespaces or the
     *            specifed target are allowed subsequently.
     * @return The index of the found target, or -1 if not found.
     */
    private int getSpaceIgnoredIndex(String text, String target, int startIndex) {
        if (text != null && startIndex > -1 && startIndex < text.length()) {
            for (int i = startIndex; i < text.length(); i++) {
                String substring = text.substring(i);
                if (substring.startsWith(target)) {
                    return i;
                }
                char ch = text.charAt(i);
                if (!Character.isWhitespace(text.charAt(i))) {
                    return -1;
                }
            }
        }
        return -1;
    }

    /**
     * Creates an exception which can be used for indicating an error, when the content of some
     * <code>doc</code> function statement is interpreted as alias name, which has to be replaced
     * by an URL, but the mapping has not been defined for this <code>UrlContentMap</code>.
     * 
     * @param undeclaredAliases A list of <code>UrlContent</code> objects, which represent the
     *            alias names within a query which can not be replaced. These objects are added to
     *            the created exception.
     * @return The created exception object.
     */
    private UrlContentException createAliasException(ArrayList undeclaredAliases) {
        String msg = "";
        for (int i = 0; i < undeclaredAliases.size(); i++) {
            Object obj = undeclaredAliases.get(i);
            if (obj instanceof UrlContent) {
                UrlContent content = (UrlContent)obj;
                String alias = content.getContent();
                int line = content.getLine();
                int index = content.getStartIndex();
                msg += msg.length() > 0 ? "\n" : "";
                msg += "No URL declared for alias name " + alias;
                msg += ", found at line " + line + ", index " + index + ".";
            }
        }
        return new UrlContentException(msg, undeclaredAliases);
    }

    /**
     * Creates an exception which can be used for indicating an error, when the content of some
     * <code>doc</code> function statement is interpreted as an URL, which has to be replaced by
     * an alias name, but the mapping has not been defined for this <code>UrlContentMap</code>.
     * 
     * @param undeclaredUrls A list of <code>UrlContent</code> objects, which represent the URLs
     *            within a query which can not be replaced. These objects are added to the created
     *            exception.
     * @return The created exception object.
     */
    private UrlContentException createUrlException(ArrayList undeclaredUrls) {
        String msg = "";
        for (int i = 0; i < undeclaredUrls.size(); i++) {
            Object obj = undeclaredUrls.get(i);
            if (obj instanceof UrlContent) {
                UrlContent content = (UrlContent)obj;
                String url = content.getContent();
                int line = content.getLine();
                int index = content.getStartIndex();
                msg += msg.length() > 0 ? "\n" : "";
                msg += "No alias name declared for URL " + url;
                msg += ", found at line " + line + ", index " + index + ".";
            }
        }
        return new UrlContentException(msg, undeclaredUrls);
    }
}