package at.jku.dke.etutor.modules.nf.algorithms;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.Collection;

public class Cover {

	private Cover() {
		// This class is not meant to be instantiated (Gerald Wimmer, 2024-01-01).
	}

	/**
	 * Tests whether two collections of functional dependencies cover each other.
	 * @param fd1 a collection of <code>FunctionalDependency</code> objects
	 * @param fd2 a collection of <code>FunctionalDependency</code> objects
	 * @return Whether, for two collections of <code>FunctionalDependency</code> objects, each
	 * <code>FunctionalDependency</code> in either collection would still apply based on the other collection of
	 * functional dependencies. (Gerald Wimmer, 2023-12-31)
	 */
	public static boolean execute(Collection<FunctionalDependency> fd1, Collection<FunctionalDependency> fd2) {
		return fd1.stream().allMatch(fd -> Member.execute(fd, fd2)) &&
				fd2.stream().allMatch(fd -> Member.execute(fd, fd1));
	}
}