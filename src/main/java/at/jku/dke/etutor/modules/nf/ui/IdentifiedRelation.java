package at.jku.dke.etutor.modules.nf.ui;

import at.jku.dke.etutor.modules.nf.RDBDSpecification;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.Relation;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class IdentifiedRelation extends Relation implements Serializable, Cloneable, RDBDSpecification {

	private static final long serialVersionUID = -8880131949030576416L;
	private String id;

	public IdentifiedRelation() {
		super();
		this.id = "";
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
        return (IdentifiedRelation)super.clone();
	}

	public IdentifiedRelation(Collection<String> attributes, Collection<FunctionalDependency> dependencies) {
		super(attributes, dependencies);
	}
	
	public void setID(String id) throws MalformedRelationIDException{
		if (id != null){
			if (id.matches("(?idmsux)\\d*(\\.\\d*)*")){
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
	
	public String toString(){
		String toString = "";
		
		toString = toString.concat("Relation: " + this.getName() + "(" + this.getID() + ")\n");

		toString = toString.concat("Attributes: \n");
		Iterator<String> attributesIter = this.iterAttributes();
		while (attributesIter.hasNext()){
			toString = toString.concat(attributesIter.next().toString() + "; ");
		}
		toString = toString.concat("\n");

		toString = toString.concat("Dependencies: \n");
		Iterator<FunctionalDependency> fdIter = this.iterFunctionalDependencies();
		while (fdIter.hasNext()){
			toString = toString.concat(fdIter.next().toString() + "; ");
		}
		toString = toString.concat("\n");

		toString = toString.concat("Keys: \n");
		Iterator<Key> minKeysiter = this.iterMinimalKeys();
		while (minKeysiter.hasNext()){
			toString = toString.concat(minKeysiter.next().toString() + "; ");
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
