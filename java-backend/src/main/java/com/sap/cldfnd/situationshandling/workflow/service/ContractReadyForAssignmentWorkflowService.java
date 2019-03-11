package com.sap.cldfnd.situationshandling.workflow.service;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cldfnd.situationshandling.workflow.model.NewWorkflowInstance;

public class ContractReadyForAssignmentWorkflowService {
	
	private static final Logger log = LoggerFactory.getLogger(ContractReadyForAssignmentWorkflowService.class);
	
	public static final String WORKFLOW_DEFINITION_ID = "contractavailable";
	
	@Inject
	private WorkflowInstanceService workflowInstanceService;
	
	public void createInstance(@Valid NewWorkflowInstance newWorkflowInstance) {
		log.debug("createInstance() is called for {}", newWorkflowInstance);
		workflowInstanceService.postInstance(newWorkflowInstance);
	}

}
