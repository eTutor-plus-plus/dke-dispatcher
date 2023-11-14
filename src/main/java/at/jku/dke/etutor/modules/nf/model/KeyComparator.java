package at.jku.dke.etutor.modules.nf.model;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;

public class KeyComparator implements Comparator<Key>, Serializable{

	public KeyComparator() {
		super();
	}

	public int compare(Key o1, Key o2) {
		Key k1;
		Key k2;
		Collator ac;

		if ((o1 == null) && (o2 == null)) {
			return 0;
		}

		if ((o1 == null) && (o2 != null)) {
			return 1;
		}

		if ((o1 != null) && (o2 == null)) {
			return -1;
		}

        k1 = o1;
		k2 = o2;
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
