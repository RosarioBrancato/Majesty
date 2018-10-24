package ch.fhnw.projectbois.communication;

public class Request {

	private String token = null;
	private RequestId requestId = null;

	private String jsonDataObject = null;

	/**
	 * For JSON parser
	 */
	public Request() {
	}

	public Request(String token, RequestId requestId, String jsonDataObject) {
		this.token = token;
		this.requestId = requestId;
		this.jsonDataObject = jsonDataObject;
	}

	public String getToken() {
		return this.token;
	}

	public RequestId getRequestId() {
		return this.requestId;
	}

	public String getJsonDataObject() {
		return this.jsonDataObject;
	}

	@Override
	public String toString() {
		return "Token: " + this.token + " RequestId: " + this.requestId.name();
	}

}
