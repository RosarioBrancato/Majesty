/*
 * 
 * @author Alexandre Miccoli
 * 
 */
package ch.fhnw.projectbois._application;

import java.util.prefs.Preferences;

public class UserPrefs {
	private static Preferences prefs;
	
	/**
	 * Instantiates a new user prefs.
	 */
	private UserPrefs() {}
	
	/**
	 * Gets the single instance of UserPrefs.
	 *
	 * @return single instance of UserPrefs
	 */
	public static Preferences getInstance() {
		if(prefs == null) {
			prefs = Preferences.userRoot().node("ch.fhnw.projectbois.majesty.preferences");
		}
		return prefs;
	}
}
