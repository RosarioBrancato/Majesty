package ch.fhnw.projectbois.translate;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import ch.fhnw.projectbois.log.LoggerFactory;

/**
 * The Class Translator.
 * @author Dario Stöckli
 */
public class Translator {

	protected Logger logger;
	private static Translator translator;
	private Locale locale;
	private ResourceBundle bundle;

	/**
	 * Gets the translator as singleton
	 *
	 * @return the translator
	 */
	public static Translator getTranslator() {
		if (translator == null) {
			translator = new Translator();
		}
		return translator;
	}

	/**
	 * Instantiates a new translator.
	 */
	public Translator() {
		this.logger = LoggerFactory.getLogger(this.getClass());
		// Set locale to English in the beginning
		this.locale = new Locale("en");
		this.bundle = ResourceBundle.getBundle("language.UIResources", locale);
	}

	/**
	 * Gets the translation.
	 *
	 * @param translation key that can be mapped to property file
	 * @return the translation
	 */
	public String getTranslation(String translation) {
		try {
			return getResourceBundle().getString(translation);
		} catch (MissingResourceException e) {
			logger.warning("Missing string: " + translation);
			return "--";
		}
	}
	
	/**
	 * Gets the translation with arguments
	 *
	 * @param translation key that can be mapped to property file
	 * @param arguments the arguments
	 * @return the translation
	 */
	public String getTranslation(String translation, Object... arguments) {
		String message = this.getTranslation(translation);
		return MessageFormat.format(message, arguments);
	}

	/**
	 * Gets the locale.
	 *
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Sets the locale.
	 *
	 * @param locale the new locale
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * Gets the resource bundle.
	 *
	 * @return the resource bundle
	 */
	public ResourceBundle getResourceBundle() {
		return bundle;
	}

	/**
	 * Sets the resource bundle from the Login Controller DropDown
	 *
	 * @param locale the new resource bundle
	 */
	public void setResourceBundle(Locale locale) {
		this.bundle = ResourceBundle.getBundle("language.UIResources", locale);
		this.locale = locale;
	}

}
