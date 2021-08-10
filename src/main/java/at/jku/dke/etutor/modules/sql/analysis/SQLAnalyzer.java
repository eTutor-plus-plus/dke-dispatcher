package at.jku.dke.etutor.modules.sql.analysis;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;

/**
 * The Analyzer that takes an SQLAnalyzerConfig configuration and performs the analysis according to it
 */
public class SQLAnalyzer {
	private final String INTERNAL_ERROR = "This is an internal system error.";
	private final String CONTACT_ADMIN = "Please contact the system administrator.";

	private Logger logger;

	public SQLAnalyzer() {
		super();

		try {
			this.logger = Logger.getLogger("at.jku.dke.etutor.modules.sql");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Analyzes the submission according to the configuration and returns the SQLAnalysis containing the analyzed
	 * SQLEvaluationCriterion´s.
	 * Calls isCriterionToAnalyze() from the config for every EvaluationCriterion and analyzes the submisison if needed
	 * @param submission the submission to be analyzed
	 * @param config the configuration
	 * @return the SQLAnalysis
	 */
	public SQLAnalysis analyze(Serializable submission, SQLAnalyzerConfig config) {
		String message;
		SQLAnalysis analysis;
		String submittedQuery;
		SQLCriterionAnalysis criterionAnalysis;

		analysis = new SQLAnalysis();
		analysis.setSubmission(submission);

		if (submission == null) {
			message ="";
			message = message.concat("Analsis stopped with errors. ");
			message = message.concat("Submission is empty. ");
			message = message.concat(INTERNAL_ERROR);
			message = message.concat(CONTACT_ADMIN);

			this.logger.log(Level.SEVERE, message);
			analysis.setAnalysisException(new AnalysisException(message));
			return analysis;
		}

		if (submission instanceof String) {
			submittedQuery = (String)submission;
		} else {
			message = "";
			message = message.concat("Analysis stopped with errors. ");
			message = message.concat("Submission is not utilizable. ");
			message = message.concat(INTERNAL_ERROR);
			message = message.concat(CONTACT_ADMIN);

			this.logger.log(Level.SEVERE, message);
			analysis.setAnalysisException(new AnalysisException(message));
			return analysis;
		}

		if (config == null) {
			message = "";
			message = message.concat("Analysis stopped with errors. ");
			message = message.concat("No configuration found. ");
			message = message.concat(INTERNAL_ERROR);
			message = message.concat(CONTACT_ADMIN);

			this.logger.log(Level.SEVERE, message);
			analysis.setAnalysisException(new AnalysisException(message));
			return analysis;
		}

		if (config.isCriterionToAnalyze(SQLEvaluationCriterion.CORRECT_SYNTAX)) {
			criterionAnalysis = this.analyzeSyntax(config, submittedQuery, analysis);
			analysis.addCriterionAnalysis(SQLEvaluationCriterion.CORRECT_SYNTAX, criterionAnalysis);
			if ((criterionAnalysis.getAnalysisException() != null) || (!criterionAnalysis.isCriterionSatisfied())) {
				analysis.setAnalysisException(criterionAnalysis.getAnalysisException());
				return analysis;
			}
		}

		if (config.isCriterionToAnalyze(SQLEvaluationCriterion.CARTESIAN_PRODUCT)) {
			criterionAnalysis = this.analyzeCartesianProduct(config, submittedQuery);
			analysis.addCriterionAnalysis(SQLEvaluationCriterion.CARTESIAN_PRODUCT, criterionAnalysis);
			if ((criterionAnalysis.getAnalysisException() != null) || (!criterionAnalysis.isCriterionSatisfied())) {
				analysis.setAnalysisException(criterionAnalysis.getAnalysisException());
				return analysis;
			}
		}

		if (config.isCriterionToAnalyze(SQLEvaluationCriterion.CORRECT_COLUMNS)) {
			criterionAnalysis = this.analyzeColumns(config, submittedQuery);
			analysis.addCriterionAnalysis(SQLEvaluationCriterion.CORRECT_COLUMNS, criterionAnalysis);
			if ((criterionAnalysis.getAnalysisException() != null) || (!criterionAnalysis.isCriterionSatisfied())) {
				analysis.setAnalysisException(criterionAnalysis.getAnalysisException());
				return analysis;
			}
		}

		if (config.isCriterionToAnalyze(SQLEvaluationCriterion.CORRECT_TUPLES)) {
			criterionAnalysis = this.analyzeTuples(config, submittedQuery);
			analysis.addCriterionAnalysis(SQLEvaluationCriterion.CORRECT_TUPLES, criterionAnalysis);
			if ((criterionAnalysis.getAnalysisException() != null) || (!criterionAnalysis.isCriterionSatisfied())) {
				analysis.setAnalysisException(criterionAnalysis.getAnalysisException());
				return analysis;
			}
		}

		if (config.isCriterionToAnalyze(SQLEvaluationCriterion.CORRECT_ORDER)) {
			criterionAnalysis = this.analyzeOrder(config, submittedQuery);
			analysis.addCriterionAnalysis(SQLEvaluationCriterion.CORRECT_ORDER, criterionAnalysis);
			if ((criterionAnalysis.getAnalysisException() != null) || (!criterionAnalysis.isCriterionSatisfied())) {
				analysis.setAnalysisException(criterionAnalysis.getAnalysisException());
				return analysis;
			}
		}

		return analysis;
	}

	/**
	 * Analyzes the correctness of the submission´s syntax by trying to execute the it.
	 * Adds the result columns and tuples to the analysis
	 * @param config the configuration
	 * @param submittedQuery the submission
	 * @param analysis the analysis
	 * @return the SQLCriterionAnalysis for the SQLEvaluationCriterion.CORRECT_SYNTAX
	 */
	private SQLCriterionAnalysis analyzeSyntax(SQLAnalyzerConfig config, String submittedQuery, SQLAnalysis analysis) {
		List<String> tuple;
		ResultSetMetaData rsmd;
		SyntaxAnalysis syntaxAnalysis;

		syntaxAnalysis = new SyntaxAnalysis();

		this.logger.log(Level.INFO, "Analyzing syntax");

		try (Statement stmt = config.getConnection().createStatement();
		ResultSet rset = stmt.executeQuery(submittedQuery)){
			rsmd = rset.getMetaData();
			for (int i=1; i<=rsmd.getColumnCount(); i++){
				analysis.addQueryResultColumnLabel(rsmd.getColumnLabel(i));
			}

			while (rset.next()){
				tuple = new ArrayList<>();
				for (int i=1; i<=rsmd.getColumnCount(); i++){
					tuple.add(rset.getString(i));
				}
				analysis.addQueryResultTuple(tuple);				
			}
			this.logger.log(Level.INFO, "Finished analyzing syntax");			
		} catch (SQLException e) {
			syntaxAnalysis.setFoundSyntaxError(true);
			syntaxAnalysis.setCriterionIsSatisfied(false);
			syntaxAnalysis.setSyntaxErrorDescription(e.toString());
		}

		return syntaxAnalysis;
	}

	/**
	 * Analyzes the submission according to the SQlEvaluationCriterion.CORRECT_ORDER,
	 * if the solution-query for the exercise uses an ORDER BY-statement,
	 * by iterating over the tuples and comparing the columns of the solution´s result set
	 * with the columns of the submission´s result set.
	 * @param config the configuration
	 * @param submittedQuery the submission
	 * @return the analysis
	 */
	private OrderingAnalysis analyzeOrder(SQLAnalyzerConfig config, String submittedQuery) {
		String message;
		int columnsCount;
		OrderingAnalysis orderingAnalysis;

		ResultSetMetaData correctQuery_RSMD;

		orderingAnalysis = new OrderingAnalysis();

		this.logger.log(Level.INFO, "Checking order of query.");

		if (this.usesOrderByStatement(config.getCorrectQuery())) {
			try (Statement correctQuery_STMT = config.getConnection().createStatement();
				Statement submittedQuery_STMT = config.getConnection().createStatement();
				ResultSet submittedQuery_RSET = submittedQuery_STMT.executeQuery(submittedQuery);
				ResultSet correctQuery_RSET = correctQuery_STMT.executeQuery(config.getCorrectQuery())){

				correctQuery_RSMD = correctQuery_RSET.getMetaData();
				columnsCount = correctQuery_RSMD.getColumnCount();

				while (correctQuery_RSET.next()) {
					submittedQuery_RSET.next();
					for (int i = 1; i <= columnsCount; i++) {
						if (correctQuery_RSET.getString(i) != null) {
							if (!correctQuery_RSET
								.getString(i)
								.equals(submittedQuery_RSET.getString(correctQuery_RSMD.getColumnName(i)))) {
								orderingAnalysis.setFoundIncorrectOrdering(true);
								orderingAnalysis.setCriterionIsSatisfied(false);
							}
						} else {
							if (!(correctQuery_RSET.getString(i) == null
								&& submittedQuery_RSET.getString(correctQuery_RSMD.getColumnName(i)) == null)) {
								orderingAnalysis.setFoundIncorrectOrdering(true);
								orderingAnalysis.setCriterionIsSatisfied(false);
							}
						}
					}
				}

				this.logger.log(Level.INFO, "Finished checking order of query.");
				
			} catch (SQLException e) {
				message = "";
				message = message.concat("Error occured while analyzing order. ");
				message = message.concat(INTERNAL_ERROR);
				message = message.concat(CONTACT_ADMIN);

				this.logger.log(Level.SEVERE, message, e);
				orderingAnalysis.setCriterionIsSatisfied(false);
				orderingAnalysis.setAnalysisException(new AnalysisException(message, e));
			}
		} else {
			orderingAnalysis.setFoundIncorrectOrdering(false);
			orderingAnalysis.setCriterionIsSatisfied(true);
		}

		return orderingAnalysis;
	}

	/**
	 * Returns whether a given query contains an ORDER BY-statement
	 * @param query the query
	 * @return a boolean indicating if an order-by statement has been found
	 */
	private boolean usesOrderByStatement(String query) {
		int lastOrderIndex;
		int afterOrderIndex;
		String normalizedQuery;

		normalizedQuery = query.toLowerCase().replace(" ", "");
		lastOrderIndex = normalizedQuery.lastIndexOf("orderby");
		afterOrderIndex = normalizedQuery.lastIndexOf("select", lastOrderIndex);

		if (afterOrderIndex == -1) {
			afterOrderIndex = normalizedQuery.lastIndexOf("from", lastOrderIndex);
		}
		if (afterOrderIndex == -1) {
			afterOrderIndex = normalizedQuery.lastIndexOf("where", lastOrderIndex);
		}
		if (afterOrderIndex == -1) {
			afterOrderIndex = normalizedQuery.lastIndexOf("groupby", lastOrderIndex);
		}
		if (afterOrderIndex == -1) {
			afterOrderIndex = normalizedQuery.lastIndexOf("having", lastOrderIndex);
		}
		return (lastOrderIndex > afterOrderIndex);
	}

	/**
	 * Analyzes the submission according to the SQlEvaluationCriterion_CORRECT_TUPLES
	 * @param config the configuration
	 * @param submittedQuery the submitted query
	 * @return the TuplesAnalysis
	 */
	private TuplesAnalysis analyzeTuples(SQLAnalyzerConfig config, String submittedQuery) {
		int columnCount;

		ResultSetMetaData rsmd;
		ResultSet checkQuery_RSET;

		List<String> tuple;
		String columns;
		String message;
		String checkQuery;
		List<Collection<String>> correctQueryTuples;
		List<Collection<String>> submittedQueryTuples;
		TuplesAnalysis tuplesAnalysis;

		checkQuery_RSET = null;

		correctQueryTuples = new ArrayList<>();
		submittedQueryTuples = new ArrayList<>();
		tuplesAnalysis = new TuplesAnalysis();

		this.logger.log(Level.INFO, "Checking tuples of query.");

		try (Statement correctQuery_STMT = config.getConnection().createStatement();
             ResultSet correctQuery_RSET = correctQuery_STMT.executeQuery(config.getCorrectQuery());
             Statement submittedQuery_STMT = config.getConnection().createStatement()
		){
			rsmd = correctQuery_RSET.getMetaData();

			columns = "";
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				tuplesAnalysis.addColumnLabel(rsmd.getColumnName(i));
				if (columns.length() > 0) {
					columns = columns.concat(", ");
				}
				columns = columns.concat(rsmd.getColumnName(i));
			}

			this.logger.log(Level.INFO, "COLUMN LABELS: {0}", columns);
			
			checkQuery = "";
			checkQuery = checkQuery.concat("SELECT " + columns + " ");
			checkQuery = checkQuery.concat("FROM (" + config.getCorrectQuery() + ") AS correctQuery ");
			checkQuery = checkQuery.concat("ORDER BY " + columns);
			
			this.logger.log(Level.INFO, "CORRECT QUERY:\n {0}", checkQuery);

			checkQuery_RSET = correctQuery_STMT.executeQuery(checkQuery);

			rsmd = checkQuery_RSET.getMetaData();
			columnCount = rsmd.getColumnCount();

			addTuplesToList(columnCount, checkQuery_RSET, correctQueryTuples);

			checkQuery = "";
			checkQuery = checkQuery.concat("SELECT " + columns + " ");
			checkQuery = checkQuery.concat("FROM (" + submittedQuery + ") AS submittedQuery ");
			checkQuery = checkQuery.concat("ORDER BY " + columns);

			this.logger.log(Level.INFO, "SUBMITTED QUERY:\n {0}", checkQuery);

			checkQuery_RSET = submittedQuery_STMT.executeQuery(checkQuery);

			addTuplesToList(columnCount, checkQuery_RSET, submittedQueryTuples);

			tuplesAnalysis.setMissingTuples(correctQueryTuples);
			tuplesAnalysis.removeAllMissingTuples(submittedQueryTuples);

			tuplesAnalysis.setSurplusTuples(submittedQueryTuples);
			tuplesAnalysis.removeAllSurplusTuples(correctQueryTuples);

			tuplesAnalysis.setCriterionIsSatisfied((tuplesAnalysis.getSurplusTuples().isEmpty()) && (tuplesAnalysis.getMissingTuples().isEmpty()));

			this.logger.log(Level.INFO, "Finished checking tuples of query.");

		} catch (SQLException e) {
			message = "";
			message = message.concat("Error encounted while analyzing tuples. ");
			message = message.concat(INTERNAL_ERROR);
			message = message.concat(CONTACT_ADMIN);

			this.logger.log(Level.SEVERE, message, e);
			tuplesAnalysis.setAnalysisException(new AnalysisException(message, e));
		} finally {
			try {
				if (checkQuery_RSET != null) {
					checkQuery_RSET.close();
				}
			} catch (SQLException e) {
				this.logger.log(Level.SEVERE, "Could not close result set.", e);
			}
		}

		return tuplesAnalysis;
	}

	private void addTuplesToList(int columnCount, ResultSet checkQuery_RSET, List<Collection<String>> correctQueryTuples) throws SQLException {
		List<String> tuple;
		while (checkQuery_RSET.next()) {
			tuple = new ArrayList<>();

			for (int i = 1; i <= columnCount; i++) {
				if (checkQuery_RSET.getString(i) != null) {
					tuple.add(checkQuery_RSET.getString(i).toUpperCase());
				} else {
					tuple.add("null");
				}
			}
			correctQueryTuples.add(tuple);
		}
	}

	/**
	 * Analyzes the columns of the submitted query
	 * @param config the configuration
	 * @param submittedQuery the submitted query
	 * @return the analysis
	 */
	private ColumnsAnalysis analyzeColumns(SQLAnalyzerConfig config, String submittedQuery) {
		String message;


		List<String> correctQueryColumns;
		List<String> submittedQueryColumns;

		int correctQueryColumnCount;
		int submittedQueryColumnCount;

		ColumnsAnalysis columnsAnalysis;

		ResultSetMetaData correctQueryMetaData;
		ResultSetMetaData submittedQueryMetaData;

		columnsAnalysis = new ColumnsAnalysis();

		this.logger.log(Level.INFO, "Checking columns of query.");

		try (Statement correctQuery_STMT = config.getConnection().createStatement();
             ResultSet correctQuery_RSET = correctQuery_STMT.executeQuery(config.getCorrectQuery());
             Statement submittedQuery_STMT = config.getConnection().createStatement();
             ResultSet submittedQuery_RSET = submittedQuery_STMT.executeQuery(submittedQuery)
		){

			correctQueryMetaData = correctQuery_RSET.getMetaData();

			submittedQueryMetaData = submittedQuery_RSET.getMetaData();

			correctQueryColumnCount = correctQueryMetaData.getColumnCount();
			submittedQueryColumnCount = submittedQueryMetaData.getColumnCount();

			correctQueryColumns = new ArrayList<>(correctQueryColumnCount);
			submittedQueryColumns = new ArrayList<>(submittedQueryColumnCount);

			for (int i = 1; i <= submittedQueryColumnCount; i++) {
				submittedQueryColumns.add(submittedQueryMetaData.getColumnName(i));
			}

			for (int i = 1; i <= correctQueryColumnCount; i++) {
				correctQueryColumns.add(correctQueryMetaData.getColumnName(i));
			}

			for (int i = 0; i < correctQueryColumnCount; i++) {
				if (!submittedQueryColumns.contains(correctQueryColumns.get(i))) {
					columnsAnalysis.addMissingColumn(correctQueryColumns.get(i));
				}
			}

			for (int i = 0; i < submittedQueryColumnCount; i++) {
				if (!correctQueryColumns.contains(submittedQueryColumns.get(i))) {
					columnsAnalysis.addSurplusColumn(submittedQueryColumns.get(i));
				}
			}

			columnsAnalysis.setCriterionIsSatisfied((columnsAnalysis.getSurplusColumns().isEmpty()) && (columnsAnalysis.getMissingColumns().isEmpty()));

			this.logger.log(Level.INFO, "Finished checking columns of query.");

		} catch (SQLException e) {
			message = "";
			message = message.concat("Error occured while analyzing columns. ");
			message = message.concat(INTERNAL_ERROR);
			message = message.concat(CONTACT_ADMIN);

			this.logger.log(Level.SEVERE, message, e);
			columnsAnalysis.setAnalysisException(new AnalysisException(message, e));
		}

		return columnsAnalysis;
	}

	/**
	 * Analyzes if the submitted query calculates a cartesian product
	 * @param config the configuration
	 * @param submittedQuery the submission
	 * @return the analysis representing the SQLEvaluationCriterion.CARTESIAN_PRODUCT
	 */
	private CartesianProductAnalysis analyzeCartesianProduct(SQLAnalyzerConfig config, String submittedQuery) {
		String message;
		int countCorrectQuery;
		int countSubmittedQuery;
		CartesianProductAnalysis cartesianProductAnalysis;

		countCorrectQuery = -1;
		countSubmittedQuery = -1;

		cartesianProductAnalysis = new CartesianProductAnalysis();

		this.logger.log(Level.INFO, "Checking for cartesian product.");

		String querySub = "SELECT COUNT(*) FROM (" + submittedQuery + ") AS submittedQuery";
		String queryCorr =  "SELECT COUNT(*) FROM (" + config.getCorrectQuery() + ") AS correctQuery";
		try (Statement submittedQuery_STMT= config.getConnection().createStatement();
			ResultSet submittedQuery_RSET= submittedQuery_STMT.executeQuery(querySub);
			Statement correctQuery_STMT = config.getConnection().createStatement();
			ResultSet correctQuery_RSET = correctQuery_STMT.executeQuery(queryCorr)){
			if (submittedQuery_RSET.next()) {
				countSubmittedQuery = submittedQuery_RSET.getInt(1);
			}

			if (correctQuery_RSET.next()) {
				countCorrectQuery = correctQuery_RSET.getInt(1);
			}

			if (countSubmittedQuery > (4 * countCorrectQuery + 50)) {
				cartesianProductAnalysis.setCartesianProductSuspected(true);
				cartesianProductAnalysis.setCriterionIsSatisfied(false);
			}

			this.logger.log(Level.INFO, "Finsihed checking for cartesian product.");

		} catch (SQLException e) {
			message = "";
			message = message.concat("Error occured while analyzing cartesian product. ");
			message = message.concat(INTERNAL_ERROR);
			message = message.concat(CONTACT_ADMIN);

			this.logger.log(Level.SEVERE, message, e);
			cartesianProductAnalysis.setAnalysisException(new AnalysisException(message, e));
		}
		return cartesianProductAnalysis;

	}
}
