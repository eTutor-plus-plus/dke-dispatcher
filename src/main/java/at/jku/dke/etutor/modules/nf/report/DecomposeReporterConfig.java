package at.jku.dke.etutor.modules.nf.report;

import at.jku.dke.etutor.modules.nf.analysis.DecomposeAnalysis;
import etutor.core.evaluation.DefaultGrading;

public class DecomposeReporterConfig extends ReporterConfig{

	private DefaultGrading grading;
	private DecomposeAnalysis analysis;
	
	public DecomposeReporterConfig() {
		super();

		this.grading = null;
		this.analysis = null;
	}

	public DecomposeAnalysis getDecomposeAnalysis() {
		return this.analysis;
	}

	public DefaultGrading getGrading() {
		return this.grading;
	}

	public void setDecomposeAnalysis(DecomposeAnalysis analysis) {
		this.analysis = analysis;
	}

	public void setGrading(DefaultGrading grading) {
		this.grading = grading;
	}
}
