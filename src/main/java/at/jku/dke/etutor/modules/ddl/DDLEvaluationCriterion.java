package at.jku.dke.etutor.modules.ddl;

public class DDLEvaluationCriterion {
    //region Constants
    public static final DDLEvaluationCriterion CORRECT_TABLES = new DDLEvaluationCriterion("CORRECT TABLES");
    public static final DDLEvaluationCriterion CORRECT_COLUMNS = new DDLEvaluationCriterion("CORRECT COLUMNS");
    public static final DDLEvaluationCriterion CORRECT_PRIMARY_KEYS = new DDLEvaluationCriterion("CORRECT PRIMARY KEYS");
    public static final DDLEvaluationCriterion CORRECT_FOREIGN_KEYS = new DDLEvaluationCriterion("CORRECT FOREIGN KEYS");
    public static final DDLEvaluationCriterion CORRECT_CONSTRAINTS = new DDLEvaluationCriterion("CORRECT CONSTRAINTS");
    public static final DDLEvaluationCriterion CORRECT_SYNTAX = new DDLEvaluationCriterion("CORRECT SYNTAX");
    //endregion

    //region Fields
    private final String name;
    //endregion

    protected DDLEvaluationCriterion(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;


        if(!(obj instanceof DDLEvaluationCriterion))
            return false;

        return this.name.equals(obj.toString());
    }

    @Override
    public String toString() {
        return this.name;
    }
}
