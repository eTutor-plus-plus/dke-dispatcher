package at.jku.dke.etutor.modules.nf.model;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;


public class AttributeCollator implements Serializable, Comparator {

	public int compare(Object o1, Object o2) {
		return Collator.getInstance().compare(o1, o2);
	}

}
