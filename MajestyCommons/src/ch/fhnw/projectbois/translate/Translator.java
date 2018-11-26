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
	private static Locale locale = new Locale("en");
	private static ResourceBundle bundle = ResourceBundle.getBundle("language.UIResources", locale);
	
    public static Translator getTranslator() {
        if (translator == null)
            translator = new Translator(bundle);
        return translator;
        
    }
    
    public Translator (ResourceBundle bundle) {
    	this.logger = LoggerFactory.getLogger(this.getClass());
    	Translator.bundle = bundle;
    }
    
    public String getTranslation(String translation) {
    	try {
    		return getResourceBundle().getString(translation);
    	} catch (MissingResourceException e){
    		logger.warning("Missing string: " + translation);
            return "--";
    	}
	}
	
	public Locale getLocale() {
		return Translator.locale;
	}
	
	public void setLocale(Locale locale) {
		Translator.locale = locale;
	}
	
	public ResourceBundle getResourceBundle() {
		return Translator.bundle;
	}
	
	public void setResourceBundle(Locale locale) {
		Translator.bundle = ResourceBundle.getBundle("language.UIResources", locale);
	}

}

