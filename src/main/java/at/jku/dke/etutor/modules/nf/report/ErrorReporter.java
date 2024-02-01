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

    protected static String generateLevel3Div(Collection<?> collection, String singularNameKey, String pluralNameKey, String issueNameKey, Locale locale) {
        StringBuilder description = new StringBuilder();

        description.append("<div>").append(collection.size());

        description.append(" ");
        if (collection.size() == 1) {
            description.append(messageSource.getMessage(singularNameKey, null, locale));
        } else {
            description.append(messageSource.getMessage(pluralNameKey, null, locale));
        }
        description.append(" ");

        description.append(messageSource.getMessage(issueNameKey, null, locale)).append(".");
        description.append("<p/>");

        description.append(generateTable(collection));

        description.append("</div>");

        return description.toString();
    }

    private static String generateTable(Collection<?> collection) {
        StringBuilder ret = new StringBuilder(TABLE_HEADER);

        for (Object k : collection) {
            ret.append("<tr><td>").append(k.toString()).append("</td></tr>");
        }

        ret.append("</table>");

        return ret.toString();
    }
}
