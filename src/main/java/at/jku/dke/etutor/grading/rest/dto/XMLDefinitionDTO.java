package at.jku.dke.etutor.grading.rest.dto;

/**
 * Contains the XMLs for a xquery-taskgroup
 */
public class XMLDefinitionDTO {
    private String diagnoseXML;
    private String submissionXML;

    public XMLDefinitionDTO(){
        //empty constructor
    }

    public String getDiagnoseXML() {
        return diagnoseXML;
    }

    public void setDiagnoseXML(String diagnoseXML) {
        this.diagnoseXML = diagnoseXML;
    }

    public String getSubmissionXML() {
        return submissionXML;
    }

    public void setSubmissionXML(String submissionXML) {
        this.submissionXML = submissionXML;
    }
}
