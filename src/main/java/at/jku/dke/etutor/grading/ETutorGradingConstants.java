package at.jku.dke.etutor.grading;

import at.jku.dke.etutor.grading.config.ApplicationProperties;

/**
 * Provides static constants for the application
 */
public class ETutorGradingConstants {
    private ApplicationProperties properties;
    public ETutorGradingConstants(ApplicationProperties properties){
        this.properties=properties;
       // ETutorGradingConstants.CORS_POLICY=properties.getGrading().getCorsPolicy();
    }
    public static final String CORS_POLICY = "*";
}
