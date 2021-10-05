package at.jku.dke.etutor.modules.xquery;

import java.io.Closeable;
import java.io.IOException;

import org.basex.build.Parser;
import org.basex.build.xml.XMLParser;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.MainOptions;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.DropDB;
import org.basex.core.cmd.XQuery;
import org.basex.io.IO;
import org.basex.io.IOContent;

/**
 * Created by reisinger on 05.07.2016. Wrapper for BaseX API Core classes
 */
public class BaseXApi implements Closeable {

    public static final String REGEX_ID = "id=.*";

    private final Context context;

    public BaseXApi() {
        // Database context.
        context = new Context();
    }

    public String createDatabase(String name, String xml) throws IOException {
        CreateDB statement = new CreateDB(name);
        statement.setParser(getParser(xml));
        return statement.execute(context);
    }

    public String dropDatabase(String name) throws BaseXException {
        return new DropDB(name).execute(context);

    }

    public String executeQuery(String query) throws IOException {
        return new XQuery(query).execute(context);
    }

    @Override
    public void close() {
        context.close();
    }

    private Parser getParser(String xmlContent) throws IOException {
        IO io = new IOContent(xmlContent);
        MainOptions options = new MainOptions();
        options.set(MainOptions.INTPARSE, true);
        return new XMLParser(io, options);
    }
}

