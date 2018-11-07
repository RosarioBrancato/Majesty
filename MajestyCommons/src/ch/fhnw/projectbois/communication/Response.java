package ch.fhnw.projectbois.communication;

public class Response {

	private ResponseId response;
	private RequestId request;
	private String jsonDataObject = null;

	public Response() {
	}

	public Response(ResponseId response, RequestId request, String jsonDataObject) {
		this.response = response;
		this.request = request;
		this.jsonDataObject = jsonDataObject;
	}

	public ResponseId getResponse() {
		return response;
	}

	public void setResponse(ResponseId response) {
		this.response = response;
	}

	public RequestId getRequest() {
		return request;
	}

	public void setRequest(RequestId request) {
		this.request = request;
	}

	public String getJsonDataObject() {
		return jsonDataObject;
	}

	public void setJsonDataObject(String jsonDataObject) {
		this.jsonDataObject = jsonDataObject;
	}

	@Override
	public String toString() {
		return "Request: " + this.request.getName() + " JSON " + this.jsonDataObject;
	}

}
