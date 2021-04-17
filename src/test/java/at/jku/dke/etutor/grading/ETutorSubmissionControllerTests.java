package at.jku.dke.etutor.grading;

import at.jku.dke.etutor.grading.rest.dto.Submission;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@SpringBootTest
public class ETutorSubmissionControllerTests {
    Submission submission;

    @BeforeEach
    void initialize() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        submission = createRandomSQLSubmssionUtil();
        assertFalse(submission == null);
    }


    @Test
    void testETutorSubmissionController_StatusCode() throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        String submissionJson = mapper.writeValueAsString(submission);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/submission"))
                .POST(HttpRequest.BodyPublishers.ofString(submissionJson))
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(202, response.statusCode());
    }




    Submission createRandomSQLSubmssionUtil() {
        Submission submission = new Submission();
        HashMap<String, String> attributeMap = new HashMap<>();
        attributeMap.put("action", "diagnose");
        attributeMap.put("diagnoseLevel", "3");
        submission.setPassedAttributes(attributeMap);
        submission.setPassedParameters(new HashMap<String, String>());
        submission.setUserId(1);
        submission.setMaxPoints(1);
        submission.setTaskType("sql");

        PreparedStatement stmt;
        ResultSet rs;
        String solution;
        int exerciseId;

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql", "sql", "sql")) {
            String query = "select * from exercises where practise_db IN (1,2,3,4) OR submission_db IN (1,2,3,4) order by random() limit 1;";
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            if (rs.next()) {
                solution = rs.getString("solution");
                exerciseId = rs.getInt("id");
                submission.setExerciseId(exerciseId);
                attributeMap.put("submission", solution);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return submission;
    }


}
