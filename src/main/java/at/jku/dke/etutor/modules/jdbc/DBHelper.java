package at.jku.dke.etutor.modules.jdbc;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Vector;

public class DBHelper {
    private static String viewQuery = "select text from all_views where owner='%user%' and view_name='%view%'";
    private static String constraintQuery = "select constraint_name, table_name from user_constraints where constraint_type = 'R'";
    private static String dropTable = "DROP TABLE %table_name%";
    private static String dropView = "DROP VIEW %table_name%";
    private static String alterQuery = "ALTER TABLE %table_name% DISABLE CONSTRAINT %constraint_name%";

    public static void clearDB(Connection dbConnection) throws SQLException {
        disableConstraints(dbConnection);

        String[] types = new String[2];
        types[0] = "VIEW";
        types[1] = "TABLE";
        dropTypes(dbConnection, types);
    }

    public static void copyDB(Connection sourceDB, Connection targetDB) throws SQLException {
        Statement target = null;
        ResultSet tables = null;
        try {
            target = targetDB.createStatement();
            //create tables & views
            String[] types = new String[2];
            types[0] = "TABLE";
            types[1] = "VIEW";
            String sql = null;
            DatabaseMetaData dmd = sourceDB.getMetaData();
            tables = dmd.getTables(null, dmd.getUserName(), "%", types);
            while (tables.next()) {
                sql = buildCreateFor(sourceDB, tables.getString("TABLE_TYPE"), tables.getString("TABLE_NAME"));
                //System.out.println(sql);
                target.executeUpdate(sql);
            }
            //copy table content
            tables = dmd.getTables(null, dmd.getUserName(), "%", new String[]{"TABLE"});
            while (tables.next()) {
                String[] inserts = buildInsertsFor(sourceDB, tables.getString("TABLE_NAME"));
                for (int i = 0; i < inserts.length; i++) {
                    //System.out.println(inserts[i]);
                    target.executeUpdate(inserts[i]);
                }
            }
            //copy constraints

        } finally {
            if (target != null) target.close();
            if (tables != null) tables.close();
        }
    }

    private static String[] buildInsertsFor(Connection dbConnection, String name) throws SQLException {
        Statement stmt = null;
        ResultSet content = null;
        StringBuffer insertSQL = new StringBuffer();
        StringBuffer values = new StringBuffer();
        Vector inserts = new Vector();


        try {
            stmt = dbConnection.createStatement();
            content = stmt.executeQuery("SELECT * FROM " + name);
            ResultSetMetaData rsmd = content.getMetaData();
            while (content.next()) {
                insertSQL.append("INSERT INTO " + name + " VALUES (");
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    if (values.length() != 0) values.append(",");
                    if ((rsmd.getColumnType(i) == Types.VARCHAR) ||
                            (rsmd.getColumnType(i) == Types.CHAR) ||
                            (rsmd.getColumnType(i) == Types.LONGVARCHAR)) {
                        values.append("'");
                        values.append(content.getString(i));
                        values.append("'");
                    } else if ((rsmd.getColumnType(i) == Types.DATE) ||
                            (rsmd.getColumnType(i) == Types.TIME) ||
                            (rsmd.getColumnType(i) == Types.TIMESTAMP)) {
                        Date d = content.getDate(i);
                        values.append("TO_DATE('");
                        values.append(d.toString());
                        values.append("', 'yyyy-mm-dd')");
                    } else {
                        values.append(content.getString(i));
                    }
                }
                insertSQL.append(values);
                insertSQL.append(" )");
                inserts.add(insertSQL.toString());
                values.setLength(0);
                insertSQL.setLength(0);
            }
        } finally {
            if (content != null) content.close();
            if (stmt != null) stmt.close();
        }

        String[] retVal = new String[inserts.size()];
        inserts.copyInto(retVal);
        return retVal;
    }

    private static String buildCreateFor(Connection dbConnection, String type, String name) throws SQLException {
        ResultSet columns = null;
        StringBuffer createSQL = new StringBuffer();
        StringBuffer cols = new StringBuffer();
        DatabaseMetaData dmd = dbConnection.getMetaData();

        if (type.equalsIgnoreCase("VIEW")) {
            createSQL.append("CREATE VIEW " + name + " AS " + getViewSQL(dbConnection, name));
        } else {
            createSQL.append("CREATE TABLE " + name + " ( ");
            try {
                columns = dmd.getColumns(null, dmd.getUserName(), name, "%");
                while (columns.next()) {
                    if (cols.length() != 0) cols.append(", ");
                    cols.append(columns.getString("COLUMN_NAME"));
                    cols.append("  ");
                    cols.append(columns.getString("TYPE_NAME"));
                    if (!((columns.getInt("DATA_TYPE") == Types.DATE) ||
                            (columns.getInt("DATA_TYPE") == Types.TIME) ||
                            (columns.getInt("DATA_TYPE") == Types.TIMESTAMP))) {
                        cols.append("(");
                        cols.append(columns.getString("COLUMN_SIZE"));
                        if (columns.getString("DECIMAL_DIGITS") != null) {
                            cols.append("," + columns.getString("DECIMAL_DIGITS"));
                        }
                        cols.append(")");
                    }
                    if (columns.getString("IS_NULLABLE").equalsIgnoreCase("NO")) {
                        cols.append(" NOT NULL");
                    }
                }
            } finally {
                if (columns != null) columns.close();
            }
            createSQL.append(cols);
            createSQL.append(" )");
        }
        return createSQL.toString();
    }

    private static String getViewSQL(Connection dbConnection, String viewName) throws SQLException {
        Statement stmt1 = null;
        ResultSet rs = null;
        String query = viewQuery;
        query = query.replaceAll("%user%", dbConnection.getMetaData().getUserName());
        query = query.replaceAll("%view%", viewName);

        try {
            stmt1 = dbConnection.createStatement();
            rs = stmt1.executeQuery(query);
            if (rs.next()) {
                return rs.getString("text");
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt1 != null) stmt1.close();
        }
        return "";
    }

    public static void disableConstraints(Connection dbConnection) throws SQLException {
        Statement stmt1 = null;
        Statement stmt2 = null;
        ResultSet rs = null;
        try {
            stmt1 = dbConnection.createStatement();
            stmt2 = dbConnection.createStatement();

            //disable constraints
            rs = stmt1.executeQuery(constraintQuery);
            String tableName = null;
            String constraintName = null;
            String update = null;
            while (rs.next()) {
                tableName = rs.getString("table_name");
                constraintName = rs.getString("constraint_name");
                update = alterQuery;
                update = update.replaceFirst("%table_name%", tableName);
                update = update.replaceFirst("%constraint_name%", constraintName);
                //System.out.println(update);
                stmt2.executeUpdate(update);
            }
            rs.close();
            stmt1.close();
            stmt2.close();
        } finally {
            if (rs != null) rs.close();
            if (stmt1 != null) stmt1.close();
            if (stmt2 != null) stmt2.close();
        }
    }

    public static void dropTypes(Connection dbConnection, String[] types) throws SQLException {
        Statement stmt1 = null;
        ResultSet rs = null;
        //drop tables
        try {
            String tableName = null;
            String update = null;
            String tableType = null;
            stmt1 = dbConnection.createStatement();
            DatabaseMetaData dmd = dbConnection.getMetaData();
            for (int i = 0; i < types.length; i++) {
                rs = dmd.getTables(null, dmd.getUserName(), "%", types);
                while (rs.next()) {
                    tableName = rs.getString("TABLE_NAME");
                    tableType = rs.getString("TABLE_TYPE");
                    if (tableType.equalsIgnoreCase("VIEW")) {
                        update = dropView;
                    } else {
                        update = dropTable;
                    }
                    update = update.replaceAll("%table_name%", tableName);
                    //System.out.println(update);
                    stmt1.executeUpdate(update);
                }
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt1 != null) stmt1.close();
        }
    }

}