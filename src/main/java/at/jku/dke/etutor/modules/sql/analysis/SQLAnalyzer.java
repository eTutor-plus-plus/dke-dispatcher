package at.jku.dke.etutor.modules.sql.analysis;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;

public class SQLAnalyzer {

	private Logger logger;

	public SQLAnalyzer() {
		super();

		try {
			this.logger = Logger.getLogger("at.jku.dke.etutor.modules.sql");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
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
			message = message.concat("This is an internal system error. ");
			message = message.concat("Please inform the system administrator.");

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
			message = message.concat("This is an internal system error. ");
			message = message.concat("Please inform the system administrator.");

			this.logger.log(Level.SEVERE, message);
			analysis.setAnalysisException(new AnalysisException(message));
			return analysis;
		}

		if (config == null) {
			message = "";
			message = message.concat("Analysis stopped with errors. ");
			message = message.concat("No configuration found. ");
			message = message.concat("This is an internal system error. ");
			message = message.concat("Please inform the system administrator");

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
	

	private SQLCriterionAnalysis analyzeSyntax(SQLAnalyzerConfig config, String submittedQuery, SQLAnalysis analysis) {
		Vector<String> tuple;
		ResultSet rset;
		Statement stmt;
		ResultSetMetaData rsmd;
		SyntaxAnalysis syntaxAnalysis;

		rset = null;
		stmt = null;
		syntaxAnalysis = new SyntaxAnalysis();

		this.logger.log(Level.INFO, "Analyzing syntax");

		try {
			stmt = config.getConnection().createStatement();
			rset = stmt.executeQuery(submittedQuery);
			rsmd = rset.getMetaData();
			for (int i=1; i<=rsmd.getColumnCount(); i++){
				analysis.addQueryResultColumnLabel(rsmd.getColumnLabel(i));
			}

			while (rset.next()){
				tuple = new Vector<>();
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
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				this.logger.log(Level.SEVERE, "Could not close result set.", e);
			}
		}

		return syntaxAnalysis;
	}

	private OrderingAnalysis analyzeOrder(SQLAnalyzerConfig config, String submittedQuery) {
		String message;
		int columnsCount;
		OrderingAnalysis orderingAnalysis;

		ResultSet correctQuery_RSET;
		Statement correctQuery_STMT;
		ResultSet submittedQuery_RSET;
		Statement submittedQuery_STMT;
		ResultSetMetaData correctQuery_RSMD;

		correctQuery_RSET = null;
		correctQuery_STMT = null;
		submittedQuery_RSET = null;
		submittedQuery_STMT = null;
		orderingAnalysis = new OrderingAnalysis();

		this.logger.log(Level.INFO, "Checking order of query.");

		if (this.usesOrderByStatement(config.getCorrectQuery())) {
			try {
				correctQuery_STMT = config.getConnection().createStatement();
				correctQuery_RSET = correctQuery_STMT.executeQuery(config.getCorrectQuery());
				correctQuery_RSMD = correctQuery_RSET.getMetaData();
				columnsCount = correctQuery_RSMD.getColumnCount();

				submittedQuery_STMT = config.getConnection().createStatement();

				submittedQuery_RSET = submittedQuery_STMT.executeQuery(submittedQuery);

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
				message = message.concat("This is an internal system error. ");
				message = message.concat("Please inform the system administrator.");

				this.logger.log(Level.SEVERE, message, e);
				orderingAnalysis.setCriterionIsSatisfied(false);
				orderingAnalysis.setAnalysisException(new AnalysisException(message, e));
			} finally {
				try {
					if (correctQuery_RSET != null) {
						correctQuery_RSET.close();
					}
					if (submittedQuery_RSET != null) {
						submittedQuery_RSET.close();
					}
					if (correctQuery_STMT != null) {
						correctQuery_STMT.close();
					}
					if (submittedQuery_STMT != null) {
						submittedQuery_STMT.close();
					}
				} catch (SQLException e) {
					this.logger.log(Level.SEVERE, "Could not close result set.", e);
				}
			}
		} else {
			orderingAnalysis.setFoundIncorrectOrdering(false);
			orderingAnalysis.setCriterionIsSatisfied(true);
		}

		return orderingAnalysis;
	}

	private boolean usesOrderByStatement(String query) {
		int lastOrderIndex;
		int afterOrderIndex;
		String normalizedQuery;

		normalizedQuery = query.toLowerCase().replaceAll(" ", "");
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

	private TuplesAnalysis analyzeTuples(SQLAnalyzerConfig config, String submittedQuery) {
		int columnCount;

		ResultSetMetaData rsmd;
		ResultSet checkQuery_RSET;
		Statement correctQuery_STMT;
		ResultSet correctQuery_RSET;
		Statement submittedQuery_STMT;

		Vector tuple;
		String columns;
		String message;
		String checkQuery;
		Vector correctQueryTuples;
		Vector submittedQueryTuples;
		TuplesAnalysis tuplesAnalysis;

		checkQuery_RSET = null;
		correctQuery_RSET = null;
		correctQuery_STMT = null;
		submittedQuery_STMT = null;

		correctQueryTuples = new Vector();
		submittedQueryTuples = new Vector();
		tuplesAnalysis = new TuplesAnalysis();

		this.logger.log(Level.INFO, "Checking tuples of query.");

		try {
			correctQuery_STMT = config.getConnection().createStatement();
			correctQuery_RSET = correctQuery_STMT.executeQuery(config.getCorrectQuery());
			rsmd = correctQuery_RSET.getMetaData();

			submittedQuery_STMT = config.getConnection().createStatement();

			columns = "";
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				tuplesAnalysis.addColumnLabel(rsmd.getColumnName(i));
				if (columns.length() > 0) {
					columns = columns.concat(", ");
				}
				columns = columns.concat(rsmd.getColumnName(i));
			}

			this.logger.log(Level.INFO, "COLUMN LABELS: " + columns);
			
			checkQuery = "";
			checkQuery = checkQuery.concat("SELECT " + columns + " ");
			checkQuery = checkQuery.concat("FROM (" + config.getCorrectQuery() + ") AS correctQuery ");
			checkQuery = checkQuery.concat("ORDER BY " + columns);
			
			this.logger.log(Level.INFO, "CORRECT QUERY:\n" + checkQuery);

			checkQuery_RSET = correctQuery_STMT.executeQuery(checkQuery);

			rsmd = checkQuery_RSET.getMetaData();
			columnCount = rsmd.getColumnCount();

			while (checkQuery_RSET.next()) {
				tuple = new Vector<String>();

				for (int i = 1; i <= columnCount; i++) {
					if (checkQuery_RSET.getString(i) != null) {
						tuple.add(checkQuery_RSET.getString(i).toUpperCase());
					} else {
						tuple.add("null");
					}
				}
				correctQueryTuples.addElement(tuple);
			}

			checkQuery = "";
			checkQuery = checkQuery.concat("SELECT " + columns + " ");
			checkQuery = checkQuery.concat("FROM (" + submittedQuery + ") AS submittedQuery ");
			checkQuery = checkQuery.concat("ORDER BY " + columns);

			this.logger.log(Level.INFO, "SUBMITTED QUERY:\n" + checkQuery);

			checkQuery_RSET = submittedQuery_STMT.executeQuery(checkQuery);

			while (checkQuery_RSET.next()) {
				tuple = new Vector<String>();
				for (int i = 1; i <= columnCount; i++) {
					if (checkQuery_RSET.getString(i) != null) {
						tuple.add(checkQuery_RSET.getString(i).toUpperCase());
					} else {
						tuple.add("null");
					}
				}
				submittedQueryTuples.addElement(tuple);
			}

			tuplesAnalysis.setMissingTuples(correctQueryTuples);
			tuplesAnalysis.removeAllMissingTuples(submittedQueryTuples);

			tuplesAnalysis.setSurplusTuples(submittedQueryTuples);
			tuplesAnalysis.removeAllSurplusTuples(correctQueryTuples);
			
			if ((tuplesAnalysis.getSurplusTuples().size() == 0) && (tuplesAnalysis.getMissingTuples().size() == 0)){
				tuplesAnalysis.setCriterionIsSatisfied(true);
			} else {
				tuplesAnalysis.setCriterionIsSatisfied(false);
			}

			this.logger.log(Level.INFO, "Finished checking tuples of query.");

		} catch (SQLException e) {
			message = "";
			message = message.concat("Error encounted while analyzing tuples. ");
			message = message.concat("This is an internal system error. ");
			message = message.concat("Please inform the system administrator.");

			this.logger.log(Level.SEVERE, message, e);
			tuplesAnalysis.setAnalysisException(new AnalysisException(message, e));
		} finally {
			try {
				if (correctQuery_RSET != null) {
					correctQuery_RSET.close();
				}
				if (checkQuery_RSET != null) {
					checkQuery_RSET.close();
				}
				if (correctQuery_STMT != null) {
					correctQuery_STMT.close();
				}
				if (submittedQuery_STMT != null) {
					submittedQuery_STMT.close();
				}
			} catch (SQLException e) {
				this.logger.log(Level.SEVERE, "Could not close result set.", e);
			}
		}

		return tuplesAnalysis;
	}

	private ColumnsAnalysis analyzeColumns(SQLAnalyzerConfig config, String submittedQuery) {
		String message;

		ResultSet correctQuery_RSET;
		Statement correctQuery_STMT;
		ResultSet submittedQuery_RSET;
		Statement submittedQuery_STMT;

		Vector correctQueryColumns;
		Vector submittedQueryColumns;

		int correctQueryColumnCount;
		int submittedQueryColumnCount;

		ColumnsAnalysis columnsAnalysis;

		ResultSetMetaData correctQueryMetaData;
		ResultSetMetaData submittedQueryMetaData;

		correctQuery_RSET = null;
		correctQuery_STMT = null;
		submittedQuery_RSET = null;
		submittedQuery_STMT = null;
		columnsAnalysis = new ColumnsAnalysis();

		this.logger.log(Level.INFO, "Checking columns of query.");

		try {

			correctQuery_STMT = config.getConnection().createStatement();
			correctQuery_RSET = correctQuery_STMT.executeQuery(config.getCorrectQuery());

			correctQueryMetaData = correctQuery_RSET.getMetaData();

			submittedQuery_STMT = config.getConnection().createStatement();
			submittedQuery_RSET = submittedQuery_STMT.executeQuery(submittedQuery);
			submittedQueryMetaData = submittedQuery_RSET.getMetaData();

			correctQueryColumnCount = correctQueryMetaData.getColumnCount();
			submittedQueryColumnCount = submittedQueryMetaData.getColumnCount();

			correctQueryColumns = new Vector<String>(correctQueryColumnCount);
			submittedQueryColumns = new Vector<String>(submittedQueryColumnCount);

			for (int i = 1; i <= submittedQueryColumnCount; i++) {
				submittedQueryColumns.add(submittedQueryMetaData.getColumnName(i));
			}

			for (int i = 1; i <= correctQueryColumnCount; i++) {
				correctQueryColumns.add(correctQueryMetaData.getColumnName(i));
			}

			for (int i = 0; i < correctQueryColumnCount; i++) {
				if (!submittedQueryColumns.contains(correctQueryColumns.get(i))) {
					columnsAnalysis.addMissingColumn((String)correctQueryColumns.get(i));
				}
			}

			for (int i = 0; i < submittedQueryColumnCount; i++) {
				if (!correctQueryColumns.contains(submittedQueryColumns.get(i))) {
					columnsAnalysis.addSurplusColumn((String)submittedQueryColumns.get(i));
				}
			}

			if ((columnsAnalysis.getSurplusColumns().size() == 0) && (columnsAnalysis.getMissingColumns().size() == 0)){
				columnsAnalysis.setCriterionIsSatisfied(true);
			} else {
				columnsAnalysis.setCriterionIsSatisfied(false);
			}

			this.logger.log(Level.INFO, "Finished checking columns of query.");

		} catch (SQLException e) {
			message = "";
			message = message.concat("Error occured while analyzing columns. ");
			message = message.concat("This is an system error. ");
			message = message.concat("Please inform the system administrator.");

			this.logger.log(Level.SEVERE, message, e);
			columnsAnalysis.setAnalysisException(new AnalysisException(message, e));
		} finally {
			try {
				if (correctQuery_RSET != null) {
					correctQuery_RSET.close();
				}
				if (submittedQuery_RSET != null) {
					submittedQuery_RSET.close();
				}
				if (correctQuery_STMT != null) {
					correctQuery_STMT.close();
				}
				if (submittedQuery_STMT != null) {
					submittedQuery_STMT.close();
				}
			} catch (SQLException e) {
				this.logger.log(Level.SEVERE, "Could not close result set.", e);
			}
		}

		return columnsAnalysis;
	}

	private CartesianProductAnalysis analyzeCartesianProduct(SQLAnalyzerConfig config, String submittedQuery) {
		String query;
		String message;
		int countCorrectQuery;
		int countSubmittedQuery;
		CartesianProductAnalysis cartesianProductAnalysis;

		Statement correctQuery_STMT;
		ResultSet correctQuery_RSET;
		Statement submittedQuery_STMT;
		ResultSet submittedQuery_RSET;

		correctQuery_STMT = null;
		correctQuery_RSET = null;
		submittedQuery_STMT = null;
		submittedQuery_RSET = null;

		countCorrectQuery = -1;
		countSubmittedQuery = -1;

		cartesianProductAnalysis = new CartesianProductAnalysis();

		this.logger.log(Level.INFO, "Checking for cartesian product.");

		try {
			query = "SELECT COUNT(*) FROM (" + submittedQuery + ") AS submittedQuery";
			submittedQuery_STMT = config.getConnection().createStatement();
			submittedQuery_RSET = submittedQuery_STMT.executeQuery(query);
			if (submittedQuery_RSET.next()) {
				countSubmittedQuery = submittedQuery_RSET.getInt(1);
			}

			query = "SELECT COUNT(*) FROM (" + config.getCorrectQuery() + ") AS correctQuery";
			correctQuery_STMT = config.getConnection().createStatement();
			correctQuery_RSET = correctQuery_STMT.executeQuery(query);
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
			message = message.concat("This is an system error. ");
			message = message.concat("Please inform the system administrator.");

			this.logger.log(Level.SEVERE, message, e);
			cartesianProductAnalysis.setAnalysisException(new AnalysisException(message, e));
		} finally {
			try {
				if (correctQuery_RSET != null) {
					correctQuery_RSET.close();
				}
				if (submittedQuery_RSET != null) {
					submittedQuery_RSET.close();
				}
				if (correctQuery_STMT != null) {
					correctQuery_STMT.close();
				}
				if (submittedQuery_STMT != null) {
					submittedQuery_STMT.close();
				}
			} catch (SQLException e) {
				this.logger.log(Level.SEVERE, "Could not close result set.", e);
			}
		}
		return cartesianProductAnalysis;
	}
}
