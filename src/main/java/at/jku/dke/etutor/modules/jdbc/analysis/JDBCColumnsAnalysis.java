package at.jku.dke.etutor.modules.jdbc.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class JDBCColumnsAnalysis extends DefaultAnalysis implements Analysis {

    private Vector missingColumns;
    private Vector additionalColumns;
    private Vector wrongColumnTypes;

    public JDBCColumnsAnalysis() {
        super();
        this.missingColumns = new Vector();
        this.additionalColumns = new Vector();
        this.wrongColumnTypes = new Vector();
    }

    public boolean hasMissingOrAdditionalColumns(){
        return ((this.missingColumns.size() > 0) || (this.additionalColumns.size() > 0));
    }

    public void setMissingColumns(Collection missingColumns){
        this.missingColumns.clear();
        this.missingColumns.addAll(missingColumns);
    }

    public void setAdditionalColumns(Collection additionalColumns){
        this.additionalColumns.clear();
        this.additionalColumns.addAll(missingColumns);
    }

    public void removeAllAdditionalColumns(Collection additionalColumns){
        this.additionalColumns.removeAll(additionalColumns);
    }

    public void removeAllMissingColumns(Collection missingColumns){
        this.missingColumns.removeAll(missingColumns);
    }

    public boolean hasMissingColumns(){
        return this.missingColumns.size() > 0;
    }

    public boolean hasAdditionalColumns(){
        return this.additionalColumns.size() > 0;
    }

    public boolean hasWrongColumnTypes(){
        return this.wrongColumnTypes.size() > 0;
    }

    public void addMissingColumn(String columnName){
        this.missingColumns.add(columnName);
    }

    public void addAdditionalColumn(String columnName){
        this.additionalColumns.add(columnName);
    }

    public void addWrongColumnType(String columnName, String expectedType, String foundType){
        String[] wrongColumnTypeInfo = new String[3];
        wrongColumnTypeInfo[0] = columnName;
        wrongColumnTypeInfo[1] = expectedType;
        wrongColumnTypeInfo[2] = foundType;

        this.wrongColumnTypes.add(wrongColumnTypeInfo);
    }

    public void removeMissingColumn(String columnName){
        this.missingColumns.remove(columnName);
    }

    public void removeAdditionalColumn(String columnName){
        this.additionalColumns.remove(columnName);
    }

    public Iterator iterMissingColumns(){
        return this.missingColumns.iterator();
    }

    public Iterator iterAdditionalColumns(){
        return this.additionalColumns.iterator();
    }

    public Iterator iterWrongTypedColumns(){
        return this.wrongColumnTypes.iterator();
    }

    public Vector getWrongColumnTypes(){
        return (Vector)this.wrongColumnTypes.clone();
    }

    public Vector getMissingColumns(){
        return (Vector)this.missingColumns.clone();
    }

    public Vector getAdditionalColumns(){
        return (Vector)this.additionalColumns.clone();
    }
}
