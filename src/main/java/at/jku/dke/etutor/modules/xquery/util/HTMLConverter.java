package at.jku.dke.etutor.modules.xquery.src.etutor.modules.xquery.util;

/**
 * Utility class for making a String ready to be used in HTML context by escaping literals which
 * need special treatment.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class HTMLConverter {

    /**
     * Makes a String ready for use in HTML context. New lines <code>\n</code> are replaced with
     * the HTML element <code>&lt;br/&gt;</code>.
     * 
     * @param string The String to be converted.
     * @return The HTML compatible String.
     */
    public static String stringToHTMLString(String string) {
        if (string == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer(string.length());
        // true if last char was blank
        boolean lastWasBlankChar = false;
        int len = string.length();
        char c;

        for (int i = 0; i < len; i++) {
            c = string.charAt(i);
            if (c == ' ') {
                // blank gets extra work,
                // this solves the problem you get if you replace all
                // blanks with &nbsp;, if you do that you loss
                // word breaking
                if (lastWasBlankChar) {
                    lastWasBlankChar = false;
                    sb.append("&nbsp;");
                } else {
                    lastWasBlankChar = true;
                    sb.append(' ');
                }
            } else {
                lastWasBlankChar = false;
                //
                // HTML Special Chars
                if (c == '"') {
                    sb.append("&quot;");
                } else if (c == '&') {
                    sb.append("&amp;");
            	} else if (c == '<') {
                    sb.append("&lt;");
        		}else if (c == '>') {
                    sb.append("&gt;");
    			} else if (c == '\n') {
                    // Handle Newline
                    sb.append("<br/>");
				} else {
                    int ci = 0xffff & c;
                    if (ci < 160)
                        // nothing special only 7 Bit
                        sb.append(c);
                    else {
                        // Not 7 Bit use the unicode system
                        sb.append("&#");
                        sb.append(new Integer(ci).toString());
                        sb.append(';');
                    }
                }
            }
        }
        return sb.toString();
    }
}