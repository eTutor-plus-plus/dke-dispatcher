package at.jku.dke.etutor.modules.ddl.analysis;

import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TablesAnalysis extends AbstractDDLCriterionAnalysis implements DDLCriterionAnalysis{
    //region Fields
    private List<String> missingTables;
    private List<String> surplusTables;
    //endregion

    public TablesAnalysis() {
        this.missingTables = new ArrayList<>();
        this.surplusTables = new ArrayList<>();
    }

    public boolean isMissingTablesEmpty() {
        return this.missingTables.isEmpty();
    }

    public boolean isSurplusTablesEmpty() {
        return this.surplusTables.isEmpty();
    }

    public Iterator<String> iterMissingTables() {
        return this.missingTables.iterator();
    }

    public Iterator<String> iterSurplusTables() {
        return this.surplusTables.iterator();
    }

    @Override
    public DDLEvaluationCriterion getEvaluationCriterion() {
        return null;
    }

    //region Getter/Setter
    public List<String> getMissingTables() {
        return missingTables;
    }

    public void setMissingTables(List<String> missingTables) {
        this.missingTables = missingTables;
    }

    public void addMissingTables(String missingTable) {
        this.missingTables.add(missingTable);
    }

    public void removeMissingTables(List<String> tables) {
        this.missingTables.remove(tables);
    }

    public List<String> getSurplusTables() {
        return surplusTables;
    }

    public void setSurplusTables(List<String> surplusTables) {
        this.surplusTables = surplusTables;
    }

    public void addSurplusTable(String surplusTable) {
        this.surplusTables.add(surplusTable);
    }

    public void removeSurplusTables(List<String> tables) {
        this.surplusTables.remove(tables);
    }
    //endregion
}
