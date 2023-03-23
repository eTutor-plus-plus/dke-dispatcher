package at.jku.dke.etutor;

import at.jku.dke.etutor.grading.ETutorGradingApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@SpringBootTest(classes = ETutorGradingApplication.class)
@Profile("test")
class StartupTest {

    @Test
    void contextLoads() {
        // Test will pass if application starts successfully
    }
}
