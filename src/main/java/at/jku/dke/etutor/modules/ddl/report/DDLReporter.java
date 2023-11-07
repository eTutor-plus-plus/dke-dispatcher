package at.jku.dke.etutor.modules.ddl.report;

import at.jku.dke.etutor.modules.ddl.analysis.DDLAnalysis;

import java.util.Locale;

public class DDLReporter {
    public DDLReporter() {

    }

    public DDLReport createReport(DDLAnalysis analysis, DDLReporterConfig config, Locale locale) {
        DDLReport report = new DDLReport();

        return report;
    }

    /**
     * Returns wheter the locale equals Locale.GERMAN
     * @param locale Locale information
     * @return Returns a boolean
     */
    private boolean isGermanLocale(Locale locale){
        return locale == Locale.GERMAN;
    }

}
