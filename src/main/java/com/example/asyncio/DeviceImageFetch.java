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
public class DeviceImageFetch implements DeviceImageOperations {

	private static final Logger log = LoggerFactory.getLogger(DeviceImageFetch.class);
	private static final String imageFetchUrl = "/api/v1/image/fetch";

	private String deviceIp;
	private Integer stepCount = 1;

	public DeviceImageFetch(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	@Override
	public CompletableFuture<DeviceImageOperationTaskResult> callTask(Device device) {
		HttpUriRequest uriRequest = new HttpPost("https://" + device.getDeviceIp() + imageFetchUrl);
		CompletableFuture<DeviceImageOperationTaskResult> imageFetchOp = CompletableFuture.completedFuture(null);
		DeviceImageOperationTaskResult taskResult = new DeviceImageOperationTaskResult(device, stepCount);
		// Run under the caller's thread
		imageFetchOp = imageFetchOp.thenCompose((res) -> {
			CompletableFuture<HttpResponse> asyncIOCF = HttpRequestCall.doNetworkCall(uriRequest, new Random().nextInt(1000));
			return asyncIOCF.thenCompose((op) -> {
				// Do the call back processing
				taskResult.setResponse(HttpUtils.getHttpCallResponse(op));
				log.info("Image fetch for the device : " + deviceIp + " Completed under the thread : " + Thread.currentThread().getName());
				return CompletableFuture.completedFuture(taskResult);
			}).exceptionally(ex -> {
				taskResult.setException(ex);
				log.info("Error when fetching the image : " + deviceIp + " under the thread : " + Thread.currentThread().getName() + " " + ex.getMessage());
				return taskResult;
			});
		}).exceptionally(ex -> {
			log.info("Error when fetching the image : " + deviceIp + " under the thread : " + Thread.currentThread().getName() + " " + ex.getMessage());
			return taskResult;
		});
		return imageFetchOp;
	}
}
