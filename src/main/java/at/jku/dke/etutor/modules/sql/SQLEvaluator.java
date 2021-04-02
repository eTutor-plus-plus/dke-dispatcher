package at.jku.dke.etutor.modules.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Required;


import at.jku.dke.etutor.core.evaluation.*;

import at.jku.dke.etutor.modules.sql.analysis.SQLAnalysis;
import at.jku.dke.etutor.modules.sql.analysis.SQLAnalyzer;
import at.jku.dke.etutor.modules.sql.analysis.SQLAnalyzerConfig;
import at.jku.dke.etutor.modules.sql.analysis.SQLCriterionAnalysis;
import at.jku.dke.etutor.modules.sql.grading.GradingScope;
import at.jku.dke.etutor.modules.sql.grading.SQLCriterionGradingConfig;
import at.jku.dke.etutor.modules.sql.grading.SQLGrader;
import at.jku.dke.etutor.modules.sql.grading.SQLGraderConfig;
import at.jku.dke.etutor.modules.sql.report.SQLReport;
import at.jku.dke.etutor.modules.sql.report.SQLReporter;
import at.jku.dke.etutor.modules.sql.report.SQLReporterConfig;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;


public class SQLEvaluator implements Evaluator, MessageSourceAware {

	private Logger logger;
	private static String LINE_SEP = System.getProperty("line.separator", "\n");
	private MessageSource messageSource;

	public SQLEvaluator() {
		super();

		try {
			this.logger = Logger.getLogger("etutor.modules.sql");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Required
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public Analysis analyze(int exerciseID, int userID, Map passedAttributes, Map passedParameters) throws Exception {
		logger.info("exerciseID: " + exerciseID);
		logger.info("passedAttributes (" + passedAttributes.size() + ")");
		for (Object key: passedAttributes.keySet()) {
			logger.info("  key: "+key+" value: " + passedAttributes.get(key));
		}
		logger.info("passedParameters (" + passedParameters.size() + ")");
		for (Object key: passedParameters.keySet()) {
			logger.info("  key: "+key+" value: " + passedParameters.get(key));
		}
		
		String action;
		String message;
		String submission;
		SQLAnalysis analysis;
		SQLAnalyzerConfig analyzerConfig;
		Iterator criterionAnalysesIterator;
		SQLCriterionAnalysis criterionAnalysis;
		
		int diagnoseLevel;
		Object action_Param;
		Object submission_Param;
		Object diagnoseLevel_Param;

		analysis = new SQLAnalysis();
		analyzerConfig = new SQLAnalyzerConfig();
		
		action_Param = passedAttributes.get("action");
		submission_Param = passedAttributes.get("submission");
		diagnoseLevel_Param = passedAttributes.get("diagnoseLevel");

		if ((submission_Param == null) || (!(submission_Param instanceof String))){
			message = new String();
			message = message.concat("Stopped analysis due to errors. ");
			message = message.concat("Can not utilize submission parameter.");
			
			this.logger.log(Level.SEVERE, message);
			throw new IllegalArgumentException(message);
		}

		if ((action_Param == null) || (!(action_Param instanceof String))){
			message = new String();
			message = message.concat("Stopped analysis due to errors. ");
			message = message.concat("Can not utilize evaluation action parameter.");
			
			this.logger.log(Level.SEVERE, message);
			throw new IllegalArgumentException(message);
		}

		if ((diagnoseLevel_Param == null) || (!(diagnoseLevel_Param instanceof String))){
			message = new String();
			message = message.concat("Stopped analysis due to errors. ");
			message = message.concat("Can not utilize diagnose level parameter.");
			
			this.logger.log(Level.SEVERE, message);
			throw new IllegalArgumentException(message);
		}
		
		action = (String)action_Param;
		diagnoseLevel = Integer.parseInt((String)diagnoseLevel_Param);
		
		//SETTING THE SUBMISSION
		analysis.setSubmission(((String)submission_Param).replaceAll(";",""));

		String query;
		Connection referenceConn;

		Statement stmt = null;
		ResultSet rset = null;
		Connection conn = null;

		String correctQuery = null;
		String referenceConnPwd = null;
		String referenceConnString = null;
		String referenceConnUser = null;

		try{
			//ESTABLISHING CONNECTION TO SQL DATABASE
			java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(SQLConstants.CONN_URL, SQLConstants.CONN_USER, SQLConstants.CONN_PWD);
			conn.setAutoCommit(true);
			//System.out.println(conn.getMetaData().getURL());
			//FETCHING CONNECT_DATA TO EXERCISE SPECIFIC REFERENCE DATABASE
			query = new String();

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

			this.logger.log(Level.INFO,"QUERY for reading connection data:\n" + query);

			stmt = conn.createStatement();
			rset = stmt.executeQuery(query);

			if (rset.next()){
				referenceConnUser = rset.getString("conn_user");				
				referenceConnPwd = rset.getString("conn_pwd");				
				referenceConnString = rset.getString("conn_string");
			}
			
			//ESTABLISHING CONNECTION TO EXERCISE SPECIFIC REFERENCE DATABASE
			java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			this.logger.log(Level.INFO,referenceConnString + " - " + referenceConnUser + " - " + referenceConnPwd);

			referenceConn = DriverManager.getConnection("jdbc:oracle:thin:@" + referenceConnString, referenceConnUser, referenceConnPwd);
			referenceConn.setAutoCommit(true);
			
			//DETERMINING CORRECT QUERY
			if ((action.equalsIgnoreCase("test")) || (action.equalsIgnoreCase("run"))){
				correctQuery = analysis.getSubmission().toString();
			} else {
				query = new String();
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
			message = new String();
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
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e){
				message = new String();
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
		analyzerConfig.setCorrectQuery(correctQuery.replaceAll(";"," "));

		SQLAnalysis sqlAnalysis;
		SQLAnalyzer analyzer = new SQLAnalyzer();
		
		sqlAnalysis = analyzer.analyze(analysis.getSubmission(), analyzerConfig);
		sqlAnalysis.setSubmission(analysis.getSubmission());
		sqlAnalysis.setSubmissionSuitsSolution(true);
		//code copied from SQLReporter (2005-10-24, g.n.)
		criterionAnalysesIterator = sqlAnalysis.iterCriterionAnalyses();
		while (criterionAnalysesIterator.hasNext()) {
			criterionAnalysis = (SQLCriterionAnalysis)criterionAnalysesIterator.next();
			if (!criterionAnalysis.isCriterionSatisfied()) {
				sqlAnalysis.setSubmissionSuitsSolution(false);

			}
		}
			
		return sqlAnalysis;
	}

	public Grading grade(Analysis analysis, int maxPoints, Map passedAttributes, Map passedParameters) throws Exception {
		logger.info("analysis: " + analysis);
		logger.info("maxPoints: " + maxPoints);
		logger.info("passedAttributes (" + passedAttributes.size() + ")");
		for (Object key: passedAttributes.keySet()) {
			logger.info("  key: "+key+" value: " + passedAttributes.get(key));
		}
		logger.info("passedParameters (" + passedParameters.size() + ")");
		for (Object key: passedParameters.keySet()) {
			logger.info("  key: "+key+" value: " + passedParameters.get(key));
		}
		
		SQLGrader grader;
		DefaultGrading grading;
		SQLGraderConfig graderConfig;
		SQLCriterionGradingConfig criterionGradingConfig;

		String action;
		String message;
		Object action_Param;

		grader = new SQLGrader();
		grading = new DefaultGrading();
		graderConfig = new SQLGraderConfig();
		action_Param = passedAttributes.get("action");

		if ((action_Param == null) || (!(action_Param instanceof String))){
			message = new String();
			message = message.concat("Stopped grading due to errors. ");
			message = message.concat("Can not utilize evaluation action parameter.");
			
			this.logger.log(Level.SEVERE, message);
			throw new IllegalArgumentException(message);
		}
		
		action = (String)action_Param;

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
		if (!action.toUpperCase().equals(SQLEvaluationAction.RUN.toString())){
			graderConfig.addCriteriaGradingConfig(SQLEvaluationCriterion.CARTESIAN_PRODUCT, criterionGradingConfig);
		}

		//columns
		criterionGradingConfig = new SQLCriterionGradingConfig();
		criterionGradingConfig.setPositivePoints(1);
		criterionGradingConfig.setNegativePoints(0);
		criterionGradingConfig.setPositiveScope(GradingScope.CRITERION);
		criterionGradingConfig.setNegativeScope(GradingScope.CRITERION);
		if (!action.toUpperCase().equals(SQLEvaluationAction.RUN.toString())){
			graderConfig.addCriteriaGradingConfig(SQLEvaluationCriterion.CORRECT_COLUMNS, criterionGradingConfig);
		}
		
		//tuples
		criterionGradingConfig = new SQLCriterionGradingConfig();
		criterionGradingConfig.setPositivePoints(1);
		criterionGradingConfig.setNegativePoints(0);
		criterionGradingConfig.setPositiveScope(GradingScope.CRITERION);
		criterionGradingConfig.setNegativeScope(GradingScope.CRITERION);
		if (!action.toUpperCase().equals(SQLEvaluationAction.RUN.toString())){
			graderConfig.addCriteriaGradingConfig(SQLEvaluationCriterion.CORRECT_TUPLES, criterionGradingConfig);
		}
		
		//ordering
		criterionGradingConfig = new SQLCriterionGradingConfig();
		criterionGradingConfig.setPositivePoints(1);
		criterionGradingConfig.setNegativePoints(0);
		criterionGradingConfig.setPositiveScope(GradingScope.CRITERION);
		criterionGradingConfig.setNegativeScope(GradingScope.CRITERION);
		if (!action.toUpperCase().equals(SQLEvaluationAction.RUN.toString())){
			graderConfig.addCriteriaGradingConfig(SQLEvaluationCriterion.CORRECT_ORDER, criterionGradingConfig);
		}
		
		graderConfig.setMaximumPoints(maxPoints);
		
		//Fetching maximum number of points
		/*
		String query;
		int maxPoints = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;

		//TODO: do not use points defined in task declaration; calculation is done in CoreManagerServlet
		try{
			query = new String();
			query = query.concat("SELECT	points " + LINE_SEP);
			query = query.concat("FROM 		taskdeclaration " + LINE_SEP);
			query = query.concat("WHERE 	task_ID = " + taskID + LINE_SEP);
			
			conn = CoreManager.newConnection();
			stmt = conn.createStatement();
			rset = stmt.executeQuery(query);
			while (rset.next()){
				maxPoints = rset.getInt("points");				
			}
			graderConfig.setMaximumPoints(maxPoints);
		} catch (InternalConfigurationException e){
			message = new String();
			message = message.concat("Stopped analysis due to errors. ");
			message = message.concat("Problems fetching maximum number of points.");
			
			this.logger.log(Level.SEVERE, message);
			throw e;
		} catch (SQLException e){
			message = new String();
			message = message.concat("Stopped analysis due to errors. ");
			message = message.concat("Problems fetching maximum number of points.");
			
			this.logger.log(Level.SEVERE, message);
			throw e;
		} finally {
			SQLException exc = null;
			if (rset != null){
			    try {
			        rset.close();
			    } catch (SQLException e){
					message = new String();
					message = message.concat("Stopped analysis due to errors. ");
					message = message.concat("Problems closing resultset.");
				
					this.logger.log(Level.SEVERE, message);
					exc = e;
				}
			}
			if (stmt != null){
			    try {
			        stmt.close();
			    } catch (SQLException e){
					message = new String();
					message = message.concat("Stopped analysis due to errors. ");
					message = message.concat("Problems closing statement.");
				
					this.logger.log(Level.SEVERE, message);
					exc = e;
				}
			}
			if (conn != null){
			    try {
			        conn.close();
			    } catch (SQLException e){
					message = new String();
					message = message.concat("Stopped analysis due to errors. ");
					message = message.concat("Problems closing connection.");
				
					this.logger.log(Level.SEVERE, message);
					exc = e;
				}
			}
			if (exc != null) {
			    throw exc;
			}		    
		}*/

		return grader.grade((SQLAnalysis)analysis, graderConfig);
	}

	public Report report(Analysis analysis, Grading grading, Map passedAttributes, Map passedParameters, Locale locale) {
		logger.info("analysis: " + analysis);
		logger.info("grading: " + grading);
		logger.info("passedAttributes (" + passedAttributes.size() + ")");
		for (Object key: passedAttributes.keySet()) {
			logger.info("  key: "+key+" value: " + passedAttributes.get(key));
		}
		logger.info("passedParameters (" + passedParameters.size() + ")");
		for (Object key: passedParameters.keySet()) {
			logger.info("  key: "+key+" value: " + passedParameters.get(key));
		}
		SQLReport report = new SQLReport();
		SQLReporter reporter = new SQLReporter(messageSource);
		SQLReporterConfig reporterConfig = new SQLReporterConfig();

		String action;
		String message;
		String diagnoseLevel;
		Object action_Param = passedAttributes.get("action");
		Object diagnoseLevel_Param = passedAttributes.get("diagnoseLevel");

		if ((action_Param == null) || (!(action_Param instanceof String))){
			message = new String();
			message = message.concat("Stopped reporting due to errors. ");
			message = message.concat("Can not utilize evaluation action parameter.");
			
			this.logger.log(Level.SEVERE, message);
			throw new IllegalArgumentException(message);
		}

		if ((diagnoseLevel_Param == null) || (!(diagnoseLevel_Param instanceof String))){
			message = new String();
			message = message.concat("Stopped reporting due to errors. ");
			message = message.concat("Can not utilize diagnose level parameter.");
			
			this.logger.log(Level.SEVERE, message);
			throw new IllegalArgumentException(message);
		}
		
		action = (String)action_Param;
		diagnoseLevel = (String)diagnoseLevel_Param;
		
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
}
