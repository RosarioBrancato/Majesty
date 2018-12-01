package ch.fhnw.projectbois.utils;

import java.util.Map;
import java.util.Objects;

/**
 * 
 * @author leeko
 *
 */
public class MapUtils {
	
	public static <T, E> T getKeysByValue(Map<T, E> map, E value) {
	    return map.entrySet()
	              .stream()
	              .filter(entry -> Objects.equals(entry.getValue(), value))
	              .map(Map.Entry::getKey)
	              .findFirst()
	              .get();
	}
	
}
