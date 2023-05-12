package at.jku.dke.etutor.modules.jdbc.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;

import java.util.*;

public class DBAnalysis extends DefaultAnalysis implements Analysis {

    private Vector tableAnalyses;
    private Vector missingTables;
    private Vector additionalTables;

    public DBAnalysis() {
        super();

        this.missingTables = new Vector();
        this.tableAnalyses = new Vector();
        this.additionalTables = new Vector();
        this.setSubmissionSuitsSolution(true);
    }

    public boolean hasMissingOrAdditionalTables(){
        return ((this.missingTables.size() > 0) || (this.additionalTables.size() > 0));
    }

    public void addTableAnalysis(TableAnalysis analysis){
        this.tableAnalyses.add(analysis);
    }

    public Iterator iterTableAnalyses(){
        return this.tableAnalyses.iterator();
    }

    public Vector getTableAnalyses(){
        return (Vector)this.tableAnalyses.clone();
    }

    public void addMissingTable(String name){
        this.missingTables.add(name);
    }

    public Iterator iterMissingTables(){
        return this.missingTables.iterator();
    }

    public Vector getMissingTables(){
        return (Vector)this.missingTables.clone();
    }

    public void setMissingTables(Collection tables){
        this.missingTables.clear();
        this.missingTables.addAll(tables);
    }


    public void addAdditionalTable(String name){
        this.additionalTables.add(name);
    }

    public Iterator iterAdditionalTables(){
        return this.additionalTables.iterator();
    }

    public Vector getAdditionalTables(){
        return (Vector) this.additionalTables.clone();
    }

    public void setAdditionalTables(Collection tables){
        this.additionalTables.clear();
        this.additionalTables.addAll(tables);
    }

}
