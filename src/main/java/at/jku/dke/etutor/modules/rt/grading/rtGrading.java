package at.jku.dke.etutor.modules.rt.grading;

import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.modules.rt.RTSolution;
import at.jku.dke.etutor.modules.rt.analysis.RTAnalysis;

public class rtGrading extends DefaultGrading{

    RTAnalysis rtAnalysis;
    public rtGrading(RTAnalysis rtAnalysis, int maxPoints) {
        super();
        this.setMaxPoints(maxPoints);
        this.rtAnalysis = rtAnalysis;
        this.doGrading();
    }

    public void doGrading(){
        this.setPoints(rtAnalysis.calcPoints());
    }


}
