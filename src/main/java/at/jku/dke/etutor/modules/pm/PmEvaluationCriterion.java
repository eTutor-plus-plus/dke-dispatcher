package at.jku.dke.etutor.modules.pm;

import java.util.Objects;

public class PmEvaluationCriterion {

    private String name;

    // ordering relations criteria
    public static final PmEvaluationCriterion CORRECT_PAIRS = new PmEvaluationCriterion("CORRECT PAIRS");

    // alpha algorithm
    public static final PmEvaluationCriterion CORRECT_EVENT = new PmEvaluationCriterion("CORRECT EVENT");
    public static final PmEvaluationCriterion CORRECT_PAIRS_ALPHA = new PmEvaluationCriterion("CORRECT PAIRS ALPHA");

    protected PmEvaluationCriterion(String name){
        super();
        this.name = name;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }
        if(!(obj instanceof PmEvaluationCriterion)){
            return false;
        }

        return this.name.equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return -1;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
