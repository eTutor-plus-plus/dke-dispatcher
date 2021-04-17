package at.jku.dke.etutor.modules.sql.feedback;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.core.evaluation.Feedback;
import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.modules.sql.SQLEvaluationAction;
import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;
import at.jku.dke.etutor.modules.sql.analysis.*;
import at.jku.dke.etutor.modules.sql.report.SQLErrorReport;
import at.jku.dke.etutor.modules.sql.report.SQLReport;
import at.jku.dke.etutor.modules.sql.report.SQLReporterConfig;

import java.sql.ResultSetMetaData;
import java.util.*;

public class SQLFeedback extends Feedback {
    Vector queryResultTuples;
    Vector queryResultColumnLabels;
    Vector missingTuples;
    Vector surplusTuples;
    private Vector errorReports;


    private boolean showHints;
    private boolean showPrologue;
    private boolean showException;
    private boolean showQueryResult;
    private boolean showErrorReports;
    private boolean showErrorDescriptions;





    public Vector getErrorReports() {
        return errorReports;
    }

    public void setErrorReports(Vector errorReports) {
        this.errorReports = errorReports;
    }

    public boolean isShowHints() {
        return showHints;
    }

    public void setShowHints(boolean showHints) {
        this.showHints = showHints;
    }

    public boolean isShowPrologue() {
        return showPrologue;
    }

    public void setShowPrologue(boolean showPrologue) {
        this.showPrologue = showPrologue;
    }

    public boolean isShowException() {
        return showException;
    }

    public void setShowException(boolean showException) {
        this.showException = showException;
    }

    public boolean isShowQueryResult() {
        return showQueryResult;
    }

    public void setShowQueryResult(boolean showQueryResult) {
        this.showQueryResult = showQueryResult;
    }

    public boolean isShowErrorReports() {
        return showErrorReports;
    }

    public void setShowErrorReports(boolean showErrorReports) {
        this.showErrorReports = showErrorReports;
    }

    public boolean isShowErrorDescriptions() {
        return showErrorDescriptions;
    }

    public void setShowErrorDescriptions(boolean showErrorDescriptions) {
        this.showErrorDescriptions = showErrorDescriptions;
    }

    public Vector getMissingTuples() {
        return missingTuples;
    }

    public void setMissingTuples(Vector missingTuples) {
        this.missingTuples = missingTuples;
    }

    public Vector getSurplusTuples() {
        return surplusTuples;
    }

    public void setSurplusTuples(Vector surplusTuples) {
        this.surplusTuples = surplusTuples;
    }

    public Vector getQueryResultTuples() {
        return queryResultTuples;
    }

    public void setQueryResultTuples(Vector queryResultTuples) {
        this.queryResultTuples = queryResultTuples;
    }

    public Vector getQueryResultColumnLabels() {
        return queryResultColumnLabels;
    }

    public void setQueryResultColumnLabels(Vector queryResultColumnLabels) {
        this.queryResultColumnLabels = queryResultColumnLabels;
    }


    public void addErrorReport(SQLErrorReport errorReport) {
        this.errorReports.add(errorReport);
    }


    public static SQLFeedback createFeedback(SQLAnalysis analysis, DefaultGrading grading, SQLReporterConfig config, Locale locale) {
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
        SQLFeedback feedback = new SQLFeedback();
        feedback.setErrorReports(new Vector());

        //Setting the query result tuples and query result column labels
        feedback.setQueryResultTuples(analysis.getQueryResultTuples());
        feedback.setQueryResultColumnLabels(analysis.getQueryResultColumnLabels());
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

                                feedback.setMissingTuples(tuplesAnalysis.getMissingTuples());


                                columnLabelsIterator = tuplesAnalysis.iterColumnLabels();
                                while (columnLabelsIterator.hasNext()) {
                                    description.append(columnLabelsIterator.next() + tab+tab);
                                }
                                description.append(LS);

                                tuplesIterator = tuplesAnalysis.iterMissingTuples();
                                while (tuplesIterator.hasNext()) {
                                    // description.append("<tr>");
                                    tuple = (Collection) tuplesIterator.next();

                                    tupleAttributesIterator = tuple.iterator();
                                    while (tupleAttributesIterator.hasNext()) {
                                        description.append(tupleAttributesIterator.next() + tab+tab);
                                    }
                                    description.append(LS);
                                }
                            }

                            if ((tuplesAnalysis.hasMissingTuples()) && (tuplesAnalysis.hasSurplusTuples())) {
                                description.append(LS);
                            }

                            if (tuplesAnalysis.hasSurplusTuples()) {

                                description.append("The following " + surplusTuplesCount + " Tuples are too much in the result of your query " + LS);

                                feedback.setSurplusTuples(tuplesAnalysis.getSurplusTuples());

                                columnLabelsIterator = tuplesAnalysis.iterColumnLabels();
                                while (columnLabelsIterator.hasNext()) {
                                    description.append(columnLabelsIterator.next() + tab +tab);
                                }
                                description.append(LS);

                                tuplesIterator = tuplesAnalysis.iterSurplusTuples();
                                while (tuplesIterator.hasNext()) {
                                    // description.append("<tr>");
                                    tuple = (Collection) tuplesIterator.next();

                                    tupleAttributesIterator = tuple.iterator();
                                    while (tupleAttributesIterator.hasNext()) {
                                        description.append(tupleAttributesIterator.next() + tab+tab);
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
                                description.append("The following collumns are missing: "+ LS);

                                columnsIterator = columnsAnalysis.iterMissingColumns();
                                while (columnsIterator.hasNext()) {
                                    description.append(columnsIterator.next() + tab);
                                }
                            }

                            if (columnsAnalysis.getSurplusColumns().size() > 0) {
                                description.append("The following collumns are too much: "+LS);
                                columnsIterator = columnsAnalysis.iterSurplusColumns();
                                while (columnsIterator.hasNext()) {
                                    description.append(columnsIterator.next() + tab);
                                }
                            }
                        }

                    }
                        /* NOCH ZU IMPLEMENTIEREN
                        if ((criterionAnalysis instanceof OrderingAnalysis) && (config.getDiagnoseLevel() > 0)) {
                            hint.append(messageSource.getMessage("sqlreporter.orderbymanual", new Object[]{props.getProperty("hint_sql_order_by")}, locale));
                            error.append(messageSource.getMessage("sqlreporter.incorrectorder", null, locale));
                            description.append(messageSource.getMessage("sqlreporter.incorrectordertuples", null, locale));
                        }

                        if ((criterionAnalysis instanceof CartesianProductAnalysis) && (config.getDiagnoseLevel() > 0)) {
                            hint.append(messageSource.getMessage("sqlreporter.wheremanual", new Object[]{props.getProperty("hint_sql_where")}, locale));
                            error.append(messageSource.getMessage("sqlreporter.incorrectnumbertuples", null, locale));
                            description.append(messageSource.getMessage("sqlreporter.notexecuted", null, locale));
                        }
                        */
                        if ((hint.toString().length() != 0) || (error.toString().length() != 0) || (description.toString().length() != 0)) {
                            errorReport.setHint(hint.toString());
                            errorReport.setError(error.toString());
                            errorReport.setDescription(description.toString());

                            String tempDesc = feedback.getDescription();
                            tempDesc = tempDesc + description.toString() + LS;
                            feedback.setDescription(tempDesc);

                            String tempErr = feedback.getError();
                            tempErr = tempErr + error.toString() + LS;
                            feedback.setError(tempErr);

                            feedback.addErrorReport(errorReport);
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

                return feedback;
            }

        }





