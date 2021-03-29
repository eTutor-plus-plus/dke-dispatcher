package at.jku.dke.etutor.grading.rest.dto;

public class IdWrapper {
    private int id;
    public IdWrapper(int id){
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
