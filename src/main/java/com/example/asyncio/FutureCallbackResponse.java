package com.example.asyncio;

import java.net.ConnectException;
import java.util.concurrent.CompletableFuture;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

/**
 * author ganesh.jayaraman
 * Mar 29, 2020
 */
public class FutureCallbackResponse implements FutureCallback<HttpResponse> {

	private CompletableFuture<HttpResponse> callbackResponse;

	public FutureCallbackResponse(CompletableFuture<HttpResponse> callbackResponse) {
		this.callbackResponse = callbackResponse;
	}

	@Override
	public void completed(HttpResponse result) {
		callbackResponse.complete(result);
	}

	@Override
	public void failed(Exception ex) {
		callbackResponse.completeExceptionally(new ConnectException("Failed to connect the device"));
	}

	@Override
	public void cancelled() {
		callbackResponse.cancel(true);
	}

	
}
