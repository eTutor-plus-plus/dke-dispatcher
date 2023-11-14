package at.jku.dke.etutor.modules.nf.model;

import java.io.Serializable;
import java.util.Comparator;

public class NormalformLevelComparator implements Comparator<NormalformLevel>, Serializable{

	public NormalformLevelComparator() {
		super();
	}

	public int compare(NormalformLevel o1, NormalformLevel o2) {
		int nl1;
		int nl2;

		if ((o1 == null) && (o2 == null)) {
			return 0;

		} 
		
		if ((o1 == null) && (o2 != null)) {
			return 1;
		} 
		
		if ((o1 != null) && (o2 == null)) {
			return -1;
		}

        nl1 = o1.hashCode();
		nl2 = o2.hashCode();

		if (nl1 > nl2){
			return 1;			
		}
		
		if (nl1 < nl2){
			return -1;
		}

		return 0;
	}
}
