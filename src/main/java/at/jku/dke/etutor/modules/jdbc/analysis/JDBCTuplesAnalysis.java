package at.jku.dke.etutor.modules.jdbc.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class JDBCTuplesAnalysis extends DefaultAnalysis implements Analysis {

    private Vector columnLabels;
    private Vector missingTuples;
    private Vector additionalTuples;

    public JDBCTuplesAnalysis() {
        super();
        this.columnLabels = new Vector();
        this.missingTuples = new Vector();
        this.additionalTuples = new Vector();
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

    public boolean hasAdditionalTuples(){
        return this.additionalTuples.size() > 0;
    }

    public boolean hasMissingTuples(){
        return this.missingTuples.size() > 0;
    }

    public boolean foundIncorrectTuples(){
        return (this.missingTuples.size() > 0 || this.additionalTuples.size() > 0);
    }

    public void setMissingTuples(Collection missingTuples){
        this.missingTuples.clear();
        this.missingTuples.addAll(missingTuples);
    }

    public void addMissingTuple(Collection tupleAttributes){
        this.missingTuples.add(tupleAttributes);
    }

    public void setAdditionalTuples(Collection additionalTuples){
        this.additionalTuples.clear();
        this.additionalTuples.addAll(additionalTuples);
    }

    public void addAdditionalTuple(Collection tupleAttributes){
        this.additionalTuples.add(tupleAttributes);
    }

    public void removeAllMissingTuples(Collection missingTuplesToRemove){
        this.missingTuples.removeAll(missingTuplesToRemove);
    }

    public void removeMissingTuple(Collection tupleAttributes){
        this.missingTuples.remove(tupleAttributes);
    }

    public void removeAllAdditionalTuples(Collection additionalTuplesToRemove){
        this.additionalTuples.removeAll(additionalTuplesToRemove);
    }

    public void removeAdditionalTuple(Collection tupleAttributes){
        this.additionalTuples.remove(tupleAttributes);
    }

    public Iterator iterMissingTuples(){
        return this.missingTuples.iterator();
    }

    public Iterator iterAdditionalTuples(){
        return this.additionalTuples.iterator();
    }

    public Vector getMissingTuples(){
        return (Vector)this.missingTuples.clone();
    }

    public Vector getAdditionalTuples(){
        return (Vector)this.additionalTuples.clone();
    }
}
