package com.example.asyncio;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.domain.Device;
import com.example.domain.DeviceImageOperationTaskResult;

/**
 * author ganesh.jayaraman
 * Mar 29, 2020
 */
public class DeviceImageUpgradeUtils {

	private static Logger log = LoggerFactory.getLogger(DeviceImageUpgradeUtils.class);

	public static DeviceImageOperations getNextOp(Device device, int stepCount) {
		switch (stepCount) {
		case 1:
			return new DeviceImageFetch(device.getDeviceIp());
		case 2:
			return new DeviceImageInstall(device.getDeviceIp());
		case 3:
			return new DeviceImageReboot(device.getDeviceIp());
		default :
			return null;
		}
	}

	private static String getNextOpDescription(int stepCount) {
		switch (stepCount) {
		case 1:
			return "Device Image Fetch";
		case 2:
			return "Device Image Install";
		case 3:
			return "Device Reboot";
		default :
			return "Unsupported Operation";
		}
	}

	public static CompletableFuture<DeviceImageOperationTaskResult> doNextOp(CompletableFuture<DeviceImageOperationTaskResult> previousStep, ExecutorService upgradeTaskExecutorService) {
		CompletableFuture<DeviceImageOperationTaskResult> nextOpRes = CompletableFuture.completedFuture(null);
		nextOpRes = previousStep.thenCompose(res -> {
			Integer stepCount = res.getStepCount();
			stepCount++;
			Device device = res.getDevice();
			log.info("Going to perform Next Operation [{}] for the device : {}", getNextOpDescription(stepCount), device);
			DeviceImageOperations nextOp = DeviceImageUpgradeUtils.getNextOp(device, stepCount);
			return nextOp.callTask(device);
		});
		return nextOpRes;
	}
}
