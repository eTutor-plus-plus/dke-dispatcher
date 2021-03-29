package at.jku.dke.etutor.grading.rest.dto;
/**
 * Used as a Wrapper for the submissionId
 *  in order to facilitate sending the id as part of an EntityModel<T> Object
 */
public class SubmissionId {
    private int value;
    public SubmissionId(int value){
        this.value=value;
    }

    public int getId() {
        return value;
    }

    public void setId(int value) {
        this.value = value;
    }
}
