package at.jku.dke.etutor.modules.nf.ui;

import etutor.modules.rdbd.model.Relation;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class IdentifiedRelationComparator  implements Comparator, Serializable{

	public IdentifiedRelationComparator() {
		super();
	}

	public int compare(Object o1, Object o2) {

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

		if (!(o1 instanceof Relation)){
			return 0;
		}
		
		if (!(o2 instanceof Relation)){
			return 0;
		}
		
		id1 = ((IdentifiedRelation)o1).getID().split("\\.");
		id2 = ((IdentifiedRelation)o2).getID().split("\\.");
		
		if (id1.length <= id2.length){
			maxComparisons = id1.length;
		} else {
			maxComparisons = id2.length;
		}
		
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
		TreeSet s = new TreeSet(new IdentifiedRelationComparator());
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
		
		Iterator i = s.iterator();
		while (i.hasNext()){
			relation = (IdentifiedRelation)i.next();
			System.out.println("Relation: " + relation.getID() + " Ident: " + relation.getID().split("\\.").length);
		}
	}
}
