package at.jku.dke.etutor.modules.rt;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RTSolution {
    @JsonProperty("gewichtungLoesung")
    private int gewichtungLoesung;
    @JsonProperty("gewichtungRelationen")
    private int[] gewichtungRelationen;
    @JsonProperty("solution")
    private String[] solution;

    int maxPoints;
    int points;
    int weightPK;
    int weightDependencies;
    int weightAttributes;

    public int getGewichtungLoesung() {
        return gewichtungLoesung;
    }

    public void setGewichtungLoesung(int gewichtungLoesung) {
        this.gewichtungLoesung = gewichtungLoesung;
    }

    public int[] getGewichtungRelationen() {
        return gewichtungRelationen;
    }

    public void setGewichtungRelationen(int[] gewichtungRelationen) {
        this.gewichtungRelationen = gewichtungRelationen;
    }

    public String[] getSolution() {
        return solution;
    }

    public void setSolution(String[] solution) {
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

    public int getWeightPK() {
        return weightPK;
    }

    public void setWeightPK(int weightPK) {
        this.weightPK = weightPK;
    }

    public int getWeightDependencies() {
        return weightDependencies;
    }

    public void setWeightDependencies(int weightDependencies) {
        this.weightDependencies = weightDependencies;
    }

    public int getWeightAttributes() {
        return weightAttributes;
    }

    public void setWeightAttributes(int weightAttributes) {
        this.weightAttributes = weightAttributes;
    }
}
