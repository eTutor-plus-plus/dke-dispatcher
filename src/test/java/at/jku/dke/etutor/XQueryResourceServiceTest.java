package at.jku.dke.etutor;

import at.jku.dke.etutor.grading.ETutorGradingApplication;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.dto.XMLDefinitionDTO;
import at.jku.dke.etutor.grading.rest.dto.XQExerciseDTO;
import at.jku.dke.etutor.grading.service.XQueryResourceService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes= ETutorGradingApplication.class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
//@Disabled
public class XQueryResourceServiceTest {
    @Autowired
    private XQueryResourceService service;
    @Autowired
    private ApplicationProperties properties;

    private final XMLDefinitionDTO VALID_DTO = new XMLDefinitionDTO();
    private final XMLDefinitionDTO INVALID_DTO = new XMLDefinitionDTO();
    private final XQExerciseDTO exercise = new XQExerciseDTO();
    private final String TASK_GROUP = "TEST_GROUP";

    @BeforeAll
    public void init(){
        VALID_DTO.setDiagnoseXML("<root></root>");
        VALID_DTO.setSubmissionXML("<rootS></rootS>");
        INVALID_DTO.setDiagnoseXML("<root><root>");
        INVALID_DTO.setSubmissionXML("<rootS><rootS>");
        exercise.setQuery("return $solution");
        var list = new ArrayList<String>();
        list.add("/root");
        exercise.setSortedNodes(list);
    }

    @BeforeEach
    private void deleteTaskGroup() {
        try {
            service.deleteTaskGroup(TASK_GROUP);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void when_addXMLToFileSystem_then_no_exception() throws SQLException {
        var fileIds = service.addXMLToDatabase(TASK_GROUP, VALID_DTO);
        assertDoesNotThrow(()->service.addXMLToFileSystem(VALID_DTO, fileIds[0], fileIds[1]));
    }

    @Test
    public void when_addXMLToFileSystem_then_files_equal() throws SQLException, IOException {
        var fileIds = service.addXMLToDatabase(TASK_GROUP, VALID_DTO);
        assertFalse(Files.exists(Paths.get(properties.getXquery().getQuestionFolderBaseName()+"/"+fileIds[0]+".xml")));
        assertFalse(Files.exists(Paths.get(properties.getXquery().getQuestionFolderBaseName()+"/"+fileIds[1]+".xml")));
        assertDoesNotThrow(()->service.addXMLToFileSystem(VALID_DTO, fileIds[0], fileIds[1]));
        assertTrue(Files.exists(Paths.get(properties.getXquery().getQuestionFolderBaseName()+"/"+fileIds[0]+".xml")));
        assertTrue(Files.exists(Paths.get(properties.getXquery().getQuestionFolderBaseName()+"/"+fileIds[1]+".xml")));
        String diagnoseContent = Files.readString(Paths.get(properties.getXquery().getQuestionFolderBaseName()+"/"+fileIds[0]+".xml"), StandardCharsets.US_ASCII);
        String submissionContent = Files.readString(Paths.get(properties.getXquery().getQuestionFolderBaseName()+"/"+fileIds[1]+".xml"), StandardCharsets.US_ASCII);
        assertEquals(VALID_DTO.getDiagnoseXML(), diagnoseContent);
        assertEquals(VALID_DTO.getSubmissionXML(), submissionContent);
    }

    @Test
    public void when_addXMLToDatabase_invalid_then_exception(){
        assertThrows(SQLException.class, ()->service.addXMLToDatabase(TASK_GROUP, INVALID_DTO));
    }

    @Test
    public void when_addXMLToDatabase_valid_then_no_exception(){
        assertDoesNotThrow(()->service.addXMLToDatabase(TASK_GROUP, VALID_DTO));
    }

    @Test
    public void when_addXMLToDatabase_and_getXML_then_strings_equal() throws SQLException {
        assertEquals("", service.getXML(TASK_GROUP));
        service.addXMLToDatabase(TASK_GROUP, VALID_DTO);
        assertEquals(VALID_DTO.getDiagnoseXML(), service.getXML(TASK_GROUP));
        assertNotEquals(VALID_DTO.getSubmissionXML(), service.getXML(TASK_GROUP));
    }

    @Test
    public void when_deleteTaskGroup_then_no_xml() throws SQLException {
        service.addXMLToDatabase(TASK_GROUP, VALID_DTO);
        assertNotEquals("", service.getXML(TASK_GROUP));
        deleteTaskGroup();
        assertEquals("", service.getXML(TASK_GROUP));
    }

    @Test
    public void when_getXMLById_then_equals() throws Exception {
       var ids= service.addXMLToDatabase(TASK_GROUP, VALID_DTO);
       assertEquals(VALID_DTO.getDiagnoseXML(), service.getXMLById(ids[0]));
    }


    @Test
    public void when_createExercise_invalid_taskGroup_then_exception(){
        assertThrows(SQLException.class, ()-> service.createExercise(TASK_GROUP, exercise));
    }
    @Test
    public void when_createExercise_and_fetchExercise_then_equals() throws Exception {
       service.addXMLToDatabase(TASK_GROUP, VALID_DTO);
       int id = service.createExercise(TASK_GROUP, exercise);
       var solution = service.fetchExercise(id);
       assertEquals(exercise.getQuery(), solution.getQuery());
       assertEquals(exercise.getSortedNodes().get(0), solution.getSortedNodes().get(0));
    }

    @Test
    public void when_updateExercise_then_equals() throws Exception {
        service.addXMLToDatabase(TASK_GROUP, VALID_DTO);
        int id = service.createExercise(TASK_GROUP, exercise);

        var updatedExercise = new XQExerciseDTO();
        updatedExercise.setQuery("new solution");
        var newSortings = new ArrayList<String>();
        newSortings.add("new sortings");
        updatedExercise.setSortedNodes(newSortings);
        service.updateExercise(updatedExercise, id);

        var solution = service.fetchExercise(id);
        assertEquals(updatedExercise.getQuery(), solution.getQuery());
        assertEquals(updatedExercise.getSortedNodes().get(0), solution.getSortedNodes().get(0));
    }

    @Test
    public void when_deleteExercise_then_exercise_deleted() throws Exception {
        service.addXMLToDatabase(TASK_GROUP, VALID_DTO);
        int id = service.createExercise(TASK_GROUP, exercise);
        var solution = service.fetchExercise(id);
        assertEquals(exercise.getQuery(), solution.getQuery());
        var isDeleted = service.deleteExercise(id);
        solution = service.fetchExercise(id);
        assertTrue(isDeleted);
        assertEquals("Could not find exercise with id "+id, solution.getQuery());
    }


}
