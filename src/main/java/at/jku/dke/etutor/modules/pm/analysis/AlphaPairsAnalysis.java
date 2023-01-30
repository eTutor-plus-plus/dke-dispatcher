package at.jku.dke.etutor.modules.pm.analysis;

import at.jku.dke.etutor.modules.pm.AlphaAlgo.Event;
import at.jku.dke.etutor.modules.pm.AlphaAlgo.Pair;
import at.jku.dke.etutor.modules.pm.PmEvaluationCriterion;

import java.util.ArrayList;
import java.util.List;

public class AlphaPairsAnalysis extends AbstractPmCriterionAnalysis implements PmCriterionAnalysis{

    // OBJECT FIELD
    private final List<Pair<List<Event>, List<Event>>> missingAlphaPairs;
    private final List<Pair<List<Event>, List<Event>>> surplusAlphaPairs;

    // CONSTRUCTOR
    public AlphaPairsAnalysis(){
        super();

        missingAlphaPairs = new ArrayList<>();
        surplusAlphaPairs = new ArrayList<>();
    }

    // METHODS
    public void addMissingAlphaPairs(List<Pair<List<Event>, List<Event>>> missingAlphaPairs){
        this.missingAlphaPairs.addAll(missingAlphaPairs);
    }
    public void addSurplusAlphaPairs(List<Pair<List<Event>, List<Event>>> surplusAlphaPairs){
        this.surplusAlphaPairs.addAll(surplusAlphaPairs);
    }
    public List<Pair<List<Event>, List<Event>>> getMissingAlphaPairs() {
        return missingAlphaPairs;
    }
    public List<Pair<List<Event>, List<Event>>> getSurplusAlphaPairs() {
        return surplusAlphaPairs;
    }
    public boolean hasMissingAlphaPairs(){
        return !this.missingAlphaPairs.isEmpty();
    }
    public boolean hasSurplusAlphaPairs(){
        return !this.surplusAlphaPairs.isEmpty();
    }

    @Override
    public PmEvaluationCriterion getEvaluationCriterion() {
        return PmEvaluationCriterion.CORRECT_PAIRS_ALPHA;
    }
}
