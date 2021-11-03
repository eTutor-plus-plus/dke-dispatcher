package at.jku.dke.etutor.modules.ra2sql;


import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.modules.sql.analysis.SQLAnalysis;
import at.jku.dke.etutor.modules.sql.report.SQLReport;
import at.jku.dke.etutor.modules.sql.report.SQLReporter;
import at.jku.dke.etutor.modules.sql.report.SQLReporterConfig;

import java.util.Locale;


public class RAReporter {
	private SQLReporter sqlReporter;

	public RAReporter(SQLReporter reporter) {
		this.sqlReporter = reporter;
	}

	public SQLReport createReport(SQLAnalysis analysis, DefaultGrading grading, SQLReporterConfig config, Locale locale) {
		return sqlReporter.createReport(analysis, grading, config, locale);
	}
}
