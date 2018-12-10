package ch.fhnw.projectbois._application;

import java.util.prefs.Preferences;

public class UserPrefs {
	private static Preferences prefs;
	
	private UserPrefs() {}
	
	public static Preferences getInstance() {
		if(prefs == null) {
			prefs = Preferences.userRoot().node("ch.fhnw.projectbois.majesty.preferences");
		}
		return prefs;
	}
}
