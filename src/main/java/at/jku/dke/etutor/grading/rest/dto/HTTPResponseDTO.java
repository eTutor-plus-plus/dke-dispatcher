package at.jku.dke.etutor.grading.rest.dto;

public class HTTPResponseDTO {
    public String message;
    public HTTPResponseDTO(){

    }
    public HTTPResponseDTO(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
