package com.example.asyncio;

import java.nio.charset.CodingErrorAction;
import java.util.Arrays;

import org.apache.http.Consts;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author ganesh.jayaraman
 * Mar 29, 2020
 */
@Configuration
public class HttpAsyncClientConfig {

	@Bean
	public static HttpAsyncClient initializeAsynIO() throws Exception {

		try {
			ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();

			PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);

			ConnectionConfig connectionConfig = ConnectionConfig.custom()
					.setMalformedInputAction(CodingErrorAction.IGNORE)
					.setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8).build();
			connManager.setDefaultConnectionConfig(connectionConfig);
			connManager.setMaxTotal(100);
			connManager.setDefaultMaxPerRoute(20);

			RequestConfig defaultRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.DEFAULT)
					.setExpectContinueEnabled(true)
					.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
					.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();

			CloseableHttpAsyncClient asyncHttpClient = HttpAsyncClients.custom().setConnectionManager(connManager)
					.setDefaultRequestConfig(defaultRequestConfig).build();

			asyncHttpClient.start();

			return asyncHttpClient;
		} catch (Exception ex) {
			throw new Exception(String.format("Failed to create AsyncHttpClient: %s", ex.getMessage()), ex);
		}

	}

}
