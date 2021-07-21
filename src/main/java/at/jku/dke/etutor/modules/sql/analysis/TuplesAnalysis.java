package at.jku.dke.etutor.modules.sql.analysis;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;

public class TuplesAnalysis extends AbstractSQLCriterionAnalysis implements SQLCriterionAnalysis{

	private final Vector<Collection<String>> missingTuples;
	private final Vector<Collection<String>> surplusTuples;
	private Vector<String> columnLabels;
	

	public TuplesAnalysis() {
		super();
		this.missingTuples = new Vector<>();
		this.surplusTuples = new Vector<>();
		this.columnLabels = new Vector<>();
	}
	
	public void addColumnLabel(String columnLabel){
		this.columnLabels.add(columnLabel);
	}

	public Iterator<String> iterColumnLabels(){
		return this.columnLabels.iterator();
	}
	
	public void setColumnLabels(Vector<String> queryResultColumnLabels) {
		this.columnLabels = queryResultColumnLabels;
	}

	public Vector<String> getColumnLabels() {
		return (Vector<String>)this.columnLabels.clone();
	}

	public boolean hasSurplusTuples(){
		return this.surplusTuples.size() > 0;
	}
	
	public boolean hasMissingTuples(){
		return this.missingTuples.size() > 0;
	}
	
	public boolean foundIncorrectTuples(){
		return (this.missingTuples.size() > 0 || this.surplusTuples.size() > 0);
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

	public void removeSurplusTuple(Collection tupleAttributes){
		this.surplusTuples.remove(tupleAttributes);
	}
	
	public Iterator<Collection<String>> iterMissingTuples(){
		return this.missingTuples.iterator();
	}
	
	public Iterator<Collection<String>> iterSurplusTuples(){
		return this.surplusTuples.iterator();
	}
	
	public Vector<Collection<String>> getMissingTuples(){
		return (Vector)this.missingTuples.clone();
	}

	public Vector<Collection<String>> getSurplusTuples(){
		return (Vector)this.surplusTuples.clone();
	}

	public SQLEvaluationCriterion getEvaluationCriterion(){
		return SQLEvaluationCriterion.CORRECT_TUPLES;
	}
}
