package at.jku.dke.etutor.modules.sql.report;

import java.sql.ResultSetMetaData;
import java.util.*;


import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.modules.sql.analysis.*;
import org.springframework.context.MessageSource;


import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;


public class SQLReporter {

    java.util.Properties props;
    private MessageSource messageSource;

    public SQLReporter() {
        super();

    }

    public SQLReport createReport(SQLAnalysis analysis, DefaultGrading grading, SQLReporterConfig config, Locale locale) {
        final String LS ="<br>";
        final String tab = " \t";
        SQLReport report;
        Collection<String> tuple;
        ResultSetMetaData rsmd;
        SQLErrorReport errorReport;
        TuplesAnalysis tuplesAnalysis;
        ColumnsAnalysis columnsAnalysis;
        SQLCriterionAnalysis criterionAnalysis;

        boolean queryIsCorrect;

        Iterator<Collection<String>> tuplesIterator;
        Iterator<String> columnsIterator;
        Iterator<String> columnLabelsIterator;
        Iterator<String> tupleAttributesIterator;
        Iterator<SQLCriterionAnalysis> criterionAnalysesIterator;

        StringBuilder hint;
        StringBuilder error;
        StringBuilder description;



        report = new SQLReport();


        //Setting the query result tuples and query result column labels
        report.setQueryResultTuples(analysis.getQueryResultTuples());
        report.setQueryResultColumnLabels(analysis.getQueryResultColumnLabels());
        //report.setMissingOrSurplusColumnLabels(analysis.get)

        //Checking overall correctness of query
        queryIsCorrect = true;
        criterionAnalysesIterator = analysis.iterCriterionAnalyses();
        while (criterionAnalysesIterator.hasNext()) {
            criterionAnalysis = criterionAnalysesIterator.next();
            if (!criterionAnalysis.isCriterionSatisfied()) {
                queryIsCorrect = false;
            }
        }

        //Creating error reports
        criterionAnalysesIterator = analysis.iterCriterionAnalyses();
        while (criterionAnalysesIterator.hasNext()) {
            criterionAnalysis = criterionAnalysesIterator.next();

            if (!criterionAnalysis.isCriterionSatisfied()) {
                hint = new StringBuilder();
                error = new StringBuilder();
                description = new StringBuilder();
                errorReport = new SQLErrorReport();

                if ((criterionAnalysis instanceof SyntaxAnalysis) && (config.getDiagnoseLevel() > 0)) {
                    description.append(((SyntaxAnalysis) criterionAnalysis).getSyntaxErrorDescription());
                }

                if ((criterionAnalysis instanceof TuplesAnalysis) && (config.getDiagnoseLevel() > 0)) {
                    tuplesAnalysis = (TuplesAnalysis) criterionAnalysis;
                    error.append("<strong>The tuples of your query are not correct</strong>");

                    int missingTuplesCount = tuplesAnalysis.getMissingTuples().size();
                    int surplusTuplesCount = tuplesAnalysis.getSurplusTuples().size();

                    if (config.getDiagnoseLevel() == 1) {
                        if (tuplesAnalysis.hasMissingTuples()) {
                            description.append("There are missing tuples in the result of your query");
                        }

                        if ((tuplesAnalysis.hasSurplusTuples()) && (tuplesAnalysis.hasMissingTuples())) {
                            description.append(LS);
                        }

                        if (tuplesAnalysis.hasSurplusTuples()) {
                            description.append("There are too much tuples in the result of your query");
                        }
                    }


                    if (config.getDiagnoseLevel() == 2) {

                        if (tuplesAnalysis.hasMissingTuples()) {
                            description.append("There are ").append(missingTuplesCount).append(" missing tuples in the result of your query");
                        }


                        if ((tuplesAnalysis.hasMissingTuples()) && (tuplesAnalysis.hasSurplusTuples())) {
                            description.append(LS).append(LS);
                        }

                        if (tuplesAnalysis.hasSurplusTuples()) {
                            description.append("There are ").append(surplusTuplesCount).append("  surplus tuples in the result of your query");

                        }
                    }

                    if (config.getDiagnoseLevel() == 3) {

                        if (tuplesAnalysis.hasMissingTuples()) {
                            description.append("The following ").append(missingTuplesCount).append(" tuples are missing in the result of your query: ").append(LS);

                            report.setMissingTuples(tuplesAnalysis.getMissingTuples());


                            columnLabelsIterator = tuplesAnalysis.iterColumnLabels();
                            description.append("<table border=1 frame=void rules=rows><tr>");
                            while (columnLabelsIterator.hasNext()) {
                                description.append("<th>").append(columnLabelsIterator.next()).append("</th>");
                            }
                            description.append("</tr>");

                            tuplesIterator = tuplesAnalysis.iterMissingTuples();
                            while (tuplesIterator.hasNext()) {

                                tuple = (Collection<String>) tuplesIterator.next();

                                tupleAttributesIterator = tuple.iterator();
                                description.append("<tr>");
                                while (tupleAttributesIterator.hasNext()) {
                                    description.append("<td>").append(tupleAttributesIterator.next()).append("</td>");
                                }
                                description.append("</tr>");
                            }
                            description.append("</table>");
                        }

                        if ((tuplesAnalysis.hasMissingTuples()) && (tuplesAnalysis.hasSurplusTuples())) {
                            description.append(LS).append(LS);
                        }

                        if (tuplesAnalysis.hasSurplusTuples()) {

                            description.append("The following ").append(surplusTuplesCount).append(" tuples are too much in the result of your query: ").append(LS);
                            description.append("<table border=1 frame=void rules=rows>");
                            report.setSurplusTuples(tuplesAnalysis.getSurplusTuples());

                            columnLabelsIterator = tuplesAnalysis.iterColumnLabels();
                            description.append("<tr>");
                            while (columnLabelsIterator.hasNext()) {
                                description.append("<th>").append(columnLabelsIterator.next()).append("</th>");
                            }
                            description.append("</tr>");

                            tuplesIterator = tuplesAnalysis.iterSurplusTuples();
                            while (tuplesIterator.hasNext()) {
                                description.append("<tr>");
                                tuple = (Collection<String>) tuplesIterator.next();

                                tupleAttributesIterator = tuple.iterator();
                                while (tupleAttributesIterator.hasNext()) {
                                    description.append("<td>").append(tupleAttributesIterator.next()).append("</td>");
                                }
                                description.append("</tr>");
                            }
                            description.append("</table>");
                        }


                    }
                }

                if ((criterionAnalysis instanceof ColumnsAnalysis) && (config.getDiagnoseLevel() > 0)) {
                    columnsAnalysis = (ColumnsAnalysis) criterionAnalysis;

                    error.append("<strong>The columns of your result are not correct<strong><br>");

                    int missingColumnsCount = columnsAnalysis.getMissingColumns().size();
                    int surplusColumnsCount = columnsAnalysis.getSurplusColumns().size();

                    if (config.getDiagnoseLevel() == 1) {
                        if (columnsAnalysis.hasMissingColumns()) {
                            description.append("There are missing columns ").append(LS);
                        }

                        if ((columnsAnalysis.hasMissingColumns()) && (columnsAnalysis.hasSurplusColumns())) {
                            description.append(LS).append(LS);
                        }

                        if (columnsAnalysis.hasSurplusColumns()) {
                            description.append("There are surplus columns").append(LS);
                        }
                    }


                    if (config.getDiagnoseLevel() == 2) {

                        if (columnsAnalysis.hasMissingColumns()) {
                            description.append("There are ").append(missingColumnsCount).append(" columns missing ").append(LS);


                            if ((columnsAnalysis.hasMissingColumns()) && (columnsAnalysis.hasSurplusColumns())) {
                                description.append(LS);
                            }

                            if (columnsAnalysis.hasSurplusColumns()) {
                                description.append("There are ").append(surplusColumnsCount).append(" too much ").append(LS);

                            }
                        }
                    }

                    if (config.getDiagnoseLevel() == 3) {

                        if (columnsAnalysis.hasMissingColumns()) {
                            description.append("The following columns are missing: ").append(LS);

                            columnsIterator = columnsAnalysis.iterMissingColumns();
                            description.append("<strong>");
                            while (columnsIterator.hasNext()) {
                                description.append(columnsIterator.next()).append(" ");
                            }
                            description.append("</strong>");
                            description.append(LS);
                        }

                        if (columnsAnalysis.hasSurplusColumns()) {
                            description.append("The following columns are too much: ").append(LS);
                            columnsIterator = columnsAnalysis.iterSurplusColumns();
                            description.append("<strong>");
                            while (columnsIterator.hasNext()) {
                                description.append(columnsIterator.next()).append(" ");
                            }
                            description.append("</strong>");
                            description.append(LS);
                        }
                    }

                }

                if ((criterionAnalysis instanceof OrderingAnalysis) && (config.getDiagnoseLevel() > 0)) {
                    //hint.append(messageSource.getMessage("sqlreporter.orderbymanual", new Object[]{props.getProperty("hint_sql_order_by")}, locale));
                    error.append("<strong>The order of your tuples is not correct</strong>");
                    description.append("The order of your tuples is not correct");
                }

                if ((criterionAnalysis instanceof CartesianProductAnalysis) && (config.getDiagnoseLevel() > 0)) {
                    //hint.append(messageSource.getMessage("sqlreporter.wheremanual", new Object[]{props.getProperty("hint_sql_where")}, locale));
                    error.append("<strong>There are too many tuples in the result of your query</strong>");
                    description.append("Your result may be a cartesian product");
                }

                if ((hint.toString().length() != 0) || (error.toString().length() != 0) || (description.toString().length() != 0)) {
                    errorReport.setHint(hint.toString());
                    errorReport.setError(error.toString());
                    errorReport.setDescription(description.toString());

                    String tempDesc = report.getDescription();
                    tempDesc = tempDesc + description.toString() + LS;
                    report.setDescription(tempDesc);

                    String tempErr = report.getError();
                    tempErr = tempErr + error.toString() + LS;
                    report.setError(tempErr);

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
