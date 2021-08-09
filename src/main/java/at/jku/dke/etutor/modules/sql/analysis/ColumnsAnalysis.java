package at.jku.dke.etutor.modules.sql.analysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;

/**
 * The SQLCriterionAnalysis with regards to the SQLEvaluationCriterion.CORRECT_COLUMNS
 */
public class ColumnsAnalysis extends AbstractSQLCriterionAnalysis implements SQLCriterionAnalysis{

	private final List<String> missingColumns;
	private final List<String> surplusColumns;
	private boolean foundIncorrectColumns;

	public ColumnsAnalysis() {
		super();
		this.foundIncorrectColumns = false;
		this.missingColumns = new ArrayList<>();
		this.surplusColumns = new ArrayList<>();
	}
	
	public boolean hasMissingColumns(){
		return !this.missingColumns.isEmpty();
	}
	
	public boolean hasSurplusColumns(){
		return !this.surplusColumns.isEmpty();
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
	
	public List<String> getMissingColumns(){
		return new ArrayList<>(missingColumns);
	}

	public List<String> getSurplusColumns(){
		return new ArrayList<>(surplusColumns);
	}
	
	public SQLEvaluationCriterion getEvaluationCriterion(){
		return SQLEvaluationCriterion.CORRECT_COLUMNS;
	}
}
