package at.jku.dke.etutor.modules.nf.ui;

import at.jku.dke.etutor.modules.nf.model.Relation;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class IdentifiedRelationComparator  implements Comparator<IdentifiedRelation>, Serializable{

	public IdentifiedRelationComparator() {
		super();
	}

	public int compare(IdentifiedRelation o1, IdentifiedRelation o2) {

		String[] id1;
		String[] id2;
		
		int valueID1;
		int valueID2;
		int maxComparisons;

		if ((o1 == null) && (o2 == null)){
			return 0;
		}

		if ((o1 == null) && (o2 != null)){
			return 1;
		}

		if ((o1 != null) && (o2 == null)){
			return -1;
		}

        id1 = o1.getID().split("\\.");
		id2 = o2.getID().split("\\.");

        maxComparisons = Math.min(id1.length, id2.length);
		
		for (int i=0; i<maxComparisons; i++){
			valueID1 = Integer.parseInt(id1[i]);
			valueID2 = Integer.parseInt(id2[i]);

			if (valueID1 > valueID2){
				return 1;
			}
			
			if (valueID1 < valueID2){
				return -1;
			}
		}
		
		if (id1.length > id2.length){
			return 1;
		}

		if (id1.length < id2.length){
			return -1;
		}
		
		return 0;
	}
	
	public static void main(String[] args){
		TreeSet<IdentifiedRelation> s = new TreeSet<IdentifiedRelation>(new IdentifiedRelationComparator());
		IdentifiedRelation relation;
		
		try{
			relation = new IdentifiedRelation();
			relation.setID("1*");
			System.out.println("Adding Relation: '" + relation.getID() + "'.");
			s.add(relation);

			relation = new IdentifiedRelation();
			relation.setID("2");
			System.out.println("Adding Relation: '" + relation.getID() + "'.");
			s.add(relation);

			relation = new IdentifiedRelation();
			relation.setID("1.1");
			System.out.println("Adding Relation: '" + relation.getID() + "'.");
			s.add(relation);
		
			relation = new IdentifiedRelation();
			relation.setID("1.2");
			System.out.println("Adding Relation: '" + relation.getID() + "'.");
			s.add(relation);

			relation = new IdentifiedRelation();
			relation.setID("1.1.1");
			System.out.println("Adding Relation: '" + relation.getID() + "'.");
			s.add(relation);
		} catch (Exception e){
			e.printStackTrace();
			return;
		}
		
		Iterator<IdentifiedRelation> i = s.iterator();
		while (i.hasNext()){
			relation = i.next();
			System.out.println("Relation: " + relation.getID() + " Ident: " + relation.getID().split("\\.").length);
		}
	}
}
