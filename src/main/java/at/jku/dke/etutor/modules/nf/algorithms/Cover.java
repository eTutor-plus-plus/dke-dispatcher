package at.jku.dke.etutor.modules.nf.algorithms;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.Collection;

public class Cover {

	/**
	 * Tests whether two collections of functional dependencies cover each other.
	 * @param fd1 a collection of <code>FunctionalDependency</code> objects
	 * @param fd2 a collection of <code>FunctionalDependency</code> objects
	 * @return whether, for two collections of <code>FunctionalDependency</code> objects, each
	 * <code>FunctionalDependency</code> in either collection would still apply based on the other collection of
	 * functional dependencies. (Gerald Wimmer, 2023-12-31)
	 */
	public static boolean execute(Collection<FunctionalDependency> fd1, Collection<FunctionalDependency> fd2) {
		for (FunctionalDependency fd : fd1) {
			if (!Member.execute(fd, fd2)) {
				return false;
			}
		}

		for (FunctionalDependency fd : fd2) {
			if (!Member.execute(fd, fd1)) {
				return false;
			}
		}

		return true;
	}
}