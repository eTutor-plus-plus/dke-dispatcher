package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.modules.nf.NFConstants;
import at.jku.dke.etutor.modules.nf.NFHelper;
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
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Set;

/**
 * Service used for managing the exercises of the NF module
 */
@Service
public class NFResourceService {
    /**
     * Creates a new exercise in the database from the supplied <code>NFExerciseDTO</code>.
     * @param dto The <code>NFExerciseDTO</code> with the content of the new exercise
     * @return The id of the newly created exercise
     * @throws DatabaseException If the changed exercise would be a duplicate of an existing exercise or any other
     * error occurs regarding database access.
     * @throws ExerciseNotValidException If a valid exercise cannot be created from the supplied dto.
     */
    public int createExercise(NFExerciseDTO dto) throws DatabaseException, ExerciseNotValidException {
        int exerciseId = 1;

        try (
                Connection conn = NFHelper.getPooledConnection();
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
                Connection conn = NFHelper.getPooledConnection();
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
            if(s.getMessage().contains("type_and_spec_combo_unique")) {
                int existingId = getIdOfExistingCombo(numericSubtypeId, specificationJSON.toString());
                throw new DatabaseException("Tried to save new exercise " + exerciseId + " as duplicate of existing exercise " + existingId + ".");
            }
            throw new DatabaseException(s);
        }

        return exerciseId;
    }

    /**
     * Replaces the specified exercise in the database with one specified in the supplied <code>NFExerciseDTO</code>.
     * @param exerciseId The id of the exercise to be replaced
     * @param dto The <code>NFExerciseDTO</code> whose content is to replace the existing exercise
     * @return Whether the operation succeeded
     * @throws DatabaseException If the changed exercise would be a duplicate of an existing exercise or any other
     * error occurs regarding database access.
     * @throws ExerciseNotValidException If a valid exercise cannot be created from the supplied dto.
     */
    public boolean modifyExercise(int exerciseId, NFExerciseDTO dto) throws DatabaseException, ExerciseNotValidException {
        int numericSubtypeId = getNumericSubtypeId(dto.getNfTaskSubtypeId());
        String specificationJSON = convertToJSONString(dto);

        try (
                Connection conn = NFHelper.getPooledConnection();
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
            if(s.getMessage().contains("type_and_spec_combo_unique")) {
                int existingId = getIdOfExistingCombo(numericSubtypeId, specificationJSON.toString());
                throw new DatabaseException("Tried to save modified exercise " + exerciseId + " as duplicate of existing exercise " + existingId + ".");
            }
            throw new DatabaseException(s);
        }
    }

    /**
     * Deletes the exercise with the specified id from the database.
     * @param exerciseId The id of the exercise to be deleted
     * @return Whether the operation succeeded
     * @throws DatabaseException If an error occurs with the database access.
     */
    public boolean deleteExercise(int exerciseId) throws DatabaseException {
        try (
                Connection conn = NFHelper.getPooledConnection();
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

    /**
     * Automatically generates the assignment text for the exercise with the specified id, in the suitable language for
     * the specified locale
     * @param exerciseId The id of the exercise whose assignment text is to be generated.
     * @param locale The locale whose language is to be used for the exercise generation. Currently, only an English
     *               and German locale are supported.
     * @return An HTML string containing the auto-generated assignment text.
     * @throws DatabaseException If an error occurs with the database access.
     */
    public String generateAssignmentText(int exerciseId, Locale locale) throws DatabaseException {
        try (
                Connection conn = NFHelper.getPooledConnection();
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

                NFConstants.Type rdbdType = NFConstants.Type.values()[rdbdTypeId];

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

                return NFHelper.getAssignmentText(specification, 0, locale, rdbdType);
            } else {
                throw new DatabaseException("Exercise id " + exerciseId + " not present.");
            }
        } catch (SQLException s) {
            throw new DatabaseException(s);
        }
    }

    /**
     * Returns the id of an existing exercise with the supplied combination of rdbd_type and specification JSON.
     * @param rdbdType The rdbd_type of the searched exercise.
     * @param specificationString The JSON specification String of the searched exercise.
     * @return The id of an existing exercise with the supplied combination of rdbd_type and specification JSON,
     * -1 otherwise.
     * @throws DatabaseException If an error occurs with the database access.
     */
    private int getIdOfExistingCombo(int rdbdType, String specificationString) throws DatabaseException {
        int id = -1;

        try (
                Connection conn = NFHelper.getPooledConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT id FROM exercises " +
                        "WHERE rdbd_type = ? AND specification = ?::JSONB"
                )
        ) {
            stmt.setInt(1, rdbdType);
            stmt.setObject(2, specificationString);

            ResultSet rset = stmt.executeQuery();

            if(rset.next()) {
                id = rset.getInt("id");
            }
        } catch (SQLException s) {
            throw new DatabaseException(s);
        }

        return id;
    }

    /**
     * Returns the numeric equivalent of the supplied platform task type URL.
     * @param subTypeString The URL used to denote the task type within the platform.
     * @return the numeric equivalent of the supplied platform task type URL.
     */
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

    /**
     * Takes an <code>NFExerciseDTO</code> and converts it to a JSON serialized <code>String</code> of the appropriate
     * NF exercise specification class.
     * @param dto The <code>NFExerciseDTO</code> to be converted
     * @return A JSON serialized <code>String</code> of the appropriate NF exercise specification class.
     * @throws ExerciseNotValidException if the supplied NF task subtype is not supported or JSON serialization fails.
     */
    private String convertToJSONString(NFExerciseDTO dto) throws ExerciseNotValidException {
        IdentifiedRelation baseRelation = new IdentifiedRelation();

        baseRelation.setName(dto.getNfBaseRelationName());
        baseRelation.setID(dto.getNfBaseRelationName());

        NFParserErrorCollector errorCollector = new NFParserErrorCollector();

        NFParser baseAttributesParser = getParser(dto.getNfBaseAttributes(), errorCollector);

        Set<String> baseAttributes = baseAttributesParser.attributeSetSubmission().attributes;

        if(!errorCollector.getSyntaxErrors().isEmpty()) {
            throw new ExerciseNotValidException("Syntax error(s) in base attributes: " + errorCollector.getStringOfAllErrors());
        }

        baseRelation.setAttributes(baseAttributes);

        if(!dto.getNfBaseDependencies().isBlank()) {
            NFParser baseDependenciesParser = getParser(dto.getNfBaseDependencies(), errorCollector);

            Set<FunctionalDependency> baseDependencies = baseDependenciesParser.functionalDependencySetSubmission().functionalDependencies;

            if (!errorCollector.getSyntaxErrors().isEmpty()) {
                throw new ExerciseNotValidException("Syntax error(s) in base dependencies: " + errorCollector.getStringOfAllErrors());
            }

            baseRelation.setFunctionalDependencies(baseDependencies);
        }

        // source: https://stackoverflow.com/a/15786175 (Gerald Wimmer, 2024-01-05)
        ObjectWriter objectWriter = new ObjectMapper().writer();

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
                    NFParser targetLevelParser = getParser(dto.getNfNormalizationTargetLevel(), errorCollector);

                    NormalformLevel targetLevel = targetLevelParser.normalFormSpecification().level;

                    if(!errorCollector.getSyntaxErrors().isEmpty()) {
                        throw new ExerciseNotValidException("Syntax error(s) in target level: " + errorCollector.getStringOfAllErrors());
                    }

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
                    specification.setPenaltyPerMissingKey(dto.getNfNormalizationPenaltyPerMissingKey());
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
                    NFParser acBaseAttributesParser = getParser(dto.getNfAttributeClosureBaseAttributes(), errorCollector);

                    Set<String> acBaseAttributes = acBaseAttributesParser.attributeSetSubmission().attributes;

                    if(!errorCollector.getSyntaxErrors().isEmpty()) {
                        throw new ExerciseNotValidException("Syntax error(s) in attribute closure base attributes: " + errorCollector.getStringOfAllErrors());
                    }

                    AttributeClosureSpecification specification = new AttributeClosureSpecification();
                    specification.setBaseRelation(baseRelation);

                    specification.setBaseAttributes(acBaseAttributes);

                    specification.setPenaltyPerMissingAttribute(dto.getNfAttributeClosurePenaltyPerMissingAttribute());
                    specification.setPenaltyPerIncorrectAttribute(dto.getNfAttributeClosurePenaltyPerIncorrectAttribute());

                    return objectWriter.writeValueAsString(specification);
                }
                case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#NFTask#NormalformDeterminationTask" -> {
                    NormalformDeterminationSpecification specification = new NormalformDeterminationSpecification();
                    specification.setBaseRelation(baseRelation);

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

    /**
     * Creates a new <code>NFParser</code> instance from the supplied input <code>String</code> and with the
     * supplied <code>NFParserErrorCollector</code>
     * @param input The <code>String</code> to serve as the parser input
     * @param errorCollector The <code>NFParserErrorCollector</code> to collect any syntax errors
     * @return A new <code>NFParser</code> object
     */
    private NFParser getParser(String input, NFParserErrorCollector errorCollector) {
        // Source: https://datacadamia.com/antlr/getting_started (Gerald Wimmer, 2023-11-27)
        CharStream lexerInput = CharStreams.fromString(input);
        Lexer lexer = new NFLexer(lexerInput);
        TokenStream parserInput = new CommonTokenStream(lexer);
        NFParser parser = new NFParser(parserInput);

        parser.addErrorListener(errorCollector);

        return parser;
    }
}
