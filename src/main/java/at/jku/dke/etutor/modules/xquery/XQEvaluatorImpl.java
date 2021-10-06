package at.jku.dke.etutor.modules.xquery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.core.evaluation.Report;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.xquery.analysis.UrlContentMap;
import at.jku.dke.etutor.modules.xquery.analysis.XQAnalysis;
import at.jku.dke.etutor.modules.xquery.analysis.XQAnalysisConfig;
import at.jku.dke.etutor.modules.xquery.analysis.XQProcessor;
import at.jku.dke.etutor.modules.xquery.exercise.XQExerciseBean;
import at.jku.dke.etutor.modules.xquery.exercise.XQExerciseManagerImpl;
import at.jku.dke.etutor.modules.xquery.grading.XQGrading;
import at.jku.dke.etutor.modules.xquery.grading.XQGradingConfig;
import at.jku.dke.etutor.modules.xquery.report.XQReport;
import at.jku.dke.etutor.modules.xquery.report.XQReportConfig;
import at.jku.dke.etutor.modules.xquery.util.PropertyFile;
import org.apache.log4j.Logger;

/**
 * This class serves as entry point for evaluating XQuery queries. There are some basic methods for
 * analyzing the differences between two query solutions, grading the analysis and reporting it.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class XQEvaluatorImpl implements XQEvaluator {

    private final static Logger LOGGER = Logger.getLogger(String.valueOf(XQEvaluatorImpl.class));
	private static final String LINE_SEP = System.getProperty("line.separator", "\n");
	private final ApplicationProperties applicationProperties;
	
    /**
     * Constructs a new <code>XQEvaluatorImpl</code>.
     * 
     */
    public XQEvaluatorImpl(ApplicationProperties applicationProperties)  {
        super();
        //evaluator implementation is bound to RMI registry at startup process; 
        //requesting core manager instance causes creation of singleton
        //and initialization of basic resources with configuration errors
        //being logged
        XQCoreManager.getInstance(applicationProperties);
        this.applicationProperties = applicationProperties;
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

        action = passedAttributes.get(XQConstants.ATTR_ACTION);
        command = passedAttributes.get(XQConstants.ATTR_COMMAND);
        isSpec = XQConstants.COMMAND_EVALUATE_SPEC.equals(command);
        
        msg = new String();
        msg += "Start analyzing with exercise ID " + exerciseId;
    	msg += ", user ID " + userId + ", action '" + action + "'; " + LINE_SEP;
    	msg += "Command is '" + command + "' -> ";
    	msg += "evaluating query as assistant's exercise specification? ";
    	msg += isSpec ? "Yes " : "No ";
    	msg += "evaluating query as student's exercise submission? ";
    	msg += isSpec ? "No " : "Yes ";
        LOGGER.info(msg);
        
        if (isSpec) {
        	return analyzeSpecification(exerciseId, userId, passedAttributes, passedParameters);
        } else  {
        	return analyzeSubmission(exerciseId, userId, passedAttributes, passedParameters);
        }
    }

    @Override
    public String generateHTMLResult(Analysis analysis, Map<String, String> passedAttributes, Locale locale) {
        return "";
    }

    public Analysis analyzeSubmission(int exerciseId, int userId, Map passedAttributes, Map passedParameters) throws Exception {
    	String msg;
    	String action;
    	String command;
    	XQAnalysis analysis;
    	XQAnalysisConfig config;
        XQCoreManager coreManager;
        XQProcessor processor;
        XQExerciseBean exercise;
        XQExerciseManagerImpl exerciseMgr;
        PropertyFile properties;
        Iterator it;
        String querySubmitted;
        boolean debugMode;
        String queryCorrect;
        String[] sortedNodes;
        UrlContentMap urlContents;
        String url;
        String hiddenUrl;
        
        action = (String)passedAttributes.get(XQConstants.ATTR_ACTION);
        command = (String)passedAttributes.get(XQConstants.ATTR_COMMAND);
        
        msg = new String();
        msg += "Start analyzing with exercise ID " + exerciseId;
    	msg += ", user ID " + userId + ", action '" + action + "'";
    	msg += ", command " + command;
        LOGGER.info(msg);

        try {
			querySubmitted = (String)passedAttributes.get(XQConstants.ATTR_SUBMISSION);
		} catch (ClassCastException e) {
			msg += "Passed attribute " + XQConstants.ATTR_SUBMISSION;
            msg += " is not of type " + String.class.getName() + ". ";
            LOGGER.error(msg, e);
            throw new AnalysisException(msg, e);
		}
        
		if (querySubmitted == null) {
            msg = "Passed attribute " + XQConstants.ATTR_SUBMISSION + " is null.";
            LOGGER.error(msg);
            throw new AnalysisException(msg);
        }
		
		//fetch execution properties
        try {
            coreManager = XQCoreManager.getInstance();
            properties = coreManager.getPropertyFile();
            debugMode = properties.parseBooleanProperty(XQCoreManager.MODUS_DEBUG);
        } catch (Exception e) {
        	msg = new String();
            msg += "Analysis was stopped (Exercise id: " + exerciseId + "). ";
            msg += "An exception was thrown when trying to read properties. ";
            LOGGER.error(msg, e);
            throw new AnalysisException(msg, e);
        }

        exerciseMgr = new XQExerciseManagerImpl(applicationProperties);
        exercise = (XQExerciseBean)exerciseMgr.fetchExercise(exerciseId);

        if (exercise == null) {
        	msg = new String();
        	msg += "No exercise definition found based on id " + exerciseId + ".";
            LOGGER.error(msg);
            throw new AnalysisException(msg);
        }

        queryCorrect = exercise.getQuery();
        sortedNodes = (String[])exercise.getSortedNodes().toArray(new String[]{});
        urlContents = new UrlContentMap();
        it = exercise.getUrls().aliasSet().iterator();
        while (it.hasNext()) {
        	hiddenUrl = (String)it.next();
        	url = exercise.getUrls().getUrl(hiddenUrl);
            msg = new String();            
            msg += "Url specification: real url = '" + url + "', ";
            msg += "hidden url = '" + hiddenUrl + "'. " + LINE_SEP;
        	//null values for hidden url are permitted, implying that only the real url
            //is used, which means that in 'submission' mode faking of XQuery statements
            //can not be avoided.
        	if (XQConstants.ACTION_SUBMIT.equalsIgnoreCase(action)) {
                if (hiddenUrl == null) {
                	msg += "No hidden url specified in database, but needed for '";
                	msg += XQConstants.ACTION_SUBMIT + "' mode. ";
                	msg += "Submitted queries could be faked.";
                	msg += "Setting real url as hidden url. ";
                    LOGGER.warn(msg);
                    hiddenUrl = url;
                }
        	} else {
        		if (hiddenUrl == null) {
                	msg += "No hidden url specified. ";
                	msg += "Setting real url as hidden url. ";
                    LOGGER.debug(msg);
                    hiddenUrl = url;
                } else {
                	msg += "Evaluation not in '" + XQConstants.ACTION_SUBMIT + "' mode. ";
            		msg += "Setting real url as hidden url. ";
                    LOGGER.debug(msg);
                    hiddenUrl = url;
                }
            }

            urlContents.addUrlAlias(hiddenUrl, url);
        }
        
        try {
            processor = new XQProcessor(applicationProperties);
        	config = new XQAnalysisConfig();
            
        	config.setProcessor(processor);
            config.setQuery1(queryCorrect);
            //LOGGER.debug("CORRECT QUERY: " + queryCorrect);
            config.setQuery2(querySubmitted);
            config.setSortedNodes(sortedNodes);
            config.setUrls(urlContents);
            config.setDebugMode(debugMode);
            config.setUserID(userId);
            config.setExerciseID(exerciseId);
            
            analysis = new XQAnalysis(config);

            if (!debugMode) {
            	analysis.deleteTempFiles();
            }
            return analysis;
        } catch (Exception e) {
        	msg = "Analysis was stopped (Exercise id: " + exerciseId + ").";
            LOGGER.fatal(msg, e);
            throw new AnalysisException(msg, e);
        }
    }

    public Analysis analyzeSpecification(int exerciseId, int userId, Map passedAttributes, Map passedParameters) throws Exception {
    	String msg;
    	String action;
    	String command;
    	XQAnalysis analysis;
    	XQAnalysisConfig config;
        XQCoreManager coreManager;
        XQProcessor processor;
        XQExerciseBean exercise;
        PropertyFile properties;
        Iterator it;
        boolean debugMode;
        String queryCorrect;
        String[] sortedNodes;
        UrlContentMap urlContents;
        String url;
        String hiddenUrl;
        
        action = (String)passedAttributes.get(XQConstants.ATTR_ACTION);
        command = (String)passedAttributes.get(XQConstants.ATTR_COMMAND);
        
        msg = new String();
        msg += "Start analyzing with exercise ID " + exerciseId;
    	msg += ", user ID " + userId + ", action '" + action + "'";
    	msg += ", command " + command;
        LOGGER.info(msg);

        try {
			exercise = (XQExerciseBean)passedAttributes.get(XQConstants.ATTR_EXERCISE_SPECIFICATION);
		} catch (ClassCastException e) {
			msg = new String();
            msg += "Passed attribute " + XQConstants.ATTR_EXERCISE_SPECIFICATION;
            msg += " is not of type " + XQExerciseBean.class.getName() + " but ";
            msg += passedAttributes.get(XQConstants.ATTR_EXERCISE_SPECIFICATION).getClass().getName();
            LOGGER.error(msg, e);
            throw new AnalysisException(msg, e);
		}
        
		if (exercise == null) {
            msg = "Passed attribute " + XQConstants.ATTR_EXERCISE_SPECIFICATION + " is null.";
            LOGGER.error(msg);
            throw new AnalysisException(msg);
        }
		
		//fetch execution properties
        try {
            coreManager = XQCoreManager.getInstance();
            properties = coreManager.getPropertyFile();
            debugMode = properties.parseBooleanProperty(XQCoreManager.MODUS_DEBUG);
        } catch (Exception e) {
        	msg = new String();
            msg += "Analysis was stopped (Exercise id: " + exerciseId + "). ";
            msg += "An exception was thrown when trying to read properties. ";
            LOGGER.error(msg, e);
            throw new AnalysisException(msg, e);
        }

        queryCorrect = exercise.getQuery();
        sortedNodes = (String[])exercise.getSortedNodes().toArray(new String[]{});
        urlContents = new UrlContentMap();
        it = exercise.getUrls().aliasSet().iterator();
        while (it.hasNext()) {
        	hiddenUrl = (String)it.next();
        	url = exercise.getUrls().getUrl(hiddenUrl);
            msg = new String();            
            msg += "Url specification: real url = '" + url + "', ";
            msg += "hidden url = '" + hiddenUrl + "'. " + LINE_SEP;
        	//null values for hidden url are permitted, implying that only the real url
            //is used, which means that in 'submission' mode faking of XQuery statements
            //can not be avoided.
        	if (XQConstants.ACTION_SUBMIT.equalsIgnoreCase(action)) {
                if (hiddenUrl == null) {
                	msg += "No hidden url specified in database, but needed for '";
                	msg += XQConstants.ACTION_SUBMIT + "' mode. ";
                	msg += "Submitted queries could be faked.";
                	msg += "Setting real url as hidden url. ";
                    LOGGER.warn(msg);
                    hiddenUrl = url;
                }
        	} else {
        		if (hiddenUrl == null) {
                	msg += "No hidden url specified. ";
                	msg += "Setting real url as hidden url. ";
                    LOGGER.debug(msg);
                    hiddenUrl = url;
                } else {
                	msg += "Evaluation not in '" + XQConstants.ACTION_SUBMIT + "' mode. ";
            		msg += "Setting real url as hidden url. ";
                    LOGGER.debug(msg);
                    hiddenUrl = url;
                }
            }

            urlContents.addUrlAlias(hiddenUrl, url);
        }
        
        try {
            processor = new XQProcessor(applicationProperties);
        	config = new XQAnalysisConfig();
        	config.setProcessor(processor);
            config.setQuery1(queryCorrect);
            config.setQuery2(queryCorrect);
            config.setSortedNodes(sortedNodes);
            config.setUrls(urlContents);
            config.setDebugMode(debugMode);
            config.setUserID(userId);
            config.setExerciseID(exerciseId);
            
            analysis = new XQAnalysis(config);
            if (!debugMode) {
            	analysis.deleteTempFiles();
            }
            return analysis;
        } catch (Exception e) {
        	msg = "Analysis was stopped (Exercise id: " + exerciseId + ").";
            LOGGER.fatal(msg, e);
            throw new AnalysisException(msg, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see etutor.core.evaluation.Evaluator#grade(etutor.core.evaluation.Analysis, int,
     *      java.util.Map, java.util.Map)
     */
    public Grading grade(Analysis analysis, int taskId, Map passedAttributes, Map passedParameters)
            throws Exception {
    	String msg;
    	String action;
    	int exerciseId;
    	XQAnalysis xqAnalysis;
    	XQGradingConfig config;
        XQGrading grading;
        XQExerciseManagerImpl exerciseMgr;

        try {
        	xqAnalysis = (XQAnalysis)analysis;
		} catch (ClassCastException e) {
			msg = new String();
            msg += "Passed analysis object is not of type ";
            msg += XQAnalysis.class.getName() + " but ";
            msg += analysis.getClass().getName();
            LOGGER.error(msg, e);
            throw new GradingException(msg, e);
		}
        
		if (xqAnalysis == null) {
            msg = "Passed analysis object is null.";
            LOGGER.error(msg);
            throw new GradingException(msg);
        }
		
        exerciseId = (xqAnalysis).getExerciseID();
        msg = new String();
    	msg += "Start grading. taskID = " + taskId;
        msg += "; exerciseID = " + exerciseId;
        LOGGER.info(msg);

        action = (String)passedAttributes.get(XQConstants.ATTR_ACTION);
        if (XQConstants.ACTION_SUBMIT.equalsIgnoreCase(action)
        || XQConstants.ACTION_DIAGNOSE.equalsIgnoreCase(action)
                || XQConstants.ACTION_RUN.equalsIgnoreCase(action)
                || XQConstants.ACTION_CHECK.equalsIgnoreCase(action)) {
        	msg = new String();
        	msg += "Modus is '" + action;
        	msg += "', grading will be processed and reported.";
            LOGGER.debug(msg);
            try {
            	exerciseMgr = new XQExerciseManagerImpl(applicationProperties);
                config = exerciseMgr.fetchGradingConfig(exerciseId);
                grading = new XQGrading(xqAnalysis, config);
                return grading;
            } catch (Exception e) {
            	msg = "Grading was stopped (Exercise id: " + exerciseId + ").";
                LOGGER.error(msg, e);
                throw new GradingException(msg, e);
            }
        } else {
        	msg = new String();
            msg += "Report processing was stopped. ";
            msg += "Passed '" + XQConstants.ATTR_ACTION + "' attribute '";
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
    public Report report(Analysis analysis, Grading grading, Map passedAttributes,
                         Map passedParameters, Locale locale) throws Exception {
    	String msg;
    	String action;
    	XQAnalysis xqAnalysis;
    	XQGrading xqGrading;
    	XQReportConfig config;
    	XQReport xqReport;
    	Object diagnoseLevelObj;
        int diagnoseLevel;
    	int exerciseId;

        try {
        	xqAnalysis = (XQAnalysis)analysis;
		} catch (ClassCastException e) {
			msg = new String();
            msg += "Passed analysis object is not of type ";
            msg += XQAnalysis.class.getName() + " but ";
            msg += analysis.getClass().getName();
            LOGGER.error(msg, e);
            throw new GradingException(msg, e);
		}
        
		try {
        	xqGrading = (XQGrading)grading;
		} catch (ClassCastException e) {
			msg = new String();
            msg += "Passed grading object is not of type ";
            msg += XQGrading.class.getName() + " but ";
            msg += grading.getClass().getName();
            LOGGER.error(msg, e);
            throw new GradingException(msg, e);
		}
		
		if (xqAnalysis == null) {
            msg = "Passed analysis object is null.";
            LOGGER.error(msg);
            throw new GradingException(msg);
        }
		
		//grading may be null
		
        action = (String)passedAttributes.get(XQConstants.ATTR_ACTION);
		exerciseId = xqAnalysis.getExerciseID();

		msg = new String();
        msg += "Start reporting with exerciseID " + exerciseId;
        LOGGER.info(msg);

        diagnoseLevelObj = passedAttributes.get(XQConstants.ATTR_DIAGNOSE_LEVEL);
        if (diagnoseLevelObj == null || !(diagnoseLevelObj instanceof String)) {
        	msg = new String();
            msg += "Report processing was stopped. ";
            msg += "Passed attribute " + XQConstants.ATTR_DIAGNOSE_LEVEL;
            msg += " is null or no String.";
            LOGGER.error(msg);
            throw new ReportException(msg);
        }
        try {
            diagnoseLevel = Integer.parseInt((String)diagnoseLevelObj);
        } catch (NumberFormatException e) {
        	msg = new String();
            msg += "Report processing was stopped. ";
            msg += "No valid diagnose level: " + diagnoseLevelObj.toString() + ".";
            LOGGER.error(msg);
            throw new ReportException(msg);
        }
        
    	config = new XQReportConfig();
    	config.setMode(action);
    	config.setIncludesGrading(XQConstants.ACTION_SUBMIT.equalsIgnoreCase(action));
    	config.setDiagnoseLevel(diagnoseLevel);

        try {
            xqReport = new XQReport(xqAnalysis, xqGrading, config);
            if (!xqAnalysis.isDebugMode()) {
            	xqAnalysis.deleteTempFiles();
            }
            return xqReport;
        } catch (ParameterException e) {
            msg = "Report processing was stopped. ";
            LOGGER.error(msg, e);
            throw new ReportException(msg, e);
        } catch (Exception e) {
            msg = "Report processing was stopped. ";
            LOGGER.fatal(msg, e);
            throw new ReportException(msg, e);
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
    public String getFileContent(String filepath) throws IOException {
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
                    LOGGER.fatal(msg, e);
                }
            }
        }
    }
}