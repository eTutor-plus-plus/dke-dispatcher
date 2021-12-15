package at.jku.dke.etutor.modules.dlg.analysis;

import at.jku.dke.etutor.modules.dlg.AnalysisException;
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
    private final String DLV_CMD = "C:\\Users\\Public\\dlvtest\\dlv.mingw";
    private final String CAUTIOUS_OPTION = "-cautious";
    private final String WORK_DIR = "C:\\Users\\Public\\dlvtest\\";
    private final File WORK_DIR_FILE;
    private final String LINE_BREAK = "\n";
    private final int SKIP_LINES = 1;
    private Process p;

    public DlvCliDatalogProcessor(String facts){
        this.FACTS = facts;
        this.CMD_ARRAY = new String[3];
        this.CMD_ARRAY[0] = DLV_CMD;
        this.CMD_ARRAY[2] = CAUTIOUS_OPTION;
        this.WORK_DIR_FILE = new File(WORK_DIR);
    }

    @Override
    public WrappedModel[] executeQuery(String submission, String[] queries, boolean notAllowFacts) throws QuerySyntaxException {
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
                tempDlv = File.createTempFile(UUID.randomUUID().toString(), ".dlv");
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
                pb.redirectErrorStream(true);

                // Start process
                p = pb.start();

                // Handle InputStream
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                int j = 0;
                String s;
                while ((s = br.readLine()) != null) {
                    if(j >= SKIP_LINES) {
                        if(s.contains("error")) handleSyntaxError(br, p, tempDlv);
                        if(Strings.isNotBlank(s))facts.add(s);
                    }
                    j++;
                }
                // Add to map
                model.put(predicate, facts);
                p.waitFor();
                tempDlv.delete();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                //TODO: Handle IO exceptions
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
                if ((s = br.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
                throw new QuerySyntaxException();
            }
            errorMessage.append(s);
        }
        p.destroy();
        file.delete();
        throw new QuerySyntaxException(errorMessage.toString());
    }

    private String getPredicateFromQuery(String query){
        return query.substring(0, query.indexOf("("));
    }

}
