package com.example.domain;

import com.example.asyncio.HttpCallResponse;

/**
 * author ganesh.jayaraman
 * Mar 29, 2020
 */
public class DeviceImageOperationTaskResult {

	private Device device;
	private Integer stepCount;
	private HttpCallResponse response;
	private Throwable exception;

	public DeviceImageOperationTaskResult(Device device, Integer stepCount) {
		this.device = device;
		this.stepCount = stepCount;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Integer getStepCount() {
		return stepCount;
	}

	public void setStepCount(Integer stepCount) {
		this.stepCount = stepCount;
	}

	public HttpCallResponse getResponse() {
		return response;
	}

	public void setResponse(HttpCallResponse response) {
		this.response = response;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}
}
