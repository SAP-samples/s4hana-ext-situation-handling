package com.sap.cldfnd.situationshandling.workflow.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cldfnd.situationshandling.util.DestinationHelper;
import com.sap.cldfnd.situationshandling.workflow.model.NewWorkflowInstance;

/**
 * @see <a href=
 *      "https://help.sap.com/doc/72317aec52144df8bc04798fd232a585/Cloud/en-US/wfs-core-api-docu.html#api-WorkflowInstances">SAP
 *      Workflow REST Service API documentation - API Methods -
 *      WorkflowInstances</a>
 * 
 * @see <a href="https://api.sap.com/api/SAP_CP_Workflow/resource">Workflow API
 *      on SAP API Hub</a>
 */
@Path("/workflow-service/rest/v1/workflow-instances")
public interface WorkflowInstanceService {
	
	static final Logger log = LoggerFactory.getLogger(WorkflowInstanceService.class);
	
	public static final String DESTINATION_NAME = "bpmworkflowruntime";
	
	/**
	 * A relative path to the endpoint that is used to fetch X-CSRF-Token.
	 * 
	 * @see <a href=
	 *      "https://help.sap.com/doc/72317aec52144df8bc04798fd232a585/Cloud/en-US/wfs-core-api-docu.html#api-XSRFHandling-v1XsrfTokenGet">SAP
	 *      Workflow REST Service API documentation - GET /v1/xsrf-token</a>
	 */
	public static final String XSRF_TOKEN_ENDPOINT = "/workflow-service/rest/v1/xsrf-token";
	
	default void postInstance(NewWorkflowInstance newInstance) {
		postInstance(newInstance, fetchXCsrfToken());
	}
	
	/**
	 * Please use
	 * {@link WorkflowInstanceService#postInstance(NewWorkflowInstance)} method
	 * that handles X-CSRF-Token automatically.
	 * 
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	void postInstance(NewWorkflowInstance newInstance, @HeaderParam("X-CSRF-Token") String xCsrfToken);

	/**
	 * Fetches X-CSRF-Token from
	 * {@link WorkflowInstanceService#XSRF_TOKEN_ENDPOINT}
	 * 
	 * @return X-CSRF-Token
	 */
	default String fetchXCsrfToken() {
		return DestinationHelper.fetchXCsrfToken(DESTINATION_NAME, XSRF_TOKEN_ENDPOINT);
	}
	
}
