package at.jku.dke.etutor.modules.sql.report;

import java.util.*;


import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.modules.sql.analysis.*;


import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;


public class SQLReporter {

    public SQLReporter() {
        super();

    }

    public SQLReport createReport(SQLAnalysis analysis, DefaultGrading grading, SQLReporterConfig config, Locale locale) {
        final String LS ="<br>";
        SQLReport report;
        Collection<String> tuple;
        SQLErrorReport errorReport;
        TuplesAnalysis tuplesAnalysis;
        ColumnsAnalysis columnsAnalysis;
        SQLCriterionAnalysis criterionAnalysis;

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
                    if(isGermanLocale(locale)) error.append("<strong>Die Tupel der Ergebnistabelle sind nicht richtig</strong>").append(LS);
                    else error.append("<strong>The tuples of your query are not correct</strong>");

                    int missingTuplesCount = tuplesAnalysis.getMissingTuples().size();
                    int surplusTuplesCount = tuplesAnalysis.getSurplusTuples().size();

                    if (config.getDiagnoseLevel() == 1) {
                        if (tuplesAnalysis.hasMissingTuples()) {
                            if(isGermanLocale(locale)) description.append("Es fehlen Tupel.");
                            else description.append("There are missing tuples in the result of your query.");
                        }

                        if ((tuplesAnalysis.hasSurplusTuples()) && (tuplesAnalysis.hasMissingTuples())) {
                            description.append(LS);
                        }

                        if (tuplesAnalysis.hasSurplusTuples()) {
                            if(isGermanLocale(locale)) description.append("Es sind zu viele Tupel.");
                            description.append("There are too much tuples in the result of your query.");
                        }
                    }


                    if (config.getDiagnoseLevel() == 2) {

                        if (tuplesAnalysis.hasMissingTuples()) {
                            if (isGermanLocale(locale)) description.append("Es fehlen " ).append(missingTuplesCount).append(" Tupel.");
                            else description.append("There are ").append(missingTuplesCount).append(" missing tuples in the result of your query.");
                        }


                        if ((tuplesAnalysis.hasMissingTuples()) && (tuplesAnalysis.hasSurplusTuples())) {
                            description.append(LS).append(LS);
                        }

                        if (tuplesAnalysis.hasSurplusTuples()) {
                            if(isGermanLocale(locale)) description.append("Es sind ").append(surplusTuplesCount).append(" Tupel zu viel.");
                            else description.append("There are ").append(surplusTuplesCount).append("  surplus tuples in the result of your query.");


                        }
                    }

                    if (config.getDiagnoseLevel() == 3) {

                        if (tuplesAnalysis.hasMissingTuples()) {
                            if(isGermanLocale(locale)) description.append("Die folgenden ").append(missingTuplesCount).append(" Tupel fehlen:").append(LS);
                            else description.append("The following ").append(missingTuplesCount).append(" tuples are missing in the result of your query: ").append(LS);

                            report.setMissingTuples(tuplesAnalysis.getMissingTuples());


                            columnLabelsIterator = tuplesAnalysis.iterColumnLabels();
                            description.append("<table border=1 frame=void rules=rows><tr>");
                            while (columnLabelsIterator.hasNext()) {
                                description.append("<th>").append(columnLabelsIterator.next()).append("</th>");
                            }
                            description.append("</tr>");

                            tuplesIterator = tuplesAnalysis.iterMissingTuples();
                            while (tuplesIterator.hasNext()) {

                                tuple =  tuplesIterator.next();

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
                            if(isGermanLocale(locale))description.append("Die folgenden ").append(surplusTuplesCount).append(" Tupel sind zu viel: ").append(LS);
                            else description.append("The following ").append(surplusTuplesCount).append(" tuples are too much in the result of your query: ").append(LS);

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
                                tuple = tuplesIterator.next();

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

                    if(isGermanLocale(locale))error.append("<strong> Die Spalten der Ergebnistabelle sind nicht richtig! </strong>");
                    else error.append("<strong>The columns of your result are not correct!<strong><br>");

                    int missingColumnsCount = columnsAnalysis.getMissingColumns().size();
                    int surplusColumnsCount = columnsAnalysis.getSurplusColumns().size();

                    if (config.getDiagnoseLevel() == 1) {
                        if (columnsAnalysis.hasMissingColumns()) {
                            if(isGermanLocale(locale)) description.append("Es fehlen Spalten.");
                            else description.append("There are missing columns ").append(LS);
                        }

                        if ((columnsAnalysis.hasMissingColumns()) && (columnsAnalysis.hasSurplusColumns())) {
                            description.append(LS).append(LS);
                        }

                        if (columnsAnalysis.hasSurplusColumns()) {
                            if(isGermanLocale(locale))description.append("Es sind zu viele Spalten.");
                            else description.append("There are surplus columns").append(LS);
                        }
                    }


                    if (config.getDiagnoseLevel() == 2 ) {
                            if(columnsAnalysis.hasMissingColumns()){
                                if(isGermanLocale(locale))description.append("Es fehlen ").append(missingColumnsCount).append(" Spalten.").append(LS);
                                else description.append("There are ").append(missingColumnsCount).append(" columns missing. ").append(LS);
                            }

                            if ((columnsAnalysis.hasMissingColumns()) && (columnsAnalysis.hasSurplusColumns())) {
                                description.append(LS);
                            }

                            if (columnsAnalysis.hasSurplusColumns()) {
                                if(isGermanLocale(locale)) description.append("Es sind ").append(surplusColumnsCount).append(" Spalten zu viel.").append(LS);
                                description.append("There are ").append(surplusColumnsCount).append(" too much. ").append(LS);
                            }
                    }

                    if (config.getDiagnoseLevel() == 3) {

                        if (columnsAnalysis.hasMissingColumns()) {
                            if(isGermanLocale(locale))description.append("Die folgenden Spalten fehlen: ").append(LS);
                            else description.append("The following columns are missing: ").append(LS);

                            columnsIterator = columnsAnalysis.iterMissingColumns();
                            description.append("<strong>");
                            while (columnsIterator.hasNext()) {
                                description.append(columnsIterator.next()).append(" ");
                            }
                            description.append("</strong>");
                            description.append(LS);
                        }

                        if (columnsAnalysis.hasSurplusColumns()) {
                            if(isGermanLocale(locale)) description.append("Die folgenden Spalten sind zu viel: ").append(LS);
                            else description.append("The following columns are too much: ").append(LS);

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
                    if(isGermanLocale(locale)){
                        error.append("<strong>Die Sortierung der Ergebnistabelle ist nicht richtig!</strong>");
                        description.append("Die Sortierung der Ergebnistabelle ist nicht richtig.");
                    }else{
                        error.append("<strong>The order of your tuples is not correct!</strong>");
                        description.append("The order of your tuples is not correct");
                    }
                }

                if ((criterionAnalysis instanceof CartesianProductAnalysis) && (config.getDiagnoseLevel() > 0)) {
                    if(isGermanLocale(locale)){
                        error.append("<strong> Es sind viel zu viele Tupel in der Ergebnistabelle!</strong>");
                        description.append("Es handelt sich eventuell um ein kartesisches Produkt.");
                    }else{
                        error.append("<strong>There are way too many tuples in the result of your query</strong>");
                        description.append("Your result may be a cartesian product.");
                    }
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

    /**
     * Returns wheter the locale equals Locale.GERMAN
     * @param locale the locale
     * @return a boolean
     */
    private boolean isGermanLocale(Locale locale){
        return locale == Locale.GERMAN;
    }
}
