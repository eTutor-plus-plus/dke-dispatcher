package at.jku.dke.etutor.modules.nf.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NFMessageSource implements MessageSource {

    private static final Map<String, String> ENGLISH_MESSAGES;

    private static final Map<String, String> GERMAN_MESSAGES;

    static {
        ENGLISH_MESSAGES = new HashMap<>();

        ENGLISH_MESSAGES.put("keysreporter.correctsolution", "Congratulations! You have determined the correct keys.");

        GERMAN_MESSAGES = new HashMap<>();
    }

    @Override
    public String getMessage(String s, Object[] objects, String s1, Locale locale) {
        return s;
    }

    @Override
    public String getMessage(String s, Object[] objects, Locale locale) throws NoSuchMessageException {
        return s;
    }

    @Override
    public String getMessage(MessageSourceResolvable messageSourceResolvable, Locale locale) throws NoSuchMessageException {
        return messageSourceResolvable.getDefaultMessage();
    }
}
