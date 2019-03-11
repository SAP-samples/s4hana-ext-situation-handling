package com.sap.cldfnd.situationshandling.workflow.service;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import org.apache.openejb.junit.jee.EJBContainerRule;
import org.apache.openejb.junit.jee.InjectRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.sap.cldfnd.situationshandling.testutil.MockUtils;
import com.sap.cldfnd.situationshandling.workflow.model.NewWorkflowInstance;
import com.sap.cldfnd.situationshandling.workflow.model.SituationContext;

public class ContractReadyForAssignmentWorkflowServiceIT {
	
	@ClassRule
	public static final MockUtils mockUtils = new MockUtils();
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(MockUtils.WIREMOCK_PORT);
	
	/**
	 * Runs the integration test in an OpenEJB container in a server-like
	 * environment
	 */
	@ClassRule
	public static EJBContainerRule ejbContainerRule = new EJBContainerRule();
	
	/**
	 * Enables CDI Injection via @{@link Inject} in this test
	 */
	@Rule
	public InjectRule injectRule = new InjectRule(this);
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Inject
	ContractReadyForAssignmentWorkflowService testee;
	
	@Test
	public void testCreateInstanceWhenContextIsInvalidThenConstraintViolationExceptionIsThrown()
			throws ConstraintViolationException {
		
		thrown.expect(ConstraintViolationException.class);
		
		// Given a situation context where context id is null
		final SituationContext situationContext = SituationContext.of(
				/* situationContextId */ null, 
				"situation instance id");
		
		// When
		testee.createInstance(NewWorkflowInstance.of("workflow definition", situationContext));
		
		// Then expected exception is thrown
	}

}
