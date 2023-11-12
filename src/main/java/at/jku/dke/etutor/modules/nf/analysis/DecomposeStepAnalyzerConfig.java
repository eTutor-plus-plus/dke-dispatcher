package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.modules.nf.ui.IdentifiedRelation;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class DecomposeStepAnalyzerConfig extends NormalizationAnalyzerConfig {

	private Vector allRelations;

	public DecomposeStepAnalyzerConfig() {
		super();
		this.allRelations = new Vector();
	}

	public void setAllRelations(Collection allRelations){
		this.allRelations.clear();
		this.allRelations.addAll(allRelations);
	}
	
	public void addRelationToAll(IdentifiedRelation relation){
		this.allRelations.add(relation);
	}

	public Iterator iterAllRelations(){
		return this.allRelations.iterator();
	}
	
	public Vector getAllRelations(){
		return (Vector)this.allRelations.clone();
	}
}
