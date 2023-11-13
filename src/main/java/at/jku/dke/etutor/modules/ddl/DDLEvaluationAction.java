package at.jku.dke.etutor.modules.ddl;

import at.jku.dke.etutor.modules.sql.SQLEvaluationAction;

public class DDLEvaluationAction {
    //region Constants
    public static final DDLEvaluationAction RUN = new DDLEvaluationAction("RUN");
    public static final DDLEvaluationAction CHECK = new DDLEvaluationAction("CHECK");
    public static final DDLEvaluationAction SUBMIT = new DDLEvaluationAction("SUBMIT");
    public static final DDLEvaluationAction DIAGNOSE = new DDLEvaluationAction("DIAGNOSE");
    //endregion

    //region Fields
    private String action;
    //endregion

    protected DDLEvaluationAction(String action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof SQLEvaluationAction))
            return false;

        return this.action.equals(obj.toString());
    }

    @Override
    public String toString() {
        return this.action;
    }
}
