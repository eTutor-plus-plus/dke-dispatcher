package at.jku.dke.etutor.modules.sql.analysis;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;

public class TuplesAnalysis extends AbstractSQLCriterionAnalysis implements SQLCriterionAnalysis{

	private Vector missingTuples;
	private Vector surplusTuples;
	private Vector columnLabels;
	

	public TuplesAnalysis() {
		super();
		this.missingTuples = new Vector();
		this.surplusTuples = new Vector();
		this.columnLabels = new Vector();
	}
	
	public void addColumnLabel(String columnLabel){
		this.columnLabels.add(columnLabel);
	}

	public Iterator iterColumnLabels(){
		return this.columnLabels.iterator();
	}
	
	public void setColumnLabels(Vector queryResultColumnLabels) {
		this.columnLabels = queryResultColumnLabels;
	}

	public Vector getColumnLabels() {
		return (Vector)this.columnLabels.clone();
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
	
	public void setMissingTuples(Collection missingTuples){
		this.missingTuples.clear();
		this.missingTuples.addAll(missingTuples);
	}

	public void addMissingTuple(Collection tupleAttributes){
		this.missingTuples.add(tupleAttributes);
	}
	
	public void setSurplusTuples(Collection surplusTuples){
		this.surplusTuples.clear();
		this.surplusTuples.addAll(surplusTuples);
	}

	public void addSurplusTuple(Collection tupleAttributes){
		this.surplusTuples.add(tupleAttributes);
	}
	
	public void removeAllMissingTuples(Collection missingTuplesToRemove){
		this.missingTuples.removeAll(missingTuplesToRemove);
	}

	public void removeMissingTuple(Collection tupleAttributes){
		this.missingTuples.remove(tupleAttributes);
	}

	public void removeAllSurplusTuples(Collection surplusTuplesToRemove){
		this.surplusTuples.removeAll(surplusTuplesToRemove);
	}

	public void removeSurplusTuple(Collection tupleAttributes){
		this.surplusTuples.remove(tupleAttributes);
	}
	
	public Iterator iterMissingTuples(){
		return this.missingTuples.iterator();
	}
	
	public Iterator iterSurplusTuples(){
		return this.surplusTuples.iterator();
	}
	
	public Vector getMissingTuples(){
		return (Vector)this.missingTuples.clone();
	}

	public Vector getSurplusTuples(){
		return (Vector)this.surplusTuples.clone();
	}

	public SQLEvaluationCriterion getEvaluationCriterion(){
		return SQLEvaluationCriterion.CORRECT_TUPLES;
	}
}
