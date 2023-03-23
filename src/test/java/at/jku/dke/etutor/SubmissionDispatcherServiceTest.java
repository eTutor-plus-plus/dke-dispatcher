package at.jku.dke.etutor;

import at.jku.dke.etutor.grading.ETutorGradingApplication;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.IfProfileValue;

@SpringBootTest(classes = ETutorGradingApplication.class)
@EnabledIfSystemProperty(named = "run_test", matches="true")
public class SubmissionDispatcherServiceTest {

}
