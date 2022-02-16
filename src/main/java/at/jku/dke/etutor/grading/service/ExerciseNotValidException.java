package at.jku.dke.etutor.grading.service;

/**
 * Exception that indicates that an exercise solution is graded with 0 points -> has syntax errors
 */
public class ExerciseNotValidException extends Exception{
    public ExerciseNotValidException(String message){
        super(message);
    }
}
