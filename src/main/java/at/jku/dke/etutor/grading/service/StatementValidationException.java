package at.jku.dke.etutor.grading.service;

/**
 * Custom exception used to validate SQL-statements see {@link at.jku.dke.etutor.grading.service.SQLResourceService#createTables(String, String)}
 */
public class StatementValidationException extends Exception{
    public StatementValidationException(String s){
        super(s);
    }

}
