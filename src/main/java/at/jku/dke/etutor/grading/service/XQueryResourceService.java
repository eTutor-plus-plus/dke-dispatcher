package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.dto.XMLDefinitionDTO;
import at.jku.dke.etutor.grading.rest.dto.XQExerciseDTO;
import at.jku.dke.etutor.modules.xquery.analysis.UrlContentMap;
import at.jku.dke.etutor.modules.xquery.exercise.XQExerciseBean;
import at.jku.dke.etutor.modules.xquery.exercise.XQExerciseManagerImpl;
import at.jku.dke.etutor.modules.xquery.util.XMLUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.Objects;

/**
 * Service class for handling xQuery resources
 */
@Service
public class XQueryResourceService {
    private ApplicationProperties properties;
    private XQExerciseManagerImpl xqExerciseManager;
    private final String URL;
    private final String USER;
    private final String PWD;

    public XQueryResourceService(ApplicationProperties properties, XQExerciseManagerImpl xqExerciseManager){
        this.properties = properties;
        this.xqExerciseManager = xqExerciseManager;
        URL = properties.getXquery().getConnUrl();
        USER = properties.getXquery().getConnUser();
        PWD = properties.getXquery().getConnPwd();
    }

    /**
     * Adds the xml-files to the filepath
     * @param dto the dto with the xml's
     * @param diagnoseId the id for the diagnose-xml
     * @param submissionId the id for the submission-xml
     * @throws IOException if an error occurs while writing the file
     */
    public void createXMLFiles(XMLDefinitionDTO dto, int diagnoseId, int submissionId) throws IOException {
        File diagnoseFile;
        File submissionFile;

        diagnoseFile = new File(properties.getXquery().getQuestionFolderBaseName()+"/"+diagnoseId+".xml");
        XMLUtil.printFile(dto.getDiagnoseXML(), diagnoseFile);

        submissionFile = new File(properties.getXquery().getQuestionFolderBaseName()+"/"+submissionId+".xml");
        XMLUtil.printFile(dto.getSubmissionXML(), submissionFile);
    }

    /**
     * Takes a taskGroup and the 2 xml's and processes them.
     * @param taskGroup the taskGroup identifying the taskGroup
     * @param xmls the xml's
     * @return the id's of the diagnose and submission xml
     * @throws SQLException if an error occurs while accessing the database
     */
    public int[] addXML(String taskGroup, XMLDefinitionDTO xmls) throws SQLException {
        boolean isNew ;
        int[] result = new int[2];

        String tableName = properties.getXquery().getTable().getTaskGroup_fileIds_mapping();
        String mappingExistsQuery = "SELECT * FROM " + tableName + " WHERE taskGroup = ?";

        try(Connection con = DriverManager.getConnection(URL, USER, PWD);
            PreparedStatement stmt = con.prepareStatement(mappingExistsQuery)){
            con.setAutoCommit(false);

            stmt.setString(1, taskGroup);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()){
                isNew = false;
                result[0] = resultSet.getInt("diagnoseFileId");
                result[1] = resultSet.getInt("submissionFileId");
            }else{
                isNew = true;
                result = fetchAvailableIds(con);
                addFileIdMappings(con, taskGroup, result[0], result[1]);
            }
            persistXMLinDatabase(con, result[0], xmls.getDiagnoseXML(), isNew);
            persistXMLinDatabase(con, result[1], xmls.getSubmissionXML(), isNew);
            addPublicFileId(con, result[0]);
            con.commit();
        } catch (SQLException throwables) {
            throw new SQLException(throwables);
        }
        return result;
    }

    /**
     * Adds the diagnose-file-id of a task group to the list of public available xml files
     * @param con the Connection
     * @param i the id
     */
    private void addPublicFileId(Connection con, int i) throws SQLException {
        String query = "INSERT INTO public_file_ids values(?)";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setInt(1, i);
            stmt.executeUpdate();
        }
    }

    /**
     * Removes a public file id
     * @param con the Connection
     * @param i the id
     * @throws SQLException if an error occurs
     */
    private void removePublicFileId(Connection con, int i) throws SQLException {
        String query = "DELETE FROM public_file_ids where id = ?";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setInt(1, i);
            stmt.executeUpdate();
        }
    }

    /**
     * Adds an xml-string to the xmldocs table
     * @param con the Connection
     * @param fileId the file id
     * @param xml the xml string
     * @param isNew flag stating wheter the id is already associated with an xml
     * @throws SQLException if an error occurs while accessing the database
     */
    private void persistXMLinDatabase(Connection con, int fileId, String xml, boolean isNew) throws SQLException {
        String query;
        SQLXML xmlVal = con.createSQLXML();
        xmlVal.setString(xml);
        if(isNew){
            query = "INSERT INTO xmldocs (id, doc) values(?,?)";
            try(PreparedStatement stmt = con.prepareStatement(query)){
                stmt.setInt(1, fileId);
                stmt.setSQLXML(2, xmlVal);
                stmt.executeUpdate();
            }
        }else{
            query = "UPDATE xmldocs SET doc = ? WHERE id = ?";
            try(PreparedStatement stmt = con.prepareStatement(query)){
                stmt.setSQLXML(1, xmlVal);
                stmt.setInt(2, fileId);
                stmt.executeUpdate();
            }
        }

    }

    /**
     * Adds the mapping from the taskGroup-taskGroup to the file id#s to the database
     * @param con the Connection
     * @param taskGroup the taskGroup of the taskGroup
     * @param diagnoseId the file id for the diagnose xml
     * @param submissionId the file id for the submission xml
     * @throws SQLException if an error occurs while accessing the database
     */
    private void addFileIdMappings(Connection con, String taskGroup, int diagnoseId, int submissionId) throws SQLException {
        String tableName = properties.getXquery().getTable().getTaskGroup_fileIds_mapping();
        String query = "INSERT INTO "+tableName+"(taskGroup, diagnoseFileId, submissionFileId) VALUES(?,?,?)";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1, taskGroup);
            stmt.setInt(2, diagnoseId);
            stmt.setInt(3, submissionId);
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes xml files from the xmldocs table
     * @param con the Connection
     * @param diagnoseFileId the id of the diagnose file
     * @param submissionFileId the id of the submission file
     */
    private void deleteXMLDocsFromDB(Connection con, int diagnoseFileId, int submissionFileId) throws SQLException {
        String query = "DELETE FROM xmldocs WHERE id = ?";
        try(PreparedStatement diagnoseStmt = con.prepareStatement(query);
        PreparedStatement submissionStmt = con.prepareStatement(query)){
            diagnoseStmt.setInt(1, diagnoseFileId);
            submissionStmt.setInt(1, submissionFileId);
            diagnoseStmt.executeUpdate();
            submissionStmt.executeUpdate();
        }
    }
    /**
     * Fetches available ids for xml files
     * @param con the Connection
     * @return the id's
     * @throws SQLException if an error occurs while accessing the database
     */
    private int[] fetchAvailableIds(Connection con) throws SQLException {
        String query = "Select max(id) from xmldocs";
        int maxId;
        int[] ids = new int[2];

        try(PreparedStatement stmt = con.prepareStatement(query);
        ResultSet resultSet = stmt.executeQuery()){
            if(resultSet.next()){
                maxId = resultSet.getInt(1);
                maxId++;
                ids[0] = maxId;
                maxId++;
                ids[1] = maxId;
                return ids;
            }else{
                throw new SQLException("Could not fetch available ids");
            }
        }
    }

    /**
     * Returns an xml for a task group
     * @param taskGroup the name of the task group
     * @return a String containing the xml
     * @throws SQLException if an error occurs while accessing the database
     */
    public String getXML(String taskGroup) throws SQLException {
        String query = "SELECT diagnoseFileId FROM taskGroup_fileIds_mapping WHERE taskGroup = ?";
        String xml = "";

        try(Connection con = DriverManager.getConnection(URL, USER, PWD);
        PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1, taskGroup);
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()){
                int id = resultSet.getInt("diagnoseFileId");
                xml = fetchXML(con ,id);
            }

        } catch (SQLException throwables) {
            throw new SQLException("No XML for taskGroup "+ taskGroup + " found.");
        }
        return xml;
    }

    /**
     * Fetches an xml from the database according to the file id
     * @param con the Connection
     * @param id the file id
     * @return a String containing the xml
     * @throws SQLException if an error occurs while accessing the database
     */
    private String fetchXML(Connection con, int id) throws SQLException {
        String query = "SELECT doc FROM xmldocs WHERE id = ?";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("doc");
            }
            throw new SQLException("No XML for id: "+id+" found.");
        } catch (SQLException throwables) {
            throw new SQLException(throwables);
        }
    }

    /**
     * Deletes all database entries associated with a task group
     * @param taskGroup the name of the task group
     * @throws SQLException if an error occurs
     */
    public void deleteXML(String taskGroup) throws SQLException {
        int diagnoseFileId;
        int submissionFileId;

        String tableName = properties.getXquery().getTable().getTaskGroup_fileIds_mapping();
        String mappingExistsQuery = "SELECT * FROM " + tableName + " WHERE taskGroup = ?";
        String deleteMappingQuery = "DELETE FROM "+tableName + " WHERE taskGroup = ?";

        try(Connection con = DriverManager.getConnection(URL, USER, PWD);
            PreparedStatement stmt = con.prepareStatement(mappingExistsQuery);
            PreparedStatement deleteStmt = con.prepareStatement(deleteMappingQuery)){
            con.setAutoCommit(false);

            stmt.setString(1, taskGroup);
            deleteStmt.setString(1, taskGroup);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()){
               diagnoseFileId = resultSet.getInt("diagnoseFileId");
               submissionFileId = resultSet.getInt("submissionFileId");
               deleteStmt.executeUpdate();
               deleteXMLDocsFromDB(con, diagnoseFileId, submissionFileId);
               deleteXMLFiles(diagnoseFileId, submissionFileId);
               removePublicFileId(con, diagnoseFileId);
            }else{
                return;
            }
            con.commit();
        } catch (SQLException throwables) {
            throw new SQLException(throwables);
        }
    }

    /**
     * Deletes xml files saved in the question folder
     * @param diagnoseFileId the id of the diagnose xml file
     * @param submissionFileId the id of the submsission xml file
     */
    private void deleteXMLFiles(int diagnoseFileId, int submissionFileId) {
        File diagnoseFile = new File(properties.getXquery().getQuestionFolderBaseName(), diagnoseFileId+".xml");
        File submissionFile = new File(properties.getXquery().getQuestionFolderBaseName(), submissionFileId+".xml");
        diagnoseFile.delete();
        submissionFile.delete();
    }

    /**
     * Returns an xml file, if id refers to a public xml file
     * @param id the id
     * @return the xml as string
     * @throws SQLException if an error occurs
     */
    public String getXMLById(int id) throws Exception {
        String isIdPublicQuery = "SELECT * FROM public_file_ids WHERE id = ?";
        try(Connection con = DriverManager.getConnection(URL, USER, PWD);
        PreparedStatement stmt = con.prepareStatement(isIdPublicQuery)){
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()){
                return fetchXML(con, id);
            }else throw new Exception("ID not public");
        } catch (SQLException throwables) {
            throw new SQLException(throwables);
        }
    }

    /**
     * Creates an xquery exercise for a specific task-group
     * @param taskGroup the task group
     * @param dto the dto containing the solution query and a list of sorted nodes
     * @throws Exception if an error occurs
     */
    public int createExercise(String taskGroup, XQExerciseDTO dto) throws Exception {
        XQExerciseBean exercise = new XQExerciseBean();
        UrlContentMap urlContentMap = new UrlContentMap();
        var ids = getIdsForTaskGroup(taskGroup);

        urlContentMap.addUrlAlias(properties.getXquery().getXmlFileURLPrefix()+ids[1],
                properties.getXquery().getXmlFileURLPrefix()+ids[0]);

        exercise.setQuery(dto.getQuery());
        exercise.setPoints(1.0);
        exercise.setSortedNodes(dto.getSortedNodes());
        exercise.setUrls(urlContentMap);


        return xqExerciseManager.createExercise(exercise, null, null);
    }

    /**
     * Returns the mapped file ids for a task group
     * @param taskGroup the task group
     * @return the ids
     * @throws SQLException if an error occurs
     */
    private int[] getIdsForTaskGroup(String taskGroup) throws SQLException {
        String table = properties.getXquery().getTable().getTaskGroup_fileIds_mapping();
        String query = "SELECT diagnoseFileId, submissionFileId FROM "+table + " WHERE taskGroup = ?";
        try(Connection con = DriverManager.getConnection(properties.getXquery().getConnUrl(), properties.getXquery().getConnUser(), properties.getXquery().getConnPwd());
        PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1, taskGroup);
            ResultSet set = stmt.executeQuery();
            if(set.next()){
                var result = new int[2];
                result[0] = set.getInt("diagnoseFileId");
                result[1] = set.getInt("submissionFileId");
                return result;
            }else{
                throw new SQLException("Could not find file ids for task group");
            }
        }
    }

    /**
     * Returns the solution-query and the sortings of an exercise
     * @param id the exercise id
     * @return an XQExerciseDTO
     */
    public XQExerciseDTO fetchExercise(int id) throws Exception {
        Serializable e = xqExerciseManager.fetchExercise(id);
        XQExerciseDTO result = new XQExerciseDTO();
        result.setQuery("Could not find exercise with id "+ id);

        if(e instanceof XQExerciseBean exercise){
            result.setQuery(exercise.getQuery());
            result.setSortedNodes(exercise.getSortedNodes());
        }
        return result;
    }

    /**
     * Updates an exercise (solution and sorting)
     * @param dto the dto
     * @param id the id of the exercise
     * @return a String indicating wheter updating has been succesfull
     * @throws Exception if an error occurs while updating
     */
    public String updateExercise(XQExerciseDTO dto, int id) throws Exception {
        Objects.requireNonNull(dto.getQuery());

        XQExerciseBean exercise = new XQExerciseBean();
        exercise.setQuery(dto.getQuery());
        exercise.setSortedNodes(dto.getSortedNodes());
        exercise.setPoints(1.0);

        xqExerciseManager.modifyExercise(id, exercise, null, null);
        return "Exercise with id "+id +" updated";
    }

    /**
     * Deletes an exercise according tothe id
     * @param id the id
     * @return boolean indicating wheter exercise has been deleted
     */
    public boolean deleteExercise(int id) throws Exception {
        return xqExerciseManager.deleteExercise(id);
    }
}
