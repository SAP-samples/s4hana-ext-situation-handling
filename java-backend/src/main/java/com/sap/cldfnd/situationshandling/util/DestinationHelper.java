package com.sap.cldfnd.situationshandling.util;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonMap;

import java.io.IOException;
import java.util.Collection;

import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpClientAccessor;

import feign.Request;
import feign.Request.HttpMethod;
import feign.Request.Options;
import feign.Response;
import feign.httpclient.ApacheHttpClient;

public class DestinationHelper {

	/**
	 * @return the URL of the destination with {@code destinationName}
	 */
	public static String getUrl(String destinationName) {
		return DestinationAccessor
				.getDestination(destinationName)
				.getUri()
				.toString();
	}

	/**
	 * @return an HTTP client preconfigured for the destination
	 */
	public static ApacheHttpClient getHttpClient(final String destinationName) {
		return new ApacheHttpClient(HttpClientAccessor.getHttpClient(destinationName));
	}

	/**
	 * Fetches X-CSRF-Token making a {@code GET} call with
	 * {@code X-CSRF-Token: Fetch} header to the {@code xCsrfTokenServicePath}
	 * of SAP Business Technology Platform (BTP) {@code destination}
	 * 
	 * @return X-CSRF-Token fetched from the {@code destination} using a service
	 *         located on {@code xCsrfTokenServicePath}
	 */
	public static String fetchXCsrfToken(String destination, String xCsrfTokenServicePath) {
		final String tokenHeader = "X-CSRF-Token";
		try {
			final Request csrfRequest = Request.create(
					HttpMethod.GET, 
					/* url */ getUrl(destination) + xCsrfTokenServicePath, 
					/* headers */ singletonMap(tokenHeader, singleton("Fetch")), 
					/* body */ null, 
					/* charset */ null);
			
			final Response response = getHttpClient(destination)
					.execute(csrfRequest, new Options());
			
			final Collection<String> tokens = response.headers().get(tokenHeader);
			if (tokens == null || tokens.isEmpty()) {
				throw new IOException("No X-CSRF-Token present in the server's response");
			}
			
			return tokens.iterator().next();
		} catch (IOException e) {
			throw new RuntimeException("Error while fetching X-CSRF-Token", e);
		}
	}

}
