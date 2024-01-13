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

		return o1.compareTo(o2);

	}

	public static void main(String[] args) {
		/*NormalformLevelComparator comparator = new NormalformLevelComparator();

		System.out.println(comparator.compare(NormalformLevel.FIRST, NormalformLevel.FIRST) + "");
		System.out.println(comparator.compare(NormalformLevel.FIRST, NormalformLevel.SECOND) + "");
		System.out.println(comparator.compare(NormalformLevel.SECOND, NormalformLevel.FIRST) + "");
		System.out.println(comparator.compare(NormalformLevel.FIRST, null) + "");
		System.out.println(comparator.compare(null, NormalformLevel.FIRST) + "");*/
	}
}
