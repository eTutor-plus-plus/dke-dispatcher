package at.jku.dke.etutor;

import at.jku.dke.etutor.grading.ETutorGradingApplication;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelationComparator;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.report.ErrorReport;
import at.jku.dke.etutor.modules.nf.report.ErrorReportGroup;
import at.jku.dke.etutor.modules.nf.report.NFReport;
import at.jku.dke.etutor.modules.nf.ui.HTMLPrinter;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Locale;
import java.util.TreeSet;

/**
 * Currently, this class mainly exists for the sake of separating test cases from the old RDBD module class files from
 * actual application code, without deleting the potentially useful tests outright.
 */
@SpringBootTest(classes = ETutorGradingApplication.class)
@EnabledIfSystemProperty(named = "run_test", matches="true")
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class TestNFModule {

    // Methods originally in HTMLPrinter

    public static void testDecompose(){
        //CREATE RELATION 1
        IdentifiedRelation relation = new IdentifiedRelation();
        try{
            relation.setID("1");
        } catch (Exception e){
            e.printStackTrace();
        }
        relation.setName("R1");
        relation.addAttribute("A");
        relation.addAttribute("B");
        relation.addAttribute("C");
        relation.addAttribute("D");

        FunctionalDependency dependency = new FunctionalDependency();
        dependency.addLHSAttribute("A");
        dependency.addRHSAttribute("B");
        dependency.addRHSAttribute("C");
        relation.addFunctionalDependency(dependency);

        Key key = new Key();
        key.addAttribute("A");
        key.addAttribute("D");
        relation.addMinimalKey(key);

        TreeSet<IdentifiedRelation> relations = new TreeSet<>(new IdentifiedRelationComparator());
        relations.add(relation);

        //CREATE RELATION 2
        relation = new IdentifiedRelation();
        try{
            relation.setID("2");
        } catch (Exception e){
            e.printStackTrace();
        }
        relation.setName("R2");
        relation.addAttribute("A");
        relation.addAttribute("B");
        relation.addAttribute("C");
        relation.addAttribute("D");

        dependency = new FunctionalDependency();
        dependency.addLHSAttribute("A");
        dependency.addRHSAttribute("B");
        dependency.addRHSAttribute("C");
        relation.addFunctionalDependency(dependency);

        key = new Key();
        key.addAttribute("A");
        key.addAttribute("D");
        relation.addMinimalKey(key);

        relations.add(relation);

        //CREATE RELATION 1.1
        relation = new IdentifiedRelation();
        try{
            relation.setID("1.1");
        } catch (Exception e){
            e.printStackTrace();
        }
        relation.setName("R1.1");
        relation.addAttribute("A");
        relation.addAttribute("B");
        relation.addAttribute("C");
        relation.addAttribute("D");

        dependency = new FunctionalDependency();
        dependency.addLHSAttribute("A");
        dependency.addRHSAttribute("B");
        dependency.addRHSAttribute("C");
        relation.addFunctionalDependency(dependency);

        key = new Key();
        key.addAttribute("A");
        key.addAttribute("D");
        relation.addMinimalKey(key);

        relations.add(relation);

        //CREATE RELATION 1.2
        relation = new IdentifiedRelation();
        try{
            relation.setID("1.2");
        } catch (Exception e){
            e.printStackTrace();
        }
        relation.setName("R1.2");
        relation.addAttribute("A");
        relation.addAttribute("B");
        relation.addAttribute("C");
        relation.addAttribute("D");

        dependency = new FunctionalDependency();
        dependency.addLHSAttribute("A");
        dependency.addRHSAttribute("B");
        dependency.addRHSAttribute("C");
        relation.addFunctionalDependency(dependency);

        key = new Key();
        key.addAttribute("A");
        key.addAttribute("D");
        relation.addMinimalKey(key);

        relations.add(relation);

        //CREATE RELATION 1.1.1
        relation = new IdentifiedRelation();
        try{
            relation.setID("1.1.1");
        } catch (Exception e){
            e.printStackTrace();
        }
        relation.setName("R1.1.1");
        relation.addAttribute("A");
        relation.addAttribute("B");
        relation.addAttribute("C");
        relation.addAttribute("D");

        dependency = new FunctionalDependency();
        dependency.addLHSAttribute("A");
        dependency.addRHSAttribute("B");
        dependency.addRHSAttribute("C");
        relation.addFunctionalDependency(dependency);

        key = new Key();
        key.addAttribute("A");
        key.addAttribute("D");
        relation.addMinimalKey(key);

        relations.add(relation);

        //CREATE RELATION 1.1.2
        relation = new IdentifiedRelation();
        try{
            relation.setID("1.1.2");
        } catch (Exception e){
            e.printStackTrace();
        }
        relation.setName("R1.1.2");
        relation.addAttribute("A");
        relation.addAttribute("B");
        relation.addAttribute("C");
        relation.addAttribute("D");

        dependency = new FunctionalDependency();
        dependency.addLHSAttribute("A");
        dependency.addRHSAttribute("B");
        dependency.addRHSAttribute("C");
        relation.addFunctionalDependency(dependency);

        key = new Key();
        key.addAttribute("A");
        key.addAttribute("D");
        relation.addMinimalKey(key);

        relations.add(relation);


        HTMLPrinter.createHTMLSiteForDecompose(relations, Locale.ENGLISH);
    }

    public static void testReport(){
        NFReport report = new NFReport();
        report.setPrologue("Congratulations! Everything is correct.");
        report.setShowPrologue(true);

        //ERROR REPORT 1
        ErrorReport er1 = new ErrorReport();
        er1.setHint("Hint 1");
        er1.setError("Error 1");
        er1.setDescription("Description 1");

        er1.setShowHint(true);
        er1.setShowError(true);
        er1.setShowErrorDescription(true);
        report.addErrorReport(er1);

        //ERROR REPORT 2
        ErrorReport er2 = new ErrorReport();
        er2.setHint("Hint 2");
        er2.setError("Error 2");
        er2.setDescription("Description 2");

        er2.setShowHint(true);
        er2.setShowError(true);
        er2.setShowErrorDescription(true);
        report.addErrorReport(er2);

        //ERROR REPORT GROUP 1
        ErrorReportGroup erg1 = new ErrorReportGroup();
        erg1.setHeader("Header for ERG 1");

        ErrorReport er1_1 = new ErrorReport();
        er1_1.setHint("Hint 1.1");
        er1_1.setError("Error 1.1");
        er1_1.setDescription("Description 1.1");

        er1_1.setShowHint(true);
        er1_1.setShowError(true);
        er1_1.setShowErrorDescription(true);
        erg1.addErrorReport(er1_1);

        ErrorReport er1_2 = new ErrorReport();
        er1_2.setHint("Hint 1.2");
        er1_2.setError("Error 1.2");
        er1_2.setDescription("Description 1.2");

        er1_2.setShowHint(true);
        er1_2.setShowError(true);
        er1_2.setShowErrorDescription(true);
        erg1.addErrorReport(er1_2);

        //ERROR REPORT GROUP 1.1
        ErrorReportGroup erg1_1 = new ErrorReportGroup();
        erg1_1.setHeader("Header for ERG 1.1");

        ErrorReport er1_1_1 = new ErrorReport();
        er1_1_1.setHint("Hint 1.1.1");
        er1_1_1.setError("Error 1.1.1");
        er1_1_1.setDescription("Description 1.1.1");

        er1_1_1.setShowHint(true);
        er1_1_1.setShowError(true);
        er1_1_1.setShowErrorDescription(true);
        erg1_1.addErrorReport(er1_1_1);
        erg1.addSubErrorReportGroup(erg1_1);

        report.addErrorReportGroup(erg1);



        //ERROR REPORT GROUP 2
        ErrorReportGroup erg2 = new ErrorReportGroup();
        erg2.setHeader("Header for ERG 2");

        ErrorReport er2_1 = new ErrorReport();
        er2_1.setHint("Hint 2.1");
        er2_1.setError("Error 2.1");
        er2_1.setDescription("Description 2.1");

        er2_1.setShowHint(true);
        er2_1.setShowError(true);
        er2_1.setShowErrorDescription(true);
        erg2.addErrorReport(er2_1);

        ErrorReport er2_2 = new ErrorReport();
        er2_2.setHint("Hint 2.2");
        er2_2.setError("Error 2.2");
        er2_2.setDescription("Description 2.2");

        er2_2.setShowHint(true);
        er2_2.setShowError(true);
        er2_2.setShowErrorDescription(true);
        erg2.addErrorReport(er2_2);

        report.addErrorReportGroup(erg2);



        HTMLPrinter.createHTMLSiteForReport(report);
    }
}
