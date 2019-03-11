package com.sap.cldfnd.situationshandling.rest;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * Custom JSON serializer for CXF
 *
 */
public class JsonProvider extends JacksonJsonProvider {

	@Override
	public ObjectMapper locateMapper(Class<?> type, MediaType mediaType) {
		final ObjectMapper om = super.locateMapper(type, mediaType);
		return configure(om);
	}
	
	public static ObjectMapper locateMapper() {
		return configure(new ObjectMapper());
	}

	public static ObjectMapper configure(final ObjectMapper om) {
		// recommended: resolve conflict between Jackson naming and Java Bean
		// naming for properties which names start with two or more upper-case
		// letters
		// see https://github.com/FasterXML/jackson-databind/issues/1824
		om.enable(MapperFeature.USE_STD_BEAN_NAMING);
		
		om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		// optional: include only properties with non-null values
		om.setSerializationInclusion(Include.NON_NULL);

		// optional: pretty print
		om.enable(SerializationFeature.INDENT_OUTPUT);
		
		om.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
		
		return om;
	}
	
}
