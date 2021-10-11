package at.jku.dke.etutor.grading;

/**
 * Provides static constants for the application
 */
public class ETutorCORSPolicy {
    private ETutorCORSPolicy(){
        throw new IllegalStateException("Utility class");
    }
    public static final String CORS_POLICY = "*";
}
