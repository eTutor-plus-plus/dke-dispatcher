package at.jku.dke.etutor.modules.pm.analysis;

import at.jku.dke.etutor.modules.pm.AlphaAlgo.Event;
import at.jku.dke.etutor.modules.pm.PmEvaluationCriterion;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class EventAnalysis extends AbstractPmCriterionAnalysis implements PmCriterionAnalysis{

    // OBJECT FIELDS
    private final SortedSet<Event> missingEvents;
    private final SortedSet<Event> surplusEvents;

    // CONSTRUCTOR
    public EventAnalysis(){
        super();

        missingEvents = new TreeSet<>();
        surplusEvents = new TreeSet<>();
    }

    // METHODS
    public void addMissingEvents(Set<Event> missingEvents){
        this.missingEvents.addAll(missingEvents);
    }
    public void addSurplusEvents(Set<Event> surplusEvents){
        this.surplusEvents.addAll(surplusEvents);
    }

    public SortedSet<Event> getMissingEvents() {
        return missingEvents;
    }
    public SortedSet<Event> getSurplusEvents() {
        return surplusEvents;
    }

    public boolean hasMissingEvents(){
        return !this.missingEvents.isEmpty();
    }
    public boolean hasSurplusEvents(){
        return !this.surplusEvents.isEmpty();
    }

    @Override
    public PmEvaluationCriterion getEvaluationCriterion() {
        return PmEvaluationCriterion.CORRECT_EVENT;
    }





}
