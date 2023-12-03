package at.jku.dke.etutor.modules.rt.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.ETutorRTController;
import at.jku.dke.etutor.grading.service.RTResourceService;
import at.jku.dke.etutor.modules.rt.RTObject;
import at.jku.dke.etutor.modules.rt.RTSolution;
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
    private List<RTSolution> solution;
    private List<String> solutionStudent;

    private int maxPoints;

    private boolean hasSyntaxError = false;

    private boolean hasSemantikError = true;

    private String errorLogSyntax="";

    private String errorLogSemantik="";

    public RTAnalysis(int id, String inputSolution) throws SQLException {
        super();
        this.id = id;
        this.inputSolution = inputSolution;
        setDataBaseProperties();
        setSolutionStudent();
        initAnalyse();
    }

    public void initAnalyse(){
        if (!checkSyntax()){
            return;
        }
        int countRalations = 0;
        for (RTSolution rtSolution : this.solution){
            rtSolution.initAnalyse(this.solutionStudent);
            countRalations = countRalations + rtSolution.getRtSemanticsAnalysis().getRelations().size();
            this.errorLogSemantik = errorLogSemantik.concat(rtSolution.getRtSemanticsAnalysis().getErrorLogSemantik());
        }
        checkRelations();
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
                    this.errorLogSyntax = errorLogSyntax.concat("<br>" + str + ": " +  e.getMessage());
                    checkSuccess = false;
                    hasSyntaxError = true;
                }
            }
        }
        return checkSuccess;
    }

    public void checkRelations(){
        int blank = 0;
        for (String elem : solutionStudent){
            if (elem.isEmpty() || elem.isBlank()){
                blank++;
            }
        }
        int solutionStudent = this.solutionStudent.size() - blank;
        int solution = 0;
        for (RTSolution rtSolution : this.solution){
            solution += rtSolution.getRtSemanticsAnalysis().getRelations().size();
            if(!rtSolution.getRtSemanticsAnalysis().getErrorLogSyntax().isBlank() && !rtSolution.getRtSemanticsAnalysis().getErrorLogSyntax().isEmpty()){
                this.hasSyntaxError = true;
                this.errorLogSyntax = errorLogSyntax.concat(rtSolution.getRtSemanticsAnalysis().getErrorLogSyntax());
            }
        }
        if(solution != solutionStudent){
            this.errorLogSyntax = errorLogSyntax.concat("<br>Redundante oder fehlende Relation!");
            this.hasSyntaxError = true;
        }
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
            this.solution = rtObject.getDbSolution();
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

    public List<RTSolution> getSolution() {
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
    public double calcPoints(){
        if (this.getHasSyntaxError()){
            return 0;
        }
        else{
            int points = 0;
            for(RTSolution rtSolution : getSolution()){
                points = points + rtSolution.getRtSemanticsAnalysis().getTotalPoints();
            }
            if(points == this.maxPoints){
                this.setSubmissionSuitsSolution(true);
                this.hasSemantikError = false;
            }
            return points;
        }
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

    public String getErrorLogSyntax() {
        return errorLogSyntax;
    }

    public String getErrorLogSemantik() {
        return errorLogSemantik;
    }
}
