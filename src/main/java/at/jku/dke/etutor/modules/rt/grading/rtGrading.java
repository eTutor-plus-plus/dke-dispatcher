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
        if (this.rtAnalysis.getHasSyntaxError()){
            this.setPoints(0);
        }
        else{
            int points = 0;
            for(RTSolution rtSolution : this.rtAnalysis.getSolution()){
                points = points + rtSolution.getRtSemanticsAnalysis().getTotalPoints();
            }
            System.err.println(points);
            this.setPoints(points);
        }
    }


}
