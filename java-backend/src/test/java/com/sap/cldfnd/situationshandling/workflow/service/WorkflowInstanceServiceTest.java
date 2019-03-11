package com.sap.cldfnd.situationshandling.workflow.service;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.sap.cldfnd.situationshandling.testutil.MockUtils;
import com.sap.cldfnd.situationshandling.workflow.model.NewWorkflowInstance;
import com.sap.cldfnd.situationshandling.workflow.model.SituationContext;
import com.sap.cldfnd.situationshandling.workflow.service.WorkflowInstanceService;
import com.sap.cldfnd.situationshandling.workflow.service.WorkflowInstanceServiceProducer;

public class WorkflowInstanceServiceTest {
	
	@ClassRule
	public static final MockUtils mockUtils = new MockUtils();

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(MockUtils.WIREMOCK_PORT);
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	WorkflowInstanceService testee;

	/**
	 * This method replaces the CDI injection by instantiating a testee service
	 * using the same CDI Producer {@link WorkflowInstanceServiceProducer}<br>
	 * 
	 * {@link WorkflowInstanceService} is injected using CDI in the application,
	 * thus {@link WorkflowInstanceService} should ideally also be injected in
	 * tests. The problem is that then tests should run in a container
	 * environment (e.g. an OpenEJB container) and they run way too long
	 * there.<br>
	 * 
	 */
	@Before
	public void setUp() {
		testee = new WorkflowInstanceServiceProducer().workflowInstanceService();
	}
	
	@Test
	public void testPostInstanceWithoutToken() {
		// Given a response mocked in 
		// src/test/resources/mappings/workflow-instances/postInstance.json

		// When
		testee.postInstance(NewWorkflowInstance.of("testDefinitionId", 
				SituationContext.of("testSituationContextId", "testSituationInstanceId")));

		// Then no error is thrown
	}
	
	@Test
	public void testPostInstanceWithToken() {
		// Given a response mocked in 
		// src/test/resources/mappings/workflow-instances/postInstance.json

		// When
		testee.postInstance(
				NewWorkflowInstance.of("testDefinitionId", SituationContext.of("testSituationContextId", "testSituationInstanceId")), 
				/* xCsrfToken = */ "dummyToken");

		// Then no error is thrown
	}

}
