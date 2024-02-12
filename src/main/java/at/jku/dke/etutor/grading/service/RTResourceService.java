package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.model.repositories.GradingDTORepository;
import at.jku.dke.etutor.modules.rt.RTDataSource;
import at.jku.dke.etutor.modules.rt.RTObject;
import at.jku.dke.etutor.modules.rt.RTSolution;
import at.jku.dke.etutor.modules.rt.analysis.errorListener;
import at.jku.dke.etutor.modules.rt.analysis.rtSyntaxLexer;
import at.jku.dke.etutor.modules.rt.analysis.rtSyntaxParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.parser.ParseException;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class RTResourceService {
    private ApplicationProperties properties;
    private RTDataSource rtDataSource;

    private GradingDTORepository gradingDTORepository;

    private SubmissionDispatcherService dispatcherService;



    public RTResourceService(ApplicationProperties properties, SubmissionDispatcherService dispatcherService, GradingDTORepository gradingDTORepository) throws SQLException {
        this.properties = properties;
        this.rtDataSource = new RTDataSource(properties);
        this.gradingDTORepository = gradingDTORepository;
        this.dispatcherService = dispatcherService;
    }

    public Long insertTask(String solution, Integer maxPoints) throws SQLException {
        Long generatedID = null;
        Connection con = this.rtDataSource.getConnection();
        try (con) {
            String insertQuery = "INSERT INTO tasks (solution, maxPoints) VALUES (?,?) RETURNING id";
            try (PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, solution);
                preparedStatement.setInt(2,maxPoints);
                if (preparedStatement.execute()) {
                    con.commit();
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            generatedID = resultSet.getLong("id");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            con.close();
        }
        return generatedID - 1;
    }

    public RTObject getRTObject(String elem) throws ParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        RTObject rtObject = null;
        try {
            rtObject = objectMapper.readValue(elem, RTObject.class);
        } catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
        return rtObject;
    }

    public List<RTSolution> getRTSolutions(String elem) throws JsonProcessingException {
        List<RTSolution> rtSolutions = null;
        elem = elem.replace("\\t\\r\\n","\t\r\n");
        elem = elem.replace("\\r\\n", "\r\n");
        elem = elem.replace("\\","");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            rtSolutions = objectMapper.readValue(elem, new TypeReference<List<RTSolution>>() {});
        }
        catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }

        return rtSolutions;
    }

    public void editTask(String solution, Integer maxPoints, Integer id) throws SQLException {
        Connection con = this.rtDataSource.getConnection();
        try (con) {
            String updateQuery = "UPDATE tasks SET solution = ?, maxPoints = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, solution);
                preparedStatement.setInt(2,maxPoints);
                preparedStatement.setInt(3,id);
                preparedStatement.execute();
                con.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            con.close();
        }
    }

    public void deleteTask(Integer id) throws SQLException {
        Connection con = this.rtDataSource.getConnection();
        try (con) {
            String deleteQuery = "DELETE FROM tasks WHERE id = ?;";
            try (PreparedStatement preparedStatement = con.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, id);
                preparedStatement.execute();
                con.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            con.close();
        }
    }

    public RTObject getTask(Integer id) throws SQLException {
        Connection con = this.rtDataSource.getConnection();
        RTObject rtObject = new RTObject();
        try (con) {
            String getQuery = "SELECT * from tasks WHERE id = ?;";
            try (PreparedStatement preparedStatement = con.prepareStatement(getQuery)) {
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String solution = resultSet.getString("solution");
                    List<RTSolution> rtSolutions = getRTSolutions(solution);
                    Integer maxPoints = resultSet.getInt("maxPoints");
                    Integer idDb = resultSet.getInt("id");
                    rtObject.setId(idDb.toString());
                    rtObject.setMaxPoints(maxPoints.toString());
                    rtObject.setDbSolution(rtSolutions);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } finally {
                con.close();
            }
        }
        return rtObject;
    }

    public int checkRTSolution(List<RTSolution> rtSolutions, int maxPoints){
        int solutionPoints = 0;
        for(RTSolution s : rtSolutions){
            solutionPoints += s.getGesamtGewichtung();
            for (Map.Entry<Integer,List<String>> entry : s.getSolution().entrySet()){
                for (String elem : entry.getValue()){
                    try {
                        errorListener errorListener = new errorListener();
                        ANTLRInputStream input = new ANTLRInputStream(elem);
                        rtSyntaxLexer lexer = new rtSyntaxLexer(input);
                        lexer.addErrorListener(errorListener);
                        CommonTokenStream tokens = new CommonTokenStream(lexer);
                        rtSyntaxParser parser = new rtSyntaxParser(tokens);
                        parser.addErrorListener(errorListener);
                        ParseTree tree = parser.start();
                    } catch (Exception e) {
                        return -2;
                    }
                }
            }
        }
        if (solutionPoints > maxPoints){
            return -3;
        }
        return 1;
    }

}
