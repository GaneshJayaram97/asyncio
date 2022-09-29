package com.example.asyncio;

import org.apache.http.nio.client.HttpAsyncClient;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 * author ganesh.jayaraman
 * Mar 29, 2020
 */
public class MockHttpClient {

	@Mock
	public static HttpAsyncClient asyncClient =  Mockito.mock(HttpAsyncClient.class);

}
