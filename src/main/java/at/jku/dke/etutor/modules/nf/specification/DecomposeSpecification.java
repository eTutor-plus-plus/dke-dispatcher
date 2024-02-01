package at.jku.dke.etutor.modules.nf.specification;

import java.io.Serial;

public class DecomposeSpecification extends NormalizationSpecification {

	@Serial
	private static final long serialVersionUID = 1411810798506094612L;

	public DecomposeSpecification() {
		super();
	}

	public boolean semanticallyEquals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (!(obj instanceof DecomposeSpecification)) {
			return false;
		}

        return super.semanticallyEquals(obj);
    }
}
