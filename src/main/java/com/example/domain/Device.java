package com.example.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.util.ReflectionUtils;

/**
 * author ganesh.jayaraman
 * Mar 09, 2020
 */

public class Device {
	private String deviceIp;
	private String hostname;

	public Device(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
