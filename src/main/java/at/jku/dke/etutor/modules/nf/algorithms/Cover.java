package at.jku.dke.etutor.modules.nf.algorithms;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.Collection;
import java.util.Iterator;

public class Cover {

	public static boolean execute(Collection<FunctionalDependency> fd1, Collection<FunctionalDependency> fd2){
		Iterator<FunctionalDependency> dependenciesIterator = fd1.iterator();
		while (dependenciesIterator.hasNext()) {
			if (!Member.execute(dependenciesIterator.next(), fd2)) {
				return false;
			}
		}

		dependenciesIterator = fd2.iterator();
		while (dependenciesIterator.hasNext()) {
			if (!Member.execute(dependenciesIterator.next(), fd1)) {
				return false;
			}
		}

		return true;
	}
}