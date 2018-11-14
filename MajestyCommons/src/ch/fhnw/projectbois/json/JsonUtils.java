package ch.fhnw.projectbois.json;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.fhnw.projectbois.log.LoggerFactory;

public class JsonUtils {

	private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

	public static <T> String Serialize(T state) {
		String json = null;

		try {
			ObjectMapper mapper = new ObjectMapper();
			json = mapper.writeValueAsString(state);

		} catch (JsonProcessingException e) {
			logger.log(Level.SEVERE, "LoggerUtils.Serialize()", e);
		}

		return json;
	}

	public static <T> T Deserialize(String json, Class<T> valueType) {
		T state = null;

		if (json != null) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				state = (T) mapper.readValue(json, valueType);

			} catch (IOException e) {
				logger.log(Level.SEVERE, "LoggerUtils.Deserialize()", e);
			}
		}

		return state;
	}

}
