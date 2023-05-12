package at.jku.dke.etutor.modules.jdbc.analysis;

import at.jku.dke.etutor.modules.jdbc.JDBCHelper;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;

public class DBAnalyzer {

    public DBAnalyzer(){super();}

    public DBAnalysis analyze(Connection correctDBConn, Connection testDBConn) throws Exception{
        List temp;
        List testDBTables;
        List correctDBTables;
        DBAnalysis dbAnalysis;

        dbAnalysis = new DBAnalysis();
        dbAnalysis.setSubmissionSuitsSolution(true);

        //READ TABLES
        testDBTables = this.readTables(testDBConn);
        correctDBTables = this.readTables(correctDBConn);

        JDBCHelper.getLogger().log(Level.INFO, "Number of Tables in test DB: " + testDBTables.size());
        JDBCHelper.getLogger().log(Level.INFO, "Number of Tables in correct DB: " + correctDBTables.size());

        //DETERMINE MISSING AND ADDITIONAL TABLES
        temp = new Vector();
        temp.addAll(correctDBTables);
        temp.removeAll(testDBTables);
        dbAnalysis.setMissingTables(temp);

        temp = new Vector();
        temp.addAll(testDBTables);
        temp.removeAll(correctDBTables);
        dbAnalysis.setAdditionalTables(temp);

        if ((dbAnalysis.getMissingTables().size() > 0) || (dbAnalysis.getAdditionalTables().size() > 0)){
            dbAnalysis.setSubmissionSuitsSolution(false);
        }

        //ANALYZE TABLES - Columns, Column Types, Constraints, Tuples
        TableAnalysis tableAnalysis;

        if (dbAnalysis.submissionSuitsSolution()){
            for (int i=0; i<correctDBTables.size(); i++){
                tableAnalysis = this.analyzeTable(correctDBTables.get(i).toString(), correctDBConn, testDBConn);
                if (!tableAnalysis.submissionSuitsSolution()){
                    dbAnalysis.addTableAnalysis(tableAnalysis);
                    dbAnalysis.setSubmissionSuitsSolution(false);
                }
            }
        }

        return dbAnalysis;
    }

    private TableAnalysis analyzeTable(String tableName, Connection correctDBConn, Connection testDBConn) throws SQLException {
        TableAnalysis analysis;
        JDBCTuplesAnalysis tuplesAnalysis;
        JDBCColumnsAnalysis columnsAnalysis;
        DatabaseMetaData testDBMetaData = testDBConn.getMetaData();
        DatabaseMetaData correctDBMetaData = correctDBConn.getMetaData();

        analysis = new TableAnalysis();
        analysis.setSubmissionSuitsSolution(true);

        //SET THE TABLE NAME
        analysis.setTableName(tableName);

        //ANALYZE COLUMNS AND COLUMN TYPES
        columnsAnalysis = this.analyzeColumns(testDBMetaData, correctDBMetaData, tableName);
        analysis.setColumnsAnalysis(columnsAnalysis);
        if (!columnsAnalysis.submissionSuitsSolution()) {
            analysis.setSubmissionSuitsSolution(false);
            return analysis;
        }

        //ANALYZE TUPLES
        tuplesAnalysis = this.analyzeTuples(correctDBConn, testDBConn, tableName);
        analysis.setTuplesAnalysis(tuplesAnalysis);
        if (!tuplesAnalysis.submissionSuitsSolution()){
            analysis.setSubmissionSuitsSolution(false);
        }

        return analysis;
    }

    private List readTables(Connection conn) throws Exception{
            ResultSet rset = null;
            List tables = new ArrayList();

            try {
                DatabaseMetaData dbmd = conn.getMetaData();
                rset=dbmd.getTables(null ,dbmd.getUserName(), "%", new String[]{"TABLE"});
                while (rset.next()) {
                    tables.add(rset.getString(3));
                }
            } catch (Exception ex) {
                throw ex;
            } finally {
                try{
                    if(rset!=null) {
                        rset.close();
                    }
                } catch(Exception ignore){
                    ignore.printStackTrace();
                }
            }

            return tables;
    }

    private JDBCColumnsAnalysis analyzeColumns(DatabaseMetaData testDBMetaData, DatabaseMetaData correctDBMetaData, String tableName) throws SQLException {
        String column;
        ResultSet rset;
        String foundType;
        String expectedType;
        Iterator columnsIterator;
        HashMap testTableColumns;
        HashMap correctTableColumns;
        JDBCColumnsAnalysis columnsAnalysis;

        rset = null;
        testTableColumns = new HashMap();
        correctTableColumns = new HashMap();
        columnsAnalysis = new JDBCColumnsAnalysis();
        columnsAnalysis.setSubmissionSuitsSolution(true);

        try{
            //INIT COLUMNS INFO OF TEST TABLE
            rset = testDBMetaData.getColumns(null, testDBMetaData.getUserName(), tableName, "%");
            while (rset.next()){
                testTableColumns.put(rset.getString("COLUMN_NAME"), rset.getString("TYPE_NAME"));
            }

            //INIT COLUMNS INFO OF CORRECT TABLE
            rset.close();
            rset = correctDBMetaData.getColumns(null, correctDBMetaData.getUserName(), tableName, "%");
            while (rset.next()){
                correctTableColumns.put(rset.getString("COLUMN_NAME"), rset.getString("TYPE_NAME"));
            }

            //DETERMINING MISSING COLUMNS AND WRONG COLUMN TYPES
            columnsIterator = correctTableColumns.keySet().iterator();
            while (columnsIterator.hasNext()){
                column = (String)columnsIterator.next();
                if (testTableColumns.containsKey(column)){
                    expectedType = (String)correctTableColumns.get(column);
                    foundType = (String)testTableColumns.get(column);

                    if (!expectedType.equals(foundType)){
                        columnsAnalysis.addWrongColumnType(column, expectedType, foundType);
                        columnsAnalysis.setSubmissionSuitsSolution(false);
                    }
                } else {
                    columnsAnalysis.addMissingColumn(column);
                    columnsAnalysis.setSubmissionSuitsSolution(false);
                }
            }

            //DETERMINING ADDITIONAL COLUMNS
            columnsIterator = testTableColumns.keySet().iterator();
            while (columnsIterator.hasNext()){
                column = (String)columnsIterator.next();
                if (!correctTableColumns.containsKey(column)){
                    columnsAnalysis.addAdditionalColumn(column);
                    columnsAnalysis.setSubmissionSuitsSolution(false);
                }
            }

        } catch (SQLException e){
            throw e;
        } finally {
            try{
                if (rset != null){
                    rset.close();
                }
            } catch (Exception ignore){
                ignore.printStackTrace();
            }
        }

        return columnsAnalysis;
    }

    private JDBCTuplesAnalysis analyzeTuples(Connection correctDBConn, Connection testDBConn, String tableName) throws SQLException{

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
        JDBCTuplesAnalysis tuplesAnalysis;

        checkQuery_RSET = null;
        correctQuery_RSET = null;
        correctQuery_STMT = null;
        submittedQuery_STMT = null;

        correctQueryTuples = new Vector();
        submittedQueryTuples = new Vector();
        tuplesAnalysis = new JDBCTuplesAnalysis();
        tuplesAnalysis.setSubmissionSuitsSolution(true);

        try {
            correctQuery_STMT = correctDBConn.createStatement();
            correctQuery_RSET = correctQuery_STMT.executeQuery("SELECT * FROM " + tableName);
            rsmd = correctQuery_RSET.getMetaData();

            columns = new String();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                tuplesAnalysis.addColumnLabel(rsmd.getColumnName(i));
                if (columns.length() > 0) {
                    columns = columns.concat(", ");
                }
                columns = columns.concat(rsmd.getColumnName(i));
            }

            JDBCHelper.getLogger().log(Level.INFO, "COLUMN LABELS: " + columns);

            checkQuery = new String();
            checkQuery = checkQuery.concat("SELECT " + columns + " ");
            checkQuery = checkQuery.concat("FROM (SELECT * FROM " + tableName + ") ");
            checkQuery = checkQuery.concat("ORDER BY " + columns);

            JDBCHelper.getLogger().log(Level.INFO, "CORRECT QUERY:\n" + checkQuery);

            checkQuery_RSET = correctQuery_STMT.executeQuery(checkQuery);

            rsmd = checkQuery_RSET.getMetaData();
            columnCount = rsmd.getColumnCount();

            while (checkQuery_RSET.next()) {
                tuple = new Vector();

                for (int i = 1; i <= columnCount; i++) {
                    if (checkQuery_RSET.getString(i) != null) {
                        tuple.add(checkQuery_RSET.getString(i).toUpperCase());
                    } else {
                        tuple.add("NULL");
                    }
                }
                correctQueryTuples.addElement(tuple);
            }

            checkQuery = new String();
            checkQuery = checkQuery.concat("SELECT " + columns + " ");
            checkQuery = checkQuery.concat("FROM (SELECT * FROM " + tableName + ") ");
            checkQuery = checkQuery.concat("ORDER BY " + columns);

            JDBCHelper.getLogger().log(Level.INFO, "SUBMITTED QUERY:\n" + checkQuery);

            submittedQuery_STMT = testDBConn.createStatement();
            checkQuery_RSET = submittedQuery_STMT.executeQuery(checkQuery);

            while (checkQuery_RSET.next()) {
                tuple = new Vector();
                for (int i = 1; i <= columnCount; i++) {
                    if (checkQuery_RSET.getString(i) != null) {
                        tuple.add(checkQuery_RSET.getString(i).toUpperCase());
                    } else {
                        tuple.add("NULL");
                    }
                }
                submittedQueryTuples.addElement(tuple);
            }

            tuplesAnalysis.setMissingTuples(correctQueryTuples);
            tuplesAnalysis.removeAllMissingTuples(submittedQueryTuples);

            tuplesAnalysis.setAdditionalTuples(submittedQueryTuples);
            tuplesAnalysis.removeAllAdditionalTuples(correctQueryTuples);

            if ((tuplesAnalysis.getAdditionalTuples().size() > 0) || (tuplesAnalysis.getMissingTuples().size() > 0)){
                tuplesAnalysis.setSubmissionSuitsSolution(false);
            }

            JDBCHelper.getLogger().log(Level.INFO, "Finished checking tuples of query.");

        } catch (SQLException e) {
            throw e;
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
            } catch (SQLException ignore) {
                JDBCHelper.getLogger().log(Level.SEVERE, "Could not close result set.", ignore);
            }
        }

        return tuplesAnalysis;
    }
}
