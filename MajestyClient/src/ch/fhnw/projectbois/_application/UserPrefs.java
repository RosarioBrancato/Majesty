/**
 * Handle User Preferences using the Java Preferences package
 * @author Alexandre Miccoli
 * 
 */
package ch.fhnw.projectbois._application;

import java.util.prefs.Preferences;

public class UserPrefs {
	private static Preferences prefs;
	
	/**
	 * Gets the single instance of User Preferences.
	 *
	 * @return single instance of UserPrefs
	 */
	public static Preferences getInstance() {
		if(prefs == null) {
			prefs = Preferences.userRoot().node("ch.fhnw.projectbois.majesty.preferences");
		}
		return prefs;
	}
	
	/**
	 * Instantiates user preferences.
	 */
	private UserPrefs() {}
}
