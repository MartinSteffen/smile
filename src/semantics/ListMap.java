package semantics;

import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;


public class ListMap<K, V> {

	private final HashMap<K, List<V>> map;

	public ListMap() {
		this.map = new HashMap<K, List<V>>();
	}

	public List<V> get(K key) {
		List<V> value = this.map.get(key);
		if (value == null) {
			value = new LinkedList<V>();
			this.map.put(key, value);
		}
		return value;
	}

	public void addTo(K key, V elem) {
		final List<V> l = get(key);
		l.add(elem);
	}
}
