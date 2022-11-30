package at.jku.dke.etutor.modules.pm.analysis;

/**
 * Represents an exception that gets thrown during the analysis if PmEvaluationCriterion is not satisfied
 * The AnalysisException class (subclass) inherits the attributes and methods from the Exception class (superclass)
 */
public class AnalysisException extends Exception{

    // CONSTRUCTOR
    public AnalysisException() {
        super();
    }

    public AnalysisException(String message) {
        super(message);
    }

    public AnalysisException(String message, Throwable cause) {
        super(message, cause);
    }

}
