package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.modules.nf.RDBDConstants;
import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.parser.NFLexer;
import at.jku.dke.etutor.modules.nf.parser.NFParser;
import at.jku.dke.etutor.modules.nf.parser.NFParserErrorCollector;
import at.jku.dke.etutor.modules.nf.specification.AttributeClosureSpecification;
import at.jku.dke.etutor.modules.nf.specification.KeysDeterminationSpecification;
import at.jku.dke.etutor.modules.nf.specification.MinimalCoverSpecification;
import at.jku.dke.etutor.modules.nf.specification.NFSpecification;
import at.jku.dke.etutor.modules.nf.specification.NormalformDeterminationSpecification;
import at.jku.dke.etutor.modules.nf.specification.NormalizationSpecification;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
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

    public String generateAssignmentText(int exerciseId, Locale locale) throws DatabaseException {
        try (
                Connection conn = RDBDHelper.getPooledConnection();
                PreparedStatement stmt = conn.prepareStatement(
                "SELECT rdbd_type, specification FROM exercises " +
                    "WHERE id = ?"
                )
        ) {
            stmt.setInt(1, exerciseId);

            ResultSet rset = stmt.executeQuery();

            if(rset.next()) {
                int rdbdTypeId = rset.getInt("rdbd_type");
                String specificationString = rset.getString("specification");

                RDBDConstants.Type rdbdType = RDBDConstants.Type.values()[rdbdTypeId];

                NFSpecification specification = switch (rdbdType) {
                    case KEYS_DETERMINATION -> {
                        try {	// Source: https://mkyong.com/java/how-to-convert-java-object-to-from-json-jackson/
                            yield new ObjectMapper().readValue(specificationString, KeysDeterminationSpecification.class);
                        } catch (Exception e) {
                            throw new DatabaseException("Invalid KeysDeterminationSpecification in database for exercise id " + exerciseId);
                        }
                    }
                    case MINIMAL_COVER -> {
                        try {
                            yield new ObjectMapper().readValue(specificationString, MinimalCoverSpecification.class);
                        } catch (Exception e) {
                            throw new DatabaseException("Invalid KeysDeterminationSpecification in database for exercise id " + exerciseId);
                        }
                    }
                    case ATTRIBUTE_CLOSURE -> {
                        try {
                            yield new ObjectMapper().readValue(specificationString, AttributeClosureSpecification.class);
                        } catch (Exception e) {
                            throw new DatabaseException("Invalid KeysDeterminationSpecification in database for exercise id " + exerciseId);
                        }
                    }
                    case NORMALIZATION -> {
                        try {
                            yield new ObjectMapper().readValue(specificationString, NormalizationSpecification.class);
                        } catch (Exception e) {
                            throw new DatabaseException("Invalid KeysDeterminationSpecification in database for exercise id " + exerciseId);
                        }
                    }
                    case NORMALFORM_DETERMINATION -> {
                        try {
                            yield new ObjectMapper().readValue(specificationString, NormalformDeterminationSpecification.class);
                        } catch (Exception e) {
                            throw new DatabaseException("Invalid KeysDeterminationSpecification in database for exercise id " + exerciseId);
                        }
                    }
                    default -> throw new DatabaseException("Cannot handle rdbd_type " + rdbdType + " for exercise id " + exerciseId);
                };

                try {
                    return RDBDHelper.getAssignmentText(specification, 0, locale, rdbdType);
                } catch (IOException i) {
                    i.printStackTrace();
                    return null;
                }
            } else {
                throw new DatabaseException("Exercise id " + exerciseId + " not present.");
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
        NFParserErrorCollector errorCollector = new NFParserErrorCollector();

        // Source: https://datacadamia.com/antlr/getting_started (Gerald Wimmer, 2023-11-27)
        CharStream baseAttributesLexerInput = CharStreams.fromString(dto.getNfBaseAttributes());
        Lexer baseAttributesLexer = new NFLexer(baseAttributesLexerInput);
        TokenStream baseAttributesParserInput = new CommonTokenStream(baseAttributesLexer);
        NFParser baseAttributesParser = new NFParser(baseAttributesParserInput);


        baseAttributesParser.addErrorListener(errorCollector);

        Set<String> baseAttributes = baseAttributesParser.attributeSet().attributes;

        CharStream baseDependenciesLexerInput = CharStreams.fromString(dto.getNfBaseDependencies());
        Lexer baseDependenciesLexer = new NFLexer(baseDependenciesLexerInput);
        TokenStream baseDependenciesParserInput = new CommonTokenStream(baseDependenciesLexer);
        NFParser baseDependenciesParser = new NFParser(baseDependenciesParserInput);

        NFParserErrorCollector baseDependenciesErrorCollector = new NFParserErrorCollector();
        baseDependenciesParser.addErrorListener(baseDependenciesErrorCollector);

        Set<FunctionalDependency> baseDependencies = baseDependenciesParser.functionalDependencySet().functionalDependencies;

        IdentifiedRelation baseRelation = new IdentifiedRelation();
        baseRelation.setAttributes(baseAttributes);
        baseRelation.setFunctionalDependencies(baseDependencies);

        // source: https://stackoverflow.com/a/15786175 (Gerald Wimmer, 2024-01-05)
        ObjectWriter objectWriter = new ObjectMapper().writer();

        JSONArray baseDependenciesJSON = new JSONArray();
        baseDependencies.forEach(baseDependenciesJSON::put);

        try {
            switch (dto.getNfTaskSubtypeId()) {
                case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#NFTask#KeysDeterminationTask" -> {
                    KeysDeterminationSpecification specification = new KeysDeterminationSpecification();
                    specification.setBaseRelation(baseRelation);

                    specification.setPenaltyPerMissingKey(dto.getNfKeysDeterminationPenaltyPerMissingKey());
                    specification.setPenaltyPerIncorrectKey(dto.getNfKeysDeterminationPenaltyPerIncorrectKey());

                    return objectWriter.writeValueAsString(specification);
                }
                case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#NFTask#NormalizationTask" -> {
                    CharStream targetLevelLexerInput = CharStreams.fromString(dto.getNfNormalizationTargetLevel());
                    Lexer targetLevelLexer = new NFLexer(targetLevelLexerInput);
                    TokenStream targetLevelParserInput = new CommonTokenStream(targetLevelLexer);
                    NFParser targetLevelParser = new NFParser(targetLevelParserInput);

                    NFParserErrorCollector targetLevelErrorCollector = new NFParserErrorCollector();
                    targetLevelParser.addErrorListener(targetLevelErrorCollector);

                    NormalformLevel targetLevel = targetLevelParser.normalForm().level;

                    NormalizationSpecification specification = new NormalizationSpecification();
                    specification.setBaseRelation(baseRelation);

                    specification.setTargetLevel(targetLevel);
                    specification.setMaxLostDependencies(dto.getNfNormalizationMaxLostDependencies());

                    specification.setPenaltyPerLostAttribute(dto.getNfNormalizationPenaltyPerLostAttribute());
                    specification.setPenaltyForLossyDecomposition(dto.getNfNormalizationPenaltyForLossyDecomposition());
                    specification.setPenaltyPerNonCanonicalDependency(dto.getNfNormalizationPenaltyPerNonCanonicalDependency());
                    specification.setPenaltyPerTrivialDependency(dto.getNfNormalizationPenaltyPerTrivialDependency());
                    specification.setPenaltyPerExtraneousAttributeInDependencies(dto.getNfNormalizationPenaltyPerExtraneousAttributeInDependencies());
                    specification.setPenaltyPerRedundantDependency(dto.getNfNormalizationPenaltyPerRedundantDependency());
                    specification.setPenaltyPerExcessiveLostDependency(dto.getNfNormalizationPenaltyPerExcessiveLostDependency());
                    specification.setPenaltyPerMissingNewDependency(dto.getNfNormalizationPenaltyPerMissingNewDependency());
                    specification.setPenaltyPerIncorrectNewDependency(dto.getNfNormalizationPenaltyPerIncorrectNewDependency());
                    specification.setPenaltyPerMissingKey(dto.getNfKeysDeterminationPenaltyPerMissingKey());
                    specification.setPenaltyPerIncorrectKey(dto.getNfNormalizationPenaltyPerIncorrectKey());
                    specification.setPenaltyPerIncorrectNFRelation(dto.getNfNormalizationPenaltyPerIncorrectNFRelation());

                    return objectWriter.writeValueAsString(specification);
                }
                case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#NFTask#MinimalCoverTask" -> {
                    MinimalCoverSpecification specification = new MinimalCoverSpecification();
                    specification.setBaseRelation(baseRelation);

                    specification.setPenaltyPerNonCanonicalDependency(dto.getNfMinimalCoverPenaltyPerNonCanonicalDependency());
                    specification.setPenaltyPerTrivialDependency(dto.getNfMinimalCoverPenaltyPerTrivialDependency());
                    specification.setPenaltyPerExtraneousAttribute(dto.getNfMinimalCoverPenaltyPerExtraneousAttribute());
                    specification.setPenaltyPerRedundantDependency(dto.getNfMinimalCoverPenaltyPerRedundantDependency());
                    specification.setPenaltyPerMissingDependencyVsSolution(dto.getNfMinimalCoverPenaltyPerMissingDependencyVsSolution());
                    specification.setPenaltyPerIncorrectDependencyVsSolution(dto.getNfMinimalCoverPenaltyPerIncorrectDependencyVsSolution());

                    return objectWriter.writeValueAsString(specification);
                }
                case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#NFTask#AttributeClosureTask" -> {
                    CharStream acBaseAttributesLexerInput = CharStreams.fromString(dto.getNfAttributeClosureBaseAttributes());
                    Lexer acBaseAttributesLexer = new NFLexer(acBaseAttributesLexerInput);
                    TokenStream acBaseAttributesParserInput = new CommonTokenStream(acBaseAttributesLexer);
                    NFParser acBaseAttributesParser = new NFParser(acBaseAttributesParserInput);

                    NFParserErrorCollector acBaseAttributesErrorCollector = new NFParserErrorCollector();
                    acBaseAttributesParser.addErrorListener(acBaseAttributesErrorCollector);

                    Set<String> acBaseAttributes = acBaseAttributesParser.attributeSet().attributes;

                    AttributeClosureSpecification specification = new AttributeClosureSpecification();
                    specification.setBaseRelation(baseRelation);

                    specification.setBaseAttributes(acBaseAttributes);

                    specification.setPenaltyPerMissingAttribute(dto.getNfAttributeClosurePenaltyPerMissingAttribute());
                    specification.setPenaltyPerIncorrectAttribute(dto.getNfAttributeClosurePenaltyPerIncorrectAttribute());

                    return objectWriter.writeValueAsString(specification);
                }
                case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#NFTask#NormalformDeterminationTask" -> {
                    NormalformDeterminationSpecification specification = new NormalformDeterminationSpecification();

                    specification.setPenaltyForIncorrectNFOverall(dto.getNfNormalFormDeterminationPenaltyForIncorrectOverallNormalform());
                    specification.setPenaltyPerIncorrectNFDependency(dto.getNfNormalFormDeterminationPenaltyPerIncorrectDependencyNormalform());

                    return objectWriter.writeValueAsString(specification);
                }
                default -> throw new ExerciseNotValidException("Could not generate JSON for exercise specification due to unrecognized task type \"" + dto.getNfTaskSubtypeId() + "\".");
            }
        } catch (JsonProcessingException jp) {
            jp.printStackTrace();
            throw new ExerciseNotValidException("Could not generate JSON for exercise specification because: " + jp.getMessage());
        }
    }
}
