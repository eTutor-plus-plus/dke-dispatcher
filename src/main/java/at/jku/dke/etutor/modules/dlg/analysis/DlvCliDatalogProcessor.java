package at.jku.dke.etutor.modules.dlg.analysis;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.dlg.InternalException;
import at.jku.dke.etutor.modules.dlg.QuerySyntaxException;
import at.jku.dke.etutor.modules.dlg.exercise.TermDescription;
import org.apache.logging.log4j.util.Strings;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Processor that executes datalog queries by wrapping an executable file
 * @author Kevin Schütz 2021
 */
public class DlvCliDatalogProcessor implements DatalogProcessor{
    /**
     * The facts of the program
     */
    private final String FACTS;
    /**
     * The list of unchecked terms that will not be manipulated
     */
    private final List<TermDescription> UNCHECKED_TERMS;
    /**
     * Array holding the commands to execute a query
     */
    private final String[] CMD_ARRAY;
    /**
     * The working directory for the process
     */
    private final File WORK_DIR_FILE;
    private final String LINE_BREAK = "\n";
    /**
     * Indicates where the temp files needed for execution will be stored
     */
    private final String TEMP_FOLDER;
    /**
     * The suffix used to manipulate the facts
     */
    private final String SUFFIX;
    /**
     * The process wrapping the executable
     */
    private Process p;

    /**
     * Initializes a new processor
     * @param facts the facts of the program
     * @param uncheckedTerms the unchecked terms
     * @param encodeFacts flag indicating if facts have to be manipulated by {@link #encodeFacts(String)}
     * @param properties the application properties
     */
    public DlvCliDatalogProcessor(String facts, List<TermDescription> uncheckedTerms, boolean encodeFacts, ApplicationProperties properties){
        this.CMD_ARRAY = new String[3];
        this.CMD_ARRAY[0] = properties.getDatalog().getDLVPathAsCommand();
        this.CMD_ARRAY[2] = properties.getDatalog().getCautiousOptionForDlv();
        this.WORK_DIR_FILE = new File(properties.getDatalog().getWorkDirForDlv());
        this.TEMP_FOLDER = properties.getDatalog().getTempFolder();
        this.UNCHECKED_TERMS = uncheckedTerms;
        this.SUFFIX = properties.getDatalog().getFactEncodingSuffix();
        if(encodeFacts) {
            this.FACTS = encodeFacts(facts);
        }else this.FACTS = facts;
    }

    /**
     * Executes a program consisting of the facts, the submission (rules) and the queries
     * @param submission the submission (rules)
     * @param queries the queries that have to be evaluated
     * @return an array of {@link at.jku.dke.etutor.modules.dlg.analysis.WrappedModel}, wrapping the result of the execution
     * @throws QuerySyntaxException if syntax errors occur during evaluation
     * @throws InternalException if an internal error occurs
     */
    @Override
    public WrappedModel[] executeQuery(String submission, String[] queries) throws QuerySyntaxException, InternalException {
        String query;
        String predicate;
        File tempDlv;
        Map<String, List<String>> model = new HashMap<>();

        for(int i = 0; i<queries.length; i++){
            query = queries[i];
            predicate = getPredicateFromQuery(query);
            try {
                List<String> resultFacts = new ArrayList<>();

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
                String s;
                var errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                if((s= errorReader.readLine()) != null) handleSyntaxError(s, errorReader, p, tempDlv);

                // Handle InputStream from process
                var resultReader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                resultReader.readLine();
                while ((s = resultReader.readLine()) != null) {
                    if(Strings.isNotBlank(s)) resultFacts.add(s);
                }
                resultReader.close();
                // Add entry to map
                if(p.waitFor()==0) model.put(predicate, resultFacts);
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

    /**
     * Encodes facts by appending the suffix defined in application.properties.
     * Terms defined in {@link #UNCHECKED_TERMS} will be excluded from encoding
     * @param factString the facts
     * @return the encoded facts
     */
    private String encodeFacts(String factString){
      var facts = Arrays.stream(factString.split("\\.")).toList();
      var newFacts = new StringBuilder();
      for(String fact : facts){
          if(Strings.isBlank(fact)) {
              newFacts.append("\n");
              continue;
          }

          var index = fact.indexOf("(");
          var predicate = fact
                  .substring(0, index != -1 ? index : fact.length())
                  .replace("\n", "")
                  .replace("\r", "")
                  .replace(" ", "");
          var newFact = new StringBuilder();
          newFact.append(predicate);

          if(index != -1){
              var terms = fact.substring(index+1, fact.indexOf(")")).split(",");
              newFact.append("(");
              for(int i = 0; i < terms.length; i++){
                  int termPosition = i+1;
                  int finalI = i;
                  var subList = UNCHECKED_TERMS
                          .stream()
                          .filter(term -> term.getPredicate().equals(predicate))
                          .filter(term -> term.getPosition().equals(termPosition+""))
                          .filter(term -> term.getTerm().replace(" ", "").equals(terms[finalI].replace(" ", "")))
                          .toList();
                  if(i > 0){
                      newFact.append(",");
                  }

                  newFact.append(terms[i]);

                  if(subList.isEmpty()){
                      newFact.append(SUFFIX);
                  }
              }
              newFact.append(").").append("\n");
          }
          newFacts.append(newFact);
      }
      return newFacts.toString();
    }

    /**
     * Handles a syntax error thrown while executing a query by {@link #executeQuery(String, String[])}
     * @param br the {@link BufferedReader} wrapping the error stream
     * @param p the {@link Process} that threw the error
     * @param file the {@link File} that threw the error
     * @throws QuerySyntaxException to indicate the error
     */
    private void handleSyntaxError(String firstLine, BufferedReader br, Process p, File file) throws QuerySyntaxException {
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

    /**
     * Takes a query in the form of "predicate(terms...)?" and returns the predicate
     * @param query the query
     * @return the predicate
     */
    private String getPredicateFromQuery(String query){
        if (query.contains("(")) return query.substring(0, query.indexOf("("));
        else return query;
    }

}
