package at.jku.dke.etutor.modules.ddl.report;

import at.jku.dke.etutor.modules.ddl.analysis.*;

import java.util.Iterator;
import java.util.Locale;

public class DDLReporter {
    //region Constants
    private final String LS = "<br>";
    //endregion

    public DDLReporter() {

    }

    public DDLReport createReport(DDLAnalysis analysis, DDLReporterConfig config, Locale locale) {
        // Initialize variables
        DDLReport report = new DDLReport();
        DDLErrorReport errorReport;
        DDLCriterionAnalysis criterionAnalysis;

        SyntaxAnalysis syntaxAnalysis;
        TablesAnalysis tablesAnalysis;
        ColumnsAnalysis columnsAnalysis;
        PrimaryKeysAnalysis primaryKeysAnalysis;
        ForeignKeysAnalysis foreignKeysAnalysis;
        ConstraintsAnalysis constraintsAnalysis;

        Iterator<String> tablesIterator;
        Iterator<String> columnsIterator;
        Iterator<String> primaryKeysIterator;
        Iterator<String> foreignKeysIterator;
        Iterator<String> constraintsIterator;
        Iterator<DDLCriterionAnalysis> criterionAnalysisIterator;

        StringBuilder description;
        StringBuilder error;

        //todo Check if this is necessary
        // Set query results

        // If diagnose level == 0 -> no feedback
        if(config.getDiagnoseLevel() == 0) {
            return report;
        }

        // Create error reports
        criterionAnalysisIterator = analysis.iterCriterionAnalysis();
        while (criterionAnalysisIterator.hasNext()) {
            criterionAnalysis = criterionAnalysisIterator.next();

            // Check if the criterion is satisfied
            if(!criterionAnalysis.isCriterionSatisfied()) {
                error = new StringBuilder();
                description = new StringBuilder();
                errorReport = new DDLErrorReport();

                // Check on criterion type
                if(criterionAnalysis instanceof SyntaxAnalysis) {
                    description.append(((at.jku.dke.etutor.modules.sql.analysis.SyntaxAnalysis) criterionAnalysis).getSyntaxErrorDescription());
                }

                if(criterionAnalysis instanceof TablesAnalysis) {
                    tablesAnalysis = (TablesAnalysis) criterionAnalysis;

                    // Append error type
                    if(isGermanLocale(locale)) {
                        error.append("<strong>Die Tabellen in der Datenbank sind nicht richtig.</strong>");
                    } else {
                        error.append("<strong>The tables in the database are not correct</strong>");
                    }

                    // Get number of missing/surplus tables
                    int missingTablesCount = tablesAnalysis.getMissingTables().size();
                    int surplusTablesCount = tablesAnalysis.getSurplusTables().size();

                    // Check on diagnose level
                    if(config.getDiagnoseLevel() == 1) {
                        if(!tablesAnalysis.isMissingTablesEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Es fehlen Tabellen.");
                            } else {
                                description.append("There are missing tables.");
                            }
                        }

                        if(!tablesAnalysis.isMissingTablesEmpty() && !tablesAnalysis.isSurplusTablesEmpty()) {
                            description.append(LS);
                        }

                        if(!tablesAnalysis.isSurplusTablesEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Es sind zu viele Tabellen.");
                            } else {
                                description.append("There are too much tables.");
                            }
                        }
                    }

                    if(config.getDiagnoseLevel() == 2) {
                        if(!tablesAnalysis.isMissingTablesEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Es fehlen ").append(missingTablesCount).append(" Tabellen.");
                            } else {
                                description.append("There are ").append(missingTablesCount).append(" missing tables.");
                            }
                        }

                        if(!tablesAnalysis.isMissingTablesEmpty() && !tablesAnalysis.isSurplusTablesEmpty()) {
                            description.append(LS).append(LS);
                        }

                        if(!tablesAnalysis.isSurplusTablesEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Es sind ").append(surplusTablesCount).append(" Tabellen zu viel.");
                            } else {
                                description.append("There are ").append(surplusTablesCount).append(" surplus tables.");
                            }
                        }
                    }

                    if(config.getDiagnoseLevel() == 3) {
                        if(!tablesAnalysis.isMissingTablesEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Die folgenden ").append(missingTablesCount).append(" Tabellen fehlen: ").append(LS);
                            } else {
                                description.append("The following ").append(missingTablesCount).append(" tabels are missing: ").append(LS);
                            }

                            report.setMissingTables(tablesAnalysis.getMissingTables());

                            tablesIterator = tablesAnalysis.iterMissingTables();
                            description.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\" align=\"center\"><tr>");
                            while(tablesIterator.hasNext()) {
                                description.append("<th>").append(tablesIterator.next()).append("</th>");
                            }
                            description.append("</tr>");
                            description.append("</table>");
                        }

                        if(!tablesAnalysis.isMissingTablesEmpty() && !tablesAnalysis.isSurplusTablesEmpty()) {
                            description.append(LS).append(LS);
                        }

                        if(!tablesAnalysis.isSurplusTablesEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Die folgenden ").append(surplusTablesCount).append(" Tabellen sind zu viel: ").append(LS);
                            } else {
                                description.append("The following ").append(surplusTablesCount).append(" tabels are too much: ").append(LS);
                            }

                            report.setSurplusTables(tablesAnalysis.getSurplusTables());

                            tablesIterator = tablesAnalysis.iterSurplusTables();
                            description.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\" align=\"center\"><tr>");
                            while(tablesIterator.hasNext()) {
                                description.append("<th>").append(tablesIterator.next()).append("</th>");
                            }
                            description.append("</tr>");
                            description.append("</table>");
                        }
                    }
                }

                //todo Check for corresponding tables in diagnose level 3
                if(criterionAnalysis instanceof ColumnsAnalysis) {
                    columnsAnalysis = (ColumnsAnalysis) criterionAnalysis;

                    // Append error type
                    if(isGermanLocale(locale)) {
                        error.append("<strong>Die Spalten einer Tabelle sind nicht richtig.</strong>");
                    } else {
                        error.append("<strong>The columns in a table are not correct</strong>");
                    }

                    // Get number of wrong columns
                    int missingColumnsCount = columnsAnalysis.getMissingColumns().size();
                    int surplusColumnsCount = columnsAnalysis.getSurplusColumns().size();
                    int datatypeCount = columnsAnalysis.getWrongDatatypeColumns().size();
                    int nullCount = columnsAnalysis.getWrongNullColumns().size();
                    int defaultCount = columnsAnalysis.getWrongDefaultColumns().size();

                    // Check on diagnose level
                    if(config.getDiagnoseLevel() == 1) {
                        // Check for missing/surplus columns
                        if(!columnsAnalysis.isMissingColumnsEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Es fehlen Spalten.");
                            } else {
                                description.append("There are missing columns.");
                            }
                        }

                        if(!columnsAnalysis.isMissingColumnsEmpty() && !columnsAnalysis.isSurplusColumnsEmpty()) {
                            description.append(LS);
                        }

                        if(!columnsAnalysis.isSurplusColumnsEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Es sind zu viele Spalten.");
                            } else {
                                description.append("There are too much columns.");
                            }
                        }

                        // Check for datatypes
                        if(!columnsAnalysis.isWrongDatatypeColumnsEmpty()) {
                            description.append(LS);
                            if(isGermanLocale(locale)) {
                                description.append("Es gibt Spalten mit falschem Datentyp.");
                            } else {
                                description.append("There are columns with wrong datatype.");
                            }
                        }

                        // Check for nullable
                        if(!columnsAnalysis.isWrongNullColumnsEmpty()) {
                            description.append(LS);
                            if(isGermanLocale(locale)) {
                                description.append("Es gibt Spalten mit falschem Null-Ausdruck.");
                            } else {
                                description.append("There are columns with wrong null-statement.");
                            }
                        }

                        // Check for default values
                        if(!columnsAnalysis.isWrongDefaultColumnsEmpty()) {
                            description.append(LS);
                            if(isGermanLocale(locale)) {
                                description.append("Es gibt Spalten mit falschem Defaultwert.");
                            } else {
                                description.append("There are columns with wrong default value.");
                            }
                        }
                    }

                    if(config.getDiagnoseLevel() == 2) {
                        // Check for missing/surplus columns
                        if(!columnsAnalysis.isMissingColumnsEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Es fehlen ").append(missingColumnsCount).append(" Spalten.");
                            } else {
                                description.append("There are ").append(missingColumnsCount).append(" missing columns.");
                            }
                        }

                        if(!columnsAnalysis.isMissingColumnsEmpty() && !columnsAnalysis.isSurplusColumnsEmpty()) {
                            description.append(LS);
                        }

                        if(!columnsAnalysis.isSurplusColumnsEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Es sind ").append(surplusColumnsCount).append(" Spalten zu viel.");
                            } else {
                                description.append("There are ").append(surplusColumnsCount).append(" columns too much.");
                            }
                        }

                        // Check for datatypes
                        if(!columnsAnalysis.isWrongDatatypeColumnsEmpty()) {
                            description.append(LS);
                            if(isGermanLocale(locale)) {
                                description.append("Es gibt ").append(datatypeCount).append(" Spalten mit falschem Datentyp.");
                            } else {
                                description.append("There are ").append(datatypeCount).append(" columns with wrong datatype.");
                            }
                        }

                        // Check for nullable
                        if(!columnsAnalysis.isWrongNullColumnsEmpty()) {
                            description.append(LS);
                            if(isGermanLocale(locale)) {
                                description.append("Es gibt ").append(nullCount).append(" Spalten mit falschem Null-Ausdruck.");
                            } else {
                                description.append("There are ").append(nullCount).append(" columns with wrong null-statement.");
                            }
                        }

                        // Check for default values
                        if(!columnsAnalysis.isWrongDefaultColumnsEmpty()) {
                            description.append(LS);
                            if(isGermanLocale(locale)) {
                                description.append("Es gibt ").append(defaultCount).append(" Spalten mit falschem Defaultwert.");
                            } else {
                                description.append("There are ").append(defaultCount).append(" columns with wrong default value.");
                            }
                        }
                    }

                    if(config.getDiagnoseLevel() == 3) {
                        //todo implement
                    }
                }

                //todo Check for corresponding tables in diagnose level 3
                if(criterionAnalysis instanceof PrimaryKeysAnalysis) {
                    primaryKeysAnalysis = (PrimaryKeysAnalysis) criterionAnalysis;

                    // Append error type
                    if(isGermanLocale(locale)) {
                        error.append("<strong>Die Primärschlüssel in den Tabellen sind nicht richtig.</strong>");
                    } else {
                        error.append("<strong>The primary keys in the tables are not correct</strong>");
                    }

                    // Get number of wrong primary keys
                    int missingPrimaryKeysCount = primaryKeysAnalysis.getMissingPrimaryKeys().size();
                    int surplusPrimaryKeysCount = primaryKeysAnalysis.getSurplusPrimaryKeys().size();

                    // Check on diagnose level
                    if(config.getDiagnoseLevel() == 1) {
                        if(!primaryKeysAnalysis.isMissingPrimaryKeysEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Es fehlen Primärschlüssel in den Tabellen.");
                            } else {
                                description.append("There are missing primary keys in the tables.");
                            }
                        }

                        if(!primaryKeysAnalysis.isMissingPrimaryKeysEmpty() && !primaryKeysAnalysis.isSurplusPrimaryKeysEmpty()) {
                            description.append(LS);
                        }

                        if(!primaryKeysAnalysis.isSurplusPrimaryKeysEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Es sind zu viele Primärschlüssel in den Tabellen.");
                            } else {
                                description.append("There are too much primary keys in the tables.");
                            }
                        }
                    }

                    if(config.getDiagnoseLevel() == 2) {
                        if(!primaryKeysAnalysis.isMissingPrimaryKeysEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Es fehlen ").append(missingPrimaryKeysCount).append(" Primärschlüssel in den Tabellen.");
                            } else {
                                description.append("There are ").append(missingPrimaryKeysCount).append(" missing primary keys in the tables.");
                            }
                        }

                        if(!primaryKeysAnalysis.isMissingPrimaryKeysEmpty() && !primaryKeysAnalysis.isSurplusPrimaryKeysEmpty()) {
                            description.append(LS);
                        }

                        if(!primaryKeysAnalysis.isSurplusPrimaryKeysEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Es sind ").append(surplusPrimaryKeysCount).append(" Primärschlüssel zu viel in den Tabellen.");
                            } else {
                                description.append("There are ").append(surplusPrimaryKeysCount).append(" primary keys too much in the tables.");
                            }
                        }
                    }

                    if(config.getDiagnoseLevel() == 3) {
                        //todo implement
                    }
                }

                //todo Check for corresponding tables in diagnose level 3
                if(criterionAnalysis instanceof ForeignKeysAnalysis) {
                    foreignKeysAnalysis = (ForeignKeysAnalysis) criterionAnalysis;

                    // Append error type
                    if(isGermanLocale(locale)) {
                        error.append("<strong>Die Fremdschlüssel in den Tabellen sind nicht richtig.</strong>");
                    } else {
                        error.append("<strong>The foreign keys in the tables are not correct</strong>");
                    }

                    // Get number of wrong columns
                    int missingForeignKeysCount = foreignKeysAnalysis.getMissingForeignKeys().size();
                    int surplusForeignKeysCount = foreignKeysAnalysis.getSurplusForeignKeys().size();

                    // Check on diagnose level
                    if(config.getDiagnoseLevel() == 1) {
                        if(!foreignKeysAnalysis.isMissingForeignKeysEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Es fehlen Fremdschlüssel in den Tabellen.");
                            } else {
                                description.append("There are missing foreign keys in the tables.");
                            }
                        }

                        if(!foreignKeysAnalysis.isMissingForeignKeysEmpty() && !foreignKeysAnalysis.isSurplusForeignKeysEmpty()) {
                            description.append(LS);
                        }

                        if(!foreignKeysAnalysis.isSurplusForeignKeysEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Es sind zu viele Fremdschlüssel in den Tabellen.");
                            } else {
                                description.append("There are too much foreign keys in the tables.");
                            }
                        }
                    }

                    if(config.getDiagnoseLevel() == 2) {
                        if(!foreignKeysAnalysis.isMissingForeignKeysEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Es fehlen ").append(missingForeignKeysCount).append(" Fremdschlüssel in den Tabellen.");
                            } else {
                                description.append("There are ").append(missingForeignKeysCount).append(" missing foreign keys in the tables.");
                            }
                        }

                        if(!foreignKeysAnalysis.isMissingForeignKeysEmpty() && !foreignKeysAnalysis.isSurplusForeignKeysEmpty()) {
                            description.append(LS);
                        }

                        if(!foreignKeysAnalysis.isSurplusForeignKeysEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Es sind ").append(surplusForeignKeysCount).append(" Fremdschlüssel zu viel in den Tabellen.");
                            } else {
                                description.append("There are ").append(surplusForeignKeysCount).append(" foreign keys too much in the tables.");
                            }
                        }
                    }

                    if(config.getDiagnoseLevel() == 3) {
                        //todo implement
                    }
                }

                //todo Check for corresponding tables in diagnose level 3
                if(criterionAnalysis instanceof ConstraintsAnalysis) {
                    constraintsAnalysis = (ConstraintsAnalysis) criterionAnalysis;

                    // Append error type
                    if(isGermanLocale(locale)) {
                        error.append("<strong>Die Constraints in den Tabellen sind nicht richtig.</strong>");
                    } else {
                        error.append("<strong>The constraints in the tables are not correct</strong>");
                    }

                    // Get number of wrong columns
                    int missingConstraintsCount = constraintsAnalysis.getMissingConstraints().size();
                    int surplusConstraintsCount = constraintsAnalysis.getSurplusConstraints().size();

                    // Check on diagnose level
                    if(config.getDiagnoseLevel() == 1) {
                        if(!constraintsAnalysis.isMissingConstraintsEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Es fehlen unique Constraints in den Tabellen.");
                            } else {
                                description.append("There are missing unique constraints in the tables.");
                            }
                        }

                        if(!constraintsAnalysis.isMissingConstraintsEmpty() && !constraintsAnalysis.isSurplusConstraintsEmpty()) {
                            description.append(LS);
                        }

                        if(!constraintsAnalysis.isSurplusConstraintsEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Es sind zu viele unique Constraints in den Tabellen.");
                            } else {
                                description.append("There are too much unique constraints in the tables.");
                            }
                        }
                    }

                    if(config.getDiagnoseLevel() == 2) {
                        if(!constraintsAnalysis.isMissingConstraintsEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Es fehlen ").append(missingConstraintsCount).append(" unique Constraints in den Tabellen.");
                            } else {
                                description.append("There are ").append(missingConstraintsCount).append(" missing unique constraints in the tables.");
                            }
                        }

                        if(!constraintsAnalysis.isMissingConstraintsEmpty() && !constraintsAnalysis.isSurplusConstraintsEmpty()) {
                            description.append(LS);
                        }

                        if(!constraintsAnalysis.isSurplusConstraintsEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Es sind ").append(surplusConstraintsCount).append(" unique Constraints zu viel in den Tabellen.");
                            } else {
                                description.append("There are ").append(surplusConstraintsCount).append(" unique Constraints too much in the tables.");
                            }
                        }
                    }

                    if(config.getDiagnoseLevel() == 3) {
                        //todo implement
                    }
                }
            }
        }

        // Configure displayed information
        report.setShowErrorReports(true);
        report.setShowQueryResult(true);

        if (config.getDiagnoseLevel() >= 1) {
            report.setShowErrorDescriptions(true);
        }

        return report;
    }

    /**
     * Returns wheter the locale equals Locale.GERMAN
     * @param locale Locale information
     * @return Returns a boolean
     */
    private boolean isGermanLocale(Locale locale){
        return locale == Locale.GERMAN;
    }

}
