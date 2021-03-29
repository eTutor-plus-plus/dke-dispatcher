package at.jku.dke.etutor.grading.rest;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import at.jku.dke.etutor.grading.rest.dto.Submission;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletResponse;

public class TestApp {
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Submission submission = new Submission();
        String s = new ObjectMapper().writeValueAsString(submission);


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/submission"))
                .POST(HttpRequest.BodyPublishers.ofString(s))
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        System.out.println(response.request());
        System.out.println(response.previousResponse());
        System.out.println(response.toString());
        System.out.println(response.uri());
        System.out.println(response.headers());


    }

}
