package at.jku.dke.etutor.modules.nf.algorithms;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.Collection;
import java.util.Iterator;

public class Cover {

	public static boolean execute(Collection fd1, Collection fd2){
		Iterator dependenciesIterator;
 
		dependenciesIterator = fd1.iterator();
		while (dependenciesIterator.hasNext()) {
			if (!Member.execute((FunctionalDependency)dependenciesIterator.next(), fd2)) {
				return false;
			}
		}

		dependenciesIterator = fd2.iterator();
		while (dependenciesIterator.hasNext()) {
			if (!Member.execute((FunctionalDependency)dependenciesIterator.next(), fd1)) {
				return false;
			}
		}

		return true;
	}
}