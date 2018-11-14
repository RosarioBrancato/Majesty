package ch.fhnw.projectbois.communication;

public class Response {

	private int responseId;
	private int requestId;
	private String jsonDataObject = null;

	public Response() {
	}

	public Response(int responseId, int requestId, String jsonDataObject) {
		this.responseId = responseId;
		this.requestId = requestId;
		this.jsonDataObject = jsonDataObject;
	}

	public int getResponseId() {
		return responseId;
	}

	public void setResponseId(int responseId) {
		this.responseId = responseId;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public String getJsonDataObject() {
		return jsonDataObject;
	}

	public void setJsonDataObject(String jsonDataObject) {
		this.jsonDataObject = jsonDataObject;
	}

	@Override
	public String toString() {
		return "Response: " + this.responseId + " Request: " + this.requestId + " JSON " + this.jsonDataObject;
	}

}
