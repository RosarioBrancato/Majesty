package ch.fhnw.projectbois.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.log.LoggerFactory;
import javafx.beans.property.SimpleObjectProperty;

public class Network {

	private static Network instance = null;

	private Logger logger = null;

	private Socket socket = null;
	private SimpleObjectProperty<Response> responseProperty = null;

	private Network() {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.responseProperty = new SimpleObjectProperty<>();
	}

	public static Network getInstance() {
		if (instance == null) {
			instance = new Network();
		}
		return instance;
	}

	public synchronized void sendRequest(Request request) {
		try {
			String json = JsonUtils.Serialize(request);
			this.logger.info("Network.sendRequest() - " + json);

			OutputStream stream = this.socket.getOutputStream();
			PrintWriter writer = new PrintWriter(stream);

			writer.println(json);
			writer.flush();

		} catch (Exception ex) {
		}
	}

	public boolean initConnection(String host, int port) {
		boolean success = false;
		
		try {
			this.stopConnection();

			socket = new Socket(host, port);

			Runnable r = new Runnable() {
				@Override
				public void run() {
					try {
						BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						String json;
						while ((json = reader.readLine()) != null && !socket.isClosed()) {
							try {
								logger.info("Network.Runnable() - " + json);

								Response response = JsonUtils.Deserialize(json, Response.class);
								responseProperty.setValue(response);

							} catch (Exception ex) {
								logger.log(Level.SEVERE, "Network.Runnable()", ex);
							}
						}
					} catch (Exception ex) {
					}
				}
			};

			Thread t = new Thread(r);
			t.start();
			
			success = true;

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Network.initConnection()", e);
		}
		
		return success;
	}

	public void stopConnection() {
		if (this.socket != null) {
			try {
				this.socket.close();
				logger.log(Level.INFO, "Socket to Server has been closed.");
			} catch (IOException e) {
			}

			this.socket = null;
		}
	}

	public SimpleObjectProperty<Response> getResponseProperty() {
		return this.responseProperty;
	}

}
