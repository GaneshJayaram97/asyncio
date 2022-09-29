package com.example.asyncio;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * author ganesh.jayaraman
 * Mar 29, 2020
 */
public class HttpRequestCall extends MockHttpClient {

	private static final Logger log = LoggerFactory.getLogger(HttpRequestCall.class);
	public static final ExecutorService httpExecutorService = Executors.newSingleThreadExecutor();

	public static CompletableFuture<HttpResponse> doNetworkCall(HttpUriRequest uriRequest, int sleepTime) {
		// Try to do asyncio with the given url.
		CompletableFuture<HttpResponse> httpRequestCallback = new CompletableFuture();
		FutureCallbackResponse httpResponseCallback = new FutureCallbackResponse(httpRequestCallback);

		// Running under separate thread as Http Thread
		CompletableFuture<Boolean> httpCall = CompletableFuture.supplyAsync(() -> {
			// Call the asyncIO Request
			asyncClient.execute(uriRequest, httpResponseCallback);
			// Mock the response. But set the response with delay simulating the Server response time
			Mockito.when(asyncClient.execute(Mockito.any(), Mockito.any())).then(invocationOnMock -> {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException ex) {
					log.error("Exception : {}", ex);
				}
				httpResponseCallback.completed(HttpUtils.getResponse());
				return null;
			});
			return true;
		}, httpExecutorService);
		return httpRequestCallback;
	}
}
