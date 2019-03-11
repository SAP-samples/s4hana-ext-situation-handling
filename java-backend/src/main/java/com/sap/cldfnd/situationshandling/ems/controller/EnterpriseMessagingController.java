package com.sap.cldfnd.situationshandling.ems.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cldfnd.situationshandling.ems.model.BusinessEvent;
import com.sap.cldfnd.situationshandling.ems.model.BusinessSituationEvent;
import com.sap.cldfnd.situationshandling.rest.JsonProvider;
import com.sap.cldfnd.situationshandling.workflow.model.NewWorkflowInstance;
import com.sap.cldfnd.situationshandling.workflow.model.SituationContext;
import com.sap.cldfnd.situationshandling.workflow.service.ContractReadyForAssignmentWorkflowService;

@Path("/ems-rest")
@Produces(MediaType.APPLICATION_JSON)
public class EnterpriseMessagingController {
	
	private static final Logger log = LoggerFactory.getLogger(EnterpriseMessagingController.class);
	
	@Inject
	private ContractReadyForAssignmentWorkflowService contractReadyForAssignmentWorkflowInstanceService;
	
	/**
	 * REST endpoint to be used as a webhook to consume messages from SAP Cloud
	 * Platform Enterprise Messaging service via Messaging REST API (push model).
	 * 
	 * @param payloadJson
	 *            String that contains message body.
	 *            <p>
	 *            JAX-RS allows to deserialize the message body for the known
	 *            content types using providers (see
	 *            {@link javax.ws.rs.ext.Provider}), however SAP Enterprise
	 *            Messaging Service sets a generic
	 *            {@code "application/octet-stream"} content-type header for all
	 *            messages from SAP S/4HANA Cloud because they are received via
	 *            MQTT protocol which doesn't specify that the content type is
	 *            indeed JSON. Thus we deserialize the JSON manually.
	 * 
	 * @see <a href=
	 *      "https://help.sap.com/viewer/bf82e6b26456494cbdd197057c09979f/Cloud/en-US/6a0e4c77e3014acb8738af039bd9df71.html">SAP
	 *      Cloud Platform Enterprise Messaging service documentation on SAP
	 *      Help Portal</a>
	 */
	@POST	
	public void postMessage(String payloadJson) {
		log.debug("POST /ems-rest {}", payloadJson);
		
		BusinessEvent<?> businessEvent;
		try {
			businessEvent = JsonProvider.locateMapper().readValue(payloadJson, BusinessEvent.class);
		} catch (IOException e) {
			log.error("Cannot parse the JSON representation of an event received from S/4HANA", e);
			
			// hide the expected exception for QoS level 1. 
			// If you are using QoS level 0, consider rethrowing the exception
			// so that it will be then handled by DefaultExceptionMapper
			return;
		}
		
		if (!(businessEvent instanceof BusinessSituationEvent)) {
			final String errorMessage = String.format("Received S/4HANA event is instance of %s. It should be instance of %s.",
				businessEvent.getClass().getSimpleName(),
				BusinessSituationEvent.class.getSimpleName());
			
			throw new ConstraintViolationException(errorMessage, /* constraintViolations */ null);
		}
		
		BusinessSituationEvent situationEvent = (BusinessSituationEvent) businessEvent;
		final String situationContextId = situationEvent.getPayload().getSituationContextId();
		final String situationInstanceId = situationEvent.getPayload().getSituationInstanceId();
		
		final NewWorkflowInstance newWorkflowInstance = NewWorkflowInstance.of(
				ContractReadyForAssignmentWorkflowService.WORKFLOW_DEFINITION_ID, 
				SituationContext.of(situationContextId, situationInstanceId));
		
		contractReadyForAssignmentWorkflowInstanceService.createInstance(newWorkflowInstance);
	}
	
	/**
	 * Handshake endpoint allows to additionally secure the messaging endpoint
	 * by checking if the request sender (which origin is sent in a
	 * {@code WebHook-Request-Origin} HTTP header parameter) is allowed to send
	 * messages to the endpoint.
	 * <p>
	 * This method allows all origins, you can implement additional logic here
	 * to restrict them.
	 * 
	 * @see <a href=
	 *      "https://help.sap.com/viewer/bf82e6b26456494cbdd197057c09979f/Cloud/en-US/6a0e4c77e3014acb8738af039bd9df71.html">SAP
	 *      Cloud Platform Enterprise Messaging service documentation on SAP
	 *      Help Portal</a>
	 */
	@OPTIONS
	public Response handshake(@HeaderParam("WebHook-Request-Origin") String requestOrigin) {
		log.info("HTTP OPTIONS handshake request from WebHook-Request-Origin {}", requestOrigin);
		
		return Response.ok()
				.header("WebHook-Allowed-Origin", "*")
				.build();
	}
	
}
