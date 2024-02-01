package at.jku.dke.etutor.modules.nf.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.util.Collection;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class IdentifiedRelation extends Relation {

	@Serial
	private static final long serialVersionUID = -8880131949030576416L;
	private String id;

	public IdentifiedRelation() {
		super();
		this.id = "R1";
	}

	public IdentifiedRelation(Collection<String> attributes, Collection<FunctionalDependency> dependencies) {
		super(attributes, dependencies);
	}
	
	public void setID(String id) {
		this.id = id;
	}

	public String getID(){
		return this.id;
	}

	@Override
	public String toString(){
		String toString = "";
		
		toString = toString.concat("Relation: " + this.name + "(" + this.id + ")\n");

		toString = toString.concat("Attributes: \n");
		for (String a : getAttributes()) {
			toString = toString.concat(a + "; ");
		}
		toString = toString.concat("\n");

		toString = toString.concat("Dependencies: \n");
		for (FunctionalDependency fd : getFunctionalDependencies()) {
			toString = toString.concat(fd + "; ");
		}
		toString = toString.concat("\n");

		toString = toString.concat("Keys: \n");
		for (Key k : getMinimalKeys()) {
			toString = toString.concat(k + "; ");
		}
		toString = toString.concat("\n");
		
		return toString;
	}

	@Override
	public boolean semanticallyEquals(Object obj){
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof IdentifiedRelation)) {
			return false;
		}

		return super.semanticallyEquals(obj);
	}
}
