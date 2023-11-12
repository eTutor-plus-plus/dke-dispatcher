package at.jku.dke.etutor.modules.nf.exercises;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class TupleSet extends TreeSet implements Set {

	public TupleSet() {
		super(new TupleComparator());
	}
	
	public int[] get(int pos){
		return (int[])this.toArray()[pos];
	}

	public boolean add(int[] tuple){
		return super.add(tuple);
	}
	
	public int size() {
		return super.size();
	}

	public boolean isEmpty() {
		return super.isEmpty();
	}

	public boolean contains(Object o) {
		return super.contains(o);
	}

	public Iterator iterator() {
		return super.iterator();
	}

	public Object[] toArray() {
		return super.toArray();
	}

	public Object[] toArray(Object[] a) {
		return super.toArray(a);
	}

	public boolean add(Object o) {
		return super.add(o);
	}

	public boolean remove(Object o) {
		return super.remove(o);
	}

	public boolean containsAll(Collection c) {
		return super.containsAll(c);
	}

	public boolean addAll(Collection c) {
		return super.addAll(c);
	}

	public boolean retainAll(Collection c) {
		return super.retainAll(c);
	}

	public boolean removeAll(Collection c) {
		return super.removeAll(c);
	}

	public void clear() {
		super.clear();
	}
	
	public static void main(String[] args){
		TupleSet set = new TupleSet();
		
		int[] t1 = new int[]{1,2};		
		int[] t2 = new int[]{1,2};		

		System.out.println(set.add(t1));
		System.out.println(set.contains(t2));
	}
}