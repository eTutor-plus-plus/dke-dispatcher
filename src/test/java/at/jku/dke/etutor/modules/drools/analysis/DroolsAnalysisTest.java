package at.jku.dke.etutor.modules.drools.analysis;

import at.jku.dke.etutor.modules.drools.analysis.DroolsAnalysis;
import at.jku.dke.etutor.modules.drools.grading.DroolsGrading;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DroolsAnalysisTest {

    private DroolsAnalysis droolsAnalysis;
    private DroolsGrading droolsGrading;
    private int taskId = 1;
    private boolean isForDiagnose = true;

    @Test
    public void testAnalyzeCorrect() throws Exception {
        droolsAnalysis = new DroolsAnalysis(taskId, CORRECT_RULES, isForDiagnose);
        droolsAnalysis.analyze();
        assertEquals(3, droolsAnalysis.getSampleSolutionOutput().size());
        assertEquals(0, droolsAnalysis.getWrongFactList().size());
        assertEquals(0, droolsAnalysis.getAdditionalFacts());
        assertEquals(3, droolsAnalysis.getStudentOutput().size());
        assertTrue(droolsAnalysis.getSampleSolutionOutput().containsAll(droolsAnalysis.getStudentOutput()));

        droolsGrading = new DroolsGrading(droolsAnalysis, 100);
        droolsGrading.grade();
        assertEquals(100, droolsGrading.getPoints());
    }

    @Test
    public void testAnalyzeAdditionalFacts() throws Exception {
        droolsAnalysis = new DroolsAnalysis(taskId, RULES_MORE_INVOICES, isForDiagnose);
        droolsAnalysis.analyze();
        assertEquals(3, droolsAnalysis.getSampleSolutionOutput().size());
        assertEquals(0, droolsAnalysis.getWrongFactList().size());
        assertEquals(1, droolsAnalysis.getAdditionalFacts());
        assertEquals(4, droolsAnalysis.getStudentOutput().size());
        assertFalse(droolsAnalysis.getSampleSolutionOutput().containsAll(droolsAnalysis.getStudentOutput()));

        droolsGrading = new DroolsGrading(droolsAnalysis, 100);
        droolsGrading.grade();
        assertEquals(80, droolsGrading.getPoints());
    }

    @Test
    public void testAnalyzeLessFacts() throws Exception {
        droolsAnalysis = new DroolsAnalysis(taskId, RULES_LESS_INVOICES, isForDiagnose);
        droolsAnalysis.analyze();
        assertEquals(3, droolsAnalysis.getSampleSolutionOutput().size());
        assertEquals(0, droolsAnalysis.getWrongFactList().size());
        assertEquals(-2, droolsAnalysis.getAdditionalFacts());
        assertEquals(1, droolsAnalysis.getStudentOutput().size());
        assertNotEquals(droolsAnalysis.getSampleSolutionOutput(), droolsAnalysis.getStudentOutput());

        droolsGrading = new DroolsGrading(droolsAnalysis, 100);
        droolsGrading.grade();
        assertEquals(60, droolsGrading.getPoints());
    }

    @Test
    public void testAnalyzeWrongFacts() throws Exception {
        droolsAnalysis = new DroolsAnalysis(taskId, RULES_WRONG_PRICE, isForDiagnose);
        droolsAnalysis.analyze();
        assertEquals(3, droolsAnalysis.getSampleSolutionOutput().size());
        assertEquals(3, droolsAnalysis.getWrongFactList().size());
        assertEquals(0, droolsAnalysis.getAdditionalFacts());
        assertEquals(3, droolsAnalysis.getStudentOutput().size());
        assertFalse(droolsAnalysis.getSampleSolutionOutput().containsAll(droolsAnalysis.getStudentOutput()));

        droolsGrading = new DroolsGrading(droolsAnalysis, 100);
        droolsGrading.grade();
        assertEquals(40, droolsGrading.getPoints());
    }



    private final String CORRECT_RULES = "package at.jku.dke.etutor.modules.drools.jit;\n" +
            "\n" +
            "            rule \"Combine parking intervals if reentry within 15 min\"\n" +
            "            when\n" +
            "                $enter1 : EnterParkingLotEvent()\n" +
            "                $exit : ExitParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after $enter1)\n" +
            "                $enter2 : EnterParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after[0s, 15m] $exit)\n" +
            "            then\n" +
            "                delete($exit);\n" +
            "                delete($enter2);\n" +
            "            end\n" +
            "\n" +
            "            rule \"Do not combine parking intervals if reentry after 15 min\"\n" +
            "            when\n" +
            "                $enter1 : EnterParkingLotEvent()\n" +
            "                $exit : ExitParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after $enter1)\n" +
            "                $enter2 : EnterParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after[15m1s] $exit)\n" +
            "            then\n" +
            "                delete($enter1);\n" +
            "                delete($exit);\n" +
            "            end\n" +
            "\n" +
            "            rule \"Issue invoice\"\n" +
            "            when\n" +
            "                $enter : EnterParkingLotEvent()\n" +
            "                $exit : ExitParkingLotEvent(vehicle.licensePlate() == $enter.vehicle().licensePlate(), this after[2h1m] $enter)\n" +
            "            then\n" +
            "                insert(new Invoice($exit.timestamp(), $enter.vehicle(), (int)Math.ceil(($exit.diffInMinutes($enter) - 120) / 60.0) * 3));\n" +
            "            end";

    private final String RULES_MORE_INVOICES = "package at.jku.dke.etutor.modules.drools.jit;\n" +
            "\n" +
            "            rule \"Combine parking intervals if reentry within 15 min\"\n" +
            "            when\n" +
            "                $enter1 : EnterParkingLotEvent()\n" +
            "                $exit : ExitParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after $enter1)\n" +
            "                $enter2 : EnterParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after[0s, 5m] $exit)\n" +
            "            then\n" +
            "                delete($exit);\n" +
            "                delete($enter2);\n" +
            "            end\n" +
            "\n" +
            "            rule \"Do not combine parking intervals if reentry after 15 min\"\n" +
            "            when\n" +
            "                $enter1 : EnterParkingLotEvent()\n" +
            "                $exit : ExitParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after $enter1)\n" +
            "                $enter2 : EnterParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after[15m1s] $exit)\n" +
            "            then\n" +
            "                delete($enter1);\n" +
            "                delete($exit);\n" +
            "            end\n" +
            "\n" +
            "            rule \"Issue invoice\"\n" +
            "            when\n" +
            "                $enter : EnterParkingLotEvent()\n" +
            "                $exit : ExitParkingLotEvent(vehicle.licensePlate() == $enter.vehicle().licensePlate(), this after[2h1m] $enter)\n" +
            "            then\n" +
            "                insert(new Invoice($exit.timestamp(), $enter.vehicle(), (int)Math.ceil(($exit.diffInMinutes($enter) - 120) / 60.0) * 3));\n" +
            "            end";

    private final String RULES_LESS_INVOICES = "package at.jku.dke.etutor.modules.drools.jit;\n" +
            "\n" +
            "            rule \"Combine parking intervals if reentry within 15 min\"\n" +
            "            when\n" +
            "                $enter1 : EnterParkingLotEvent()\n" +
            "                $exit : ExitParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after $enter1)\n" +
            "                $enter2 : EnterParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after[0s, 15m] $exit)\n" +
            "            then\n" +
            "                delete($exit);\n" +
            "                delete($enter2);\n" +
            "            end\n" +
            "\n" +
            "            rule \"Do not combine parking intervals if reentry after 15 min\"\n" +
            "            when\n" +
            "                $enter1 : EnterParkingLotEvent()\n" +
            "                $exit : ExitParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after $enter1)\n" +
            "                $enter2 : EnterParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after[15m1s] $exit)\n" +
            "            then\n" +
            "                delete($enter1);\n" +
            "                delete($exit);\n" +
            "            end\n" +
            "\n" +
            "            rule \"Issue invoice\"\n" +
            "            when\n" +
            "                $enter : EnterParkingLotEvent()\n" +
            "                $exit : ExitParkingLotEvent(vehicle.licensePlate() == $enter.vehicle().licensePlate(), this after[3h1m] $enter)\n" +
            "            then\n" +
            "                insert(new Invoice($exit.timestamp(), $enter.vehicle(), (int)Math.ceil(($exit.diffInMinutes($enter) - 120) / 60.0) * 3));\n" +
            "            end";

    private final String RULES_WRONG_PRICE = "package at.jku.dke.etutor.modules.drools.jit;\n" +
            "\n" +
            "            rule \"Combine parking intervals if reentry within 15 min\"\n" +
            "            when\n" +
            "                $enter1 : EnterParkingLotEvent()\n" +
            "                $exit : ExitParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after $enter1)\n" +
            "                $enter2 : EnterParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after[0s, 15m] $exit)\n" +
            "            then\n" +
            "                delete($exit);\n" +
            "                delete($enter2);\n" +
            "            end\n" +
            "\n" +
            "            rule \"Do not combine parking intervals if reentry after 15 min\"\n" +
            "            when\n" +
            "                $enter1 : EnterParkingLotEvent()\n" +
            "                $exit : ExitParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after $enter1)\n" +
            "                $enter2 : EnterParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after[15m1s] $exit)\n" +
            "            then\n" +
            "                delete($enter1);\n" +
            "                delete($exit);\n" +
            "            end\n" +
            "\n" +
            "            rule \"Issue invoice\"\n" +
            "            when\n" +
            "                $enter : EnterParkingLotEvent()\n" +
            "                $exit : ExitParkingLotEvent(vehicle.licensePlate() == $enter.vehicle().licensePlate(), this after[2h1m] $enter)\n" +
            "            then\n" +
            "                insert(new Invoice($exit.timestamp(), $enter.vehicle(), (int)Math.ceil(($exit.diffInMinutes($enter) - 120) / 60.0) * 4));\n" +
            "            end";
}