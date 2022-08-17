package at.jku.dke.etutor.grading.rest;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ApiException.class)
    public ResponseEntity<String> handleError(ApiException e){
       return ResponseEntity.status(e.getStatusCode()).body(e.getErrorMessage());
    }
}
