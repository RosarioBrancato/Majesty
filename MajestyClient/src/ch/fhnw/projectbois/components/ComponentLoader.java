package ch.fhnw.projectbois.components;

import java.net.URL;

public class ComponentLoader {

	public static URL getResource(Component component) {
		return ComponentLoader.class.getResource(component.name() + ".fxml");
	}

}
