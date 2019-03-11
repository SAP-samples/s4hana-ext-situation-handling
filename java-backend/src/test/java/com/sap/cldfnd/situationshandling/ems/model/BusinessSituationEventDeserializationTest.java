package com.sap.cldfnd.situationshandling.ems.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

import com.sap.cldfnd.situationshandling.testutil.JsonDeserializationTester;

@RunWith(Theories.class)
public class BusinessSituationEventDeserializationTest extends JsonDeserializationTester<BusinessSituationEvent, BusinessEvent<?>> {
	
	/**
	 * Context should be properly deserialized for both event types using Jackson deserialization
	 * 
	 * @see 
	 */
	@DataPoints
	public static final String[] JSON_TYPE_IDS = {BusinessSituationEvent.JSON_TYPE_ID_CHANGED, BusinessSituationEvent.JSON_TYPE_ID_CREATED};
	
	@Override
	public Class<? super BusinessEvent<?>> getBaseClass() {
		return BusinessEvent.class;
	}

	@Override
	public Class<BusinessSituationEvent> getConcreteClass() {
		return BusinessSituationEvent.class;
	}

	@Override
	public String getTesteeJson(String eventType) {
		return "{\r\n" + 
				"  \"eventType\": \"" + eventType + "\",\r\n" + 
				"  \"cloudEventsVersion\": \"0.1\",\r\n" + 
				"  \"source\": \"\",\r\n" + 
				"  \"eventID\": \"ABY+LHs5Hti56Xn6eGqGVw==\",\r\n" + 
				"  \"eventTime\": \"2018-11-13T13:55:44Z\",\r\n" + 
				"  \"schemaURL\": \"/sap/opu/odata/IWXBE/BROWSER_SRV/\",\r\n" + 
				"  \"contentType\": \"application/json\",\r\n" + 
				"  \"data\": {\r\n" + 
				"    \"KEY\": [\r\n" + 
				"      {\r\n" + 
				"        \"SITNINSTANCEID\": \"00163E2C7B391EE8B6EF41966A37764D\"\r\n" + 
				"      }\r\n" + 
				"    ],\r\n" + 
				"    \"SITNDATACONTEXTID\": \"00163E2C7B391ED8B9E97A0E7E844657\",\r\n" + 
				"    \"SITNINSTCEACTIVITYID\": \"00163E2C7B391ED8B9E979A5B86C6656\"\r\n" + 
				"  }\r\n" + 
				"}";
	}

	@Override
	public void assertDeserializedTestee(final BusinessSituationEvent testee) {
		assertThat("String contentType", testee.getContentType(), is(equalTo("application/json")));
		
		assertThat("The context ID is in the payload", testee.getPayload().getSituationContextId(), is(equalTo("00163E2C7B391ED8B9E97A0E7E844657")));
		assertThat("The instance ID is in the payload", testee.getPayload().getSituationInstanceId(), is(equalTo("00163E2C7B391EE8B6EF41966A37764D")));
	}

}
