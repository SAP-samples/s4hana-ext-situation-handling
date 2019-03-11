package com.sap.cldfnd.situationshandling.testutil;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOException;

import org.junit.experimental.theories.Theory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @param <T>
 *            concrete class to be deserialized, see
 *            {@link JsonDeserializationTester#getConcreteClass()}
 * @param <S>
 *            base class to be deserialized, see
 *            {@link JsonDeserializationTester#getBaseClass()}
 */
public abstract class JsonDeserializationTester<T extends S, S> {
	
	public abstract Class<T> getConcreteClass();
	
	public abstract Class<? super S> getBaseClass();
	
	public abstract String getTesteeJson(String typeId);
	
	public abstract void assertDeserializedTestee(final T testee);
	
	@Theory
	public void testDeserializeAsTargetClass(String typeId) throws JsonParseException, JsonMappingException, IOException {
		// Given
		final ObjectMapper om = givenObjectMapper();
		
		// When the test json is deserialized using concrete class
		final T testee = om.readValue(getTesteeJson(typeId), getConcreteClass());
		
		// Then
		assertDeserializedTestee(testee);
	}

	@Theory
	public void testDeserializeAsBaseClass(String typeId) throws JsonParseException, JsonMappingException, IOException {
		// Given
		final ObjectMapper om = givenObjectMapper();
		
		// When the test json is deserialized using base class 
		final Object resultOfBaseClass = om.readValue(getTesteeJson(typeId), getBaseClass());
		final T testee = castToConcreteClass(resultOfBaseClass);
		
		// Then
		assertDeserializedTestee(testee);
	}
	
	@SuppressWarnings("unchecked")
	protected T castToConcreteClass(Object testee) {
		assertThat("testee", testee, is(instanceOf(getConcreteClass())));
		
		return (T) testee;
	}

	protected ObjectMapper givenObjectMapper() {
		final ObjectMapper om = new ObjectMapper();
		
		// we don't care if events or their payloads have properties in which we
		// are not interested
		om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		// recommended: resolve conflict between Jackson naming and Java Bean
		// naming for properties which names start with two or more upper-case
		// letters
		// see https://github.com/FasterXML/jackson-databind/issues/1824
		om.enable(MapperFeature.USE_STD_BEAN_NAMING);
		
		return om;
	}

}
