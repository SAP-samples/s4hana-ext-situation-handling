package com.sap.cldfnd.situationshandling.s4hanaodata.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.olingo.odata2.api.batch.BatchException;
import org.apache.olingo.odata2.api.client.batch.BatchChangeSet;
import org.apache.olingo.odata2.api.client.batch.BatchChangeSetPart;
import org.apache.olingo.odata2.api.client.batch.BatchSingleResponse;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.sdk.cloudplatform.connectivity.HttpClientAccessor;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataExceptionType;
import com.sap.cloud.sdk.odatav2.connectivity.internal.ODataConnectivityUtil;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.PurchaseRequisitionService;

public class PurchaseRequisitionBatchService {
	
	private static final Logger log = LoggerFactory.getLogger(PurchaseRequisitionBatchService.class);
	
	public static final String DESTINATION_NAME = "ErpQueryEndpoint";

	public static final String PURCHASE_REQUISITION_ITEM_ENTITY_SET_NAME = "A_PurchaseRequisitionItem";
	public static final String PURCHASE_REQUISITION_HEADER_ENTITY_SET_NAME = "A_PurchaseRequisitionHeader";

	/* entity keys */	
	public static final String PURCHASE_REQUISITION = "PurchaseRequisition";
	public static final String PURCHASE_REQUISITION_ITEM = "PurchaseRequisitionItem";
	
	public void updatePurchaseRequisitionItem(String purchaseRequisition, 
			String purchaseRequisitionItem, 
			String prItem) throws ODataException {

		final HttpClient httpClient = HttpClientAccessor.getHttpClient(DESTINATION_NAME);
		
		String csrfToken = null;
		try {
			csrfToken = ODataConnectivityUtil
					.readMetadataWithCSRF(PurchaseRequisitionService.DEFAULT_SERVICE_PATH, httpClient, null, null, false, null)
					.getCsrfToken();
		} catch (IOException cause) {
			final ODataException e = new ODataException(ODataExceptionType.METADATA_FETCH_FAILED, 
					"Exception when getting X-CSRF-Token to call the OData service on S/4HANA", 
					cause);
			log.error(e.getMessage(), cause);
			throw e;
		}
		
		final BatchChangeSetPart patchRequest = BatchChangeSetPart
				.method("PATCH")
				.headers(Collections.singletonMap(HttpHeaders.CONTENT_TYPE, "application/json"))
				.uri(String.format("%s(%s='%s',%s='%s')",
						PURCHASE_REQUISITION_ITEM_ENTITY_SET_NAME,
						PURCHASE_REQUISITION, purchaseRequisition,
						PURCHASE_REQUISITION_ITEM, purchaseRequisitionItem))
				.body(prItem)
				.build();
		
		final BatchChangeSet changeSet = BatchChangeSet.newBuilder().build();
		changeSet.add(patchRequest);
		
		final String boundary = "BatchBoundary";
		final InputStream batchRequestInputStream = EntityProvider.writeBatchRequest(Collections.singletonList(changeSet), boundary);
		
		final HttpPost httpRequest = new HttpPost(PurchaseRequisitionService.DEFAULT_SERVICE_PATH + "/$batch");
		httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, "multipart/mixed;boundary=" + boundary);
		httpRequest.setHeader(ODataConnectivityUtil.CSRF_HEADER, csrfToken);
		httpRequest.setEntity(new InputStreamEntity(batchRequestInputStream));
		
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpRequest);
		} catch (IOException cause) {
			final ODataException e = new ODataException(ODataExceptionType.ODATA_OPERATION_EXECUTION_FAILED, 
					"Exception when making OData $batch request to S/4HANA", 
					cause);
			log.error(e.getMessage(), cause);
			throw e;
		}
		
		final int httpCode = httpResponse.getStatusLine().getStatusCode();
		if (httpCode != 202) {
			final ODataException e = new ODataException(ODataExceptionType.ODATA_OPERATION_EXECUTION_FAILED, 
					"$batch response HTTP code should be 202, was " + httpCode + " with message " + httpResponse.getStatusLine().getReasonPhrase());
			log.error(e.getMessage());
			throw e;
		}
		
		final HttpEntity responseEntity = httpResponse.getEntity();
		if (responseEntity == null) {
			final ODataException e = new ODataException(ODataExceptionType.RESPONSE_DESERIALIZATION_FAILED, 
					"$batch response contains no body");
			log.error(e.getMessage());
			throw e;
		}
		
		final Header contentTypeHeader = httpResponse.getFirstHeader(HttpHeaders.CONTENT_TYPE);
		if (contentTypeHeader == null) {
			final ODataException e = new ODataException(ODataExceptionType.RESPONSE_DESERIALIZATION_FAILED, 
					"$batch response does not contain " + HttpHeaders.CONTENT_TYPE + " header");
			log.error(e.getMessage());
			throw e;
		}
		
		List<BatchSingleResponse> batchResponse = null;
		try {
			batchResponse = EntityProvider.parseBatchResponse(responseEntity.getContent(), contentTypeHeader.getValue());
		} catch (UnsupportedOperationException | IOException | BatchException cause) { // responseEntity.getContent()
			final ODataException e = new ODataException(ODataExceptionType.RESPONSE_DESERIALIZATION_FAILED, 
					"Exception when trying to parse single batch response",
					cause);
			log.error(e.getMessage(), cause);
			throw e;
		}
		
		if (batchResponse.size() != 1) {
			final ODataException e = new ODataException(ODataExceptionType.RESPONSE_DESERIALIZATION_FAILED, 
					"$batch response should contain single response");
			log.error(e.getMessage());
			throw e;
		}
		
		final String patchResponseCode = batchResponse.get(0).getStatusCode();
		if (!"204".equals(patchResponseCode)) {
			final ODataException e = new ODataException(ODataExceptionType.ODATA_OPERATION_EXECUTION_FAILED, 
					"single $batch response status code should be 204, was " + patchResponseCode + " with body " +  batchResponse.get(0).getBody());
			log.error(e.getMessage());
			throw e;
		}
	}
	
}
