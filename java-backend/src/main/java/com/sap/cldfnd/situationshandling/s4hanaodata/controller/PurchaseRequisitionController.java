package com.sap.cldfnd.situationshandling.s4hanaodata.controller;

import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cldfnd.situationshandling.s4hanaodata.service.PurchaseRequisitionBatchService;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;

@Path("/API_PURCHASEREQ_PROCESS_SRV")
@Produces(MediaType.APPLICATION_JSON)
public class PurchaseRequisitionController {
	
	private static final Logger log = LoggerFactory.getLogger(PurchaseRequisitionController.class);
	
	@Inject
	PurchaseRequisitionBatchService prBatchService;
	
	@PUT
	@Path("A_PurchaseRequisitionItem(PurchaseRequisition='{PurchaseRequisition}',PurchaseRequisitionItem='{PurchaseRequisitionItem}')")
	public void patchPurchaseRequisitionItem(
			@PathParam("PurchaseRequisition") String purchaseRequisition, 
			@PathParam("PurchaseRequisitionItem") String purchaseRequisitionItem,
			String prItem) throws ODataException {
		
		log.debug("PUT /A_PurchaseRequisitionItem(PurchaseRequisition='{}',PurchaseRequisitionItem='{}') with body {}",
				purchaseRequisition,
				purchaseRequisitionItem,
				prItem);
		
		prBatchService.updatePurchaseRequisitionItem(purchaseRequisition, purchaseRequisitionItem, prItem);
	}

}
