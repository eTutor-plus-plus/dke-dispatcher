package at.jku.dke.etutor.modules.drools;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.drools.analysis.DroolsAnalysis;

import java.io.IOException;

public class DroolsTestApplication {
    public static void main(String[] args) throws IOException, ReflectiveOperationException {

        DroolsAnalysis analysis = new DroolsAnalysis(1,"");

        if(analysis.hasSyntaxError()) System.out.println("Regeln haben Syntaxfehler.");
        else System.out.println("Regeln haben keine Syntaxfehler.");

        analysis.hasSemantikError();


        System.out.println(analysis.toString());

    }
}
