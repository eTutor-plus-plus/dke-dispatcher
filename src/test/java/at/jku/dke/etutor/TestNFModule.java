package at.jku.dke.etutor;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.grading.ETutorGradingApplication;
import at.jku.dke.etutor.modules.nf.NFEvaluator;
import at.jku.dke.etutor.modules.nf.analysis.closure.AttributeClosureAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.keys.KeysAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.minimalcover.MinimalCoverAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.normalformdetermination.NormalformDeterminationAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.normalization.NormalizationAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Currently, this class mainly exists for the sake of separating test cases from the old RDBD module class files from
 * actual application code, without deleting the potentially useful tests outright.
 */
@SpringBootTest(classes = ETutorGradingApplication.class)
@EnabledIfSystemProperty(named = "run_test", matches="true")
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class TestNFModule {

    private NFEvaluator evaluator = new NFEvaluator();

    private Map<String, String> passedAttributes;

    @BeforeEach
    void setup() {
        passedAttributes = new HashMap<>();
        passedAttributes.put("diagnoseLevel", "0");
    }

    @Test
    void keyDetermination1Correct() {
        String submission = "D;A,B";

        KeysAnalysis analysis = (KeysAnalysis) submit(1, submission);

        assertTrue(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getMissingKeys().size());
        assertEquals(0, analysis.getAdditionalKeys().size());
    }

    @Test
    void keysDetermination1Missing() {
        String submission = "D";

        KeysAnalysis analysis = (KeysAnalysis) submit(1, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(1, analysis.getMissingKeys().size());
        assertEquals(0, analysis.getAdditionalKeys().size());
    }

    @Test
    void keysDetermination1Incorrect() {
        String submission = "D;A,B;C";

        KeysAnalysis analysis = (KeysAnalysis) submit(1, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getMissingKeys().size());
        assertEquals(1, analysis.getAdditionalKeys().size());
    }

    @Test
    void keyDetermination17Correct() {
        String submission = "A,E";

        KeysAnalysis analysis = (KeysAnalysis) submit(17, submission);

        assertTrue(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getMissingKeys().size());
        assertEquals(0, analysis.getAdditionalKeys().size());
    }

    @Test
    void keysDetermination17Missing() {
        String submission = "A,B"; // An empty String would cause a syntax error, so we have to specify something.

        KeysAnalysis analysis = (KeysAnalysis) submit(17, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(1, analysis.getMissingKeys().size());
        assertEquals(1, analysis.getAdditionalKeys().size()); // Of course, we now have to mind the incorrect key here, too.
    }

    @Test
    void keysDetermination17Incorrect() {
        String submission = "A,E;A,B";

        KeysAnalysis analysis = (KeysAnalysis) submit(17, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getMissingKeys().size());
        assertEquals(1, analysis.getAdditionalKeys().size());
    }

    @Test
    void attributeClosure224Correct() {
        String submission = "A,C,D,E,G";

        AttributeClosureAnalysis analysis = (AttributeClosureAnalysis) submit(224, submission);

        assertTrue(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getMissingAttributes().size());
        assertEquals(0, analysis.getAdditionalAttributes().size());
    }

    @Test
    void attributeClosure224Missing() {
        String submission = "A,C,E,G";

        AttributeClosureAnalysis analysis = (AttributeClosureAnalysis) submit(224, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(1, analysis.getMissingAttributes().size());
        assertEquals(0, analysis.getAdditionalAttributes().size());
    }

    @Test
    void attributeClosure224Incorrect() {
        String submission = "A,B,C,D,E,G";

        AttributeClosureAnalysis analysis = (AttributeClosureAnalysis) submit(224, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getMissingAttributes().size());
        assertEquals(1, analysis.getAdditionalAttributes().size());
    }

    @Test
    void attributeClosure305Correct() {
        String submission = "B,C";

        AttributeClosureAnalysis analysis = (AttributeClosureAnalysis) submit(305, submission);

        assertTrue(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getMissingAttributes().size());
        assertEquals(0, analysis.getAdditionalAttributes().size());
    }

    @Test
    void attributeClosure305Missing() {
        String submission = "B";

        AttributeClosureAnalysis analysis = (AttributeClosureAnalysis) submit(305, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(1, analysis.getMissingAttributes().size());
        assertEquals(0, analysis.getAdditionalAttributes().size());
    }

    @Test
    void attributeClosure305Incorrect() {
        String submission = "A,B,C";

        AttributeClosureAnalysis analysis = (AttributeClosureAnalysis) submit(305, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getMissingAttributes().size());
        assertEquals(1, analysis.getAdditionalAttributes().size());
    }

    @Test
    void minimalCover306Correct() {
        String submission = "A->F;A->G;F->E;G->C;G->B;B,C->D";

        MinimalCoverAnalysis analysis = (MinimalCoverAnalysis) submit(306, submission);

        assertTrue(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getMissingDependencies().size());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size());
        assertEquals(0, analysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().size());
        assertEquals(0, analysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size());
        assertEquals(0, analysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size());
        assertEquals(0, analysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size());
    }

    @Test
    void minimalCover306ExtraneousAttribute() {
        String submission = "A->F;A->G;F->E;G->C;G->B;A,B,C->D";

        MinimalCoverAnalysis analysis = (MinimalCoverAnalysis) submit(306, submission);

        assertFalse(analysis.submissionSuitsSolution());
        /*
         * The correct dependency B,C->D is now missing (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getDependenciesCoverAnalysis().getMissingDependencies().size());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size());
        assertEquals(1, analysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().size());
        assertEquals(0, analysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size());
        assertEquals(0, analysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size());
        assertEquals(0, analysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size());
    }

    @Test
    void minimalCover306Missing() {
        String submission = "A->F;F->E;G->C;G->B;B,C->D";

        MinimalCoverAnalysis analysis = (MinimalCoverAnalysis) submit(306, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(1, analysis.getDependenciesCoverAnalysis().getMissingDependencies().size());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size());
        assertEquals(0, analysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().size());
        assertEquals(0, analysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size());
        assertEquals(0, analysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size());
        assertEquals(0, analysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size());
    }

    @Test
    void minimalCover306Incorrect() {
        String submission = "A->F;A->G;F->E;G->C;G->B;B,C->D;D->A";

        MinimalCoverAnalysis analysis = (MinimalCoverAnalysis) submit(306, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getMissingDependencies().size());
        assertEquals(1, analysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size());
        assertEquals(0, analysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().size());
        assertEquals(0, analysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size());
        assertEquals(0, analysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size());
        assertEquals(0, analysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size());
    }

    @Test
    void minimalCover306NotCanonical() {
        String submission = "A->F,G;F->E;G->C;G->B;B,C->D";

        MinimalCoverAnalysis analysis = (MinimalCoverAnalysis) submit(306, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getMissingDependencies().size());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size());
        assertEquals(0, analysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().size());
        assertEquals(1, analysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size());
        assertEquals(0, analysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size());
        assertEquals(0, analysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size());
    }

    @Test
    void minimalCover306Trivial() {
        String submission = "A->F;A->G;F->E;G->C;G->B;B,C->D;A->A";

        MinimalCoverAnalysis analysis = (MinimalCoverAnalysis) submit(306, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getMissingDependencies().size());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size());
        assertEquals(0, analysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().size());
        assertEquals(0, analysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size());
        assertEquals(1, analysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size());
        /*
         * Every trivial dependency is, by its very nature, also redundant. However, to avoid punishing students
         * for the same mistake twice, trivial dependencies are ignored in the redundant dependency analysis.
         */
        assertEquals(0, analysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size());
    }

    @Test
    void minimalCover306Redundant() {
        String submission = "A->F;A->G;F->E;G->C;G->B;B,C->D;A->E";

        MinimalCoverAnalysis analysis = (MinimalCoverAnalysis) submit(306, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getMissingDependencies().size());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size());
        assertEquals(0, analysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().size());
        assertEquals(0, analysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size());
        assertEquals(0, analysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size());
        assertEquals(1, analysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size());
    }

    @Test
    void minimalCover307Correct() {
        String submission = "C,E->D;A->C;A->E;C,E->B";

        MinimalCoverAnalysis analysis = (MinimalCoverAnalysis) submit(307, submission);

        assertTrue(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getMissingDependencies().size());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size());
        assertEquals(0, analysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().size());
        assertEquals(0, analysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size());
        assertEquals(0, analysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size());
        assertEquals(0, analysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size());
    }

    @Test
    void minimalCover307ExtraneousAttribute() {
        String submission = "C,E->D;A->C;A->E;A,C,E->B";

        MinimalCoverAnalysis analysis = (MinimalCoverAnalysis) submit(307, submission);

        assertFalse(analysis.submissionSuitsSolution());
        /*
         * The correct dependency C,E->B is now missing (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getDependenciesCoverAnalysis().getMissingDependencies().size());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size());
        assertEquals(1, analysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().size());
        assertEquals(0, analysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size());
        assertEquals(0, analysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size());
        assertEquals(0, analysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size());
    }

    @Test
    void minimalCover307Missing() {
        String submission = "C,E->D;A->C;C,E->B";

        MinimalCoverAnalysis analysis = (MinimalCoverAnalysis) submit(307, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(1, analysis.getDependenciesCoverAnalysis().getMissingDependencies().size());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size());
        assertEquals(0, analysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().size());
        assertEquals(0, analysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size());
        assertEquals(0, analysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size());
        assertEquals(0, analysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size());
    }

    @Test
    void minimalCover307Incorrect() {
        String submission = "C,E->D;A->C;A->E;C,E->B;B->A";

        MinimalCoverAnalysis analysis = (MinimalCoverAnalysis) submit(307, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getMissingDependencies().size());
        assertEquals(1, analysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size());
        assertEquals(0, analysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().size());
        assertEquals(0, analysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size());
        assertEquals(0, analysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size());
        assertEquals(0, analysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size());
    }

    @Test
    void minimalCover307NotCanonical() {
        String submission = "C,E->D;A->C,E;C,E->B";

        MinimalCoverAnalysis analysis = (MinimalCoverAnalysis) submit(307, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getMissingDependencies().size());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size());
        assertEquals(0, analysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().size());
        assertEquals(1, analysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size());
        assertEquals(0, analysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size());
        assertEquals(0, analysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size());
    }

    @Test
    void minimalCover307Trivial() {
        String submission = "C,E->D;A->C;A->E;C,E->B;A->A";

        MinimalCoverAnalysis analysis = (MinimalCoverAnalysis) submit(307, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getMissingDependencies().size());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size());
        assertEquals(0, analysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().size());
        assertEquals(0, analysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size());
        assertEquals(1, analysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size());
        assertEquals(0, analysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size());
    }

    @Test
    void minimalCover307Redundant() {
        String submission = "C,E->D;A->C;A->E;C,E->B;A->D";

        MinimalCoverAnalysis analysis = (MinimalCoverAnalysis) submit(307, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getMissingDependencies().size());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size());
        assertEquals(0, analysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().size());
        assertEquals(0, analysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size());
        assertEquals(0, analysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size());
        assertEquals(1, analysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size());
    }

    @Test
    void minimalCover310Correct() {
        String submission = "H,I->B;F,G->H;F->C;F->I;E,G->A;E,G->F;C,I->D";

        MinimalCoverAnalysis analysis = (MinimalCoverAnalysis) submit(310, submission);

        assertTrue(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getMissingDependencies().size());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size());
        assertEquals(0, analysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().size());
        assertEquals(0, analysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size());
        assertEquals(0, analysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size());
        assertEquals(0, analysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size());
    }

    @Test
    void minimalCover310ExtraneousAttribute() {
        String submission = "H,I->B;F,G->H;F->C;F->I;E,G->A;E,G->F;F,C,I->D";

        MinimalCoverAnalysis analysis = (MinimalCoverAnalysis) submit(310, submission);

        assertFalse(analysis.submissionSuitsSolution());
        /*
         * The correct dependency C,I->D is now missing (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getDependenciesCoverAnalysis().getMissingDependencies().size());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size());
        assertEquals(1, analysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().size());
        assertEquals(0, analysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size());
        assertEquals(0, analysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size());
        assertEquals(0, analysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size());
    }

    @Test
    void minimalCover310Missing() {
        String submission = "H,I->B;F,G->H;F->C;E,G->A;E,G->F;C,I->D";

        MinimalCoverAnalysis analysis = (MinimalCoverAnalysis) submit(310, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(1, analysis.getDependenciesCoverAnalysis().getMissingDependencies().size());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size());
        assertEquals(0, analysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().size());
        assertEquals(0, analysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size());
        assertEquals(0, analysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size());
        assertEquals(0, analysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size());
    }

    @Test
    void minimalCover310Incorrect() {
        String submission = "H,I->B;F,G->H;F->C;F->I;E,G->A;E,G->F;C,I->D;A->B";

        MinimalCoverAnalysis analysis = (MinimalCoverAnalysis) submit(310, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getMissingDependencies().size());
        assertEquals(1, analysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size());
        assertEquals(0, analysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().size());
        assertEquals(0, analysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size());
        assertEquals(0, analysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size());
        assertEquals(0, analysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size());
    }

    @Test
    void minimalCover310NotCanonical() {
        String submission = "H,I->B;F,G->H;F->C;F->I;E,G->A,F;C,I->D";

        MinimalCoverAnalysis analysis = (MinimalCoverAnalysis) submit(310, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getMissingDependencies().size());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size());
        assertEquals(0, analysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().size());
        assertEquals(1, analysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size());
        assertEquals(0, analysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size());
        assertEquals(0, analysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size());
    }

    @Test
    void minimalCover310Trivial() {
        String submission = "H,I->B;F,G->H;F->C;F->I;E,G->A;E,G->F;C,I->D;A->A";

        MinimalCoverAnalysis analysis = (MinimalCoverAnalysis) submit(310, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getMissingDependencies().size());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size());
        assertEquals(0, analysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().size());
        assertEquals(0, analysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size());
        assertEquals(1, analysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size());
        assertEquals(0, analysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size());
    }

    @Test
    void minimalCover310Redundant() {
        String submission = "H,I->B;F,G->H;F->C;F->I;E,G->A;E,G->F;C,I->D;E,G->I";

        MinimalCoverAnalysis analysis = (MinimalCoverAnalysis) submit(310, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getMissingDependencies().size());
        assertEquals(0, analysis.getDependenciesCoverAnalysis().getAdditionalDependencies().size());
        assertEquals(0, analysis.getExtraneousAttributesAnalysis().getExtraneousAttributes().size());
        assertEquals(0, analysis.getCanonicalRepresentationAnalysis().getNotCanonicalDependencies().size());
        assertEquals(0, analysis.getTrivialDependenciesAnalysis().getTrivialDependencies().size());
        assertEquals(1, analysis.getRedundantDependenciesAnalysis().getRedundantDependencies().size());
    }

    @Test
    void nFDetermination395Correct() {
        String submission = "BCNF.";

        NormalformDeterminationAnalysis analysis = (NormalformDeterminationAnalysis) submit(395, submission);

        assertEquals(NormalformLevel.BOYCE_CODD, analysis.getSubmittedLevel());
        assertTrue(analysis.submissionSuitsSolution());
        assertTrue(analysis.getOverallLevelIsCorrect());
        assertEquals(0, analysis.getWrongLeveledDependencies().size());
    }

    @Test
    void nFDetermination395IncorrectOverall() {
        String submission = "3NF.";

        NormalformDeterminationAnalysis analysis = (NormalformDeterminationAnalysis) submit(395, submission);

        assertEquals(NormalformLevel.THIRD, analysis.getSubmittedLevel());
        assertFalse(analysis.submissionSuitsSolution());
        assertFalse(analysis.getOverallLevelIsCorrect());
        assertEquals(0, analysis.getWrongLeveledDependencies().size());
    }

    @Test
    void nFDetermination395IncorrectDependency() {
        String submission = "BCNF. A->D: BCNF";

        NormalformDeterminationAnalysis analysis = (NormalformDeterminationAnalysis) submit(395, submission);

        assertEquals(NormalformLevel.BOYCE_CODD, analysis.getSubmittedLevel());
        assertFalse(analysis.submissionSuitsSolution());
        assertTrue(analysis.getOverallLevelIsCorrect());
        assertEquals(1, analysis.getWrongLeveledDependencies().size());
    }

    @Test
    void nFDetermination563Correct() {
        String submission = "1NF. B->C : 2NF;B->D,E : 2NF";

        NormalformDeterminationAnalysis analysis = (NormalformDeterminationAnalysis) submit(563, submission);

        assertEquals(NormalformLevel.FIRST, analysis.getSubmittedLevel());
        assertTrue(analysis.submissionSuitsSolution());
        assertTrue(analysis.getOverallLevelIsCorrect());
        assertEquals(0, analysis.getWrongLeveledDependencies().size());
    }

    @Test
    void nFDetermination563IncorrectOverall() {
        String submission = "2NF. B->C : 2NF;B->D,E : 2NF";

        NormalformDeterminationAnalysis analysis = (NormalformDeterminationAnalysis) submit(563, submission);

        assertEquals(NormalformLevel.SECOND, analysis.getSubmittedLevel());
        assertFalse(analysis.submissionSuitsSolution());
        assertFalse(analysis.getOverallLevelIsCorrect());
        assertEquals(0, analysis.getWrongLeveledDependencies().size());
    }

    @Test
    void nFDetermination563IncorrectDependency() {
        String submission = "1NF. B->C : 2NF;B->D,E : 3NF";

        NormalformDeterminationAnalysis analysis = (NormalformDeterminationAnalysis) submit(563, submission);

        assertEquals(NormalformLevel.FIRST, analysis.getSubmittedLevel());
        assertFalse(analysis.submissionSuitsSolution());
        assertTrue(analysis.getOverallLevelIsCorrect());
        assertEquals(1, analysis.getWrongLeveledDependencies().size());
    }

    @Test
    void nFDetermination376Correct() {
        String submission = "BCNF.";

        NormalformDeterminationAnalysis analysis = (NormalformDeterminationAnalysis) submit(376, submission);

        assertEquals(NormalformLevel.BOYCE_CODD, analysis.getSubmittedLevel());
        assertTrue(analysis.submissionSuitsSolution());
        assertTrue(analysis.getOverallLevelIsCorrect());
        assertEquals(0, analysis.getWrongLeveledDependencies().size());
    }

    @Test
    void nFDetermination376IncorrectOverall() {
        String submission = "1NF.";

        NormalformDeterminationAnalysis analysis = (NormalformDeterminationAnalysis) submit(376, submission);

        assertEquals(NormalformLevel.FIRST, analysis.getSubmittedLevel());
        assertFalse(analysis.submissionSuitsSolution());
        assertFalse(analysis.getOverallLevelIsCorrect());
        assertEquals(0, analysis.getWrongLeveledDependencies().size());
    }

    @Test
    void nFDetermination376IncorrectDependency() {
        String submission = "BCNF.C->B:3NF";

        NormalformDeterminationAnalysis analysis = (NormalformDeterminationAnalysis) submit(376, submission);

        assertEquals(NormalformLevel.BOYCE_CODD, analysis.getSubmittedLevel());
        assertFalse(analysis.submissionSuitsSolution());
        assertTrue(analysis.getOverallLevelIsCorrect());
        assertEquals(1, analysis.getWrongLeveledDependencies().size());
    }

    @Test
    void nFDetermination562Correct() {
        String submission = "2NF. D->E : 3NF; C->E: 3NF";

        NormalformDeterminationAnalysis analysis = (NormalformDeterminationAnalysis) submit(562, submission);

        assertEquals(NormalformLevel.SECOND, analysis.getSubmittedLevel());
        assertTrue(analysis.submissionSuitsSolution());
        assertTrue(analysis.getOverallLevelIsCorrect());
        assertEquals(0, analysis.getWrongLeveledDependencies().size());
    }

    @Test
    void nFDetermination562IncorrectOverall() {
        String submission = "3NF. D->E : 3NF; C->E: 3NF";

        NormalformDeterminationAnalysis analysis = (NormalformDeterminationAnalysis) submit(562, submission);

        assertEquals(NormalformLevel.THIRD, analysis.getSubmittedLevel());
        assertFalse(analysis.submissionSuitsSolution());
        assertFalse(analysis.getOverallLevelIsCorrect());
        assertEquals(0, analysis.getWrongLeveledDependencies().size());
    }

    @Test
    void nFDetermination562IncorrectDependency() {
        String submission = "2NF. D->E : BCNF; C->E: 3NF";

        NormalformDeterminationAnalysis analysis = (NormalformDeterminationAnalysis) submit(562, submission);

        assertEquals(NormalformLevel.SECOND, analysis.getSubmittedLevel());
        assertFalse(analysis.submissionSuitsSolution());
        assertTrue(analysis.getOverallLevelIsCorrect());
        assertEquals(1, analysis.getWrongLeveledDependencies().size());
    }

    @Test
    void normalization571Correct() {
        String submission = "*R1: (A,B,E,F,G) -> (F,G->B; F,G->E; B,E->F; B,F->G) # (A,B,E;A,B,F;A,F,G) ; *R2: (A,C,D) -> (A->C ; A-> D) # (A) ; *R3: (G,H) -> (G -> H) # (G)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(571, submission);

        assertTrue(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        assertEquals(0, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        assertEquals(0, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
    }

    @Test
    void normalization571LostAttribute() {
        String submission = "*R1: (A,B,E,F,G) -> (F,G->B; F,G->E; B,E->F; B,F->G) # (A,B,E;A,B,F;A,F,G) ; *R2: (A,C,D) -> (A->C ; A-> D) # (A) ; *R3: (G) -> (G -> H) # (G)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(571, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(1, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        assertEquals(0, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        /*
         * There is no attribute in this relation which I could remove to trigger the decomposition error without also
         * triggering the RBR error.
         */
        assertEquals(1, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        assertEquals(0, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
    }

    @Test
    void normalization571Lossy() {
        String submission =
                "*R1: (A,B,E,F,G) -> (F,G->B; F,G->E; B,E->F; B,F->G) # (A,B,E;A,B,F;A,F,G) ; " +
                "*R2: (A,C,D) -> (A->C) # (A) ; " +
                "*R3: (G,H) -> (G -> H) # (G)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(571, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertFalse(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        /*
         * Removing this functional dependency also counts as a lost dependency from the base relation (Gerald Wimmer, 2024-01-12)
         */
        assertEquals(1, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        /*
         * Additionally, the removed dependency is required for correct RBR (Gerald Wimmer, 2024-01-12)
         */
        assertEquals(1, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        /*
         * Furthermore, because there is no dependency with D on the RHS, D is now part of any key (Gerald Wimmer, 2024-01-12).
         */
        assertEquals(1, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        /*
         * Because D is part of any key, the specified key of A is now considered an incorrect key (Gerald Wimmer, 2024-01-12).
         */
        assertEquals(1, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        /*
         * As A is now a partial key in R2, A->C is a functional dependency with a non-prime attribute on the RHS
         * and a partial key on the LHS, violating 2NF.
         */
        assertEquals(1, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
        FunctionalDependency dep = new FunctionalDependency(Set.of("A"), Set.of("C"));
        assertEquals(NormalformLevel.SECOND, analysis.getNormalformAnalysis("R2").getViolatedNormalformLevel(dep));
    }

    @Test
    void normalization571ExtraneousAttributeInDependency() {
        String submission = "*R1: (A,B,E,F,G) -> (F,G->B; F,G->E; B,E->F; E,B,F->G) # (A,B,E;A,B,F;A,F,G) ; *R2: (A,C,D) -> (A->C ; A-> D) # (A) ; *R3: (G,H) -> (G -> H) # (G)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(571, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(1, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        /*
         * The correct functional dependency B,F->G is missing (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        /*
         * B,F->G is also missing from the dependencies required by the RBR process (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        /*
         * As E is now contained in what used to be B,F->G, A,B,F is no longer a key (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        Key key = new Key("A", "B", "F");
        assertTrue(analysis.getKeysAnalysis("R1").getAdditionalKeys().contains(key));
        assertEquals(0, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
    }

    @Test
    void normalization571NotCanonical() {
        String submission =
                "*R1: (A,B,E,F,G) -> (F,G->B; F,G->E; B,E->F; B,F->G) # (A,B,E;A,B,F;A,F,G) ; " +
                "*R2: (A,C,D) -> (A->C,D) # (A) ; " +
                "*R3: (G,H) -> (G -> H) # (G)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(571, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(1, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        assertEquals(0, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        assertEquals(0, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
    }

    @Test
    void normalization571Trivial() {
        String submission =
                "*R1: (A,B,E,F,G) -> (F,G->B; F,G->E; B,E->F; B,F->G) # (A,B,E;A,B,F;A,F,G) ; " +
                        "*R2: (A,C,D) -> (A->C;A->D;A->A) # (A) ; " +
                        "*R3: (G,H) -> (G -> H) # (G)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(571, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(1, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        assertEquals(0, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        assertEquals(0, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
    }

    @Test
    void normalization571Redundant() {
        String submission =
                "*R1: (A,B,E,F,G) -> (F,G->B; F,G->E; B,E->F; B,F->G; B,F->E) # (A,B,E;A,B,F;A,F,G) ; " +
                        "*R2: (A,C,D) -> (A->C;A->D) # (A) ; " +
                        "*R3: (G,H) -> (G -> H) # (G)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(571, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(1, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        assertEquals(0, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        assertEquals(0, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
    }

    @Test
    void normalization571LostDependency() {
        String submission =
                "*R1: (A,B,E,F,G) -> (F,G->B; F,G->E; B,F->G) # (A,B,E;A,B,F;A,F,G) ; " +
                "*R2: (A,C,D) -> (A->C ; A-> D) # (A) ; " +
                "*R3: (G,H) -> (G -> H) # (G)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(571, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        assertEquals(1, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        /*
         * The removed dependency is required for correct RBR (Gerald Wimmer, 2024-01-13)
         */
        assertEquals(1, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        /*
         * Due to the removal of the dependency, A,B,E is no longer a key of R1 (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        /*
         * As A,B,E is no longer a key, E is non-prime. E being non-prime and on the RHS of F,G->E makes a non-
         * prime attribute depend on a partial key, violating 2NF. (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
        FunctionalDependency dep = new FunctionalDependency(Set.of("F", "G"), Set.of("E"));
        assertEquals(NormalformLevel.SECOND, analysis.getNormalformAnalysis("R1").getViolatedNormalformLevel(dep));
    }

    @Test
    void normalization571RBRMissing() {
        String submission = "*R1: (A,B,E,F,G) -> (F,G->E; B,E->F; B,F->G) # (A,B,E;A,B,F;A,F,G) ; *R2: (A,C,D) -> (A->C ; A-> D) # (A) ; *R3: (G,H) -> (G -> H) # (G)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(571, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        /*
         * As the solution contains no new functional dependencies, we have to remove an original dependency (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        assertEquals(1, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        /*
         * Due to the removal of F,G->B, A,F,G is no longer a key (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        Key key = new Key("A", "F", "G");
        assertTrue(analysis.getKeysAnalysis("R1").getAdditionalKeys().contains(key));
        /*
         * As A,F,G is no longer a key, B,F->G means a non-prime attribute depends on a partial key,
         * violating 2NF (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
        FunctionalDependency dep = new FunctionalDependency(Set.of("B", "F"), Set.of("G"));
        assertEquals(NormalformLevel.SECOND, analysis.getNormalformAnalysis("R1").getViolatedNormalformLevel(dep));
    }

    @Test
    void normalization571RBRIncorrect() {
        String submission = "*R1: (A,B,E,F,G) -> (F,G->B; F,G->E; B,E->F; B,F->G) # (A,B,E;A,B,F;A,F,G) ; *R2: (A,C,D) -> (A->C ; A-> D ; C->D) # (A) ; *R3: (G,H) -> (G -> H) # (G)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(571, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        /*
         * Due to the additon of C->D, A->D is redundant because it can be transitively inferred via
         * A->C, C->D (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        FunctionalDependency dep = new FunctionalDependency(List.of("A"), List.of("D"));
        assertTrue(analysis.getRedundantDependenciesAnalysis("R2").getRedundantDependencies().contains(dep));
        assertEquals(0, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(1, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        /*
         * C->D has a non-prime attribute on the LHS and RHS, violating 3NF (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
        FunctionalDependency vioDep = new FunctionalDependency(Set.of("C"), Set.of("D"));
        assertEquals(NormalformLevel.THIRD, analysis.getNormalformAnalysis("R2").getViolatedNormalformLevel(vioDep));
    }

    @Test
    void normalization571MissingKey() {
        String submission = "*R1: (A,B,E,F,G) -> (F,G->B; F,G->E; B,E->F; B,F->G) # (A,B,E;A,F,G) ; *R2: (A,C,D) -> (A->C ; A-> D) # (A) ; *R3: (G,H) -> (G -> H) # (G)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(571, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        assertEquals(0, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        assertEquals(1, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        assertEquals(0, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
    }

    @Test
    void normalization571AdditionalKey() {
        String submission = "*R1: (A,B,E,F,G) -> (F,G->B; F,G->E; B,E->F; B,F->G) # (A,B,E;A,B,F;A,F,G) ; *R2: (A,C,D) -> (A->C ; A-> D) # (A) ; *R3: (G,H) -> (G -> H) # (G;H)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(571, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        assertEquals(0, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        assertEquals(1, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        assertEquals(0, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
    }

    @Test
    void normalization571WrongNF() {
        String submission = "*R1: (A,B,E,F,G) -> (F,G->B; F,G->E; B,E->F; B,F->G) # (A,B,E;A,B,F;A,F,G) ; *R2: (A,C,D) -> (A->C ; A-> D ; C->D) # (A) ; *R3: (G,H) -> (G -> H) # (G)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(571, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        /*
         * C->D makes A->D redundant (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        assertEquals(0, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        /*
         * The additional functional dependency is also detected as an incorrect RBR decomposition result (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        /*
         * C->D violates the third normal form, as it has non-prime attributes on both sides.
         */
        assertEquals(1, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
        FunctionalDependency vioDep = new FunctionalDependency(Set.of("C"), Set.of("D"));
        assertEquals(NormalformLevel.THIRD, analysis.getNormalformAnalysis("R2").getViolatedNormalformLevel(vioDep));
    }

    @Test
    void normalization573Correct() { // Former decompose task
        String submission =
                "*R1: (A,B,C,G,H) -> (A->H; H->A) # (A,B,C,G;B,C,G,H) ; " +
                "*R2: (C,D) -> (C->D) # (C) ; " +
                "*R3: (D,E,F) -> (D->E; D->F; E,F->D) # (D;E,F)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(573, submission);

        assertTrue(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        assertEquals(0, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        assertEquals(0, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
    }

    @Test
    void normalization573LostAttribute() { // Former decompose task
        String submission =
                "*R1: (A,B,C,H) -> (A->H; H->A) # (A,B,C,G;B,C,G,H) ; " +
                        "*R2: (C,D) -> (C->D) # (C) ; " +
                        "*R3: (D,E,F) -> (D->E; D->F; E,F->D) # (D;E,F)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(573, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(1, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        /*
         * As G is not present in any other table, the relation exhibits lossy decomposition, because the original
         * relation cannot be reconstructed (Gerald Wimmer, 2024-01-13).
         */
        assertFalse(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        assertEquals(0, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        /*
         * As G is no longer present, the correct keys would now be A,B,C and B,C,H (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(2, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        /*
         * Consequently, A,B,C,G and B,C,G,H are now incorrect (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(2, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        assertEquals(0, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
    }

    @Test
    void normalization573Lossy() { // Former decompose task
        String submission =
                "*R1: (A,B,C,G,H) -> (A->H; H->A) # (A,B,C,G;B,C,G,H) ; " +
                        "*R2: (C,D) -> (C->D) # (C) ; " +
                        "*R3: (D,E,F) -> (D->F; E,F->D) # (D;E,F)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(573, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertFalse(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        /*
         * The lossy decomposition was achieved by deleting an originally present functional dependency (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        /*
         * The deleted functional dependency was also required by the RBR process (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        /*
         * Due to the deletion of D->E, D is no longer a key on its own (replaced by D,E) (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        Key deKey = new Key("D", "E");
        assertTrue(analysis.getKeysAnalysis("R3").getMissingKeys().contains(deKey));
        /*
         * Conversely, D is now an incorrect key (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        Key dKey = new Key("D");
        assertTrue(analysis.getKeysAnalysis("R3").getAdditionalKeys().contains(dKey));
        assertEquals(0, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
    }

    @Test
    void normalization573ExtraneousAttributeInDependency() { // Former decompose task
        String submission =
                "*R1: (A,B,C,G,H) -> (A->H; H->A) # (A,B,C,G;B,C,G,H) ; " +
                        "*R2: (C,D) -> (C->D) # (C) ; " +
                        "*R3: (D,E,F) -> (D,F->E; D->F; E,F->D) # (D;E,F)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(573, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(1, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        assertEquals(0, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        assertEquals(0, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
    }

    @Test
    void normalization573NotCanonicalDependency() { // Former decompose task
        String submission =
                "*R1: (A,B,C,G,H) -> (A->H; H->A) # (A,B,C,G;B,C,G,H) ; " +
                        "*R2: (C,D) -> (C->D) # (C) ; " +
                        "*R3: (D,E,F) -> (D->E,F; E,F->D) # (D;E,F)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(573, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(1, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        assertEquals(0, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        assertEquals(0, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
    }

    @Test
    void normalization573TrivialDependency() { // Former decompose task
        String submission =
                "*R1: (A,B,C,G,H) -> (A->H; H->A; B->B) # (A,B,C,G;B,C,G,H) ; " +
                        "*R2: (C,D) -> (C->D) # (C) ; " +
                        "*R3: (D,E,F) -> (D->E; D->F; E,F->D) # (D;E,F)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(573, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(1, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        assertEquals(0, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        assertEquals(0, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
    }

    @Test
    void normalization573RedundantDependency() { // Former decompose task
        String submission =
                "*R1: (A,B,C,G,H) -> (A->H; H->A) # (A,B,C,G;B,C,G,H) ; " +
                "*R2: (C,D) -> (C->D) # (C) ; " +
                "*R3: (D,E,F) -> (D->E; D->F; E,F->D; D,F->E) # (D;E,F)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(573, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        /*
         * F is extraneous in D,F->E (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(1, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        assertEquals(0, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        assertEquals(0, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
    }

    @Test
    void normalization573LostDependency() { // Former decompose task
        String submission =
                "*R1: (A,B,C,G,H) -> (A->H; H->A) # (A,B,C,G;B,C,G,H) ; " +
                        "*R2: (C,D) -> (C->D) # (C) ; " +
                        "*R3: (D,E,F) -> (D->E; D->F) # (D;E,F)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(573, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        assertEquals(1, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        /*
         * The removed functional dependency was required by the RBR process (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        /*
         * E,F is now an incorrect key in R3 (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        assertEquals(0, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
    }

    @Test
    void normalization573RBRMissing() { // Former decompose task
        String submission =
                "*R1: (A,B,C,G,H) -> (A->H; H->A) # (A,B,C,G;B,C,G,H) ; " +
                        "*R2: (C,D) -> (C->D) # (C) ; " +
                        "*R3: (D,E,F) -> (D->E; E,F->D) # (D;E,F)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(573, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        /*
         * The removal of D->F makes it impossible to reconstruct the original relation (Gerald Wimmer, 2024-01-13).
         */
        assertFalse(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        /*
         * There were no new functional dependencies in the correct solution, so an original one had to be removed (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        assertEquals(1, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        /*
         * Without D->F, F is now part of any key (i.e., the key "D,F" is missing) (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        /*
         * Conversely, the key "D" is incorrect.
         */
        assertEquals(1, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        assertEquals(0, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
    }

    @Test
    void normalization573RBRIncorrect() { // Former decompose task
        String submission =
                "*R1: (A,B,C,G,H) -> (A->H; H->A) # (A,B,C,G;B,C,G,H) ; " +
                        "*R2: (C,D) -> (C->D; D->C) # (C) ; " +
                        "*R3: (D,E,F) -> (D->E; D->F; E,F->D) # (D;E,F)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(573, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        assertEquals(0, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(1, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        /*
         * D is missing as a key due to D->C (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        assertEquals(0, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
    }

    @Test
    void normalization573KeyMissing() { // Former decompose task
        String submission =
                "*R1: (A,B,C,G,H) -> (A->H; H->A) # (A,B,C,G;B,C,G,H) ; " +
                        "*R2: (C,D) -> (C->D) # (C) ; " +
                        "*R3: (D,E,F) -> (D->E; D->F; E,F->D) # (E,F)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(573, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        assertEquals(0, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        assertEquals(1, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        assertEquals(0, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
    }

    @Test
    void normalization573KeyIncorrect() { // Former decompose task
        String submission =
                "*R1: (A,B,C,G,H) -> (A->H; H->A) # (A,B,C,G;B,C,G,H) ; " +
                        "*R2: (C,D) -> (C->D) # (C;D) ; " +
                        "*R3: (D,E,F) -> (D->E; D->F; E,F->D) # (D;E,F)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(573, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        assertEquals(0, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        assertEquals(1, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        assertEquals(0, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
    }

    @Test
    void normalization573WrongNF() { // Former decompose task
        String submission =
                "*R1: (A,B,C,G,H) -> (A->H) # (A,B,C,G;B,C,G,H) ; " +
                        "*R2: (C,D) -> (C->D) # (C) ; " +
                        "*R3: (D,E,F) -> (D->E; D->F; E,F->D) # (D;E,F)";

        NormalizationAnalysis analysis = (NormalizationAnalysis) submit(573, submission);

        assertFalse(analysis.submissionSuitsSolution());
        assertEquals(0, analysis.getDecompositionAnalysis().getMissingAttributes().size());
        assertTrue(analysis.getLossLessAnalysis().submissionSuitsSolution());
        assertEquals(0, analysis.getCanonicalRepresentationAnalyses().values().stream()
                .mapToInt(canonicalRepresentationAnalysis -> canonicalRepresentationAnalysis.getNotCanonicalDependencies().size())
                .sum());
        assertEquals(0, analysis.getTrivialDependenciesAnalyses().values().stream()
                .mapToInt(trivialDependenciesAnalysis -> trivialDependenciesAnalysis.getTrivialDependencies().size())
                .sum());
        assertEquals(0, analysis.getExtraneousAttributesAnalyses().values().stream()
                .mapToInt(extraneousAttributeAnalysis -> extraneousAttributeAnalysis.getExtraneousAttributes().values().stream()
                        .mapToInt(List::size)
                        .sum())
                .sum());
        assertEquals(0, analysis.getRedundantDependenciesAnalyses().values().stream()
                .mapToInt(redudantDependenciesAnalysis -> redudantDependenciesAnalysis.getRedundantDependencies().size())
                .sum());
        /*
         * H->A was lost from the original relation (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
        /*
         * H->A was necessary for the RBR process (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getMissingFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getRbrAnalyses().values().stream()
                .mapToInt(rbrAnalysis -> rbrAnalysis.getAdditionalFunctionalDependencies().size())
                .sum());
        assertEquals(0, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getMissingKeys().size())
                .sum());
        /*
         * Without H->A , B,C,G,H is no longer a valid key (Gerald Wimmer, 2024-01-13).
         */
        assertEquals(1, analysis.getKeysAnalyses().values().stream()
                .mapToInt(keysAnalysis -> keysAnalysis.getAdditionalKeys().size())
                .sum());
        assertEquals(1, analysis.getNormalformAnalyses().values().stream()
                .filter(normalformAnalysis -> !normalformAnalysis.submissionSuitsSolution())
                .count());
    }

    // Helper methods

    Analysis submit(int exerciseId, String submission) {
        passedAttributes.put("action", "submit");
        passedAttributes.put("submission", submission);

        return assertDoesNotThrow(() -> evaluator.analyze(exerciseId, 0, passedAttributes, new HashMap<>(), Locale.ENGLISH));
    }

    Analysis diagnose(int exerciseId) {
        passedAttributes.put("action", "diagnose");

        return null;
    }

    Analysis check(int exerciseId) {
        passedAttributes.put("action", "check");

        return null;
    }
}
