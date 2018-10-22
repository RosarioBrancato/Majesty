package ch.fhnw.projectbois.communication;

public class Request {

	private String token = null;
	private RequestId id = null;

	private String jsonDataObject = null;

	public Request(String token, RequestId id, String jsonDataObject) {
		this.token = token;
		this.id = id;
		this.jsonDataObject = jsonDataObject;
	}
	
	public String getToken() {
		return this.token;
	}
	
	public RequestId getRequestId() {
		return this.id;
	}
	
	public String getJsonDataObject() {
		return this.jsonDataObject;
	}

}
