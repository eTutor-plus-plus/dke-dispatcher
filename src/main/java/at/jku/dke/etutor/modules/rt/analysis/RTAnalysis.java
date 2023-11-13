package at.jku.dke.etutor.modules.rt.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.ETutorRTController;
import at.jku.dke.etutor.grading.service.RTResourceService;
import at.jku.dke.etutor.modules.rt.RTObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.parser.ParseException;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import javax.xml.transform.ErrorListener;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class RTAnalysis extends DefaultAnalysis {
    private int id;
    private String inputSolution;
    private List<String> solution;
    private List<String> solutionStudent;
    private ApplicationProperties applicationProperties;
    private int maxPoints;

    private boolean hasSyntaxError = false;

    private boolean hasSemantikError = false;

    public RTAnalysis(int id, String inputSolution, ApplicationProperties applicationProperties) throws SQLException {
        super();
        this.id = id;
        this.inputSolution = inputSolution;
        this.applicationProperties = applicationProperties;
        setDataBaseProperties();
        setSolutionStudent();
    }

    public boolean checkSyntax() {
        boolean checkSuccess = true;
        for (String str : this.getSolutionStudent()){
            if (!str.isEmpty() && !str.isBlank()){
                try {
                    errorListener errorListener = new errorListener();
                    ANTLRInputStream input = new ANTLRInputStream(str);
                    rtSyntaxLexer lexer = new rtSyntaxLexer(input);
                    lexer.addErrorListener(errorListener);
                    CommonTokenStream tokens = new CommonTokenStream(lexer);
                    rtSyntaxParser parser = new rtSyntaxParser(tokens);
                    parser.addErrorListener(errorListener);
                    ParseTree tree = parser.start();
                } catch (Exception e) {
                    checkSuccess = false;
                    hasSyntaxError = true;
                }
            }
        }
        return checkSuccess;
    }

    public boolean checkSemantik() {
        boolean checkSuccess = false;
        if (this.getSolutionStudent().equals(this.solution)){
        checkSuccess = true;
        }
        return checkSuccess;
    }

    @Override
    public boolean submissionSuitsSolution() {
        if (this.checkSemantik()){
            return true;
        }
        this.hasSemantikError = true;
        return false;
    }

    public void setDataBaseProperties() throws SQLException {
        final HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8081/rt/task/getTask/" + this.id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            RTObject rtObject = getRTObject(response.body());
            this.solution = stringFormater(rtObject.getDbSolution());
            this.maxPoints = rtObject.getMaxPoints();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSolutionStudent(){
        this.solutionStudent = stringFormater(this.inputSolution);
    }

    public List<String> stringFormater(String elem){
        String[] lines = elem.split("\r\n");
        List<String> list = Arrays.asList(lines);
        return list;
    }

    public int getId() {
        return id;
    }

    public String getInputSolution() {
        return inputSolution;
    }

    public List<String> getSolution() {
        return solution;
    }

    public List<String> getSolutionStudent() {
        return solutionStudent;
    }

    public RTObject getRTObject(String elem) throws ParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        RTObject rtObject;
        try {
            rtObject = objectMapper.readValue(elem, RTObject.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return rtObject;
    }
    public int getMaxPoints() {
        return maxPoints;
    }

    public boolean getHasSyntaxError() {
        return hasSyntaxError;
    }

    public boolean getHasSemantikError() {
        return hasSemantikError;
    }
}
