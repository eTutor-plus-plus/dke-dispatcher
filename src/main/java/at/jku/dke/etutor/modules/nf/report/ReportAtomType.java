package at.jku.dke.etutor.modules.nf.report;

import java.io.Serializable;

public class ReportAtomType implements Serializable {

	private String name;

	public static ReportAtomType INFO = new ReportAtomType("Info");
	public static ReportAtomType ERROR = new ReportAtomType("Error");
	public static ReportAtomType WARNING = new ReportAtomType("Warning");

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
			return this == null;
		}

		if (o instanceof ReportAtomType){
			return this.name == ((ReportAtomType)o).getName();
		} else {
			return false;
		}
	}
}
