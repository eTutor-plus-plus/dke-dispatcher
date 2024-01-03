package at.jku.dke.etutor.modules.nf.report;

import java.util.Collection;

public abstract class ErrorReporter {
    protected static final String HTML_HEADER = "<head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head>";

    protected static final String TABLE_HEADER = "<table border='2' rules='all'>";

    protected static String generateTable(Collection<?> collection) {
        StringBuilder ret = new StringBuilder(TABLE_HEADER);

        for (Object k : collection) {
            ret.append("<tr><td>").append(k.toString()).append("</td></tr>");
        }

        ret.append("</table>");

        return ret.toString();
    }
}
