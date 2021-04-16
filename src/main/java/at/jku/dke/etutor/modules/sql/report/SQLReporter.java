package at.jku.dke.etutor.modules.sql.report;

import java.sql.ResultSetMetaData;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;


import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.modules.sql.analysis.*;
import org.springframework.context.MessageSource;


import at.jku.dke.etutor.modules.sql.SQLConstants;
import at.jku.dke.etutor.modules.sql.SQLEvaluationAction;
import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;


public class SQLReporter {
	
  java.util.Properties props;
  private MessageSource messageSource;

  public SQLReporter(MessageSource messageSource) {
		super();
		try {
			props = new Properties();
			props.load(SQLReporter.class.getClassLoader().getResourceAsStream(SQLConstants.PROPERTIES_PATH));
			this.messageSource = messageSource;
		} catch (Exception e) {
			e.printStackTrace();
		}
}

	public SQLReport createReport(SQLAnalysis analysis, DefaultGrading grading, SQLReporterConfig config, Locale locale) {
  		String LS;
		String prologue;
		SQLReport report;
		Collection tuple;
		ResultSetMetaData rsmd;
		SQLErrorReport errorReport;
		TuplesAnalysis tuplesAnalysis;
		ColumnsAnalysis columnsAnalysis;
		SQLCriterionAnalysis criterionAnalysis;
		
		boolean queryIsCorrect;

		Iterator tuplesIterator;
		Iterator columnsIterator;		
		Iterator columnLabelsIterator;
		Iterator tupleAttributesIterator;
		Iterator criterionAnalysesIterator;

		StringBuffer hint;
		StringBuffer error;
		StringBuffer description;

		LS = "\n";
		report = new SQLReport();

		//Searching exceptions of analyzer and grader		
		/*
		if (analysis.getAnalysisException() != null) {
			report.setShowException(true);
			report.setExceptionText(analysis.getAnalysisException().getMessage());
			return report;
		}

		if (grading.getGradingException() != null) {
			report.setShowException(true);
			report.setExceptionText(grading.getGradingException().getMessage());
			return report;
		}*/

		//Setting the query result tuples and query result column labels
		report.setQueryResultTuples(analysis.getQueryResultTuples());
		report.setQueryResultColumnLabels(analysis.getQueryResultColumnLabels());
		//report.setMissingOrSurplusColumnLabels(analysis.get)

		//Checking overall correctness of query
		queryIsCorrect = true;
		criterionAnalysesIterator = analysis.iterCriterionAnalyses();
		while (criterionAnalysesIterator.hasNext()) {
			criterionAnalysis = (SQLCriterionAnalysis)criterionAnalysesIterator.next();
			if (!criterionAnalysis.isCriterionSatisfied()) {
				queryIsCorrect = false;
			}
		}

		//Setting the prologue text
		prologue = new String();
		report.setShowPrologue(true);

		if (config.getAction() == SQLEvaluationAction.RUN) {
			if (queryIsCorrect) {
				prologue = prologue.concat(messageSource.getMessage("sqlreporter.parsedsuccessfully", null, locale));
			} else {
				prologue = prologue.concat(messageSource.getMessage("sqlreporter.notexecuted", null, locale));
			}
		}

		if (config.getAction() == SQLEvaluationAction.CHECK) {
			if (queryIsCorrect) {
				prologue = prologue.concat(messageSource.getMessage("sqlreporter.querycorrect", null, locale));
			} else {
				prologue = prologue.concat(messageSource.getMessage("sqlreporter.querynotcorrect", null, locale));
			}
		}

		if (config.getAction() == SQLEvaluationAction.DIAGNOSE) {
			if (queryIsCorrect) {
				prologue = prologue.concat(messageSource.getMessage("sqlreporter.querycorrect", null, locale));
			} else {
				prologue = prologue.concat(messageSource.getMessage("sqlreporter.querynotcorrect", null, locale));
			}
		}

		if (config.getAction() == SQLEvaluationAction.SUBMIT) {
			if (queryIsCorrect) {
				prologue = prologue.concat(messageSource.getMessage("sqlreporter.querysubmittedcorrect", null, locale));
			} else {
				prologue = prologue.concat(messageSource.getMessage("sqlreporter.querysubmittednotcorrect", null, locale));
			}

			prologue = prologue.concat(messageSource.getMessage("sqlreporter.suggestingpoints", new Object[] {grading.getPoints()}, locale));

			if (grading.getPoints() == 1){
				prologue = prologue.concat(" " + messageSource.getMessage("sqlreporter.point", null, locale) + " ");
			} else {
				prologue = prologue.concat(" " + messageSource.getMessage("sqlreporter.points", null, locale) + " ");
			}

			prologue = prologue.concat(messageSource.getMessage("sqlreporter.yourquery", null, locale));
		}

		report.setPrologue(prologue);

		//Creating error reports
		criterionAnalysesIterator = analysis.iterCriterionAnalyses();
		while (criterionAnalysesIterator.hasNext()) {
			criterionAnalysis = (SQLCriterionAnalysis)criterionAnalysesIterator.next();

			if (!criterionAnalysis.isCriterionSatisfied()) {
				hint = new StringBuffer();
				error = new StringBuffer();
				description = new StringBuffer();
				errorReport = new SQLErrorReport();

				if ((criterionAnalysis instanceof SyntaxAnalysis) && (config.getDiagnoseLevel() > 0)) {
					hint.append(messageSource.getMessage("sqlreporter.oraclemanual", new Object[] {props.getProperty("hint_sql_general")}, locale));

					error.append(messageSource.getMessage("sqlreporter.syntaxerrors", null, locale));

					description.append(((SyntaxAnalysis)criterionAnalysis).getSyntaxErrorDescription());
				}

				if ((criterionAnalysis instanceof TuplesAnalysis) && (config.getDiagnoseLevel() > 0)) {
					tuplesAnalysis = (TuplesAnalysis)criterionAnalysis;

					hint.append(messageSource.getMessage("sqlreporter.wheremanual", new Object[] {props.getProperty("hint_sql_where")}, locale));
					error.append(messageSource.getMessage("sqlreporter.incorrectnumbertuples", null, locale));

					int missingTuplesCount = tuplesAnalysis.getMissingTuples().size();
					int surplusTuplesCount = tuplesAnalysis.getSurplusTuples().size();

					if (config.getDiagnoseLevel() == 1){
						if (tuplesAnalysis.hasMissingTuples()){
							description.append(messageSource.getMessage("sqlreporter.tuplemissing", null, locale));
						}
						
						if ((tuplesAnalysis.hasSurplusTuples()) && (tuplesAnalysis.hasMissingTuples())){
							description.append("<br>");
						}
						
						if (tuplesAnalysis.hasSurplusTuples()){
							description.append(messageSource.getMessage("sqlreporter.tupletoomuch", null, locale));
						}
					}



					if (config.getDiagnoseLevel() == 2) {

						if (tuplesAnalysis.hasMissingTuples()){
							description.append(missingTuplesCount);
							if (missingTuplesCount == 1){
								description.append(" " + messageSource.getMessage("sqlreporter.tupleis", null, locale) + " ");
							} else {
								description.append(" " + messageSource.getMessage("sqlreporter.tuplesare", null, locale) + " ");
							}
							description.append(messageSource.getMessage("sqlreporter.inresultquery", null, locale));
						}


						if ((tuplesAnalysis.hasMissingTuples()) && (tuplesAnalysis.hasSurplusTuples())){
							description.append("<br>");
						}
						
						if (tuplesAnalysis.hasSurplusTuples()){
							description.append(surplusTuplesCount);
							if (surplusTuplesCount == 1){
								description.append(" " + messageSource.getMessage("sqlreporter.tupleis", null, locale) + " ");
							} else {
								description.append(" " + messageSource.getMessage("sqlreporter.tuplesare", null, locale) + " ");
							}
							description.append(messageSource.getMessage("sqlreporter.inresultquery", null, locale));
						}
					}


					if (config.getDiagnoseLevel() == 3) {

						if (tuplesAnalysis.hasMissingTuples()){
							description.append("<input type='hidden' id='missingTuplesDescription' value=\"");
							description.append(messageSource.getMessage("sqlreporter.tuplesmissing", null, locale));
							description.append("<table border='2' rules='all'><thead><tr>");

							columnLabelsIterator = tuplesAnalysis.iterColumnLabels();
							while (columnLabelsIterator.hasNext()){
								description.append("<th>" + columnLabelsIterator.next() + "</th>");
							}
							description.append("<tr></thead><tbody>");

							tuplesIterator = tuplesAnalysis.iterMissingTuples();
							while (tuplesIterator.hasNext()){
								description.append("<tr>");
								tuple = (Collection)tuplesIterator.next();
								
								tupleAttributesIterator = tuple.iterator();
								while (tupleAttributesIterator.hasNext()){
									description.append("<td>" + tupleAttributesIterator.next() + "</td>");														
								}
								description.append("</tr>");
							}
							description.append("</tbody></table>\"></input>");
							description.append("<a href=\"javascript:openWindow('missingTuplesDescription')\">" + missingTuplesCount);

							if (missingTuplesCount == 1){
								description.append(" " + messageSource.getMessage("sqlreporter.tupleisa", null, locale) + " ");
							} else {
								description.append(" " + messageSource.getMessage("sqlreporter.tuplesarea", null, locale) + " ");
							}
							description.append(messageSource.getMessage("sqlreporter.inresultquery", null, locale));
						}


						if ((tuplesAnalysis.hasMissingTuples()) && (tuplesAnalysis.hasSurplusTuples())){
							description.append("<br>");
						}

						if (tuplesAnalysis.hasSurplusTuples()){
							description.append("<input type='hidden' id='surplusTuplesDescription' value=\"");
							description.append(messageSource.getMessage("sqlreporter.tuplestoomuch", null, locale));
							description.append("<table border='2' rules='all'><thead><tr>");

							columnLabelsIterator = tuplesAnalysis.iterColumnLabels();
							while (columnLabelsIterator.hasNext()){
								description.append("<th>" + columnLabelsIterator.next() + "</th>");
							}
							description.append("<tr></thead><tbody>");

							tuplesIterator = tuplesAnalysis.iterSurplusTuples();
							while (tuplesIterator.hasNext()){
								description.append("<tr>");
								tuple = (Collection)tuplesIterator.next();
								
								tupleAttributesIterator = tuple.iterator();
								while (tupleAttributesIterator.hasNext()){
									description.append("<td>" + tupleAttributesIterator.next() + "</td>");														
								}
								description.append("</tr>");
							}
							description.append("</tbody></table>\"></input>");
							
							
							description.append("<a href=\"javascript:openWindow('surplusTuplesDescription')\">" + surplusTuplesCount);

							if (surplusTuplesCount == 1){
								description.append(" " + messageSource.getMessage("sqlreporter.tupleisa", null, locale) + " ");
							} else {
								description.append(" " + messageSource.getMessage("sqlreporter.tuplesarea", null, locale) + " ");
							}
							description.append(messageSource.getMessage("sqlreporter.toomuch", null, locale));
						}
					}
				}

				if ((criterionAnalysis instanceof ColumnsAnalysis) && (config.getDiagnoseLevel() > 0)){
					columnsAnalysis = (ColumnsAnalysis)criterionAnalysis;

					hint.append(messageSource.getMessage("sqlreporter.selectmanual", new Object[] {props.getProperty("hint_sql_attributes")}, locale));

					error.append(messageSource.getMessage("sqlreporter.incorrectnumbertuples", null, locale));

					int missingColumnsCount = columnsAnalysis.getMissingColumns().size();
					int surplusColumnsCount = columnsAnalysis.getSurplusColumns().size();

					if (config.getDiagnoseLevel() == 1){
						if (columnsAnalysis.hasMissingColumns()){
							description.append(messageSource.getMessage("sqlreporter.columnmissing", null, locale));
						}
						
						if ((columnsAnalysis.hasMissingColumns()) && (columnsAnalysis.hasSurplusColumns())){
							description.append("<br>");
						}
						
						if (columnsAnalysis.hasSurplusColumns()){
							description.append(messageSource.getMessage("sqlreporter.columntoomuch", null, locale));
						}
					}


					if (config.getDiagnoseLevel() == 2) {

						if (columnsAnalysis.hasMissingColumns()){
							description = description.append(missingColumnsCount);
							if (missingColumnsCount == 1){
								description = description.append(" " + messageSource.getMessage("sqlreporter.columnis", null, locale) + " ");
							} else {
								description = description.append(" " + messageSource.getMessage("sqlreporter.columnsare", null, locale) + " ");
							}
							description.append(messageSource.getMessage("sqlreporter.inresultquery", null, locale));
						}
						
						if ((columnsAnalysis.hasMissingColumns()) && (columnsAnalysis.hasSurplusColumns())){
							description.append("<br>");
						}
						
						if (columnsAnalysis.hasSurplusColumns()){
							description = description.append(surplusColumnsCount);
							if (surplusColumnsCount == 1){
								description = description.append(messageSource.getMessage("sqlreporter.columnis", null, locale));
							} else {
								description = description.append(messageSource.getMessage("sqlreporter.columnsare", null, locale));
							}
							description.append(messageSource.getMessage("sqlreporter.toomuch", null, locale));
						}
					}

					if (config.getDiagnoseLevel() == 3) {

						if (columnsAnalysis.getMissingColumns().size() > 0) {
							description.append("<div class='columns_caption'>" + messageSource.getMessage("sqlreporter.columnsmissing", null, locale) + "</div>");
							description.append("<table>");
							description.append("	<tr>");
							description.append("		<td>");
							
							description.append("			<ul>");
							
							columnsIterator = columnsAnalysis.iterMissingColumns();
							while (columnsIterator.hasNext()){
								description.append("			<li>" + columnsIterator.next() + "</li>");
							}
							description.append("			</ul>");

							description.append("		</td>");
							description.append("	</tr>");
							description.append("</table>");
						}

						if (columnsAnalysis.getSurplusColumns().size() > 0) {
							description.append("<div class='columns_caption'>" + messageSource.getMessage("sqlreporter.columnstoomuch", null, locale) + "</div>");
							description.append("<table>");
							description.append("	<tr>"	+ LS);
							description.append("		<td>");
							
							description.append("			<ul>");
							
							columnsIterator = columnsAnalysis.iterSurplusColumns();
							while (columnsIterator.hasNext()){
								description.append("				<li>" + columnsIterator.next() + "</li>");
							}
							description.append("			</ul>");

							description.append("		</td>");
							description.append("	</tr>");
							description.append("</table>");
						}
					}
				}

				if ((criterionAnalysis instanceof OrderingAnalysis) && (config.getDiagnoseLevel() > 0)) {
					hint.append(messageSource.getMessage("sqlreporter.orderbymanual", new Object[]{props.getProperty("hint_sql_order_by")}, locale));
					error.append(messageSource.getMessage("sqlreporter.incorrectorder", null, locale));
					description.append(messageSource.getMessage("sqlreporter.incorrectordertuples", null, locale));
				}

				if ((criterionAnalysis instanceof CartesianProductAnalysis) && (config.getDiagnoseLevel() > 0)) {
					hint.append(messageSource.getMessage("sqlreporter.wheremanual", new Object[] {props.getProperty("hint_sql_where")}, locale));
					error.append(messageSource.getMessage("sqlreporter.incorrectnumbertuples", null, locale));
					description.append(messageSource.getMessage("sqlreporter.notexecuted", null, locale));
				}

				if ((hint.toString().length() != 0) || (error.toString().length() != 0) || (description.toString().length() != 0)){
					errorReport.setHint(hint.toString());
					errorReport.setError(error.toString());
					errorReport.setDescription(description.toString());

					report.addErrorReport(errorReport);
				}
			}
		}

		//Configuring report printer
		if (config.getDiagnoseLevel() >= 0) {
			report.setShowErrorReports(true);
		}
		if (config.getDiagnoseLevel() >= 1) {
			report.setShowErrorDescriptions(true);
		}
		if (config.getDiagnoseLevel() >= 2) {
			report.setShowHints(true);
		}
		if (analysis.getCriterionAnalysis(SQLEvaluationCriterion.CORRECT_SYNTAX).isCriterionSatisfied()) {
			report.setShowQueryResult(true);
		}

		return report;
	}
}
