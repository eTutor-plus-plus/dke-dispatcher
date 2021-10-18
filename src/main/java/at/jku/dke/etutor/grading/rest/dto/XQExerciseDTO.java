package at.jku.dke.etutor.grading.rest.dto;


import java.util.List;

public class XQExerciseDTO {
    private String query;
    private List<String> sortedNodes;

    public XQExerciseDTO(){

    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getSortedNodes() {
        return sortedNodes;
    }

    public void setSortedNodes(List<String> sortedNodes) {
        this.sortedNodes = sortedNodes;
    }
}
