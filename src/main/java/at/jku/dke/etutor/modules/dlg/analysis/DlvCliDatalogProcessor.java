package at.jku.dke.etutor.modules.dlg.analysis;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.dlg.AnalysisException;
import at.jku.dke.etutor.modules.dlg.InternalException;
import at.jku.dke.etutor.modules.dlg.QuerySyntaxException;
import edu.harvard.seas.pl.abcdatalog.ast.Clause;
import edu.harvard.seas.pl.abcdatalog.parser.DatalogParseException;
import edu.harvard.seas.pl.abcdatalog.parser.DatalogParser;
import edu.harvard.seas.pl.abcdatalog.parser.DatalogTokenizer;
import org.apache.logging.log4j.util.Strings;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DlvCliDatalogProcessor implements DatalogProcessor{
    private final String FACTS;
    private final String[] CMD_ARRAY;
    private final File WORK_DIR_FILE;
    private final String LINE_BREAK = "\n";
    private final String TEMP_FOLDER;
    private Process p;

    public DlvCliDatalogProcessor(String facts, ApplicationProperties properties){
        this.FACTS = facts;
        this.CMD_ARRAY = new String[3];
        this.CMD_ARRAY[0] = properties.getDatalog().getDLVPathAsCommand();
        this.CMD_ARRAY[2] = properties.getDatalog().getCautiousOptionForDlv();
        this.WORK_DIR_FILE = new File(properties.getDatalog().getWorkDirForDlv());
        this.TEMP_FOLDER = properties.getDatalog().getTempFolder();
    }

    @Override
    public WrappedModel[] executeQuery(String submission, String[] queries, boolean notAllowFacts) throws QuerySyntaxException, InternalException {
        String query;
        String predicate;
        File tempDlv;
        Map<String, List<String>> model = new HashMap<>();
        if(notAllowFacts) {
            try {
                checkSubmissionForFacts(submission);
            } catch (DatalogParseException | AnalysisException e) {
                throw new QuerySyntaxException(e);
            }
        }

        for(int i = 0; i<queries.length; i++){
            query = queries[i];
            predicate = getPredicateFromQuery(query);
            try {
                List<String> facts = new ArrayList<>();

                // Create Temp dlv file and write facts + submission + query
                tempDlv = File.createTempFile(UUID.randomUUID().toString(), ".dlv", new File(TEMP_FOLDER));
                String tempFilePath = tempDlv.getAbsolutePath();

                StringBuilder program = new StringBuilder();
                program.append(FACTS);
                program.append(LINE_BREAK);
                program.append(submission);
                program.append(LINE_BREAK);
                program.append(query);
                Files.writeString(Path.of(tempFilePath), program.toString());

                // Set temp file in command array
                CMD_ARRAY[1] = tempFilePath;

                // Initialize process
                ProcessBuilder pb = new ProcessBuilder(CMD_ARRAY);
                pb.directory(WORK_DIR_FILE);
                pb.redirectErrorStream(false);

                // Start process
                p = pb.start();
                //Handle error
                var errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                if((errorReader.readLine()) != null) handleSyntaxError(errorReader, p, tempDlv);

                // Handle InputStream from process
                var resultReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String s;
                resultReader.readLine();
                while ((s = resultReader.readLine()) != null) {
                    if(Strings.isNotBlank(s)) facts.add(s);
                }
                resultReader.close();
                // Add entry to map
                if(p.waitFor()==0) model.put(predicate, facts);
                tempDlv.delete();
            } catch (IOException | InterruptedException e) {
                throw new InternalException(e);
            } finally {
                p.destroy();
            }
        }

        // Wrap model
        var wrappedModel = new WrappedModel[1];
        wrappedModel[0] = new WrappedModel(model);
        return wrappedModel;
    }

    private void checkSubmissionForFacts(String submission) throws DatalogParseException, AnalysisException {
        var r = new StringReader(submission);
        DatalogTokenizer t = new DatalogTokenizer(r);
        Set<Clause> prog = DatalogParser.parseProgram(t);
        for(Clause c : prog){
            if (c.getBody().isEmpty()) throw new AnalysisException("Analysis stopped, as the submission contains fact declarations: \n "+ c);
        }
    }

    private void handleSyntaxError(BufferedReader br, Process p, File file) throws QuerySyntaxException {
        StringBuilder errorMessage = new StringBuilder();
        String s;
        while (true) {
            try {
                if ((s = br.readLine()) == null) {
                    br.close();
                    break;
                }
            } catch (IOException e) {
                p.destroy();
                throw new QuerySyntaxException();
            }
            errorMessage.append(s);
        }
        p.destroy();
        file.delete();
        throw new QuerySyntaxException(errorMessage.toString());
    }

    private String getPredicateFromQuery(String query){
        if (query.contains("(")) return query.substring(0, query.indexOf("("));
        else return query;
    }

}
