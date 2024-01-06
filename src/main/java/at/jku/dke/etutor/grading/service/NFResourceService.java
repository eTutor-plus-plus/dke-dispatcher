package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.model.Relation;
import at.jku.dke.etutor.modules.nf.parser.NFLexer;
import at.jku.dke.etutor.modules.nf.parser.NFParser;
import at.jku.dke.etutor.modules.nf.specification.KeysDeterminationSpecification;
import at.jku.dke.etutor.modules.nf.specification.NFSpecification;
import at.jku.dke.etutor.objects.dispatcher.nf.NFExerciseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

@Service
public class NFResourceService {

    public int createExercise(NFExerciseDTO dto) throws DatabaseException, ExerciseNotValidException {
        int exerciseId = 1;

        try (
            Connection conn = RDBDHelper.getPooledConnection();
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("SELECT MAX(id) FROM exercises")
        ) {
            if(rset.next()) {
                exerciseId = rset.getInt("max") + 1;
            }
        } catch (SQLException s) {
            throw new DatabaseException(s);
        }

        int numericSubtypeId = getNumericSubtypeId(dto.getNfTaskSubtypeId());
        // JSONObject specificationJSON = convertToJSONString(dto);
        String specificationJSON = convertToJSONString(dto);

        try (
                Connection conn = RDBDHelper.getPooledConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO exercises (id, rdbd_type, specification) " +
                            "VALUES (?, ?, ?::JSONB)"
                )
        ) {
            conn.setAutoCommit(false);

            stmt.setInt(1, exerciseId);
            stmt.setInt(2, numericSubtypeId);
            stmt.setObject(3, specificationJSON.toString());

            if(stmt.executeUpdate() <= 0) {
                conn.rollback();
                return -1;
            }

            conn.commit();
        } catch (SQLException s) {
            throw new DatabaseException(s);
        }

        return exerciseId;
    }

    public boolean modifyExercise(int exerciseId, NFExerciseDTO dto) throws DatabaseException, ExerciseNotValidException {
        int numericSubtypeId = getNumericSubtypeId(dto.getNfTaskSubtypeId());
        // JSONObject specificationJSON = convertToJSONString(dto);
        String specificationJSON = convertToJSONString(dto);

        try (
                Connection conn = RDBDHelper.getPooledConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE exercises " +
                            "SET rdbd_type=?, specification=?::JSONB " +
                            "WHERE id = ?"
                )
        ) {
            conn.setAutoCommit(false);

            stmt.setInt(1, numericSubtypeId);
            stmt.setObject(2, specificationJSON.toString());
            stmt.setInt(3, exerciseId);

            if(stmt.executeUpdate() <= 0) {
                conn.rollback();
                return false;
            } else {
                conn.commit();
                return true;
            }
        } catch (SQLException s) {
            throw new DatabaseException(s);
        }
    }

    public boolean deleteExercise(int exerciseId) throws DatabaseException {
        try (
                Connection conn = RDBDHelper.getPooledConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM exercises " +
                            "WHERE id = ?"
                )
        ) {
            conn.setAutoCommit(false);

            stmt.setInt(1, exerciseId);

            if(stmt.executeUpdate() <= 0) {
                conn.rollback();
                return false;
            } else {
                conn.commit();
                return true;
            }
        } catch (SQLException s) {
            throw new DatabaseException(s);
        }
    }

    private int getNumericSubtypeId(String subTypeString) {
        return switch (subTypeString) {
            case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#NFTask#KeysDeterminationTask" -> 0;
            case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#NFTask#NormalizationTask" -> 1;
            case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#NFTask#MinimalCoverTask" -> 2;
            case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#NFTask#AttributeClosureTask" -> 3;
            case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#NFTask#NormalformDeterminationTask" -> 4;
            default -> -1;
        };
    }

    private String convertToJSONString(NFExerciseDTO dto) throws ExerciseNotValidException {
        JSONObject baseRelationJSON = new JSONObject();

        // Source: https://datacadamia.com/antlr/getting_started (Gerald Wimmer, 2023-11-27)
        CharStream baseAttributesLexerInput = CharStreams.fromString(dto.getNfBaseAttributes());
        Lexer baseAttributesLexer = new NFLexer(baseAttributesLexerInput);
        TokenStream baseAttributesParserInput = new CommonTokenStream(baseAttributesLexer);
        NFParser baseAttributesParser = new NFParser(baseAttributesParserInput);

        Set<String> baseAttributes = baseAttributesParser.attributeSet().attributes;

        CharStream baseDependenciesLexerInput = CharStreams.fromString(dto.getNfBaseDependencies());
        Lexer baseDependenciesLexer = new NFLexer(baseDependenciesLexerInput);
        TokenStream baseDependenciesParserInput = new CommonTokenStream(baseDependenciesLexer);
        NFParser baseDependenciesParser = new NFParser(baseDependenciesParserInput);

        Set<FunctionalDependency> baseDependencies = baseDependenciesParser.functionalDependencySet().functionalDependencies;

        IdentifiedRelation baseRelation = new IdentifiedRelation();
        baseRelation.setAttributes(baseAttributes);
        baseRelation.setFunctionalDependencies(baseDependencies);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer();

        JSONArray baseDependenciesJSON = new JSONArray();
        baseDependencies.forEach(baseDependenciesJSON::put);

        JSONObject specificationJSON = new JSONObject();
        try {
            /*baseRelationJSON.put("attributes", new JSONArray(baseAttributes));
            baseRelationJSON.put("functionalDependencies", baseDependenciesJSON);
            specificationJSON.put("baseRelation", baseRelationJSON);*/

            switch (dto.getNfTaskSubtypeId()) {
                case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#NFTask#KeysDeterminationTask" -> {
                    KeysDeterminationSpecification specification = new KeysDeterminationSpecification();
                    specification.setBaseRelation(baseRelation);
                    specification.setPenaltyPerMissingKey(dto.getNfKeysDeterminationPenaltyPerMissingKey());
                    specification.setPenaltyPerIncorrectKey(dto.getNfKeysDeterminationPenaltyPerIncorrectKey());

                    return objectWriter.writeValueAsString(specification);
                    /*specificationJSON.put("penaltyPerMissingKey", dto.getNfKeysDeterminationPenaltyPerMissingKey());
                    specificationJSON.put("penaltyPerIncorrectKey", dto.getNfKeysDeterminationPenaltyPerIncorrectKey());*/
                }
                case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#NFTask#NormalizationTask" -> {
                    CharStream targetLevelLexerInput = CharStreams.fromString(dto.getNfNormalizationTargetLevel());
                    Lexer targetLevelLexer = new NFLexer(targetLevelLexerInput);
                    TokenStream targetLevelParserInput = new CommonTokenStream(targetLevelLexer);
                    NFParser targetLevelParser = new NFParser(targetLevelParserInput);

                    NormalformLevel level = targetLevelParser.normalForm().level;

                    return null;

                    /*specificationJSON.put("penaltyPerLostAttribute", dto.getNfNormalizationPenaltyPerLostAttribute());
                    specificationJSON.put("penaltyForLossyDecomposition", dto.getNfNormalizationPenaltyForLossyDecomposition());
                    specificationJSON.put("penaltyPerNonCanonicalDependency", dto.getNfNormalizationPenaltyPerNonCanonicalDependency());
                    specificationJSON.put("penaltyPerTrivialDependency", dto.getNfNormalizationPenaltyPerTrivialDependency());
                    specificationJSON.put("penaltyPerExtraneousAttributeInDependencies", dto.getNfNormalizationPenaltyPerExtraneousAttributeInDependencies());
                    specificationJSON.put("penaltyPerRedundantDependency", dto.getNfNormalizationPenaltyPerRedundantDependency());
                    specificationJSON.put("penaltyPerExcessiveLostDependency", dto.getNfNormalizationPenaltyPerExcessiveLostDependency());
                    specificationJSON.put("penaltyPerMissingNewDependency", dto.getNfNormalizationPenaltyPerMissingNewDependency());
                    specificationJSON.put("penaltyPerIncorrectNewDependency", dto.getNfNormalizationPenaltyPerIncorrectNewDependency());
                    specificationJSON.put("penaltyPerMissingKey", dto.getNfNormalizationPenaltyPerMissingKey());
                    specificationJSON.put("penaltyPerIncorrectKey", dto.getNfNormalizationPenaltyPerIncorrectKey());
                    specificationJSON.put("penaltyPerIncorrectNFRelation", dto.getNfNormalizationPenaltyPerIncorrectNFRelation());
                    specificationJSON.put("maxLostDependencies", dto.getNfNormalizationMaxLostDependencies());*/

                    // specificationJSON.put("targetLevel", level.toString());
                }
                case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#NFTask#MinimalCoverTask" -> {
                    return null;

                    /*specificationJSON.put("penaltyPerNonCanonicalDependency", dto.getNfMinimalCoverPenaltyPerNonCanonicalDependency());
                    specificationJSON.put("penaltyPerTrivialDependency", dto.getNfMinimalCoverPenaltyPerTrivialDependency());
                    specificationJSON.put("penaltyPerExtraneousAttribute", dto.getNfMinimalCoverPenaltyPerExtraneousAttribute());
                    specificationJSON.put("penaltyPerRedundantDependency", dto.getNfMinimalCoverPenaltyPerRedundantDependency());
                    specificationJSON.put("penaltyPerMissingDependencyVsSolution", dto.getNfMinimalCoverPenaltyPerMissingDependencyVsSolution());
                    specificationJSON.put("penaltyPerIncorrectDependencyVsSolution", dto.getNfMinimalCoverPenaltyPerIncorrectDependencyVsSolution());*/
                }
                case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#NFTask#AttributeClosureTask" -> {
                    CharStream acBaseAttributesLexerInput = CharStreams.fromString(dto.getNfAttributeClosureBaseAttributes());
                    Lexer acBaseAttributesLexer = new NFLexer(acBaseAttributesLexerInput);
                    TokenStream acBaseAttributesParserInput = new CommonTokenStream(acBaseAttributesLexer);
                    NFParser acBaseAttributesParser = new NFParser(acBaseAttributesParserInput);

                    Set<String> acBaseAttributes = acBaseAttributesParser.attributeSet().attributes;

                    return null;
                    /*specificationJSON.put("baseAttributes", new JSONArray(acBaseAttributes));
                    specificationJSON.put("penaltyPerMissingAttribute", dto.getNfAttributeClosurePenaltyPerMissingAttribute());
                    specificationJSON.put("penaltyPerIncorrectAttribute", dto.getNfAttributeClosurePenaltyPerIncorrectAttribute());*/
                }
                case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#NFTask#NormalformDeterminationTask" -> {
                    return null;

                    /*specificationJSON.put("penaltyForIncorrectNFOverall", dto.getNfNormalFormDeterminationPenaltyForIncorrectOverallNormalform());
                    specificationJSON.put("penaltyPerIncorrectNFDependency", dto.getNfNormalFormDeterminationPenaltyPerIncorrectDependencyNormalform());*/
                }
                default -> {
                    throw new ExerciseNotValidException("Could not generate JSON for exercise specification because task type is not recognized.");
                    // do nothing
                }
            }
        } /*catch (JSONException j) {
            j.printStackTrace();
            throw new ExerciseNotValidException("Could not generate JSON for exercise specification because: " + j.getMessage());
        } */catch (JsonProcessingException jp) {
            jp.printStackTrace();
            throw new ExerciseNotValidException("Could not generate JSON for exercise specification because: " + jp.getMessage());
        }

        // return specificationJSON;
    }
}
