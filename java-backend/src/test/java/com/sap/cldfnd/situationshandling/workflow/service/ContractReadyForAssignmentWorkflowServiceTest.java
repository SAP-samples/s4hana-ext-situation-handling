package com.sap.cldfnd.situationshandling.workflow.service;

import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.sap.cldfnd.situationshandling.workflow.model.NewWorkflowInstance;
import com.sap.cldfnd.situationshandling.workflow.model.SituationContext;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class ContractReadyForAssignmentWorkflowServiceTest {
	
	@Mock
	WorkflowInstanceService workflowInstanceServiceMock;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@InjectMocks
	ContractReadyForAssignmentWorkflowService testee;
	
	@Test
	public void testCreateInstanceWhenContextIsInvalidThenConstraintViolationExceptionIsThrown() {
		// Given a workflow instance
		final NewWorkflowInstance workflowInstance = NewWorkflowInstance.of("workflow definition", 
				SituationContext.of("situation context id", "situation instance id"));
		
		// When
		testee.createInstance(workflowInstance);
		
		// Then
		verify(workflowInstanceServiceMock).postInstance(workflowInstance);
	}

}
