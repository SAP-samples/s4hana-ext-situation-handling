package com.sap.cldfnd.situationshandling.s4hanaodata.service;

import static org.hamcrest.Matchers.*;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.sap.cldfnd.situationshandling.s4hanaodata.service.PurchaseRequisitionBatchService;
import com.sap.cldfnd.situationshandling.testutil.MockUtils;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;

public class PurchaseRequisitionBatchServiceTest {
	
	private static final String EXISTING_PURCHASE_REQUISITION_ID = "0010001166";
	private static final String EXISTING_PURCHASE_REQUISITION_ITEM_ID = "10";

	private static final String EXISTING_PURCHASE_CONTRACT_ID = "4600000062";
	private static final String EXISTING_PURCHASE_CONTRACT_ITEM_ID = "10";
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@ClassRule
	public static final MockUtils mockUtils = new MockUtils();
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(MockUtils.WIREMOCK_PORT);
	
	PurchaseRequisitionBatchService testee = new PurchaseRequisitionBatchService();
	
	@Test
	public void testUpdate() throws ODataException {
		testee.updatePurchaseRequisitionItem(EXISTING_PURCHASE_REQUISITION_ID, EXISTING_PURCHASE_REQUISITION_ITEM_ID, 
				jsonBodyForPatch(EXISTING_PURCHASE_CONTRACT_ID, EXISTING_PURCHASE_CONTRACT_ITEM_ID));
	}
	
	@Test
	public void testUpdateWhenPurchaseRequisitionNotFound() throws ODataException {
		thrown.expect(ODataException.class);
		thrown.expectMessage(allOf(containsString("single $batch response status code should be 204, was 404"), 
				containsString("Resource not found for segment 'A_PurchaseRequisitionItemType'")));
		
		testee.updatePurchaseRequisitionItem("NotExistPR", EXISTING_PURCHASE_REQUISITION_ITEM_ID, 
				jsonBodyForPatch(EXISTING_PURCHASE_CONTRACT_ID, EXISTING_PURCHASE_CONTRACT_ITEM_ID));
	}
	
	/**
	 * If you are executing the tests against a real S/4HANA System, consider
	 * using this method to unassign purchase contract from purchase requisition
	 * assigned in {@link PurchaseRequisitionBatchServiceTest#testUpdate()}
	 */
	public void unassign() throws ODataException {		
		testee.updatePurchaseRequisitionItem(EXISTING_PURCHASE_REQUISITION_ID, EXISTING_PURCHASE_REQUISITION_ITEM_ID, 
				jsonBodyForPatch("", "0"));
	}
	
	protected String jsonBodyForPatch(final String purchaseContractId, final String purchaseContractItemId) {
		return "{" + 
				"  \"PurchaseContract\": \"" + purchaseContractId + "\"," + 
				"  \"PurchaseContractItem\": \"" + purchaseContractItemId + "\"" + 
				"}";
	}
	
}
