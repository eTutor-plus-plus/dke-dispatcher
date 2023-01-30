package at.jku.dke.etutor.modules.pm;

import java.util.Objects;

public class PmEvaluationAction {

    private String action;

    public static final PmEvaluationAction RUN = new PmEvaluationAction("RUN");
    public static final PmEvaluationAction TEST = new PmEvaluationAction("TEST");
    public static final PmEvaluationAction CHECK = new PmEvaluationAction("CHECK");
    // note: use only diagnose or submit
    public static final PmEvaluationAction SUBMIT = new PmEvaluationAction("SUBMIT");
    public static final PmEvaluationAction DIAGNOSE = new PmEvaluationAction("DIAGNOSE");

    // CONSTRUCTOR
    protected PmEvaluationAction(String action){
        super();
        this.action = action;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null ){
            return false;
        }

        if (!(obj instanceof PmEvaluationAction)) {
            return false;
        }

        return this.action.equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return -1;
    }

    @Override
    public String toString(){
        return this.action;
    }
}
