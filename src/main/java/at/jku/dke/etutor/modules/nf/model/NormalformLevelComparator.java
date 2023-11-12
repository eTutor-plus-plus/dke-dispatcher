package at.jku.dke.etutor.modules.nf.model;

import java.io.Serializable;
import java.util.Comparator;

public class NormalformLevelComparator implements Comparator, Serializable{

	public NormalformLevelComparator() {
		super();
	}

	public int compare(Object o1, Object o2) {
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
		
		if (!(o1 instanceof NormalformLevel)) {
			return 0;
		} 
		
		if (!(o2 instanceof NormalformLevel)) {
			return 0;
		}

		nl1 = ((NormalformLevel)o1).hashCode();
		nl2 = ((NormalformLevel)o2).hashCode();

		if (nl1 > nl2){
			return 1;			
		}
		
		if (nl1 < nl2){
			return -1;
		}

		return 0;
	}
}
