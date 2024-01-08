package at.jku.dke.etutor.modules.nf.report;

import at.jku.dke.etutor.modules.nf.NFConstants;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Collection;
import java.util.Locale;

public abstract class ErrorReporter {

    protected static final MessageSource messageSource;

    static {
        ResourceBundleMessageSource msgSrc = new ResourceBundleMessageSource();
        msgSrc.setBasename(NFConstants.MESSAGE_SOURCE_PATH);
        messageSource = msgSrc;
    }

    protected static final String HTML_HEADER = "<head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head>";

    protected static final String TABLE_HEADER = "<table border='2' rules='all'>";

    protected static String generateLevel3Div(Collection<?> collection, int count, String singularNameKey, String pluralNameKey, String issueNameKey, MessageSource messageSource, Locale locale) {
        StringBuilder description = new StringBuilder();

        description.append("<div>").append(count);
        if (count == 1){
            description.append(" ").append(messageSource.getMessage(singularNameKey, null, locale)).append(" ");
        } else {
            description.append(" ").append(messageSource.getMessage(pluralNameKey, null, locale)).append(" ");
        }
        description.append(messageSource.getMessage(issueNameKey, null, locale)).append(".");
        description.append("<p/>");

        description.append(generateTable(collection));

        description.append("</div>");

        return description.toString();
    }

    protected static String generateTable(Collection<?> collection) {
        StringBuilder ret = new StringBuilder(TABLE_HEADER);

        for (Object k : collection) {
            ret.append("<tr><td>").append(k.toString()).append("</td></tr>");
        }

        ret.append("</table>");

        return ret.toString();
    }
}
