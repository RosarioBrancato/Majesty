package ch.fhnw.projectbois.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Rosario Brancato
 *
 */
public class LoggerFactory {

	public static <T> Logger getLogger(Class<T> classType) {
		String className = classType.getName();
		Logger logger = Logger.getLogger(className);

		Handler[] handlers = logger.getHandlers();
		if (handlers.length == 1) {
			handlers[0].setLevel(Level.FINE);

			Handler fileHandler;
			try {
				fileHandler = new FileHandler("%t/" + className + "_%u" + "_%g" + ".log", 1000000, 9);
				fileHandler.setLevel(Level.WARNING);

				logger.addHandler(fileHandler);

			} catch (SecurityException | IOException e) {
				e.printStackTrace();
			}
		}

		return logger;
	}

}
