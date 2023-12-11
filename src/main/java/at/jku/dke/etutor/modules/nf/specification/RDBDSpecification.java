package at.jku.dke.etutor.modules.nf.specification;

import java.io.Serializable;

public interface RDBDSpecification extends Serializable {

	boolean semanticallyEquals(Object obj);

}
