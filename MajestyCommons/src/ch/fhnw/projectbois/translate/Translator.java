package ch.fhnw.projectbois.translate;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import ch.fhnw.projectbois.log.LoggerFactory;

public class Translator {

	protected Logger logger;
	private static Translator translator;

	// Set locale to English in the beginning
	private Locale locale;
	private ResourceBundle bundle;

	public static Translator getTranslator() {
		if (translator == null) {
			translator = new Translator();
		}
		return translator;
	}

	public Translator() {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.locale = new Locale("en");
		this.bundle = ResourceBundle.getBundle("language.UIResources", locale);
	}

	public String getTranslation(String translation) {
		try {
			return getResourceBundle().getString(translation);
		} catch (MissingResourceException e) {
			logger.warning("Missing string: " + translation);
			return "--";
		}
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public ResourceBundle getResourceBundle() {
		return bundle;
	}

	public void setResourceBundle(Locale locale) {
		this.bundle = ResourceBundle.getBundle("language.UIResources", locale);
		this.locale = locale;
	}

}
