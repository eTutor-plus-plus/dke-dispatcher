package at.jku.dke.etutor.grading.rest;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.rmi.NotBoundException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import at.jku.dke.etutor.grading.rest.dto.GradingDTO;
import at.jku.dke.etutor.grading.rest.dto.Submission;
import at.jku.dke.etutor.grading.rest.dto.SubmissionId;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TestApp {
    public static void main(String[] args) throws IOException, InterruptedException, NotBoundException {
        HttpClient client = HttpClient.newHttpClient();

        // creating submission for sql module
        Submission submission = new Submission();
        submission.setExerciseId(10042);
        submission.setTaskType("sql");
        submission.setUserId(98);
        submission.setMaxPoints(1000);
        HashMap<String, String> passedAttributes = new HashMap<>();
        passedAttributes.put("action", "diagnose");
        passedAttributes.put("submission", "SELECT * from segment");
        passedAttributes.put("diagnoseLevel", "3");
        submission.setPassedAttributes(passedAttributes);
        submission.setPassedParameters(new HashMap<String, String>());

        String submissionJson = new ObjectMapper().writeValueAsString(submission);
        // Sending submission request
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

        // unmarshalling submission response
        ObjectMapper mapper = new ObjectMapper();
        EntityModel<SubmissionId> submissionModel = mapper.readValue(response.body(), new TypeReference<EntityModel<SubmissionId>>(){});
        SubmissionId submissionId = submissionModel.getContent();
        String id = submissionId.getId();
        System.out.println(submissionId.getId());

        // Requesting grading
        TimeUnit.SECONDS.sleep(10);
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/grading/"+id))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Unmarshalling grading response
        EntityModel<GradingDTO> entityModel = mapper.readValue(response.body(), new TypeReference<EntityModel<GradingDTO>>(){});
        GradingDTO grading = entityModel.getContent();
        System.out.println(grading.getReport().getDescription());
    }

}
