package at.jku.dke.etutor.modules.nf.ui;

import at.jku.dke.etutor.modules.nf.RDBDSpecification;
import at.jku.dke.etutor.modules.nf.model.Relation;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class IdentifiedRelation extends Relation implements Serializable, Cloneable, RDBDSpecification {

	static final long serialVersionUID = -8880131949030576416L;
	private String id;

	public IdentifiedRelation() {
		super();
		this.id = new String();
	}

	public Object clone() throws CloneNotSupportedException {
		IdentifiedRelation clone = (IdentifiedRelation)super.clone();
		return clone;
	}

	public IdentifiedRelation(Collection attributes, Collection dependencies) {
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
		Iterator iter;
		String toString = new String();
		
		toString = toString.concat("Relation: " + this.getName() + "(" + this.getID() + ")\n");

		toString = toString.concat("Attributes: \n");
		iter = this.iterAttributes();
		while (iter.hasNext()){
			toString = toString.concat(iter.next().toString() + "; ");
		}
		toString = toString.concat("\n");

		toString = toString.concat("Dependencies: \n");
		iter = this.iterFunctionalDependencies();
		while (iter.hasNext()){
			toString = toString.concat(iter.next().toString() + "; ");
		}
		toString = toString.concat("\n");

		toString = toString.concat("Keys: \n");
		iter = this.iterMinimalKeys();
		while (iter.hasNext()){
			toString = toString.concat(iter.next().toString() + "; ");
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
	public static void main(String[] args) {
		new Vector().contains(null);
	}
}
