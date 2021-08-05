package at.jku.dke.etutor.modules.sql.analysis;

import java.util.Iterator;
import java.util.Vector;

import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;

/**
 * The SQLCriterionAnalysis with regards to the SQLEvaluationCriterion.CORRECT_COLUMNS
 */
public class ColumnsAnalysis extends AbstractSQLCriterionAnalysis implements SQLCriterionAnalysis{

	private final Vector<String> missingColumns;
	private final Vector<String> surplusColumns;
	private boolean foundIncorrectColumns;

	public ColumnsAnalysis() {
		super();
		this.foundIncorrectColumns = false;
		this.missingColumns = new Vector<>();
		this.surplusColumns = new Vector<>();
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
	
	public Iterator<String> iterMissingColumns(){
		return this.missingColumns.iterator();
	}
	
	public Iterator<String> iterSurplusColumns(){
		return this.surplusColumns.iterator();
	}
	
	public Vector<String> getMissingColumns(){
		return (Vector<String>)this.missingColumns.clone();
	}

	public Vector<String> getSurplusColumns(){
		return (Vector<String>)this.surplusColumns.clone();
	}
	
	public SQLEvaluationCriterion getEvaluationCriterion(){
		return SQLEvaluationCriterion.CORRECT_COLUMNS;
	}
}
