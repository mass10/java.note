package jp.mass10.java.ldap.openldap.sample1;

import java.util.Collection;
import java.util.TreeSet;

@SuppressWarnings("serial")
final class OrderedStringSet extends TreeSet<String> {

	private int _capacity;

	public OrderedStringSet(int capacity) {

		this._capacity = capacity;
	}

	@Override
	public boolean add(String e) {

		if (e == null)
			e = "";

		final int size = this.size();
		super.add(e);
		resizeTo(this._capacity);
		return size != this.size();
	}

	@Override
	public boolean addAll(Collection<? extends String> c) {

		final int size = this.size();
		for (final String e : c) {
			this.add(e);
		}
		return size != this.size();
	}

	private final void resizeTo(int capacity) {

		while (capacity < this.size()) {
			this.pollFirst();
		}
	}

	public static void main(String[] args) {

		final OrderedStringSet set = new OrderedStringSet(3);
		final String[] SOURCE = new String[] { "1", "Japan", "Deutshland", "South Africa", "Quarter", "Q", "a" };
		for (final String s : SOURCE) {
			set.add(s);
			System.out.println(set);
		}
	}
}
