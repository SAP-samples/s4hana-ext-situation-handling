package com.sap.cldfnd.situationshandling.workflow.service;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.sap.cldfnd.situationshandling.rest.JsonProvider;
import com.sap.cldfnd.situationshandling.util.DestinationHelper;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;
import feign.slf4j.Slf4jLogger;

@ApplicationScoped
public class WorkflowInstanceServiceProducer {
	
	@Produces
	public WorkflowInstanceService workflowInstanceService() { 
		return Feign.builder()
			.contract(new JAXRSContract())
			.encoder(new JacksonEncoder(JsonProvider.locateMapper()))
			.decoder(new JacksonDecoder(JsonProvider.locateMapper()))
			.logger(new Slf4jLogger())
			.client(DestinationHelper.getHttpClient(WorkflowInstanceService.DESTINATION_NAME))
			.target(WorkflowInstanceService.class, DestinationHelper.getUrl(WorkflowInstanceService.DESTINATION_NAME));
	}
	
}
