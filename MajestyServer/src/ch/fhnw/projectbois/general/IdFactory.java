package ch.fhnw.projectbois.general;

import java.util.HashMap;

/**
 * A factory for creating Id objects.
 *
 * @author Rosario Brancato
 */
public class IdFactory {

	private static IdFactory instance = null;
	
	public HashMap<String, Integer> classIds = null;
	
	/**
	 * Instantiates a new id factory.
	 */
	private IdFactory() {
		this.classIds = new HashMap<String, Integer>();
	}
	
	/**
	 * Gets the single instance of IdFactory.
	 *
	 * @return single instance of IdFactory
	 */
	public static IdFactory getInstance() {
		if(instance == null) {
			instance = new IdFactory();
		}
		return instance;
	}
	
	/**
	 * Gets the new id.
	 *
	 * @param className the class name
	 * @return the new id
	 */
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
