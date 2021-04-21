package at.jku.dke.etutor.modules.sql.report;

import java.sql.ResultSetMetaData;
import java.util.*;


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

    public SQLReporter() {
        super();

    }

    public SQLReport createReport(SQLAnalysis analysis, DefaultGrading grading, SQLReporterConfig config, Locale locale) {
        String LS;
        String tab;
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

        LS = " \n";
        tab = " \t";

        report = new SQLReport();


        //Setting the query result tuples and query result column labels
        report.setQueryResultTuples(analysis.getQueryResultTuples());
        report.setQueryResultColumnLabels(analysis.getQueryResultColumnLabels());
        //report.setMissingOrSurplusColumnLabels(analysis.get)

        //Checking overall correctness of query
        queryIsCorrect = true;
        criterionAnalysesIterator = analysis.iterCriterionAnalyses();
        while (criterionAnalysesIterator.hasNext()) {
            criterionAnalysis = (SQLCriterionAnalysis) criterionAnalysesIterator.next();
            if (!criterionAnalysis.isCriterionSatisfied()) {
                queryIsCorrect = false;
            }
        }

        //Creating error reports
        criterionAnalysesIterator = analysis.iterCriterionAnalyses();
        while (criterionAnalysesIterator.hasNext()) {
            criterionAnalysis = (SQLCriterionAnalysis) criterionAnalysesIterator.next();

            if (!criterionAnalysis.isCriterionSatisfied()) {
                hint = new StringBuffer();
                error = new StringBuffer();
                description = new StringBuffer();
                errorReport = new SQLErrorReport();

                if ((criterionAnalysis instanceof SyntaxAnalysis) && (config.getDiagnoseLevel() > 0)) {
                    description.append(((SyntaxAnalysis) criterionAnalysis).getSyntaxErrorDescription());
                }

                if ((criterionAnalysis instanceof TuplesAnalysis) && (config.getDiagnoseLevel() > 0)) {
                    tuplesAnalysis = (TuplesAnalysis) criterionAnalysis;
                    error.append("The tuples of your query are not correct");

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
                            description.append("There are " + missingTuplesCount + " missing tuples in the result of your query");
                        }


                        if ((tuplesAnalysis.hasMissingTuples()) && (tuplesAnalysis.hasSurplusTuples())) {
                            description.append(LS);
                        }

                        if (tuplesAnalysis.hasSurplusTuples()) {
                            description.append("There are " + surplusTuplesCount + "too much tuples in the result of your query");

                        }
                    }

                    if (config.getDiagnoseLevel() == 3) {

                        if (tuplesAnalysis.hasMissingTuples()) {
                            description.append("The following " + missingTuplesCount + " tuples are missing in the result of your query: " + LS);

                            report.setMissingTuples(tuplesAnalysis.getMissingTuples());


                            columnLabelsIterator = tuplesAnalysis.iterColumnLabels();
                            while (columnLabelsIterator.hasNext()) {
                                description.append(columnLabelsIterator.next() + tab + tab);
                            }
                            description.append(LS);

                            tuplesIterator = tuplesAnalysis.iterMissingTuples();
                            while (tuplesIterator.hasNext()) {
                                // description.append("<tr>");
                                tuple = (Collection) tuplesIterator.next();

                                tupleAttributesIterator = tuple.iterator();
                                while (tupleAttributesIterator.hasNext()) {
                                    description.append(tupleAttributesIterator.next() + tab + tab);
                                }
                                description.append(LS);
                            }
                        }

                        if ((tuplesAnalysis.hasMissingTuples()) && (tuplesAnalysis.hasSurplusTuples())) {
                            description.append(LS);
                        }

                        if (tuplesAnalysis.hasSurplusTuples()) {

                            description.append("The following " + surplusTuplesCount + " Tuples are too much in the result of your query " + LS);

                            report.setSurplusTuples(tuplesAnalysis.getSurplusTuples());

                            columnLabelsIterator = tuplesAnalysis.iterColumnLabels();
                            while (columnLabelsIterator.hasNext()) {
                                description.append(columnLabelsIterator.next() + tab + tab);
                            }
                            description.append(LS);

                            tuplesIterator = tuplesAnalysis.iterSurplusTuples();
                            while (tuplesIterator.hasNext()) {
                                // description.append("<tr>");
                                tuple = (Collection) tuplesIterator.next();

                                tupleAttributesIterator = tuple.iterator();
                                while (tupleAttributesIterator.hasNext()) {
                                    description.append(tupleAttributesIterator.next() + tab + tab);
                                }
                                description.append(LS);
                            }
                        }


                    }
                }

                if ((criterionAnalysis instanceof ColumnsAnalysis) && (config.getDiagnoseLevel() > 0)) {
                    columnsAnalysis = (ColumnsAnalysis) criterionAnalysis;

                    error.append("The columns of your result are not correct");

                    int missingColumnsCount = columnsAnalysis.getMissingColumns().size();
                    int surplusColumnsCount = columnsAnalysis.getSurplusColumns().size();

                    if (config.getDiagnoseLevel() == 1) {
                        if (columnsAnalysis.hasMissingColumns()) {
                            description.append("There are missing columns");
                        }

                        if ((columnsAnalysis.hasMissingColumns()) && (columnsAnalysis.hasSurplusColumns())) {
                            description.append(LS);
                        }

                        if (columnsAnalysis.hasSurplusColumns()) {
                            description.append("There are surplus columns");
                        }
                    }


                    if (config.getDiagnoseLevel() == 2) {

                        if (columnsAnalysis.hasMissingColumns()) {
                            description = description.append("There are " + missingColumnsCount + " columns missing");


                            if ((columnsAnalysis.hasMissingColumns()) && (columnsAnalysis.hasSurplusColumns())) {
                                description.append(LS);
                            }

                            if (columnsAnalysis.hasSurplusColumns()) {
                                description = description.append("There are " + surplusColumnsCount + " too much");

                            }
                        }
                    }

                    if (config.getDiagnoseLevel() == 3) {

                        if (columnsAnalysis.getMissingColumns().size() > 0) {
                            description.append("The following collumns are missing: " + LS);

                            columnsIterator = columnsAnalysis.iterMissingColumns();
                            while (columnsIterator.hasNext()) {
                                description.append(columnsIterator.next() + tab);
                            }
                        }

                        if (columnsAnalysis.getSurplusColumns().size() > 0) {
                            description.append("The following collumns are too much: " + LS);
                            columnsIterator = columnsAnalysis.iterSurplusColumns();
                            while (columnsIterator.hasNext()) {
                                description.append(columnsIterator.next() + tab);
                            }
                        }
                    }

                }

                if ((criterionAnalysis instanceof OrderingAnalysis) && (config.getDiagnoseLevel() > 0)) {
                    //hint.append(messageSource.getMessage("sqlreporter.orderbymanual", new Object[]{props.getProperty("hint_sql_order_by")}, locale));
                    error.append("The order of your tuples is not correct");
                    description.append("The order of your tuples is not correct");
                }

                if ((criterionAnalysis instanceof CartesianProductAnalysis) && (config.getDiagnoseLevel() > 0)) {
                    //hint.append(messageSource.getMessage("sqlreporter.wheremanual", new Object[]{props.getProperty("hint_sql_where")}, locale));
                    error.append("There are too many tuples in the result of your query");
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
