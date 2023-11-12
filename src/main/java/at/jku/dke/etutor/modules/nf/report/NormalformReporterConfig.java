package at.jku.dke.etutor.modules.nf.report;

import etutor.modules.rdbd.model.NormalformLevel;

public class NormalformReporterConfig extends ReporterConfig {

	private NormalformLevel desiredNormalformLevel;

	public NormalformReporterConfig() {
		super();
		this.desiredNormalformLevel = NormalformLevel.FIRST;
	}

	public NormalformLevel getDesiredNormalformLevel() {
		return this.desiredNormalformLevel;
	}

	public void setDesiredNormalformLevel(NormalformLevel level) {
		this.desiredNormalformLevel = level;
	}
}
