package at.jku.dke.etutor.modules.nf.algorithms;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.Collection;

public class Cover {

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