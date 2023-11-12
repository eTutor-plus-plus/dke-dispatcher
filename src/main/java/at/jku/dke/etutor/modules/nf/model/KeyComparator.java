package at.jku.dke.etutor.modules.nf.model;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;

public class KeyComparator implements Comparator, Serializable{

	public KeyComparator() {
		super();
	}

	public int compare(Object o1, Object o2) {
		Key k1;
		Key k2;
		Comparator ac;

		if ((o1 == null) && (o2 == null)) {
			return 0;
		}

		if ((o1 == null) && (o2 != null)) {
			return 1;
		}

		if ((o1 != null) && (o2 == null)) {
			return -1;
		}

		if (!(o1 instanceof Key)) {
			return 0;
		}

		if (!(o2 instanceof Key)) {
			return 0;
		}

		k1 = (Key)o1;
		k2 = (Key)o2;
		ac = Collator.getInstance();

		if (k1.getAttributes().size() > k2.getAttributes().size()) {
			return 1;
		}

		if (k1.getAttributes().size() < k2.getAttributes().size()) {
			return -1;
		}

		for (int i = 0; i < k1.getAttributes().size(); i++) {
			if (ac.compare(k1.getAttributes().toArray()[i], k2.getAttributes().toArray()[i]) != 0) {
				return ac.compare(k1.getAttributes().toArray()[i], k2.getAttributes().toArray()[i]);
			}
		}

		return 0;
	}
}
