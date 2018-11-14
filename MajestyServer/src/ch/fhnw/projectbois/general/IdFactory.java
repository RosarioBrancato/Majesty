package ch.fhnw.projectbois.general;

import java.util.HashMap;

public class IdFactory {

	private static IdFactory instance = null;
	
	public HashMap<String, Integer> classIds = null;
	
	private IdFactory() {
		this.classIds = new HashMap<String, Integer>();
	}
	
	public static IdFactory getInstance() {
		if(instance == null) {
			instance = new IdFactory();
		}
		return instance;
	}
	
	public int getNewId(String className) {
		int id = 1;
		
		if(!this.classIds.containsKey(className)) {
			this.classIds.put(className, id);
		}
		
		id = this.classIds.get(className).intValue();
		this.classIds.put(className, new Integer(id + 1));
		
		return id;
	}
	
}
