package ch.fhnw.projectbois.communication;

public class Request {

	private String token = null;
	private int requestId = RequestId.EMPTY;

	private String jsonDataObject = null;

	/**
	 * For JSON parser
	 */
	public Request() {
	}

	public Request(String token, int requestId, String jsonDataObject) {
		this.token = token;
		this.requestId = requestId;
		this.jsonDataObject = jsonDataObject;
	}

	public String getToken() {
		return this.token;
	}

	public int getRequestId() {
		return this.requestId;
	}

	public String getJsonDataObject() {
		return this.jsonDataObject;
	}

	@Override
	public String toString() {
		return "Token: " + this.token + " RequestId: " + this.requestId;
	}

}
