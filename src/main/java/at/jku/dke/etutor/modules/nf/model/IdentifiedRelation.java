package at.jku.dke.etutor.modules.nf.model;

import at.jku.dke.etutor.modules.nf.RDBDSpecification;
import at.jku.dke.etutor.modules.nf.ui.MalformedRelationIDException;

import java.io.Serializable;
import java.util.Collection;

public class IdentifiedRelation extends Relation implements Serializable, Cloneable, RDBDSpecification {

	private static final long serialVersionUID = -8880131949030576416L;
	private String id;

	public IdentifiedRelation() {
		super();
		this.id = "";
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
	}

	public IdentifiedRelation(Collection<String> attributes, Collection<FunctionalDependency> dependencies) {
		super(attributes, dependencies);
	}
	
	public void setID(String id) throws MalformedRelationIDException {
		if (id != null){
			if (id.matches("(?idmsux)\\d*(\\.\\d*)*")) { // Todo: Make sure this doesn't cause problems with the "R*[.*]" scheme used now. (Gerald Wimmer, 2023-11-30)
				this.id = id;
			} else {
				throw new MalformedRelationIDException("ID '" + id + "' is malformed!");
			}
		} else {
			throw new MalformedRelationIDException("ID '" + id + "' is malformed!");
		}
	}

	public String getID(){
		return this.id;
	}

	@Override
	public String toString(){
		String toString = "";
		
		toString = toString.concat("Relation: " + this.getName() + "(" + this.getID() + ")\n");

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
