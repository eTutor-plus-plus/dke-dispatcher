package at.jku.dke.etutor.grading.rest;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.rmi.NotBoundException;
import java.util.HashMap;
import at.jku.dke.etutor.grading.rest.dto.Submission;
import org.springframework.http.HttpHeaders;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TestApp {
    public static void main(String[] args) throws IOException, InterruptedException, NotBoundException {
        HttpClient client = HttpClient.newHttpClient();

        // creating submission for sql module
        Submission submission = new Submission();
        submission.setExerciseId(10042);
        submission.setTaskType("sql");
        submission.setUserId(99999);
        submission.setMaxPoints(1000);
        HashMap<String, String> passedAttributes = new HashMap<>();
        passedAttributes.put("action", "diagnose");
        passedAttributes.put("submission", "SELECT code from segment where tokm = 127");
        passedAttributes.put("diagnoseLevel", "2");
        submission.setPassedAttributes(passedAttributes);
        submission.setPassedParameters(new HashMap<String, String>());

        String submissionJson = new ObjectMapper().writeValueAsString(submission);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/submission"))
                .POST(HttpRequest.BodyPublishers.ofString(submissionJson))
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        System.out.println(response.request());
        System.out.println(response.previousResponse());
        System.out.println(response.toString());
        System.out.println(response.uri());
        System.out.println(response.headers());

        // EntityModel<SubmissionId> model = new ObjectMapper().readValue(response.body(), EntityModel.class);
        // System.out.println(model.getContent().getId());
        // System.out.println(model.getLinks().toString());

    }

}
