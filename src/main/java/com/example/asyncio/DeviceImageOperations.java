package com.example.asyncio;

import java.util.concurrent.CompletableFuture;

import com.example.domain.Device;
import com.example.domain.DeviceImageOperationTaskResult;

/**
 * author ganesh.jayaraman
 * Mar 29, 2020
 */
public interface DeviceImageOperations {

	CompletableFuture<DeviceImageOperationTaskResult> callTask(Device device);
}
