package at.jku.dke.etutor.modules.pm.analysis;

import at.jku.dke.etutor.modules.pm.AlphaAlgo.Arc;
import at.jku.dke.etutor.modules.pm.PmEvaluationCriterion;

import java.util.ArrayList;
import java.util.List;

public class AlphaArcsAnalysis extends AbstractPmCriterionAnalysis implements PmCriterionAnalysis{

    // OBJECT FIELD
    private final List<Arc<?,?>> missingAlphaArcs;
    private final List<Arc<?,?>> surplusAlphaArcs;

    // CONSTRUCTOR
    public AlphaArcsAnalysis(){
        super();

        missingAlphaArcs = new ArrayList<>();
        surplusAlphaArcs = new ArrayList<>();
    }

    // METHODS
    public void addMissingAlphaArcs(List<Arc<?,?>> missingAlphaArcs){
        this.missingAlphaArcs.addAll(missingAlphaArcs);
    }
    public void addSurplusAlphaArcs(List<Arc<?,?>> surplusAlphaArcs){
        this.surplusAlphaArcs.addAll(surplusAlphaArcs);
    }

    public List<Arc<?, ?>> getMissingAlphaArcs() {
        return missingAlphaArcs;
    }

    public List<Arc<?, ?>> getSurplusAlphaArcs() {
        return surplusAlphaArcs;
    }
    public boolean hasMissingAlphaArcs(){
        return !this.missingAlphaArcs.isEmpty();
    }
    public boolean hasSurplusAlphaArcs(){
        return !this.surplusAlphaArcs.isEmpty();
    }


    @Override
    public PmEvaluationCriterion getEvaluationCriterion() {
        return null;
    }




}
