package com.sap.cldfnd.situationshandling.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.math.BigDecimal;

import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonProviderTest {
	
	JsonProvider jsonProvider;
	
	@Before
	public void setUp() {
		jsonProvider = new JsonProvider();
	}
	
	@Test
	public void testSerializedEntityContainsAnnotatedNotNull() throws JsonProcessingException {

		// given a test entity 
		class Testee {
			@JsonProperty("NonNullProperty")
			private final String nonNullProperty = "Not null";
			
			@JsonProperty("NullProperty")
			private final String nULLProperty = null;
			
			@JsonProperty("JAVABeanNaming")
			private final String jAVABeanNaming = "Java Bean naming";
			
			@JsonProperty("BigDecimalProperty")
			private final BigDecimal bigDecimalProperty = BigDecimal.valueOf(-1.5);
			
			private final String defaultProperty = "anyValue";
			
			@SuppressWarnings("unused")
			public String getDefaultProperty() {
				return defaultProperty;
			}
			
			@SuppressWarnings("unused")
			public String getJAVABeanNaming() {
				return jAVABeanNaming;
			}
		}
		
		// when it is serialized
		final String json = serialize(new Testee());

		// then only non-null properties are included
		assertThat(json, allOf(containsString("\"NonNullProperty\""), not(containsString("\"NullProperty\""))));
		
		// and it is pretty-printed
		assertThat(json, containsString("\n"));
		
		// and by default the JSON property name is the same as the field name (starts with lower letter)
		assertThat(json, containsString("DefaultProperty"));
		
		// and there is no conflict between Java Bean and Jackson naming policies
		assertThat(json, allOf(containsString("JAVABeanNaming"), not(containsString("javabeanNaming"))));
		
		// and the BigDecimal property is included as well
		assertThat(json, containsString("-1.5"));
	}
	
	public void testObjectMapperIgnoresUnknownProperties() throws JsonParseException, JsonMappingException, IOException {
		
		class Testee {
			
			private String knownProperty;

			public String getKnownProperty() {
				return knownProperty;
			}

		}
		
		// given a serialized Testee value with unkwnown property
		final String testeeJson = "{"
					+ "knownPropery: \"expectedValue\","
					+ "unknownPropery: \"unexpected value\""
				+ "}";
		
		// when it is deserialized
		final Testee testee = JsonProvider.locateMapper().readValue(testeeJson, Testee.class);
		
		// then no error is thrown
		assertThat(testee.getKnownProperty(), is("expectedValue"));
	}
	
	public <T> String serialize(final T entity) throws JsonProcessingException {
		final ObjectMapper om = locateJsonMapper(entity.getClass());
		return om.writeValueAsString(entity);
	}

	protected <T> ObjectMapper locateJsonMapper(final Class<T> entityClass) {
		return jsonProvider.locateMapper(entityClass, MediaType.APPLICATION_JSON_TYPE);
	}

}
