package ch.fhnw.projectbois.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A factory for creating Logger objects.
 *
 * @author Rosario Brancato
 */
public class LoggerFactory {

	/**
	 * Gets the logger.
	 *
	 * @param <T> the generic type
	 * @param classType the class type
	 * @return the logger
	 */
	public static <T> Logger getLogger(Class<T> classType) {
		String className = classType.getName();
		Logger logger = Logger.getLogger(className);

		Handler[] handlers = logger.getHandlers();
		if (handlers.length == 1) {
			handlers[0].setLevel(Level.SEVERE);

			Handler fileHandler;
			try {
				fileHandler = new FileHandler("%t/" + className + "_%u" + "_%g" + ".log", 1000000, 9);
				fileHandler.setLevel(Level.SEVERE);

				logger.addHandler(fileHandler);

			} catch (SecurityException | IOException e) {
				e.printStackTrace();
			}
		}

		return logger;
	}

}
