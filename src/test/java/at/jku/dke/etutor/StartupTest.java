package at.jku.dke.etutor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@SpringBootTest
@Profile("test")
class StartupTest {

    @Test
    void contextLoads() {
        // Test will pass if application starts successfully
    }
}
