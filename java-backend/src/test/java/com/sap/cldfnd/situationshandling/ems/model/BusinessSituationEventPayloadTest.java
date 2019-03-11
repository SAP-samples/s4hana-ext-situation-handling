package com.sap.cldfnd.situationshandling.ems.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;

import com.sap.cldfnd.situationshandling.testutil.ValidationUtil;

public class BusinessSituationEventPayloadTest {
	
	@Test
	public void testValidWhenOneKey() {
		final BusinessSituationEvent.Payload testee = givenValidBusinessSituationEventPayload();
		
		final Collection<String> violations = ValidationUtil.getViolatedPropertyPaths(testee);
		
		assertThat("Violated properties", violations, is(empty()));
	}
	
	@Test
	public void testInvalidWhenKeysAreNull() {
		final BusinessSituationEvent.Payload testee = givenValidBusinessSituationEventPayload();
		
		testee.keys = null;
		
		final Collection<String> violations = ValidationUtil.getViolatedPropertyPaths(testee);

		assertThat("Violated properties", violations, containsInAnyOrder("keys"));
	}
	
	@Test
	public void testInvalidWhenKeysAreEmpty() {
		final BusinessSituationEvent.Payload testee = givenValidBusinessSituationEventPayload();
		
		testee.keys = Collections.emptyList();
		
		final Collection<String> violations = ValidationUtil.getViolatedPropertyPaths(testee);
		
		assertThat("Violated properties", violations, containsInAnyOrder("keys"));
	}
	
	@Test
	public void testInvalidWhenMoreThanOneKey() {
		final BusinessSituationEvent.Payload testee = givenValidBusinessSituationEventPayload();
		
		final BusinessSituationEvent.Payload.BusinessSituationKey key = testee.keys.iterator().next();
		testee.keys = Arrays.asList(key, key);
		
		final Collection<String> violations = ValidationUtil.getViolatedPropertyPaths(testee);
		
		assertThat("Violated properties", violations, containsInAnyOrder("keys"));
	}
	
	@Test
	public void testInvalidWhenSingleKeyIsNull() {
		final BusinessSituationEvent.Payload testee = givenValidBusinessSituationEventPayload();
		
		testee.keys = Arrays.asList((BusinessSituationEvent.Payload.BusinessSituationKey) null);
		
		final Collection<String> violations = ValidationUtil.getViolatedPropertyPaths(testee);
		
		assertThat("Violated properties", violations, containsInAnyOrder("keys"));
	}
	
	@Test
	public void testInvalidWhenBusinessSituationIsNull() {
		final BusinessSituationEvent.Payload testee = givenValidBusinessSituationEventPayload();
		
		final BusinessSituationEvent.Payload.BusinessSituationKey key = testee.keys.iterator().next();
		key.situationInstanceId = null;
		testee.keys = Arrays.asList(key);
		
		final Collection<String> violations = ValidationUtil.getViolatedPropertyPaths(testee);
		
		assertThat("Violated properties", violations, containsInAnyOrder("keys[0].situationInstanceId"));
	}
	
	@Test
	public void testValid() {
		final BusinessSituationEvent.Payload testee = givenValidBusinessSituationEventPayload();
		
		final Collection<String> violations = ValidationUtil.getViolatedPropertyPaths(testee);
		
		assertThat("Violated properties", violations, is(empty()));
	}
	
	public static BusinessSituationEvent.Payload givenValidBusinessSituationEventPayload() {
		final BusinessSituationEvent.Payload result = new BusinessSituationEvent.Payload();
		
		final BusinessSituationEvent.Payload.BusinessSituationKey key = new BusinessSituationEvent.Payload.BusinessSituationKey();
		key.situationInstanceId = "0123456789";
		result.setSituationContextId(null); // some situation definitions do not have contexts
		
		result.keys = Collections.singleton(key);
		
		return result;
	}
	
}
