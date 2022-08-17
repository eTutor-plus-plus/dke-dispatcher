package at.jku.dke.etutor.grading.rest;

import java.util.List;

public class ApiException extends Exception{
    private Integer statusCode;
    private String errorMessage;
    private List<String> errors;

    public ApiException(int statusCode, String errorMessage, List<String> errors){
        super(errorMessage);
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.errors = errors;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
