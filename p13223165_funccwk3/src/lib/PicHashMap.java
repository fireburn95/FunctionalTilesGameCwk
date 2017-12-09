package lib;

import java.util.HashMap;

public class PicHashMap {
	private HashMap<String, Picture> hashmap;
	
	public PicHashMap() {
		hashmap = new HashMap<>();
	}
	
	public PicHashMap(HashMap<String, Picture> h) {
		this.hashmap = new HashMap<>(h);
	}
	
	public Picture get(String s) {
		return new Picture(hashmap.get(s).toString());
	}
	
	public void put(String s, Picture p) {
		HashMap<String, Picture> temp = hashmap;
		temp.put(new String(s), new Picture(p.toString()));
		hashmap = new HashMap<String, Picture>(temp);
	}
	
	public int size() {
		return hashmap.size();
	}
	
	public void clear() {
		hashmap = new HashMap<>();
	}
	
	public boolean containsKey(String s) {
		return hashmap.containsKey(s);
	}
	
	public void remove(String s) {
		HashMap<String, Picture> temp = hashmap;
		temp.remove(new String(s));
		hashmap = new HashMap<String, Picture>(temp);
	}
}
