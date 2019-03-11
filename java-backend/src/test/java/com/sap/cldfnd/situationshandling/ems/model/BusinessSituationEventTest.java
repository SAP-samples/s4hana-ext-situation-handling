package com.sap.cldfnd.situationshandling.ems.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Collection;

import org.junit.Test;

import com.sap.cldfnd.situationshandling.ems.model.BusinessSituationEvent.Payload;
import com.sap.cldfnd.situationshandling.testutil.ValidationUtil;

public class BusinessSituationEventTest extends BusinessEventTest {
	
	@Override
	public BusinessSituationEvent newTestee() {
		return new BusinessSituationEvent();
	}

	@Override
	public BusinessSituationEvent makeValid(BusinessEvent<?> event) {
		super.makeValid(event);
		
		if (!(event instanceof BusinessSituationEvent)) {
			throw new IllegalArgumentException("event is not an instance of " + BusinessSituationEvent.class.getSimpleName());
		}
		final BusinessSituationEvent businessPartnerEvent = (BusinessSituationEvent) event;
		
		final Payload payload = BusinessSituationEventPayloadTest.givenValidBusinessSituationEventPayload();
		businessPartnerEvent.setPayload(payload);
		
		return businessPartnerEvent;
	}
	
	/**
	 * Tests @{@link javax.validation.constraints.NotNull} annotation on
	 * {@link BusinessSituationEvent#getPayload()} method
	 */
	@Test
	public void testWithNullPayload() {
		// Given a testee business event object...
		final BusinessSituationEvent testee = makeValid(newTestee());
		
		//...that has an incorrect payload value
		testee.setPayload(null);
		
		// When the object is validated
		final Collection<String> violations = ValidationUtil.getViolatedPropertyPaths(testee);

		// Then it has a violation of the payload property
		assertThat("Violated properties", violations, containsInAnyOrder("payload"));
	}
	
	/**
	 * Tests @{@link javax.validation.Valid} annotation on
	 * {@link BusinessSituationEvent#getPayload()} method
	 */
	@Test
	public void testWithInvalidPayload() {
		// Given a testee business event object...
		final BusinessSituationEvent testee = makeValid(newTestee());
		
		//...that has an incorrect payload value
		testee.getPayload().keys = null;
		
		// When the object is validated
		final Collection<String> violations = ValidationUtil.getViolatedPropertyPaths(testee);
		
		// Then it has a violation of the payload property
		assertThat("Violated properties", violations, containsInAnyOrder("payload.keys"));
	}

}
