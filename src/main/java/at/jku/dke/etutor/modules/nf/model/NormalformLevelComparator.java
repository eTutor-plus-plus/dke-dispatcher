package at.jku.dke.etutor.modules.nf.model;

import java.io.Serializable;
import java.util.Comparator;

public class NormalformLevelComparator implements Comparator<NormalformLevel>, Serializable{

	public NormalformLevelComparator() {
		super();
	}

	public int compare(NormalformLevel o1, NormalformLevel o2) {
		if ((o1 == null) && (o2 == null)) {
			return 0;
		} 
		
		if (o1 == null) {
			return 1;
		} 
		
		if (o2 == null) {
			return -1;
		}

		return Integer.compare(o1.hashCode(), o2.hashCode());

	}
}
