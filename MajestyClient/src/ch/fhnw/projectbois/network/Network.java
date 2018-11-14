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
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.log.LoggerFactory;
import javafx.beans.property.SimpleObjectProperty;

public class Network {

	private static Network instance = null;
	
	private Logger logger = null;

	private Socket socket = null;
	private SimpleObjectProperty<Response> response = null;

	private Network() {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.response = new SimpleObjectProperty<>();
	}

	public static Network getInstance() {
		if (instance == null) {
			instance = new Network();
		}
		return instance;
	}

	public SimpleObjectProperty<Response> getResponse() {
		return this.response;
	}

	public void sendRequest(Request request) {
		try {
			String json = JsonUtils.Serialize(request);
			this.logger.info("Network.sendRequest() - JSON: " + json);

			OutputStream stream = this.socket.getOutputStream();
			PrintWriter writer = new PrintWriter(stream);

			writer.print(json);
			writer.flush();

		} catch (Exception ex) {
		}
	}

	public void sendTest() {
		try {
			Request request = new Request("TEST-TOKEN", RequestId.DO_MOVE, "{}");

			String json = JsonUtils.Serialize(request);
			this.logger.info("Network.sendTest() - JSON: " + json);

			OutputStream stream = this.socket.getOutputStream();
			PrintWriter writer = new PrintWriter(stream);

			writer.println(json);
			writer.flush();

		} catch (Exception ex) {
		}
	}

	public void initConnection(String host, int port) {
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
								logger.info("Network.Runnable() JSON: " + json);
								
								response.setValue(JsonUtils.Deserialize(json, Response.class));
								
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

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Network.initConnection()", e);
		}
	}

	public void stopConnection() {
		if (this.socket != null) {
			try {
				this.socket.close();
			} catch (IOException e) {
			}

			this.socket = null;
		}
	}

}
