package at.jku.dke.etutor.modules.sql.analysis;

import java.util.Iterator;
import java.util.Vector;

import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;

public class ColumnsAnalysis extends AbstractSQLCriterionAnalysis implements SQLCriterionAnalysis{

	private Vector missingColumns;
	private Vector surplusColumns;
	private boolean foundIncorrectColumns;

	public ColumnsAnalysis() {
		super();
		this.foundIncorrectColumns = false;
		this.missingColumns = new Vector();
		this.surplusColumns = new Vector();
	}
	
	public boolean hasMissingColumns(){
		return this.missingColumns.size() > 0;
	}
	
	public boolean hasSurplusColumns(){
		return this.surplusColumns.size() > 0;
	}
	
	public boolean foundIncorrectColumns(){
		return this.foundIncorrectColumns;
	}
	
	public void setFoundIncorrectColumns(boolean foundIncorrectColumns){
		this.foundIncorrectColumns = foundIncorrectColumns;
	}

	public void addMissingColumn(String columnName){
		this.missingColumns.add(columnName);
	}
	
	public void addSurplusColumn(String columnName){
		this.surplusColumns.add(columnName);
	}
	
	public void removeMissingColumn(String columnName){
		this.missingColumns.remove(columnName);
	}

	public void removeSurplusColumn(String columnName){
		this.surplusColumns.remove(columnName);
	}
	
	public Iterator iterMissingColumns(){
		return this.missingColumns.iterator();
	}
	
	public Iterator iterSurplusColumns(){
		return this.surplusColumns.iterator();
	}
	
	public Vector getMissingColumns(){
		return (Vector)this.missingColumns.clone();
	}

	public Vector getSurplusColumns(){
		return (Vector)this.surplusColumns.clone();
	}
	
	public SQLEvaluationCriterion getEvaluationCriterion(){
		return SQLEvaluationCriterion.CORRECT_COLUMNS;
	}
}
