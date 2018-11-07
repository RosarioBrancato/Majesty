package ch.fhnw.projectbois.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	public static <T> String Serialize(T state) {
		String json = null;

		try {
			ObjectMapper mapper = new ObjectMapper();

			json = mapper.writeValueAsString(state);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
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
				e.printStackTrace();
			}
		}

		return state;
	}

}
