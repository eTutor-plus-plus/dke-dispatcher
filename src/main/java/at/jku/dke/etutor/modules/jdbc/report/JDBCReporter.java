package at.jku.dke.etutor.modules.jdbc.report;

import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.core.evaluation.Report;
import at.jku.dke.etutor.modules.jdbc.analysis.JDBCAnalysis;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class JDBCReporter {
    public static Report report(JDBCAnalysis analysis, DefaultGrading grading, JDBCReporterConfig config, MessageSource messageSource, Locale locale) {
        return null;
    }
}
