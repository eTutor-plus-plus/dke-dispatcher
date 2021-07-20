package at.jku.dke.etutor.grading.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "application")
public class ApplicationProperties {
    private String corsPolicy;

    public String getCorsPolicy() {
        return corsPolicy;
    }

    public void setCorsPolicy(String corsPolicy) {
        this.corsPolicy = corsPolicy;
    }
}
