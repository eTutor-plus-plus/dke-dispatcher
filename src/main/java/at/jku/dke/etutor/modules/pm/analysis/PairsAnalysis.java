package at.jku.dke.etutor.modules.pm.analysis;

import at.jku.dke.etutor.modules.pm.AlphaAlgo.Event;
import at.jku.dke.etutor.modules.pm.AlphaAlgo.Pair;
import at.jku.dke.etutor.modules.pm.PmEvaluationCriterion;

import java.util.ArrayList;
import java.util.List;

public class PairsAnalysis extends AbstractPmCriterionAnalysis implements PmCriterionAnalysis{

    // OBJECT FIELDS
    private final List<Pair<Event, Event>> missingPairs;
    private final List<Pair<Event, Event>> surplusPairs;

    // CONSTRUCTOR
    public PairsAnalysis(){
        super();

        missingPairs = new ArrayList<>();
        surplusPairs = new ArrayList<>();
    }

    // METHODS
    public void addMissingPairs(List<Pair<Event, Event>> missingPairs){
        this.missingPairs.addAll(missingPairs);
    }
    public void addSurplusPairs(List<Pair<Event, Event>> surplusPairs){
        this.surplusPairs.addAll(surplusPairs);
    }

    public List<Pair<Event, Event>> getSurplusPairs() {
        return surplusPairs;
    }
    public List<Pair<Event, Event>> getMissingPairs() {
        return missingPairs;
    }

    public boolean hasMissingPairs(){
        return !this.missingPairs.isEmpty();
    }
    public boolean hasSurplusPairs(){
        return !this.surplusPairs.isEmpty();
    }

    @Override
    public PmEvaluationCriterion getEvaluationCriterion() {
        return PmEvaluationCriterion.CORRECT_PAIRS;
    }

}
