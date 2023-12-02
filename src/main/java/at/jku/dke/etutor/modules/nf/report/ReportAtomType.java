package at.jku.dke.etutor.modules.nf.report;

import java.io.Serializable;

public class ReportAtomType implements Serializable {

	private final String name;

	public static final ReportAtomType INFO = new ReportAtomType("Info");
	public static final ReportAtomType ERROR = new ReportAtomType("Error");
	public static final ReportAtomType WARNING = new ReportAtomType("Warning");

	protected ReportAtomType(String reportAtomTypeName) {
		super();
		this.name = reportAtomTypeName;
	}

	public String toString(){
		return this.name;
	}

	public String getName(){
		return this.name;
	}

	public boolean equals(Object o){
		if (o == null){
			return false;
		}

		if (o instanceof ReportAtomType){
			return this.name == ((ReportAtomType)o).getName();
		} else {
			return false;
		}
	}
}
