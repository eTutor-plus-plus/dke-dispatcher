package at.jku.dke.etutor.grading.service;

public class DatabaseException extends Exception{

    public DatabaseException(Throwable cause) {
        super(cause);
    }

    public DatabaseException(String s){
        super(s);
    }
}
