package at.jku.dke.etutor.modules.rt;

import at.jku.dke.etutor.modules.rt.analysis.RTSemanticsAnalysis;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RTSolution {
    @JsonProperty("gesamtGewichtung")
    private int gesamtGewichtung;
    @JsonProperty("punktePK")
    private int[] punktePK;
    @JsonProperty("punkteAtt")
    private int[] punkteAtt;
    @JsonProperty("punkteInk")
    private int[] punkteInk;
    @JsonProperty("solution")
    private Map<String, List<String>> solution;
    int maxPoints;
    int points;
    List<String> solutionStudent;
    RTSemanticsAnalysis rtSemanticsAnalysis;

    public void initAnalyse(List<String> solutionStudent){
        this.solutionStudent = solutionStudent;
        List<Integer> weighting = new ArrayList<>();
        weighting.add(this.punktePK[0]);
        weighting.add(this.punkteAtt[0]);
        weighting.add(this.punkteAtt[0]);
        if(solution.size() == 1) {
            this.rtSemanticsAnalysis = new RTSemanticsAnalysis(solutionStudent, solution.get("var1"), weighting);
        }
        else{
            List<RTSemanticsAnalysis> rtSemanticsAnalyses = new ArrayList<>();
            for (Map.Entry<String,List<String>> entry : this.solution.entrySet()){
                rtSemanticsAnalyses.add(new RTSemanticsAnalysis(solutionStudent, entry.getValue(),weighting));
            }
            int maxAchievedPoints = 0;
            RTSemanticsAnalysis rtSemanticsAnalysisToUse = null;
            for (RTSemanticsAnalysis elem: rtSemanticsAnalyses){
                System.err.println(elem.getTotalPoints());
                System.out.println(elem.getRelations().toString());
                if (elem.getTotalPoints()>maxAchievedPoints){
                    maxAchievedPoints = elem.getTotalPoints();
                    rtSemanticsAnalysisToUse = elem;
                }
            }
            this.rtSemanticsAnalysis = rtSemanticsAnalysisToUse;
        }
    }

    public int getGesamtGewichtung() {
        return gesamtGewichtung;
    }

    public void setGesamtGewichtung(int gesamtGewichtung) {
        this.gesamtGewichtung = gesamtGewichtung;
    }

    public int[] getPunktePK() {
        return punktePK;
    }

    public void setPunktePK(int[] punktePK) {
        this.punktePK = punktePK;
    }

    public int[] getPunkteAtt() {
        return punkteAtt;
    }

    public void setPunkteAtt(int[] punkteAtt) {
        this.punkteAtt = punkteAtt;
    }

    public int[] getPunkteInk() {
        return punkteInk;
    }

    public void setPunkteInk(int[] punkteInk) {
        this.punkteInk = punkteInk;
    }

    public Map<String, List<String>> getSolution() {
        return solution;
    }

    public void setSolution(Map<String, List<String>> solution) {
        this.solution = solution;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<String> getSolutionStudent() {
        return solutionStudent;
    }

    public void setSolutionStudent(List<String> solutionStudent) {
        this.solutionStudent = solutionStudent;
    }

    public RTSemanticsAnalysis getRtSemanticsAnalysis() {
        return rtSemanticsAnalysis;
    }

    public void setRtSemanticsAnalysis(RTSemanticsAnalysis rtSemanticsAnalysis) {
        this.rtSemanticsAnalysis = rtSemanticsAnalysis;
    }

    @Override
    public String toString() {
        return "RTSolution{" +
                "gesamtGewichtung=" + gesamtGewichtung +
                ", punktePK=" + Arrays.toString(punktePK) +
                ", punkteAtt=" + Arrays.toString(punkteAtt) +
                ", punkteInk=" + Arrays.toString(punkteInk) +
                ", solution=" + solution +
                ", maxPoints=" + maxPoints +
                ", points=" + points +
                ", solutionStudent=" + solutionStudent +
                ", rtSemanticsAnalysis=" + rtSemanticsAnalysis +
                '}';
    }
}
