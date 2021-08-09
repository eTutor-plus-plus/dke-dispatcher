package at.jku.dke.etutor.modules.sql.analysis;

import java.util.*;

import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;

/**
 * The SQLCriterionAnalysis for the SQLEvaluationCriterion.CORRECT_TUPLES
 */
public class TuplesAnalysis extends AbstractSQLCriterionAnalysis implements SQLCriterionAnalysis{

	private final List<Collection<String>> missingTuples;
	private final List<Collection<String>> surplusTuples;
	private List<String> columnLabels;
	

	public TuplesAnalysis() {
		super();
		this.missingTuples = new ArrayList<>();
		this.surplusTuples = new ArrayList<>();
		this.columnLabels = new ArrayList<>();
	}

	public void addColumnLabel(String columnLabel){
		this.columnLabels.add(columnLabel);
	}

	public Iterator<String> iterColumnLabels(){
		return this.columnLabels.iterator();
	}
	
	public void setColumnLabels(List<String> queryResultColumnLabels) {
		this.columnLabels = queryResultColumnLabels;
	}

	public List<String> getColumnLabels() {
		return new ArrayList<>(columnLabels);
	}

	public boolean hasSurplusTuples(){
		return !this.surplusTuples.isEmpty();
	}
	
	public boolean hasMissingTuples(){
		return !this.missingTuples.isEmpty();
	}
	
	public boolean foundIncorrectTuples(){
		return (!this.missingTuples.isEmpty() || !this.surplusTuples.isEmpty());
	}
	
	public void setMissingTuples(Collection<Collection<String>> missingTuples){
		this.missingTuples.clear();
		this.missingTuples.addAll(missingTuples);
	}

	public void addMissingTuple(Collection<String> tupleAttributes){
		this.missingTuples.add(tupleAttributes);
	}
	
	public void setSurplusTuples(Collection<Collection<String>> surplusTuples){
		this.surplusTuples.clear();
		this.surplusTuples.addAll(surplusTuples);
	}

	public void addSurplusTuple(Collection<String> tupleAttributes){
		this.surplusTuples.add(tupleAttributes);
	}
	
	public void removeAllMissingTuples(Collection<Collection<String>> missingTuplesToRemove){
		this.missingTuples.removeAll(missingTuplesToRemove);
	}

	public void removeMissingTuple(Collection<String> tupleAttributes){
		this.missingTuples.remove(tupleAttributes);
	}

	public void removeAllSurplusTuples(Collection<Collection<String>> surplusTuplesToRemove){
		this.surplusTuples.removeAll(surplusTuplesToRemove);
	}

	public void removeSurplusTuple(Collection<String> tupleAttributes){
		this.surplusTuples.remove(tupleAttributes);
	}
	
	public Iterator<Collection<String>> iterMissingTuples(){
		return this.missingTuples.iterator();
	}
	
	public Iterator<Collection<String>> iterSurplusTuples(){
		return this.surplusTuples.iterator();
	}
	
	public List<Collection<String>> getMissingTuples(){
		return new ArrayList<>(missingTuples);
	}

	public List<Collection<String>> getSurplusTuples(){
		return new ArrayList<>(surplusTuples);
	}

	public SQLEvaluationCriterion getEvaluationCriterion(){
		return SQLEvaluationCriterion.CORRECT_TUPLES;
	}
}
