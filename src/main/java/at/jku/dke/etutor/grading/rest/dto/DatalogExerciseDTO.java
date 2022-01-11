package at.jku.dke.etutor.grading.rest.dto;

import at.jku.dke.etutor.modules.dlg.exercise.DatalogExerciseBean;
import at.jku.dke.etutor.modules.dlg.exercise.TermDescription;

import java.util.List;

public class DatalogExerciseDTO {
    private String solution;
    private List<String> queries;
    private List<TermDescription> uncheckedTerms;
    private int factsId;

    public DatalogExerciseDTO(){
        //empty for serialization
    }

    public DatalogExerciseDTO(DatalogExerciseBean exerciseBean){
        this.solution = exerciseBean.getQuery();
        this.queries = exerciseBean.getPredicates();
        this.uncheckedTerms = exerciseBean.getTerms();
        this.factsId = exerciseBean.getFactsId();
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public List<String> getQueries() {
        return queries;
    }

    public void setQueries(List<String> queries) {
        this.queries = queries;
    }

    public List<TermDescription> getUncheckedTerms() {
        return uncheckedTerms;
    }



    public void setUncheckedTerms(List<TermDescription> uncheckedTerms) {
        this.uncheckedTerms = uncheckedTerms;
    }

    public int getFactsId() {
        return factsId;
    }

    public void setFactsId(int factsId) {
        this.factsId = factsId;
    }
}
