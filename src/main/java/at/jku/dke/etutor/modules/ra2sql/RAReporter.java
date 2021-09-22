package at.jku.dke.etutor.modules.ra2sql;

import java.sql.ResultSetMetaData;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.modules.sql.SQLEvaluationAction;
import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;
import at.jku.dke.etutor.modules.sql.analysis.*;
import at.jku.dke.etutor.modules.sql.report.SQLErrorReport;
import at.jku.dke.etutor.modules.sql.report.SQLReport;
import at.jku.dke.etutor.modules.sql.report.SQLReporter;
import at.jku.dke.etutor.modules.sql.report.SQLReporterConfig;
import org.springframework.context.MessageSource;


public class RAReporter {
	private SQLReporter sqlReporter;

	public RAReporter(SQLReporter reporter) {
		this.sqlReporter = reporter;
	}

	public SQLReport createReport(SQLAnalysis analysis, DefaultGrading grading, SQLReporterConfig config, Locale locale) {
		return sqlReporter.createReport(analysis, grading, config, locale);
	}

	/**
	 * Returns wheter the locale equals Locale.GERMAN
	 *
	 * @param locale the locale
	 * @return a boolean
	 */
	private boolean isGermanLocale(Locale locale) {
		return locale == Locale.GERMAN;
	}

}
