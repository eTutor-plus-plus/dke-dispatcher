package at.jku.dke.etutor.modules.ra2sql;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.jku.dke.etutor.core.evaluation.*;
import at.jku.dke.etutor.modules.ra2sql.model.Expression;
import at.jku.dke.etutor.modules.sql.SQLEvaluationAction;
import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;
import at.jku.dke.etutor.modules.sql.analysis.SQLAnalysis;
import at.jku.dke.etutor.modules.sql.analysis.SQLAnalyzer;
import at.jku.dke.etutor.modules.sql.analysis.SQLAnalyzerConfig;
import at.jku.dke.etutor.modules.sql.analysis.SyntaxAnalysis;
import at.jku.dke.etutor.modules.sql.grading.GradingScope;
import at.jku.dke.etutor.modules.sql.grading.SQLCriterionGradingConfig;
import at.jku.dke.etutor.modules.sql.grading.SQLGrader;
import at.jku.dke.etutor.modules.sql.grading.SQLGraderConfig;
import at.jku.dke.etutor.modules.sql.report.SQLReport;
import at.jku.dke.etutor.modules.sql.report.SQLReporterConfig;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Service;

@Service
public class RAEvaluator implements Evaluator, MessageSourceAware {

	private Logger logger;

	private static String LINE_SEP = System.getProperty("line.separator", "\n");
	
	private static Properties ruleAliasesProps = null;
	private static Properties atomAliasesProps = null;
	private static Properties sqlMsgMappingProps = null;
	private MessageSource messageSource;

	public RAEvaluator() {
		super();
		
		try {
			this.logger = Logger.getLogger("etutor.modules.ra2sql");
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (sqlMsgMappingProps == null) {
			sqlMsgMappingProps = new Properties();
			try {
				sqlMsgMappingProps.load(this.getClass().getResourceAsStream(RAConstants.SQL_MSG_MAPPING_PATH));
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (ruleAliasesProps == null) {
			ruleAliasesProps = new Properties();
			try {
				ruleAliasesProps.load(this.getClass().getResourceAsStream(RAConstants.RULE_ALIASES_PATH));
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (atomAliasesProps == null) {
			atomAliasesProps = new Properties();
			try {
				atomAliasesProps.load(this.getClass().getResourceAsStream(RAConstants.ATOM_ALIASES_PATH));
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Analysis analyze(int exerciseID, int userID, Map passedAttributes, Map passedParameters, Locale locale) throws Exception{
		String message;
		String submQuery;
		SQLAnalysis analysis;
		SQLBuilder sqlBuilder;
		SyntaxAnalysis syntaxAnalysis;
		SQLAnalyzerConfig analyzerConfig;


		String action;
		String submission;
		int diagnoseLevel;
		Object action_Param;
		Object submission_Param;
		Object diagnoseLevel_Param;

		sqlBuilder = new SQLBuilder();
		analysis = new SQLAnalysis();
		syntaxAnalysis = new SyntaxAnalysis();
		analyzerConfig = new SQLAnalyzerConfig();
		
		action_Param = passedAttributes.get("action");
		submission_Param = passedAttributes.get("submission");
		diagnoseLevel_Param = passedAttributes.get("diagnoseLevel");
		
		//VALIDATING PARAMETERS
		if ((diagnoseLevel_Param==null) || (((String)diagnoseLevel_Param).length()==0)) {
			diagnoseLevel_Param = "0";		
		}

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
		
		//SETTING SUBMISSION IN ANALYSIS
		analysis.setSubmission(new String((String)submission_Param));

		//PREPARING SUBMISSION - REPLACING RA-SPECIFIC SYMBOLS WITH TEXTUAL REPRESENTATIONS
		submission = prepareSubmission((String)submission_Param);

		RALexer lexer;
		RAParser parser;
		String exceptionText;
		Expression expression;
		StringReader raReader;

		raReader = null;
		expression = null;
		
		//ANALYZING SYNTAX - PARSING SUBMITTED RA QUERY TO JAVA MODEL
		try {
			raReader = new StringReader(submission);
			lexer = new RALexer(raReader);
			parser = new RAParser(lexer);
			expression = parser.parse();
		} catch (UnexpectedAtomException e) {
			exceptionText = new String();
			exceptionText = exceptionText.concat("<p class='ra_syntax_error'>");
			exceptionText = exceptionText.concat("Invalid \"" + this.getRuleAlias(e.getRule()));
			exceptionText = exceptionText.concat("\".<br>");
			exceptionText = exceptionText.concat("Unexpected character \"");
			exceptionText = exceptionText.concat(this.getAtomAlias(e.getUnexpectedAtom()));
			exceptionText = exceptionText.concat("\".</p>");
			exceptionText = exceptionText.concat(this.highlightError(submission, e));

			syntaxAnalysis.setFoundSyntaxError(true);
			syntaxAnalysis.setCriterionIsSatisfied(false);
			syntaxAnalysis.setSyntaxErrorDescription(exceptionText);
			analysis.addCriterionAnalysis(SQLEvaluationCriterion.CORRECT_SYNTAX, syntaxAnalysis);
			return analysis;
		} catch (MismatchedAtomException e) {
			exceptionText = new String();
			exceptionText = exceptionText.concat("<p class='ra_syntax_error'>");
			exceptionText = exceptionText.concat("Invalid \"" + this.getRuleAlias(e.getRule()) + "\".\n");
			exceptionText =	exceptionText.concat("Expected character \"");
			exceptionText = exceptionText.concat(this.getAtomAlias(e.getExpectedAtom()) + "\".<br>");
			exceptionText =	exceptionText.concat("Found character    \"");
			exceptionText = exceptionText.concat(this.getAtomAlias(e.getFoundAtom()) + "\".</p>");
			exceptionText = exceptionText.concat(this.highlightError(submission, e));

			syntaxAnalysis.setFoundSyntaxError(true);
			syntaxAnalysis.setCriterionIsSatisfied(false);
			syntaxAnalysis.setSyntaxErrorDescription(exceptionText);
			analysis.addCriterionAnalysis(SQLEvaluationCriterion.CORRECT_SYNTAX, syntaxAnalysis);
			return analysis;
		} catch (InvalidAtomException e) {
			exceptionText = new String();
			exceptionText = exceptionText.concat("<p class='ra_syntax_error'>");
			exceptionText = exceptionText.concat("Invalid \"" + this.getRuleAlias(e.getRule()) + "\".\n");
			exceptionText =	exceptionText.concat("Invalid character \"");
			exceptionText = exceptionText.concat(this.getAtomAlias(e.getInvalidAtom()) + "\".</p>");
			exceptionText = exceptionText.concat(this.highlightError(submission, e));

			syntaxAnalysis.setFoundSyntaxError(true);
			syntaxAnalysis.setCriterionIsSatisfied(false);
			syntaxAnalysis.setSyntaxErrorDescription(exceptionText);
			analysis.addCriterionAnalysis(SQLEvaluationCriterion.CORRECT_SYNTAX, syntaxAnalysis);
			return analysis;
		} catch (AtomStreamIOException e) {
		} finally {
			raReader.close();
		}
		
		if (action.toUpperCase().equals(SQLEvaluationAction.RUN.toString())) {
			analyzerConfig.addCriterionToAnalyze(SQLEvaluationCriterion.CORRECT_SYNTAX);
			analyzerConfig.setDiagnoseLevel(1);
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

		String query;
		Statement stmt = null;
		ResultSet rset = null;
		Connection conn = null;
		Connection referenceConn = null;
		
		String correctQuery = null;
		String referenceConnPwd = null;
		String referenceConnUser = null;
		String referenceConnString = null;

		//ANALYZING SEMANTICS
		try{
			//ESTABLISHING CONNECTION TO RA DATABASE
			java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(RAConstants.CONN_URL, RAConstants.CONN_USER, RAConstants.CONN_PWD);
			conn.setAutoCommit(true);

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
				referenceConnPwd = rset.getString("conn_pwd");				
				referenceConnUser = rset.getString("conn_user");				
				referenceConnString = rset.getString("conn_string");				
			}
			
			//ESTABLISHING CONNECTION TO EXERCISE SPECIFIC REFERENCE DATABASE
			java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			this.logger.log(Level.INFO,referenceConnString + " - " + referenceConnUser + " - " + referenceConnPwd);
			
			referenceConn = DriverManager.getConnection("jdbc:oracle:thin:@" + referenceConnString, referenceConnUser, referenceConnPwd);
			referenceConn.setAutoCommit(true);
			
			//BUILDING SQL QUERY FROM SUBMITTED RA QUERY
			sqlBuilder = new SQLBuilder();
			submQuery = sqlBuilder.buildSQLQuery(expression, referenceConn);
			this.logger.log(Level.INFO,"BUILT QUERY: " + submQuery);

			//DETERMINING CORRECT RA QUERY IN TERMS OF SQL
			if ((action.equalsIgnoreCase("test")) || (action.equalsIgnoreCase("run"))){
				correctQuery = submQuery;
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
			this.logger.log(Level.INFO, "CORRECT QUERY: " + correctQuery);

			//ANALYZING SUBMITTED QUERY IN TERMS OF SQL
			analyzerConfig.setConnection(referenceConn);
			analyzerConfig.setCorrectQuery(correctQuery.replaceAll(";",""));

			SQLAnalysis sqlAnalysis;
			SQLAnalyzer analyzer = new SQLAnalyzer();
		
			sqlAnalysis = analyzer.analyze(submQuery, analyzerConfig);
			sqlAnalysis.setSubmission(analysis.getSubmission());
		
			this.logger.log(Level.INFO,"Finished RA-Evaluation!");
			return sqlAnalysis;

		} catch (IllegalArgumentException e){
			syntaxAnalysis.setFoundSyntaxError(true);
			syntaxAnalysis.setCriterionIsSatisfied(false);
			syntaxAnalysis.setSyntaxErrorDescription(e.getMessage());
			analysis.addCriterionAnalysis(SQLEvaluationCriterion.CORRECT_SYNTAX, syntaxAnalysis);
			return analysis;
		} catch (NullPointerException e){
			this.logger.log(Level.SEVERE, "", e);
			throw e;
		} catch (SQLException e){
			this.logger.log(Level.SEVERE, "", e);
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
				if (referenceConn != null){
					referenceConn.close();
				}
			} catch (SQLException e){
				this.logger.log(Level.SEVERE, "", e);
				throw e;
			}
		}
	}
	
	public Grading grade(Analysis analysis, int maxPoints, Map passedAttributes, Map passedParameters) throws Exception{
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
		criterionGradingConfig =	new SQLCriterionGradingConfig();
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
		
		try{
			query = new String();
			query = query.concat("SELECT	points ");
			query = query.concat("FROM 		taskdeclaration ");
			query = query.concat("WHERE 	task_ID = " + taskID);
			
			conn = CoreManager.newConnection();
			stmt = conn.createStatement();
			stmt.execute("ALTER SESSION SET NLS_LANGUAGE = AMERICAN");
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
	
	public Report report(Analysis analysis, Grading grading, Map passedAttributes, Map passedParameters, Locale locale) throws Exception{
		SQLReport report = new SQLReport();
		RAReporter reporter = new RAReporter(messageSource);
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

	private String getRuleAlias(String ruleName) {
		return ruleAliasesProps.getProperty(ruleName, ruleName);
	}

	private String getAtomAlias(String atom) {
		return atomAliasesProps.getProperty(atom, atom);
	}

	private String printSQLError(SQLException e) {
		return sqlMsgMappingProps.getProperty(
			Integer.toString(e.getErrorCode()),
			"Sorry, your relational algebra expression is incorect!");
	}

	public static String prepareSubmission(String submission) {
		char[] c1 = {247}; //division
		char[] c2 = {215}; //cartesian

		String division = new String(c1);
		String cartesian = new String(c2);

	
		submission = submission.replaceAll("\\(", " LEFTPARENTHESES ");
		submission = submission.replaceAll("\\)", " RIGHTPARENTHESES ");
		submission = submission.replaceAll("\\[", " LEFTBRACKET ");
		submission = submission.replaceAll("\\]", " RIGHTBRACKET ");
		submission = submission.replaceAll(",", " COMMA ");
		submission = submission.replaceAll(">", " GREATERTHAN ");
		submission = submission.replaceAll("<", " LESSTHAN ");
		submission = submission.replaceAll("=", " EQUAL ");
		submission = submission.replaceAll("\\.", " DOT ");
		submission = submission.replaceAll("'", " APOSTROPHE ");

		submission = submission.replaceAll("&#960;", " PROJECTION ");
		char[] projection = {960};
		submission = submission.replaceAll(new String(projection), " PROJECTION ");
		submission = submission.replaceAll("&#963;", " SELECTION ");
		char[] selection = {963};
		submission = submission.replaceAll(new String(selection), " SELECTION ");
		submission = submission.replaceAll("&#961;", " RENAMING ");
		char[] renaming = {961};
		submission = submission.replaceAll(new String(renaming), " RENAMING ");
		submission = submission.replaceAll("&#8904;", " JOIN ");
		char[] join = {8904};
		submission = submission.replaceAll(new String(join), " JOIN ");
		submission = submission.replaceAll("&#8906;", " RIGHTSEMI ");
		char[] rightsemi = {8906};
		submission = submission.replaceAll(new String(rightsemi), " RIGHTSEMI ");
		submission = submission.replaceAll("&#8905;", " LEFTSEMI ");
		char[] leftsemi = {8905};
		submission = submission.replaceAll(new String(leftsemi), " LEFTSEMI ");
		submission = submission.replaceAll(cartesian, " CARTESIANPRODUCT ");
		submission = submission.replaceAll("&#8592;", " LEFTARROW ");
		char[] leftarrow = {8592};
		submission = submission.replaceAll(new String(leftarrow), " LEFTARROW ");
		submission = submission.replaceAll("&#8746;", " UNION ");
		char[] union = {8746};
		submission = submission.replaceAll(new String(union), " UNION ");
		submission = submission.replaceAll("&#8745;", " INTERSECTION ");
		char[] intersection = {8745};
		submission = submission.replaceAll(new String(intersection), " INTERSECTION ");
		submission = submission.replaceAll("&#8722;", " MINUS ");
		char[] minus = {8722};
		submission = submission.replaceAll(new String(minus), " MINUS ");
		submission = submission.replaceAll(division, " DIVISION ");
		submission = submission.replaceAll("&#8847;", " LEFTCURLY ");
		char[] leftcurly = {8847};
		submission = submission.replaceAll(new String(leftcurly), " LEFTCURLY ");
		submission = submission.replaceAll("\\{", " LEFTCURLY ");
		submission = submission.replaceAll("&#8848;", " RIGHTCURLY ");
		char[] rightcurly = {8848};
		submission = submission.replaceAll(new String(rightcurly), " RIGHTCURLY ");
		submission = submission.replaceAll("\\}", " RIGHTCURLY ");
		
		return submission;
	}

	private String highlightError(String raQuery, RAParserException ex) {
		String result = new String();
		String[] lines;
		String line;
		String preError;
		String postError;

		if ((raQuery != null) && (ex != null)) {
			result = result.concat("<p class='ra_syntax_error'>");
			lines = raQuery.split("\n");

			for (int i = 0; i < lines.length; i++) {
				if (i == ex.getLine() - 1) {
					line = lines[i];
					preError = line.substring(0, ex.getColumn() - 1);
					postError = line.substring(ex.getColumn() - 1, line.length());

					result = result.concat(preError + "<u>" + postError + "\n");
				} else {
					result = result.concat(lines[i] + "\n");
				}
			}
		}

		result = result.replaceAll("PROJECTION", "&#960;");
		result = result.replaceAll("SELECTION", "&#963;");
		result = result.replaceAll("RENAMING", "&#961;");
		result = result.replaceAll("JOIN", "&#8904;");
		result = result.replaceAll("RIGHTSEMI", "&#8906;");
		result = result.replaceAll("LEFTSEMI", "&#8905;");
		result = result.replaceAll("CARTESIANPRODUCT", "&#215;");
		result = result.replaceAll("LEFTARROW", "&#8592;");
		result = result.replaceAll("UNION", "&#8746;");
		result = result.replaceAll("INTERSECTION", "&#8745;");
		result = result.replaceAll("MINUS", "&#8722;");
		result = result.replaceAll("DIVISION", "&#247;");
		result = result.replaceAll("LEFTCURLY", "&#8847;");
		result = result.replaceAll("RIGHTCURLY", "&#8848;");


		result = result.replaceAll("LEFTPARENTHESES", "(");
		result = result.replaceAll("RIGHTPARENTHESES", ")");
		result = result.replaceAll("LEFTBRACKET", "[");
		result = result.replaceAll("RIGHTBRACKET", "]");
		result = result.replaceAll("COMMA", ",");
		result = result.replaceAll("GREATERTHAN", ">");
		result = result.replaceAll("LESSTHAN", "<");
		result = result.replaceAll("EQUAL", "=");
		result = result.replaceAll("DOT", "\\.");
		result = result.replaceAll("APOSTROPHE","'");

		result = result.replaceAll("\n", "<br>");
		result = result.concat("</u></p>");

		return result;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public String generateHTMLResult(Analysis analysis, Map<String, String> passedAttributes, Locale locale) {
		return null;
	}
}
