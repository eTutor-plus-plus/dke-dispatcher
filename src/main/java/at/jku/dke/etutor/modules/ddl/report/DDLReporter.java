package at.jku.dke.etutor.modules.ddl.report;

import at.jku.dke.etutor.modules.ddl.analysis.*;

import java.util.Iterator;
import java.util.Locale;

public class DDLReporter {
    //region Constants
    private final String LS = "<br>";
    private final String TABELLE = "Tabelle";
    private final String SPALTE = "Spalte";
    private final String TABLE = "Table";
    private final String COLUMN = "Column";
    //endregion

    public DDLReporter() {

    }

    public DDLReport createReport(DDLAnalysis analysis, DDLReporterConfig config, Locale locale) {
        // Initialize variables
        DDLReport report = new DDLReport();
        DDLErrorReport errorReport;
        DDLCriterionAnalysis criterionAnalysis;

        TablesAnalysis tablesAnalysis;
        ColumnsAnalysis columnsAnalysis;
        PrimaryKeysAnalysis primaryKeysAnalysis;
        ForeignKeysAnalysis foreignKeysAnalysis;
        ConstraintsAnalysis constraintsAnalysis;

        Iterator<String> tablesIterator;
        Iterator<ErrorTupel> columnsIterator;
        Iterator<ErrorTupel> primaryKeysIterator;
        Iterator<ErrorTupel> foreignKeysIterator;
        Iterator<ErrorTupel> constraintsIterator;
        Iterator<String> checkConstraintsIterator;
        Iterator<DDLCriterionAnalysis> criterionAnalysisIterator;

        StringBuilder description;
        StringBuilder error;
        String header1;
        String header2;

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
                    description.append(((SyntaxAnalysis) criterionAnalysis).getErrorDescription());
                }

                if(criterionAnalysis instanceof TablesAnalysis) {
                    tablesAnalysis = (TablesAnalysis) criterionAnalysis;

                    // Append error type
                    if(isGermanLocale(locale)) {
                        error.append("<strong>Die Tabellen in der Datenbank sind nicht richtig.</strong>");
                    } else {
                        error.append("<strong>The tables in the database are not correct.</strong>");
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

                if(criterionAnalysis instanceof ColumnsAnalysis) {
                    columnsAnalysis = (ColumnsAnalysis) criterionAnalysis;

                    // Append error type
                    if(isGermanLocale(locale)) {
                        error.append("<strong>Die Spalten einer Tabelle sind nicht richtig.</strong>");
                    } else {
                        error.append("<strong>The columns in a table are not correct.</strong>");
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
                        // Check for missing/surplus columns
                        if(!columnsAnalysis.isMissingColumnsEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Die folgenden ").append(missingColumnsCount).append(" Spalten fehlen: ").append(LS);
                                header1 = TABELLE;
                                header2 = SPALTE;
                            } else {
                                description.append("The following ").append(missingColumnsCount).append(" columns are missing: ").append(LS);
                                header1 = TABLE;
                                header2 = COLUMN;
                            }

                            report.setMissingColumns(columnsAnalysis.getMissingColumns());

                            columnsIterator = columnsAnalysis.iterMissingColumns();
                            description.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\" align=\"center\"><tr><th>").append(header1).append("</th><th>").append(header2).append("</th></tr>");
                            while(columnsIterator.hasNext()) {
                                ErrorTupel tupel = columnsIterator.next();
                                description.append("<tr>").append("<td>").append(tupel.getSource()).append("</td><td>").append(tupel.getError()).append("</td></tr>");
                            }
                            description.append("</table>");
                        }

                        if(!columnsAnalysis.isMissingColumnsEmpty() && !columnsAnalysis.isSurplusColumnsEmpty()) {
                            description.append(LS).append(LS);
                        }

                        if(!columnsAnalysis.isSurplusColumnsEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Die folgenden ").append(surplusColumnsCount).append(" Spalten sind zu viel: ").append(LS);
                                header1 = TABELLE;
                                header2 = SPALTE;
                            } else {
                                description.append("The following ").append(surplusColumnsCount).append(" columns are too much: ").append(LS);
                                header1 = TABLE;
                                header2 = COLUMN;
                            }

                            report.setSurplusColumns(columnsAnalysis.getSurplusColumns());

                            columnsIterator = columnsAnalysis.iterSurplusColumns();
                            description.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\" align=\"center\"><tr><th>").append(header1).append("</th><th>").append(header2).append("</th></tr>");
                            while(columnsIterator.hasNext()) {
                                ErrorTupel tupel = columnsIterator.next();
                                description.append("<tr>").append("<td>").append(tupel.getSource()).append("</td><td>").append(tupel.getError()).append("</td></tr>");
                            }
                            description.append("</table>");
                        }

                        // Check for datatypes
                        if(!columnsAnalysis.isWrongDatatypeColumnsEmpty()) {
                            description.append(LS);
                            if(isGermanLocale(locale)) {
                                description.append("Die folgenden ").append(datatypeCount).append(" Spalten haben einen falschen Datentyp: ").append(LS);
                                header1 = TABELLE;
                                header2 = SPALTE;
                            } else {
                                description.append("The following ").append(datatypeCount).append(" columns have a wrong datatype: ").append(LS);
                                header1 = TABLE;
                                header2 = COLUMN;
                            }

                            report.addWrongColumns(columnsAnalysis.getWrongDatatypeColumns());

                            columnsIterator = columnsAnalysis.iterWrongDatatypeColumns();
                            description.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\" align=\"center\"><tr><th>").append(header1).append("</th><th>").append(header2).append("</th></tr>");
                            while(columnsIterator.hasNext()) {
                                ErrorTupel tupel = columnsIterator.next();
                                description.append("<tr>").append("<td>").append(tupel.getSource()).append("</td><td>").append(tupel.getError()).append("</td></tr>");
                            }
                            description.append("</table>");
                        }

                        // Check for nullable
                        if(!columnsAnalysis.isWrongNullColumnsEmpty()) {
                            description.append(LS);
                            if(isGermanLocale(locale)) {
                                description.append("Die folgenden ").append(nullCount).append(" Spalten haben einen falschen Null-Ausdruck: ").append(LS);
                                header1 = TABELLE;
                                header2 = SPALTE;
                            } else {
                                description.append("The following ").append(nullCount).append(" columns have a wrong null-statement: ").append(LS);
                                header1 = TABLE;
                                header2 = COLUMN;
                            }

                            report.addWrongColumns(columnsAnalysis.getWrongNullColumns());

                            columnsIterator = columnsAnalysis.iterWrongNullColumns();
                            description.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\" align=\"center\"><tr><th>").append(header1).append("</th><th>").append(header2).append("</th></tr>");
                            while(columnsIterator.hasNext()) {
                                ErrorTupel tupel = columnsIterator.next();
                                description.append("<tr>").append("<td>").append(tupel.getSource()).append("</td><td>").append(tupel.getError()).append("</td></tr>");
                            }
                            description.append("</table>");
                        }

                        // Check for default values
                        if(!columnsAnalysis.isWrongDefaultColumnsEmpty()) {
                            description.append(LS);
                            if(isGermanLocale(locale)) {
                                description.append("Die folgenden ").append(defaultCount).append(" Spalten haben einen falschen Defaultwert: ").append(LS);
                                header1 = TABELLE;
                                header2 = SPALTE;
                            } else {
                                description.append("The following ").append(defaultCount).append(" columns have a wrong default value: ").append(LS);
                                header1 = TABLE;
                                header2 = COLUMN;
                            }

                            report.addWrongColumns(columnsAnalysis.getWrongDefaultColumns());

                            columnsIterator = columnsAnalysis.iterWrongDefaultColumns();
                            description.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\" align=\"center\"><tr><th>").append(header1).append("</th><th>").append(header2).append("</th></tr>");
                            while(columnsIterator.hasNext()) {
                                ErrorTupel tupel = columnsIterator.next();
                                description.append("<tr>").append("<td>").append(tupel.getSource()).append("</td><td>").append(tupel.getError()).append("</td></tr>");
                            }
                            description.append("</table>");
                        }
                    }
                }

                if(criterionAnalysis instanceof PrimaryKeysAnalysis) {
                    primaryKeysAnalysis = (PrimaryKeysAnalysis) criterionAnalysis;

                    // Append error type
                    if(isGermanLocale(locale)) {
                        error.append("<strong>Die Primärschlüssel in den Tabellen sind nicht richtig.</strong>");
                    } else {
                        error.append("<strong>The primary keys in the tables are not correct.</strong>");
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
                        if(!primaryKeysAnalysis.isMissingPrimaryKeysEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Die folgenden ").append(missingPrimaryKeysCount).append(" Primärschlüssel fehlen: ").append(LS);
                                header1 = TABELLE;
                                header2 = SPALTE;
                            } else {
                                description.append("The following ").append(missingPrimaryKeysCount).append(" primary keys are missing: ").append(LS);
                                header1 = TABLE;
                                header2 = COLUMN;
                            }

                            report.setMissingPrimaryKeys(primaryKeysAnalysis.getMissingPrimaryKeys());

                            primaryKeysIterator = primaryKeysAnalysis.iterMissingPrimaryKeys();
                            description.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\" align=\"center\"><tr><th>").append(header1).append("</th><th>").append(header2).append("</th></tr>");
                            while(primaryKeysIterator.hasNext()) {
                                ErrorTupel tupel = primaryKeysIterator.next();
                                description.append("<tr>").append("<td>").append(tupel.getSource()).append("</td><td>").append(tupel.getError()).append("</td></tr>");
                            }
                            description.append("</table>");
                        }

                        if(!primaryKeysAnalysis.isMissingPrimaryKeysEmpty() && !primaryKeysAnalysis.isSurplusPrimaryKeysEmpty()) {
                            description.append(LS).append(LS);
                        }

                        if(!primaryKeysAnalysis.isSurplusPrimaryKeysEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Die folgenden ").append(surplusPrimaryKeysCount).append(" Primärschlüssel sind zu viel: ").append(LS);
                                header1 = TABELLE;
                                header2 = SPALTE;
                            } else {
                                description.append("The following ").append(surplusPrimaryKeysCount).append(" primary keys are too much: ").append(LS);
                                header1 = TABLE;
                                header2 = COLUMN;
                            }

                            report.setSurplusPrimaryKeys(primaryKeysAnalysis.getSurplusPrimaryKeys());

                            primaryKeysIterator = primaryKeysAnalysis.iterSurplusPrimaryKeys();
                            description.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\" align=\"center\"><tr><th>").append(header1).append("</th><th>").append(header2).append("</th></tr>");
                            while(primaryKeysIterator.hasNext()) {
                                ErrorTupel tupel = primaryKeysIterator.next();
                                description.append("<tr>").append("<td>").append(tupel.getSource()).append("</td><td>").append(tupel.getError()).append("</td></tr>");
                            }
                            description.append("</table>");
                        }
                    }
                }

                if(criterionAnalysis instanceof ForeignKeysAnalysis) {
                    foreignKeysAnalysis = (ForeignKeysAnalysis) criterionAnalysis;

                    // Append error type
                    if(isGermanLocale(locale)) {
                        error.append("<strong>Die Fremdschlüssel in den Tabellen sind nicht richtig.</strong>");
                    } else {
                        error.append("<strong>The foreign keys in the tables are not correct.</strong>");
                    }

                    // Get number of wrong columns
                    int missingForeignKeysCount = foreignKeysAnalysis.getMissingForeignKeys().size();
                    int surplusForeignKeysCount = foreignKeysAnalysis.getSurplusForeignKeys().size();
                    int wrongUpdateForeignKeysCount = foreignKeysAnalysis.getWrongUpdateForeignKeys().size();
                    int wrongDeleteForeignKeysCount = foreignKeysAnalysis.getWrongDeleteForeignKeys().size();

                    // Check on diagnose level
                    if(config.getDiagnoseLevel() == 1) {
                        if(!foreignKeysAnalysis.isMissingForeignKeysEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Es fehlen Fremdschlüssel in den Tabellen.");
                            } else {
                                description.append("There are missing foreign keys in the tables.");
                            }
                        }

                        if(!foreignKeysAnalysis.isSurplusForeignKeysEmpty()) {
                            description.append(LS);
                            if(isGermanLocale(locale)) {
                                description.append("Es sind zu viele Fremdschlüssel in den Tabellen.");
                            } else {
                                description.append("There are too much foreign keys in the tables.");
                            }
                        }

                        if(!foreignKeysAnalysis.isWrongUpdateForeignKeysEmpty()) {
                            description.append(LS);
                            if(isGermanLocale(locale)) {
                                description.append("Es gibt Fremdschlüssel mit falschen Update Verhalten in den Tabellen.");
                            } else {
                                description.append("There are foreign keys with wrong update behavior in the tables.");
                            }
                        }

                        if(!foreignKeysAnalysis.isWrongDeleteForeignKeysEmpty()) {
                            description.append(LS);
                            if(isGermanLocale(locale)) {
                                description.append("Es gibt Fremdschlüssel mit falschen Delete Verhalten in den Tabellen.");
                            } else {
                                description.append("There are foreign keys with wrong delete behavior in the tables.");
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

                        if(!foreignKeysAnalysis.isSurplusForeignKeysEmpty()) {
                            description.append(LS);
                            if(isGermanLocale(locale)) {
                                description.append("Es sind ").append(surplusForeignKeysCount).append(" Fremdschlüssel zu viel in den Tabellen.");
                            } else {
                                description.append("There are ").append(surplusForeignKeysCount).append(" foreign keys too much in the tables.");
                            }
                        }

                        if(!foreignKeysAnalysis.isWrongUpdateForeignKeysEmpty()) {
                            description.append(LS);
                            if(isGermanLocale(locale)) {
                                description.append("Es gibt ").append(wrongUpdateForeignKeysCount).append(" Fremdschlüssel mit falschen Update Verhalten in den Tabellen.");
                            } else {
                                description.append("There are ").append(wrongUpdateForeignKeysCount).append(" foreign keys with wrong update behavior in the tables.");
                            }
                        }

                        if(!foreignKeysAnalysis.isWrongDeleteForeignKeysEmpty()) {
                            description.append(LS);
                            if(isGermanLocale(locale)) {
                                description.append("Es gibt ").append(wrongDeleteForeignKeysCount).append(" Fremdschlüssel mit falschen Delete Verhalten in den Tabellen.");
                            } else {
                                description.append("There are ").append(wrongDeleteForeignKeysCount).append(" foreign keys with wrong delete behavior in the tables.");
                            }
                        }
                    }

                    if(config.getDiagnoseLevel() == 3) {
                        if(!foreignKeysAnalysis.isMissingForeignKeysEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Die folgenden ").append(missingForeignKeysCount).append(" Fremdschlüssel fehlen: ").append(LS);
                                header1 = TABELLE;
                                header2 = SPALTE;
                            } else {
                                description.append("The following ").append(missingForeignKeysCount).append(" foreign keys are missing: ").append(LS);
                                header1 = TABLE;
                                header2 = COLUMN;
                            }

                            report.setMissingForeignKeys(foreignKeysAnalysis.getMissingForeignKeys());

                            foreignKeysIterator = foreignKeysAnalysis.iterMissingForeignKeys();
                            description.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\" align=\"center\"><tr><th>").append(header1).append("</th><th>").append(header2).append("</th></tr>");
                            while(foreignKeysIterator.hasNext()) {
                                ErrorTupel tupel = foreignKeysIterator.next();
                                description.append("<tr>").append("<td>").append(tupel.getSource()).append("</td><td>").append(tupel.getError()).append("</td></tr>");
                            }
                            description.append("</table>");
                        }

                        if(!foreignKeysAnalysis.isSurplusForeignKeysEmpty()) {
                            description.append(LS).append(LS);
                            if(isGermanLocale(locale)) {
                                description.append("Die folgenden ").append(surplusForeignKeysCount).append(" Fremdschlüssel sind zu viel: ").append(LS);
                                header1 = TABELLE;
                                header2 = SPALTE;
                            } else {
                                description.append("The following ").append(surplusForeignKeysCount).append(" foreign keys are too much: ").append(LS);
                                header1 = TABLE;
                                header2 = COLUMN;
                            }

                            report.setSurplusForeignKeys(foreignKeysAnalysis.getSurplusForeignKeys());

                            foreignKeysIterator = foreignKeysAnalysis.iterSurplusForeignKeys();
                            description.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\" align=\"center\"><tr><th>").append(header1).append("</th><th>").append(header2).append("</th></tr>");
                            while(foreignKeysIterator.hasNext()) {
                                ErrorTupel tupel = foreignKeysIterator.next();
                                description.append("<tr>").append("<td>").append(tupel.getSource()).append("</td><td>").append(tupel.getError()).append("</td></tr>");
                            }
                            description.append("</table>");
                        }

                        if(!foreignKeysAnalysis.isWrongUpdateForeignKeysEmpty()) {
                            description.append(LS).append(LS);
                            if(isGermanLocale(locale)) {
                                description.append("Die folgenden ").append(wrongUpdateForeignKeysCount).append(" Fremdschlüssel haben falsche Update Verhalten: ").append(LS);
                                header1 = TABELLE;
                                header2 = SPALTE;
                            } else {
                                description.append("The following ").append(wrongUpdateForeignKeysCount).append(" foreign keys have wrong update behavior: ").append(LS);
                                header1 = TABLE;
                                header2 = COLUMN;
                            }

                            report.setWrongUpdateForeignKeys(foreignKeysAnalysis.getWrongUpdateForeignKeys());

                            foreignKeysIterator = foreignKeysAnalysis.iterWrongUpdateForeignKeys();
                            description.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\" align=\"center\"><tr><th>").append(header1).append("</th><th>").append(header2).append("</th></tr>");
                            while(foreignKeysIterator.hasNext()) {
                                ErrorTupel tupel = foreignKeysIterator.next();
                                description.append("<tr>").append("<td>").append(tupel.getSource()).append("</td><td>").append(tupel.getError()).append("</td></tr>");
                            }
                            description.append("</table>");
                        }

                        if(!foreignKeysAnalysis.isWrongDeleteForeignKeysEmpty()) {
                            description.append(LS).append(LS);
                            if(isGermanLocale(locale)) {
                                description.append("Die folgenden ").append(wrongDeleteForeignKeysCount).append(" Fremdschlüssel haben falsche Delete Verhalten: ").append(LS);
                                header1 = TABELLE;
                                header2 = SPALTE;
                            } else {
                                description.append("The following ").append(wrongDeleteForeignKeysCount).append(" foreign keys have wrong delete behavior: ").append(LS);
                                header1 = TABLE;
                                header2 = COLUMN;
                            }

                            report.setWrongDeleteForeignKeys(foreignKeysAnalysis.getWrongDeleteForeignKeys());

                            foreignKeysIterator = foreignKeysAnalysis.iterWrongDeleteForeignKeys();
                            description.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\" align=\"center\"><tr><th>").append(header1).append("</th><th>").append(header2).append("</th></tr>");
                            while(foreignKeysIterator.hasNext()) {
                                ErrorTupel tupel = foreignKeysIterator.next();
                                description.append("<tr>").append("<td>").append(tupel.getSource()).append("</td><td>").append(tupel.getError()).append("</td></tr>");
                            }
                            description.append("</table>");
                        }
                    }
                }

                if(criterionAnalysis instanceof ConstraintsAnalysis) {
                    constraintsAnalysis = (ConstraintsAnalysis) criterionAnalysis;

                    if(!constraintsAnalysis.isMissingConstraintsEmpty() || !constraintsAnalysis.isSurplusConstraintsEmpty()) {
                        // Append error type
                        if(isGermanLocale(locale)) {
                            error.append("<strong>Die Constraints in den Tabellen sind nicht richtig.</strong>");
                        } else {
                            error.append("<strong>The constraints in the tables are not correct.</strong>");
                        }
                    }

                    if(!constraintsAnalysis.isInsertStatementsChecked()) {
                        if(isGermanLocale(locale)) {
                            error.append("<strong>Check-Constraints konnten aufgrund des falschen Datenbankschemas nicht überprüft werden.</strong>");
                        } else {
                            error.append("<strong>Check constraints could not be checked due to incorrect database schema.</strong>");
                        }
                    }

                    // Get number of wrong columns
                    int missingConstraintsCount = constraintsAnalysis.getMissingConstraints().size();
                    int surplusConstraintsCount = constraintsAnalysis.getSurplusConstraints().size();

                    // Check on diagnose level
                    if(config.getDiagnoseLevel() == 1) {
                        // Check for unique constraints
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

                        // Check for check constraints
                        if(!constraintsAnalysis.isDmlStatementsWithMistakesEmpty()) {
                            description.append(LS);
                            if(isGermanLocale(locale)) {
                                description.append("Es gibt Fehler bei den check Constraints in den Tabellen.");
                            } else {
                                description.append("There are errors with check constraints in the tables.");
                            }
                        }
                    }

                    if(config.getDiagnoseLevel() == 2) {
                        // Check for unique constraints
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

                        // Check for check constraints
                        if(!constraintsAnalysis.isDmlStatementsWithMistakesEmpty()) {
                            description.append(LS);
                            if(isGermanLocale(locale)) {
                                description.append("Es gibt Fehler bei den check Constraints in den Tabellen.");
                            } else {
                                description.append("There are errors with check constraints in the tables.");
                            }
                        }
                    }

                    if(config.getDiagnoseLevel() == 3) {
                        // Check for unique constraints
                        if(!constraintsAnalysis.isMissingConstraintsEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Die folgenden ").append(missingConstraintsCount).append(" unique Constraints sind zu wenig: ").append(LS);
                                header1 = TABELLE;
                                header2 = SPALTE;
                            } else {
                                description.append("The following ").append(missingConstraintsCount).append(" missing unique constraints are missing: ").append(LS);
                                header1 = TABLE;
                                header2 = COLUMN;
                            }

                            report.setMissingUniqueConstraints(constraintsAnalysis.getMissingConstraints());

                            constraintsIterator = constraintsAnalysis.iterMissingConstraints();
                            description.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\" align=\"center\"><tr><th>").append(header1).append("</th><th>").append(header2).append("</th></tr>");
                            while(constraintsIterator.hasNext()) {
                                ErrorTupel tupel = constraintsIterator.next();
                                description.append("<tr>").append("<td>").append(tupel.getSource()).append("</td><td>").append(tupel.getError()).append("</td></tr>");
                            }
                            description.append("</table>");
                        }

                        if(!constraintsAnalysis.isMissingConstraintsEmpty() && !constraintsAnalysis.isSurplusConstraintsEmpty()) {
                            description.append(LS).append(LS);
                        }

                        if(!constraintsAnalysis.isSurplusConstraintsEmpty()) {
                            if(isGermanLocale(locale)) {
                                description.append("Die folgenden ").append(surplusConstraintsCount).append(" unique Constraints sind zu viel: ").append(LS);
                                header1 = TABELLE;
                                header2 = SPALTE;
                            } else {
                                description.append("The following ").append(surplusConstraintsCount).append(" unique Constraints are too much: ").append(LS);
                                header1 = TABLE;
                                header2 = COLUMN;
                            }

                            report.setSurplusUniqueConstraints(constraintsAnalysis.getSurplusConstraints());

                            constraintsIterator = constraintsAnalysis.iterSurplusConstraints();
                            description.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\" align=\"center\"><tr><th>").append(header1).append("</th><th>").append(header2).append("</th></tr>");
                            while(constraintsIterator.hasNext()) {
                                ErrorTupel tupel = constraintsIterator.next();
                                description.append("<tr>").append("<td>").append(tupel.getSource()).append("</td><td>").append(tupel.getError()).append("</td></tr>");
                            }
                            description.append("</table>");
                        }

                        // Check for check constraints
                        if(!constraintsAnalysis.isDmlStatementsWithMistakesEmpty()) {
                            description.append(LS);
                            if(isGermanLocale(locale)) {
                                description.append("Es gibt Fehler bei den check Constraints in den Tabellen bei den folgenden Insert-Statements: ").append(LS);
                            } else {
                                description.append("There are errors with check constraints in the tables with the following insert statements: ").append(LS);
                            }

                            report.setWrongInsertStatements(constraintsAnalysis.getDmlStatementsWithMistakes());

                            checkConstraintsIterator = constraintsAnalysis.iterDmlStatementsWithMistakes();
                            while(checkConstraintsIterator.hasNext()) {
                                description.append(checkConstraintsIterator.next()).append(LS);
                            }
                        }
                    }
                }

                if ((error.toString().length() != 0) || (description.toString().length() != 0)) {
                    errorReport.setError(error.toString());
                    errorReport.setDescription(description.toString());

                    String tempDesc = report.getDescription();
                    tempDesc = tempDesc + description + LS;
                    report.setDescription(tempDesc);

                    String tempErr = report.getError();
                    tempErr = tempErr + error + LS;
                    report.setError(tempErr);

                    report.addErrorReport(errorReport);
                }
            }
        }

        // Configure displayed information
        report.setShowErrorReports(true);

        if (config.getDiagnoseLevel() >= 1) {
            report.setShowErrorDescriptions(true);
        }

        return report;
    }

    /**
     * Returns whether the locale equals Locale.GERMAN
     * @param locale Locale information
     * @return Returns a boolean
     */
    private boolean isGermanLocale(Locale locale){
        return locale == Locale.GERMAN;
    }

}
