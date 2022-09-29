package com.example.asyncio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import com.example.domain.Device;
import com.example.domain.DeviceImageOperationTaskResult;

/**
 * author ganesh.jayaraman
 * Mar 29, 2020
 */
public class DeviceUpgradeTest extends MockHttpClient {

	public static String DEVICEIP_PREFIX = "1.1.1";
	public static Map<String, Device> deviceIpToDevice = new HashMap<>();
	public static List<Device> devices = new ArrayList<>();

	private static Logger log = LoggerFactory.getLogger(DeviceUpgradeTest.class);
	public static ExecutorService upgradeTaskExecutorService = Executors.newSingleThreadExecutor();
	public static Options options;

	public static void initializeDevices(int num) {
		for (int i=0; i<num;i++) {
			String deviceIp = DEVICEIP_PREFIX + "." + String.valueOf(i);
			Device device = new Device(deviceIp);
			deviceIpToDevice.put(deviceIp, device);
			devices.add(device);
		}
	}

	public static void main(String[] args) {
		int numberOfDevices = CommandLineOptionUtils.getNumberOfDevices(args);
		log.info("Number of devices : {}", numberOfDevices);
		initializeDevices(numberOfDevices);

		// Run the following task in a separate thread from that of Main Thread
		CompletableFuture<CompletableFuture<Boolean>> upgradeTask = CompletableFuture.supplyAsync(() -> {
			log.info("Starting the upgrade tasks on the thread : " + Thread.currentThread().getName());
			List<CompletableFuture<DeviceImageOperationTaskResult>> deviceImageFetchedTasks = new ArrayList();
			for (Device device : devices) {
				// Do image Fetch for the devices
				deviceImageFetchedTasks.add(CompletableFuture.completedFuture(null).thenCompose((res) -> {
					DeviceImageFetch imageFetch = new DeviceImageFetch(device.getDeviceIp());
					log.info("Image Fetch Operation running under thread {}", Thread.currentThread().getName());
					return imageFetch.callTask(device);
				}));
			}

			// Do Image Install Task
			List<CompletableFuture<DeviceImageOperationTaskResult>> deviceImageInstalledTasks = deviceImageFetchedTasks.stream().map(res -> DeviceImageUpgradeUtils.doNextOp(res, upgradeTaskExecutorService)).collect(Collectors.toList());
			// Do Image Reboot Task
			List<CompletableFuture<DeviceImageOperationTaskResult>> deviceRebootedTasks = deviceImageInstalledTasks.stream().map(res -> DeviceImageUpgradeUtils.doNextOp(res, upgradeTaskExecutorService)).collect(Collectors.toList());

			CompletableFuture.allOf(deviceImageInstalledTasks.toArray(new CompletableFuture[deviceImageInstalledTasks.size()])).join();
			CompletableFuture.allOf(deviceRebootedTasks.toArray(new CompletableFuture[deviceRebootedTasks.size()])).join();
			return CompletableFuture.completedFuture(true);
		}, upgradeTaskExecutorService);

		CompletableFuture.allOf(upgradeTask.join());
	}
}
