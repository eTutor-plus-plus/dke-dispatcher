package at.jku.dke.etutor.modules.nf.model;

import java.io.Serializable;

public class NormalformLevel implements Serializable {

	private final String degree;
	private final int intRepresentation;

	public static final NormalformLevel FIRST = new NormalformLevel("FIRST", 1);
	public static final NormalformLevel SECOND = new NormalformLevel("SECOND", 2);
	public static final NormalformLevel THIRD = new NormalformLevel("THIRD", 3);
	public static final NormalformLevel BOYCE_CODD = new NormalformLevel("BOYCE_CODD", 4);

	protected NormalformLevel(String degree, int intRepresentation) {
		this.degree = degree;
		this.intRepresentation = intRepresentation;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (!(obj instanceof NormalformLevel)) {
			return false;
		}

		return this.degree.equals(obj.toString());
	}
	
	public int hashCode(){
		return this.intRepresentation;
	}
	
	public String toString() {
		return this.degree;
	}
}
