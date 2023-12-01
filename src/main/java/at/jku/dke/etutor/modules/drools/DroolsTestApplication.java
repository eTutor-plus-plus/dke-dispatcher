package at.jku.dke.etutor.modules.drools;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.drools.analysis.DroolsAnalysis;

import java.io.IOException;

public class DroolsTestApplication {
    public static void main(String[] args) throws IOException {

        DroolsAnalysis analysis = new DroolsAnalysis(1,"");

        System.out.println(analysis.toString());
    }
}
