package com.client.bridge.model.generic;

public class BaseResultDTO {
	
	private String responseCode;
	private String responseDescription;
	
	public BaseResultDTO() {
		super();
		this.responseCode = "0";
		this.responseDescription = "";
	}

	public BaseResultDTO(String responseCode, String responseDescription) {
		super();
		this.responseCode = responseCode;
		this.responseDescription = responseDescription;
	}
	
//	public BaseResultDTO(String responseCode, String responseDescription) {
//		super();
//		this.responseCode = responseCode;
//		this.responseDescription = responseDescription;
//	}
	
	public BaseResultDTO(BaseResultDTO result) {
		super();
		this.responseCode = result.getResponseCode();
		this.responseDescription = result.getResponseDescription();
	}
	
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String respondeCode) {
		this.responseCode = respondeCode;
	}
	public String getResponseDescription() {
		return responseDescription;
	}
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}
	
}
