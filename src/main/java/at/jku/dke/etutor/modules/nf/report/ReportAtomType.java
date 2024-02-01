package at.jku.dke.etutor.modules.nf.report;

public enum ReportAtomType {
	INFO("Info"),
	ERROR("Error"),
	WARNING("Warning");

	ReportAtomType(String reportAtomTypeName) {
		this.name = reportAtomTypeName;
	}

	private final String name;

	public String toString(){
		return this.name;
	}

	public String getName(){
		return this.name;
	}
}
