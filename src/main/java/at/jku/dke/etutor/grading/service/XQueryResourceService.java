package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.dto.XMLDefinitionDTO;
import at.jku.dke.etutor.modules.xquery.exercise.XQExerciseManagerImpl;
import at.jku.dke.etutor.modules.xquery.util.XMLUtil;
import oracle.jdbc.proxy.annotation.Pre;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.sql.*;

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
     * Takes a taskGrou-UUID and the 2 xml's and processes them.
     * @param taskGroupUUID the UUID identifying the taskGroup
     * @param xmls the xml's
     * @return the id's of the diagnose and submission xml
     * @throws SQLException if an error occurs while accessing the database
     */
    public int[] getFileIds(String taskGroupUUID, XMLDefinitionDTO xmls) throws SQLException {
        boolean isNew ;
        int[] result = new int[2];

        String tableName = properties.getXquery().getTable().getTaskGroup_fileIds_mapping();
        String mappingExistsQuery = "SELECT * FROM " + tableName + " WHERE UUID = ?";

        try(Connection con = DriverManager.getConnection(URL, USER, PWD);
            PreparedStatement stmt = con.prepareStatement(mappingExistsQuery)){
            con.setAutoCommit(false);

            stmt.setString(1, taskGroupUUID);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()){
                isNew = false;
                result[0] = resultSet.getInt("diagnoseFileId");
                result[1] = resultSet.getInt("submissionFileId");
            }else{
                isNew = true;
                result = fetchAvailableIds(con);
                addFileIdMappings(con, taskGroupUUID, result[0], result[1]);
            }
            persistXMLinDatabase(con, result[0], xmls.getDiagnoseXML(), isNew);
            persistXMLinDatabase(con, result[1], xmls.getSubmissionXML(), isNew);
            con.commit();
        } catch (SQLException throwables) {
            throw new SQLException(throwables);
        }
        return result;
    }

    /**
     * Adds an xml-string to the xmldocs table
     * @param con the Connection
     * @param fileId the file id
     * @param xml the xml string
     * @param isNew flag stating wheter the id is already associated with an xml
     * @throws SQLException if an error occurs while accessing the database
     */
    public void persistXMLinDatabase(Connection con, int fileId, String xml, boolean isNew) throws SQLException {
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
     * Adds the mapping from the taskGroup-UUID to the file id#s to the database
     * @param con the Connection
     * @param taskGroupUUID the UUID of the taskGroup
     * @param diagnoseId the file id for the diagnose xml
     * @param submissionId the file id for the submission xml
     * @throws SQLException if an error occurs while accessing the database
     */
    public void addFileIdMappings(Connection con, String taskGroupUUID, int diagnoseId, int submissionId) throws SQLException {
        String tableName = properties.getXquery().getTable().getTaskGroup_fileIds_mapping();
        String query = "INSERT INTO "+tableName+" VALUES(?,?,?)";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1, taskGroupUUID);
            stmt.setInt(2, diagnoseId);
            stmt.setInt(3, submissionId);
            stmt.executeUpdate();
        }
    }

    /**
     * Fetches available ids for xml files
     * @param con the Connection
     * @return the id's
     * @throws SQLException if an error occurs while accessing the database
     */
    public int[] fetchAvailableIds(Connection con) throws SQLException {
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
     * @param UUID the UUID of the task group
     * @return a String containing the xml
     * @throws SQLException if an error occurs while accessing the database
     */
    public String getXML(String UUID) throws SQLException {
        String query = "SELECT diagnoseFileId FROM taskGroup_fileIds_mapping WHERE UUID = ?";
        String xml = "";

        try(Connection con = DriverManager.getConnection(URL, USER, PWD);
        PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1, UUID);
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()){
                int id = resultSet.getInt("diagnoseFileId");
                xml = fetchXML(con ,id);
            }

        } catch (SQLException throwables) {
            throw new SQLException("No XML for UUID "+UUID + " found.");
        }
        return xml;
    }

    /**
     * Fetches an xml from the database
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


}
