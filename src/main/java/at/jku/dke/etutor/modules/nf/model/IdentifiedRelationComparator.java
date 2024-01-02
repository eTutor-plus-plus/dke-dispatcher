package at.jku.dke.etutor.modules.nf.model;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;
import java.util.TreeSet;

public class IdentifiedRelationComparator  implements Comparator<IdentifiedRelation>, Serializable{

	public IdentifiedRelationComparator() {
		super();
	}

	public int compare(IdentifiedRelation o1, IdentifiedRelation o2) {
		if ((o1 == null) && (o2 == null)){
			return 0;
		}

		if (o1 == null){
			return 1;
		}

		if (o2 == null){
			return -1;
		}

        String id1 = o1.getID(); //.split("\\."); // Leftover from when the IDs needed to have a particular format (R*.*) (Gerald Wimmer, 2024-01-02).
		String id2 = o2.getID(); //.split("\\."); // Leftover from when the IDs needed to have a particular format (R*.*) (Gerald Wimmer, 2024-01-02).

        /*int maxComparisons = Math.min(id1.length, id2.length);
		
		for (int i=0; i<maxComparisons; i++) { // Leftover from when the IDs needed to have a particular format (R*.*) (Gerald Wimmer, 2024-01-02).
			int valueID1 = Integer.parseInt(id1[i]);
			int valueID2 = Integer.parseInt(id2[i]);

			if (valueID1 > valueID2){
				return 1;
			}
			
			if (valueID1 < valueID2){
				return -1;
			}
		}*/

		return Collator.getInstance().compare(id1, id2);

	}
	
	public static void main(String[] args){
		TreeSet<IdentifiedRelation> s = new TreeSet<>(new IdentifiedRelationComparator());
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

        for (IdentifiedRelation identifiedRelation : s) {
            relation = identifiedRelation;
            System.out.println("Relation: " + relation.getID() + " Ident: " + relation.getID().split("\\.").length);
        }
	}
}
