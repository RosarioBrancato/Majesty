package ch.fhnw.projectbois.communication;

public class Response {
	
	/**
	 * Response to request
	 */
	private RequestId request;
	private String jsonDataObject = null;
	
	
	public Response() {
	}
	
	public Response(RequestId request, String jsonDataObject) {
		this.request = request;
		this.jsonDataObject = jsonDataObject;
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
