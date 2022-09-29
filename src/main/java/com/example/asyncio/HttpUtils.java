package com.example.asyncio;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * author ganesh.jayaraman
 * Mar 29, 2020
 */
public class HttpUtils {

	public static HttpCallResponse getHttpCallResponse(HttpResponse response) {
		HttpCallResponse httpCallResponse = null;
		try {
			httpCallResponse = new HttpCallResponse(response.getStatusLine().getStatusCode(),
					EntityUtils.toString(response.getEntity()));
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return httpCallResponse;
	}

	public static HttpResponse getResponse() {
		BasicHttpResponse response = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 200, "'{\"status\": \"Operation Success\"}'"));
		HttpEntity entity = new StringEntity("'{\"status\": \"Operation Success\"}'", HTTP.UTF_8);
		response.setEntity(entity);
		return response;
	}
}
