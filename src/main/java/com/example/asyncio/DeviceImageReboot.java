package com.example.asyncio;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.domain.Device;
import com.example.domain.DeviceImageOperationTaskResult;

/**
 * author ganesh.jayaraman
 * Mar 29, 2020
 */
public class DeviceImageReboot implements DeviceImageOperations {

	private static final Logger log = LoggerFactory.getLogger(DeviceImageReboot.class);
	private static final String imageRebootUrl = "/api/v1/system/reboot";

	private String deviceIp;
	private Integer stepCount = 3;

	public DeviceImageReboot(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	@Override
	public CompletableFuture<DeviceImageOperationTaskResult> callTask(Device device) {
		HttpUriRequest uriRequest = new HttpPost("https://" + device.getDeviceIp() + imageRebootUrl);
		CompletableFuture<DeviceImageOperationTaskResult> imageRebootOp = CompletableFuture.completedFuture(null);
		DeviceImageOperationTaskResult taskResult = new DeviceImageOperationTaskResult(device, stepCount);
		// Run under the caller's thread
		return imageRebootOp.thenCompose((res) -> {
			CompletableFuture<HttpResponse> asyncIOCF = HttpRequestCall.doNetworkCall(uriRequest, new Random().nextInt(5000));
			return asyncIOCF.thenCompose((op) -> {
				taskResult.setResponse(HttpUtils.getHttpCallResponse(op));
				// Do the call back processing
				log.info("Image Reboot for the device : " + device.getDeviceIp() + " completed under the thread : " + Thread.currentThread().getName());
				return CompletableFuture.completedFuture(taskResult);
			});
		}).exceptionally(ex -> {
			taskResult.setException(ex);
			log.info("Error when Rebooting the image : " + device.getDeviceIp() + " under the thread : " + Thread.currentThread().getName() + " " + ex.getMessage());
			return taskResult;
		});
		
	}
}
