package at.jku.dke.etutor.modules.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.jku.dke.etutor.core.evaluation.*;

import at.jku.dke.etutor.modules.sql.analysis.SQLAnalysis;
import at.jku.dke.etutor.modules.sql.analysis.SQLAnalyzer;
import at.jku.dke.etutor.modules.sql.analysis.SQLAnalyzerConfig;
import at.jku.dke.etutor.modules.sql.analysis.SQLCriterionAnalysis;
import at.jku.dke.etutor.modules.sql.grading.GradingScope;
import at.jku.dke.etutor.modules.sql.grading.SQLCriterionGradingConfig;
import at.jku.dke.etutor.modules.sql.grading.SQLGrader;
import at.jku.dke.etutor.modules.sql.grading.SQLGraderConfig;
import at.jku.dke.etutor.modules.sql.report.SQLReporter;
import at.jku.dke.etutor.modules.sql.report.SQLReporterConfig;
import org.springframework.stereotype.Service;

/**
 * Implementation of the Evaluator Interface for SQL Submissions
 */
@Service
public class SQLEvaluator implements Evaluator {

	private Logger logger;
	private static final String LINE_SEP = System.getProperty("line.separator", "\n");
	private SQLConstants constants;

	public SQLEvaluator(SQLConstants constants) {
		super();

		try {
			this.logger = Logger.getLogger("at.jku.dke.etutor.modules.sql");
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.constants=constants;
	}

	/**
	 * Analyzes the submission
	 * @param exerciseID the exercise id
	 * @param userID the user id
	 * @param passedAttributes the passed attributes
	 * @param passedParameters the passed parameters
	 * @return the Analysis
	 * @throws Exception if an error occurs
	 */
	public Analysis analyze(int exerciseID, int userID, Map<String, String> passedAttributes, Map<String, String> passedParameters) throws Exception {
		logger.info("exerciseID: " + exerciseID);
		logPassedAttributes(passedAttributes, passedParameters);
		
		String action;
		String message;
		SQLAnalysis analysis;
		SQLAnalyzerConfig analyzerConfig;
		Iterator<SQLCriterionAnalysis> criterionAnalysesIterator;
		SQLCriterionAnalysis criterionAnalysis;
		
		int diagnoseLevel;
		String action_Param;
		String submission_Param;
		String diagnoseLevel_Param;

		analysis = new SQLAnalysis();
		analyzerConfig = new SQLAnalyzerConfig();
		
		action_Param = passedAttributes.get("action");
		submission_Param = passedAttributes.get("submission");
		diagnoseLevel_Param = passedAttributes.get("diagnoseLevel");

		action = action_Param;
		diagnoseLevel = Integer.parseInt(diagnoseLevel_Param);
		
		//SETTING THE SUBMISSION
		analysis.setSubmission((submission_Param).replace(";",""));

		String query;
		Connection referenceConn;

		Statement stmt = null;
		ResultSet rset = null;

		String correctQuery = "";
		String referenceConnPwd = "";
		String referenceConnString = "";
		String referenceConnUser = "";

		//ESTABLISHING CONNECTION TO SQL DATABASE
		try(Connection conn = DriverManager.getConnection(constants.getConnURL(), constants.getConnUser(), constants.getConnPwd())){
			Class.forName(constants.getJdbcDriver());
			conn.setAutoCommit(true);

			//FETCHING CONNECT_DATA TO EXERCISE SPECIFIC REFERENCE DATABASE
			query = "";

			if (action.equalsIgnoreCase("test")){
				query = query.concat("SELECT	c.conn_string, c.conn_user, c.conn_pwd " + LINE_SEP);
				query = query.concat("FROM 		connections c " + LINE_SEP);
				query = query.concat("WHERE 	c.id = " + passedAttributes.get("selected_trial_db") + LINE_SEP);
			} else {
				query = query.concat("SELECT	c.conn_string, c.conn_user, c.conn_pwd " + LINE_SEP);
				query = query.concat("FROM 		connections c, exercises e " + LINE_SEP);
				query = query.concat("WHERE 	e.id = " + exerciseID + " AND " + LINE_SEP);
				if (action.equalsIgnoreCase("submit")){
					query = query.concat("			c.id = e.submission_db" + LINE_SEP);
				} else {
					query = query.concat("			c.id = e.practise_db" + LINE_SEP);

				}
			}

			this.logger.log(Level.INFO,"QUERY for reading connection data:\n {0}", query);

			stmt = conn.createStatement();
			rset = stmt.executeQuery(query);

			if (rset.next()){
				referenceConnUser = rset.getString("conn_user");
				referenceConnPwd = rset.getString("conn_pwd");
				referenceConnString = rset.getString("conn_string");
			}

			//ESTABLISHING CONNECTION TO EXERCISE SPECIFIC REFERENCE DATABASE

			this.logger.log(Level.INFO,"{0}",referenceConnString + " - " + referenceConnUser + " - " + referenceConnPwd);

			referenceConn = DriverManager.getConnection(referenceConnString, referenceConnUser, referenceConnPwd);
			referenceConn.setAutoCommit(true);

			//DETERMINING CORRECT QUERY
			if ((action.equalsIgnoreCase("test")) || (action.equalsIgnoreCase("run"))){
				correctQuery = analysis.getSubmission().toString();
			} else {
				query = "";
				query = query.concat("SELECT	solution " + LINE_SEP);
				query = query.concat("FROM 		exercises " + LINE_SEP);
				query = query.concat("WHERE 	id = " + exerciseID + LINE_SEP);

				rset.close();
				stmt.close();
				stmt = conn.createStatement();
				rset = stmt.executeQuery(query);
				if (rset.next()){
					correctQuery = rset.getString("solution");
				}
			}
		} catch (SQLException e){
			message = "";
			message = message.concat("Stopped analysis due to errors. ");
			this.logger.log(Level.SEVERE, message, e);
			throw e;
		} finally {
			try { 
				if (rset != null){
					rset.close();
				}
				if (stmt != null){
					stmt.close();
				}
			} catch (SQLException e){
				message ="";
				message = message.concat("Stopped analysis due to errors. ");
				this.logger.log(Level.SEVERE, message);
				throw e;
			}
		}
		
		//CONFIGURING ANALYZER
		if (action.toUpperCase().equals(SQLEvaluationAction.RUN.toString())) {
			analyzerConfig.addCriterionToAnalyze(SQLEvaluationCriterion.CORRECT_SYNTAX);
			analyzerConfig.setDiagnoseLevel(1);
		} else if (action.toUpperCase().equals(SQLEvaluationAction.CHECK.toString())){
			analyzerConfig.addCriterionToAnalyze(SQLEvaluationCriterion.CORRECT_SYNTAX);
			analyzerConfig.addCriterionToAnalyze(SQLEvaluationCriterion.CARTESIAN_PRODUCT);
			analyzerConfig.addCriterionToAnalyze(SQLEvaluationCriterion.CORRECT_COLUMNS);
			analyzerConfig.addCriterionToAnalyze(SQLEvaluationCriterion.CORRECT_TUPLES);
			analyzerConfig.addCriterionToAnalyze(SQLEvaluationCriterion.CORRECT_ORDER);
			analyzerConfig.setDiagnoseLevel(0);
		} else {
			analyzerConfig.addCriterionToAnalyze(SQLEvaluationCriterion.CORRECT_SYNTAX);
			analyzerConfig.addCriterionToAnalyze(SQLEvaluationCriterion.CARTESIAN_PRODUCT);
			analyzerConfig.addCriterionToAnalyze(SQLEvaluationCriterion.CORRECT_COLUMNS);
			analyzerConfig.addCriterionToAnalyze(SQLEvaluationCriterion.CORRECT_TUPLES);
			analyzerConfig.addCriterionToAnalyze(SQLEvaluationCriterion.CORRECT_ORDER);

			if (action.toUpperCase().equals(SQLEvaluationAction.SUBMIT.toString())) {
				analyzerConfig.setDiagnoseLevel(1);
			} else {
				analyzerConfig.setDiagnoseLevel(diagnoseLevel);
			}
		}
		
		analyzerConfig.setConnection(referenceConn);
		analyzerConfig.setCorrectQuery(correctQuery.replace(";"," "));

		// Analyzing the submission
		SQLAnalysis sqlAnalysis;
		SQLAnalyzer analyzer = new SQLAnalyzer();
		sqlAnalysis = analyzer.analyze(analysis.getSubmission(), analyzerConfig);
		sqlAnalysis.setSubmission(analysis.getSubmission());
		sqlAnalysis.setSubmissionSuitsSolution(true);

		// Iterating over SQlCriterionAnalyses to determine of submission is correct
		criterionAnalysesIterator = sqlAnalysis.iterCriterionAnalyses();
		while (criterionAnalysesIterator.hasNext()) {
			criterionAnalysis = criterionAnalysesIterator.next();
			if (!criterionAnalysis.isCriterionSatisfied()) {
				sqlAnalysis.setSubmissionSuitsSolution(false);
			}
		}

		return sqlAnalysis;
	}

	/**
	 * Grades the submission according to the analysis
	 * @param analysis the analysis
	 * @param maxPoints the maxPoints
	 * @param passedAttributes the passed attributes
	 * @param passedParameters the passed parameters
	 * @return the Grading
	 * @throws Exception if an error occurs
	 */
	public Grading grade(Analysis analysis, int maxPoints, Map<String, String> passedAttributes, Map<String, String> passedParameters) throws Exception {
		logger.info("analysis: " + analysis);
		logger.info("maxPoints: " + maxPoints);
		logPassedAttributes(passedAttributes, passedParameters);
		
		SQLGrader grader;
		DefaultGrading grading;
		SQLGraderConfig graderConfig;
		SQLCriterionGradingConfig criterionGradingConfig;

		String action;
		String message;
		String action_Param;

		grader = new SQLGrader();
		grading = new DefaultGrading();
		graderConfig = new SQLGraderConfig();
		action_Param = passedAttributes.get("action");

		action = action_Param;

		//syntax
		criterionGradingConfig = new SQLCriterionGradingConfig();
		criterionGradingConfig.setPositivePoints(1);
		criterionGradingConfig.setNegativePoints(0);
		criterionGradingConfig.setPositiveScope(GradingScope.CRITERION);
		criterionGradingConfig.setNegativeScope(GradingScope.CRITERION);
		graderConfig.addCriteriaGradingConfig(SQLEvaluationCriterion.CORRECT_SYNTAX, criterionGradingConfig);
		
		//cartesian product
		criterionGradingConfig = new SQLCriterionGradingConfig();
		criterionGradingConfig.setPositivePoints(1);
		criterionGradingConfig.setNegativePoints(0);
		criterionGradingConfig.setPositiveScope(GradingScope.CRITERION);
		criterionGradingConfig.setNegativeScope(GradingScope.CRITERION);

		final var isNotRunAction = !action.toUpperCase().equals(SQLEvaluationAction.RUN.toString());

		if (isNotRunAction){
			graderConfig.addCriteriaGradingConfig(SQLEvaluationCriterion.CARTESIAN_PRODUCT, criterionGradingConfig);
		}

		//columns
		criterionGradingConfig = new SQLCriterionGradingConfig();
		criterionGradingConfig.setPositivePoints(1);
		criterionGradingConfig.setNegativePoints(0);
		criterionGradingConfig.setPositiveScope(GradingScope.CRITERION);
		criterionGradingConfig.setNegativeScope(GradingScope.CRITERION);
		if (isNotRunAction){
			graderConfig.addCriteriaGradingConfig(SQLEvaluationCriterion.CORRECT_COLUMNS, criterionGradingConfig);
		}
		
		//tuples
		criterionGradingConfig = new SQLCriterionGradingConfig();
		criterionGradingConfig.setPositivePoints(1);
		criterionGradingConfig.setNegativePoints(0);
		criterionGradingConfig.setPositiveScope(GradingScope.CRITERION);
		criterionGradingConfig.setNegativeScope(GradingScope.CRITERION);
		if (isNotRunAction){
			graderConfig.addCriteriaGradingConfig(SQLEvaluationCriterion.CORRECT_TUPLES, criterionGradingConfig);
		}
		
		//ordering
		criterionGradingConfig = new SQLCriterionGradingConfig();
		criterionGradingConfig.setPositivePoints(1);
		criterionGradingConfig.setNegativePoints(0);
		criterionGradingConfig.setPositiveScope(GradingScope.CRITERION);
		criterionGradingConfig.setNegativeScope(GradingScope.CRITERION);
		if (isNotRunAction){
			graderConfig.addCriteriaGradingConfig(SQLEvaluationCriterion.CORRECT_ORDER, criterionGradingConfig);
		}
		
		graderConfig.setMaximumPoints(maxPoints);

		return grader.grade((SQLAnalysis)analysis, graderConfig);
	}

	/**
	 * Creates a report according to the Analysis and the Grading
	 * @param analysis the analysis
	 * @param grading the grading
	 * @param passedAttributes the passed attributes
	 * @param passedParameters the passed parameters
	 * @param locale the locale --> TO BE IMPLEMENTeD
	 * @return the report
	 */
	public Report report(Analysis analysis, Grading grading, Map<String, String> passedAttributes, Map<String ,String> passedParameters, Locale locale) {
		logger.info("analysis: " + analysis);
		logger.info("grading: " + grading);
		logPassedAttributes(passedAttributes, passedParameters);

		SQLReporter reporter = new SQLReporter();
		SQLReporterConfig reporterConfig = new SQLReporterConfig();

		String action;
		String diagnoseLevel;
		String action_Param = passedAttributes.get("action");
		String diagnoseLevel_Param = passedAttributes.get("diagnoseLevel");


		action = action_Param;
		diagnoseLevel = diagnoseLevel_Param;

		// Setting action and diagnose level in the SQLReporterConfig
		if (action.toUpperCase().equals(SQLEvaluationAction.RUN.toString())){
			reporterConfig.setAction(SQLEvaluationAction.RUN);
			reporterConfig.setDiagnoseLevel(1);
		}
		if (action.toUpperCase().equals(SQLEvaluationAction.SUBMIT.toString())){
			reporterConfig.setAction(SQLEvaluationAction.SUBMIT);
			reporterConfig.setDiagnoseLevel(2);
		}
		if (action.toUpperCase().equals(SQLEvaluationAction.DIAGNOSE.toString())){
			reporterConfig.setAction(SQLEvaluationAction.DIAGNOSE);
			reporterConfig.setDiagnoseLevel(Integer.parseInt(diagnoseLevel));
		}
		if (action.toUpperCase().equals(SQLEvaluationAction.CHECK.toString())){
			reporterConfig.setAction(SQLEvaluationAction.DIAGNOSE);
			reporterConfig.setDiagnoseLevel(0);
		}

		return reporter.createReport((SQLAnalysis)analysis, (DefaultGrading)grading, reporterConfig, locale);
	}


	/**
	 * Logs the passedAttributes and passedParameters
	 * @param passedAttributes the passedAttributes
	 * @param passedParameters the passedParameters
	 */
	public void logPassedAttributes(Map<String, String> passedAttributes, Map<String, String> passedParameters){
		logger.info("passedAttributes (" + passedAttributes.size() + ")");
		for (String key: passedAttributes.keySet()) {
			logger.info("  key: "+key+" value: " + passedAttributes.get(key));
		}
		logger.info("passedParameters (" + passedParameters.size() + ")");
		for (String key: passedParameters.keySet()) {
			logger.info("  key: "+key+" value: " + passedParameters.get(key));
		}
	}
	@Override
	public String generateHTMLResult(Analysis analysis, Map<String, String> passedAttributes, Locale locale) {
		if (passedAttributes.get("action").equalsIgnoreCase("submit")) return null;

		StringBuilder result = new StringBuilder();

		if (analysis instanceof SQLAnalysis) {
			SQLAnalysis sqlAnalysis = (SQLAnalysis)analysis;
			SQLCriterionAnalysis cartesianProduct = sqlAnalysis.getCriterionAnalysis(SQLEvaluationCriterion.CARTESIAN_PRODUCT);
			if(cartesianProduct != null && !cartesianProduct.isCriterionSatisfied()) return null;

			if(locale == Locale.GERMAN) result.append("<strong> Das Ergebnis Ihrer Abfrage: </strong><br>");
			else result.append("<strong>The result of your query: </strong><br>");
			Iterator<String> columnIterator= sqlAnalysis.getQueryResultColumnLabels().iterator();


			result.append("<table border=1 frame=void rules=rows>");
			result.append("<tr>");
			while(columnIterator.hasNext()){
				result.append("<th>").append(columnIterator.next()).append("</th>");
			}
			result.append("</tr>");

			Iterator<Collection<String>> tuplesIterator = sqlAnalysis.getQueryResultTuples().iterator();
			Collection<String> tuple;
			Iterator<String> tupleAttributesIterator;
			while(tuplesIterator.hasNext()){
				result.append("<tr>");
				tuple = tuplesIterator.next();
				tupleAttributesIterator = tuple.iterator();
				while(tupleAttributesIterator.hasNext()){
					String next = tupleAttributesIterator.next();
					if(next != null)    result.append("<td>").append(next).append("</td>");
				}
				result.append("</tr>");
			}
			result.append("</table>");

			return result.toString();
		}
		return null;
	}

}
