package at.jku.dke.etutor.modules.dlg;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.core.evaluation.Report;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.dlg.analysis.DatalogAnalysis;
import at.jku.dke.etutor.modules.dlg.analysis.DatalogProcessor;
import at.jku.dke.etutor.modules.dlg.analysis.DlvCliDatalogProcessor;
import at.jku.dke.etutor.modules.dlg.exercise.DatalogExerciseBean;
import at.jku.dke.etutor.modules.dlg.exercise.DatalogExerciseManagerImpl;
import at.jku.dke.etutor.modules.dlg.grading.DatalogGrading;
import at.jku.dke.etutor.modules.dlg.grading.DatalogScores;
import at.jku.dke.etutor.modules.dlg.report.DatalogFeedback;
import at.jku.dke.etutor.modules.dlg.report.DatalogReport;
import at.jku.dke.etutor.modules.dlg.util.PropertyFile;
import at.jku.dke.etutor.modules.xquery.XQConstants;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This class serves as entry point for evaluating Datalog queries. There are some basic methods for
 * analyzing the differences between two query solutions, grading the analysis and reporting it.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class DatalogEvaluatorImpl implements DatalogEvaluator {
    private final ApplicationProperties properties;
 	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(DatalogEvaluatorImpl.class);
	private static final String LINE_SEP = System.getProperty("line.separator", "\n");
	
    /**
     * Constructs a new <code>DatalogEvaluatorImpl</code> which can be used as stub for RMI
     */
    public DatalogEvaluatorImpl(ApplicationProperties properties) {
        super();
        DatalogCoreManager.getInstance();
        this.properties = properties;
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.core.evaluation.Evaluator#analyze(int, int, java.util.Map, java.util.Map)
     */
    public Analysis analyze(int exerciseId, int userId, Map<String, String> passedAttributes, Map<String, String> passedParameters, Locale locale) throws Exception {
    	String msg;
    	String action;
    	String command;
        boolean isSpec;

        action = passedAttributes.get(DatalogConstants.ATTR_ACTION);
        command = passedAttributes.get(DatalogConstants.ATTR_COMMAND);
        isSpec = DatalogConstants.COMMAND_EVALUATE_SPEC.equals(command);
        
        msg = "";
        msg += "Start analyzing with exercise ID " + exerciseId;
    	msg += ", user ID " + userId + ", action '" + action + "'; " + LINE_SEP;
    	msg += "Command is '" + command + "' -> ";
    	msg += "evaluating query as assistant's exercise specification? ";
    	msg += isSpec ? "Yes " : "No ";
    	msg += "evaluating query as student's exercise submission? ";
    	msg += isSpec ? "No " : "Yes ";
        LOGGER.info(msg);

        return analyzeSubmission(exerciseId, userId, passedAttributes, passedParameters);
    }
    
    public Analysis analyzeSubmission(int exerciseId, int userId, Map<String, String> passedAttributes, Map<String, String> passedParameters) throws Exception {
    	String msg;
    	String action;
    	String command;
    	DatalogAnalysis analysis;
        DatalogCoreManager coreManager;
        DatalogProcessor processor;
        DatalogExerciseBean exercise;
        DatalogExerciseManagerImpl exerciseMgr;
        PropertyFile properties;
        String rulesSubmitted;
        boolean debugMode;
        boolean notAllowFacts = false;
        String facts;
        String rulesCorrect;
        String[] queries;

        facts = null;
        queries = null;
        action = passedAttributes.get(DatalogConstants.ATTR_ACTION);
        command = passedAttributes.get(DatalogConstants.ATTR_COMMAND);
        
        msg = "";
        msg += "Start analyzing with exercise ID " + exerciseId;
    	msg += ", user ID " + userId + ", action '" + action + "'";
    	msg += ", command " + command;
        LOGGER.info(msg);
        
        rulesSubmitted = passedAttributes.get(DatalogConstants.ATTR_SUBMISSION);

		if (rulesSubmitted == null) {
            msg = "Passed attribute " + DatalogConstants.ATTR_SUBMISSION + " is null.";
            LOGGER.error(msg);
            throw new AnalysisException(msg);
        }

		//fetch execution properties needed for executing datalog queries
        try {
            coreManager = DatalogCoreManager.getInstance();
            properties = coreManager.getPropertyFile();
            debugMode = properties.parseBooleanProperty(DatalogCoreManager.KEY_MODUS_DEBUG);
        } catch (InvalidResourceException e) {
        	msg = new String();
        	msg += "Could not fetch properties for datalog execution.";
        	msg += "An exception was thrown when trying to read properties. ";
        	LOGGER.error(msg, e);
            throw new AnalysisException(msg, e);
        }

        exerciseMgr = new DatalogExerciseManagerImpl();
        exercise = (DatalogExerciseBean)exerciseMgr.fetchExercise(exerciseId);
        
        if (exercise == null) {
        	msg = new String();
        	msg += "No exercise definition found based on id " + exerciseId + ".";
            LOGGER.error(msg);
            throw new AnalysisException(msg);
        }

        rulesCorrect = exercise.getQuery();
        if (exercise.getFactsId() != null) {
        	facts = exerciseMgr.fetchFacts(exercise.getFactsId());
        }
        if (exercise.getPredicates() != null) {
        	queries = (String[])exercise.getPredicates().toArray(new String[] {});
        }
        
        if (DatalogConstants.ACTION_SUBMIT.equalsIgnoreCase(action)) {
        	msg = new String();
        	msg += "'" + action + "' mode -> ";
            msg += "submission will be checked to make sure no facts have been declared by the student.";
            msg += LINE_SEP;
        	LOGGER.info(msg);
            notAllowFacts = true;
        }        
    	
        // Analysis, actually
        try {
            processor = new DlvCliDatalogProcessor(facts, exercise.getTerms(), notAllowFacts, this.properties);
            analysis = new DatalogAnalysis(rulesCorrect, rulesSubmitted, queries, processor, debugMode, this.properties);
            analysis.setExerciseID(exerciseId);
            return analysis;
        } catch (Exception e) {
            msg = "Analysis was stopped (Exercise id: " + exerciseId + ").";
            LOGGER.error(msg, e);
            throw new AnalysisException(msg, e);
        }
    }
    

    /*
     * (non-Javadoc)
     * 
     * @see etutor.core.evaluation.Evaluator#grade(etutor.core.evaluation.Analysis, int,
     *      java.util.Map, java.util.Map)
     */
    public Grading grade(Analysis analysis, int taskId, Map<String, String> passedAttributes, Map<String, String> passedParameters)
            throws Exception {
    	String msg;
    	String action;
    	int exerciseId;
    	DatalogAnalysis datalogAnalysis;
    	DatalogScores scores;
    	DatalogGrading grading;
    	DatalogExerciseManagerImpl exerciseMgr;
    	
        try {
        	datalogAnalysis = (DatalogAnalysis)analysis;
		} catch (ClassCastException e) {
			msg = new String();
            msg += "Passed analysis object is not of type ";
            msg += DatalogAnalysis.class.getName() + " but ";
            msg += analysis.getClass().getName();
            LOGGER.error(msg, e);
            throw new GradingException(msg, e);
		}
        
		if (datalogAnalysis == null) {
            msg = "Passed analysis object is null.";
            LOGGER.error(msg);
            throw new GradingException(msg);
        }
		
    	exerciseId = (datalogAnalysis).getExerciseID();
    	msg = new String();
        msg += "Start grading with task ID " + taskId;
        msg += ", exercise ID " + exerciseId;
        LOGGER.info(msg);

        action = passedAttributes.get(DatalogConstants.ATTR_ACTION);
        if (DatalogConstants.ACTION_SUBMIT.equalsIgnoreCase(action) || DatalogConstants.ACTION_DIAGNOSE.equalsIgnoreCase(action)
                || DatalogConstants.ACTION_RUN.equalsIgnoreCase(action)
                || DatalogConstants.ACTION_CHECK.equalsIgnoreCase(action)) {
        	msg = new String();
        	msg += "Modus is '" + action;
            msg += "', grading will be processed and reported.";
            LOGGER.debug(msg);
            try {
            	exerciseMgr = new DatalogExerciseManagerImpl();
            	scores = exerciseMgr.fetchScores(exerciseId);
                grading = new DatalogGrading(datalogAnalysis, scores);
                grading.setReporting(true);
                return grading;
            } catch (Exception e) {
                msg = "Grading was stopped (Exercise id: " + exerciseId + ").";
                LOGGER.error(msg, e);
                throw new GradingException(msg, e);
            }
        } else {
        	msg = new String();
            msg += "Report processing was stopped.\n ";
            msg += "Passed '" + DatalogConstants.ATTR_ACTION + "' attribute '";
            msg += action + "' is not applicable.";
            LOGGER.error(msg);
            throw new GradingException(msg);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.core.evaluation.Evaluator#report(etutor.core.evaluation.Analysis,
     *      etutor.core.evaluation.Grading, java.util.Map, java.util.Map)
     */
    public Report report(Analysis analysis, Grading grading, Map<String, String> passedAttributes,
                         Map<String, String> passedParameters, Locale locale) throws Exception {
        String msg;
        DatalogAnalysis datalogAnalysis;
        DatalogGrading datalogGrading;
        DatalogFeedback fb;
        String diagnoseLevelStr;
        int diagnoseLevel;
        int exerciseId;
        
        try {
        	datalogAnalysis = (DatalogAnalysis)analysis;
		} catch (ClassCastException e) {
			msg = new String();
            msg += "Passed analysis object is not of type ";
            msg += DatalogAnalysis.class.getName() + " but ";
            msg += analysis.getClass().getName();
            LOGGER.error(msg, e);
            throw new GradingException(msg, e);
		}
        
		try {
        	datalogGrading = (DatalogGrading)grading;
		} catch (ClassCastException e) {
			msg = new String();
            msg += "Passed grading object is not of type ";
            msg += DatalogGrading.class.getName() + " but ";
            msg += grading.getClass().getName();
            LOGGER.error(msg, e);
            throw new GradingException(msg, e);
		}
		
		if (datalogAnalysis == null) {
            msg = "Passed analysis object is null.";
            LOGGER.error(msg);
            throw new GradingException(msg);
        }

		//grading may be null
		
        exerciseId = datalogAnalysis.getExerciseID();
        msg = new String();
    	msg += "Start reporting with exerciseID " + exerciseId;
        LOGGER.info(msg);

        diagnoseLevelStr = passedAttributes.get(DatalogConstants.ATTR_DIAGNOSE_LEVEL);
        if (diagnoseLevelStr == null) {
            msg = new String();
            msg += "Report processing was stopped.\n";
            msg += "Passed attribute " + DatalogConstants.ATTR_DIAGNOSE_LEVEL;
            msg += " is null or no String.";
            LOGGER.error(msg);
            throw new ReportException(msg);
        }
        try {
            diagnoseLevel = Integer.parseInt(diagnoseLevelStr);
        } catch (NumberFormatException e) {
        	msg = new String();
            msg += "Report processing was stopped.\n ";
            msg += "No valid diagnose level: " + diagnoseLevelStr + ".";
            LOGGER.error(msg);
            throw new ReportException(msg);
        }
        try {
            fb = datalogAnalysis.getFeedback(datalogGrading, null, diagnoseLevel);
            return fb;
        } catch (ParameterException e) {
            msg = "Report processing was stopped.";
            LOGGER.error(msg, e);
            throw new ReportException(msg, e);
        } catch (Exception e) {
            msg = "Report processing was stopped.";
            LOGGER.error(msg, e);
            throw new ReportException(msg);
        }
    }

    /**
     * Gets the content of a file identified by a filepath.
     * 
     * @param filepath The filepath of the file to read the content from.
     * @param table Additional information for adjusting the error message if an exception occurs.
     *            This is the database table where the filepath was retrieved from.
     * @param id Additional information for adjusting the error message if an exception occurs. This
     *            is the exercise id.
     * @return The content of the file as String.
     * @throws InvalidResourceException if the file does not exist or an <code>IOException</code>
     *             occured.
     */
    private String getFileContent(String filepath, String table, int id)
            throws InvalidResourceException {
    	String msg;
        try {
            return getFileContent(filepath);
        } catch (IOException e) {
            msg = new String();
            msg += "Problems occured when accessing database file or query file ";
            msg += filepath + ", defined in table " + table + " with id " + id + ".";
            throw new InvalidResourceException(msg, e);
        }
    }

    /**
     * Gets the content of a file identified by a filepath.
     * 
     * @param filepath The filepath of the file to read the content from.
     * @return The content of the file as String.
     * @throws InvalidResourceException if the file does not exist or an <code>IOException</code>
     *             occured.
     */
    private String getFileContent(String filepath) throws IOException {
    	String msg;
        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(new File(filepath)));
            String line;
            String content = "";
            while ((line = input.readLine()) != null) {
                content += line + "\n ";
            }
            return content;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                	msg = "Exception when closing file input stream.";
                    LOGGER.error(msg, e);
                }
            }
        }
    }

    @Override
    public String generateHTMLResult(Analysis analysis, Map<String, String> passedAttributes, Locale locale) {
        if(analysis instanceof DatalogAnalysis datalogAnalysis){
            if(passedAttributes.get("action").equals(XQConstants.ACTION_SUBMIT)) {
                return datalogAnalysis.isCorrect() ? "<div> Your solution is correct </div>" : "<div> Your solution seems not to be correct </div>";
            }
            try {
                return ((DatalogReport)this.report
                        (analysis, null, passedAttributes, new HashMap<>(), locale))
                        .getRenderedResult();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}