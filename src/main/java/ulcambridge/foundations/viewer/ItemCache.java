package ulcambridge.foundations.viewer;

import java.util.LinkedHashMap;
import java.util.Map;

import ulcambridge.foundations.viewer.model.Item;

/** 
 * This is a very simple cache that stores the last CAPACITY items 
 * requested and allows quick access to them. 
 * 
 * @author jennie
 *
 */
public class ItemCache extends LinkedHashMap<String, Item> {

	private static final long serialVersionUID = 1447337912579613915L;
	private int CAPACITY;

	public ItemCache(int capacity) {
		super(capacity + 1, 1.1f, true);
		this.CAPACITY = capacity;
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry eldest) {
		return size() > CAPACITY;
		
	}
}
