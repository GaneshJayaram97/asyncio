package com.example.asyncio;

/**
 * author ganesh.jayaraman
 * Mar 29, 2020
 */
public class HttpCallResponse {

	private Integer status;
	private String content;

	public HttpCallResponse(Integer status, String content) {
		this.status = status;
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
