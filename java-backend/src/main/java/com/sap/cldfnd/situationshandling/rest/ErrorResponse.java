package com.sap.cldfnd.situationshandling.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * A DTO to represent the error response body in JSON
 * 
 * @see <a href="https://api.sap.com/shell/discover/contentpackage/SAPS4HANACloud/api/API_SALES_ORDER_SRV">Response model for an rrror for Process Sales Order API</a>
 *
 */
@JsonRootName("error")
public class ErrorResponse {
	
	public static class ErrorMessage {
		@JsonProperty("lang")
		private final String lang = "en";
		
		@JsonProperty("value")
		private String value;
		
		public String getValue() {
			return value;
		}
	}
	
	@JsonProperty("code")
	private String code;
	
	@JsonProperty("message")
	private ErrorResponse.ErrorMessage message = new ErrorMessage();
	
	@JsonProperty("cause")
	private Collection<ErrorResponse> cause;
	
	private ErrorResponse(String message, String code, Collection<ErrorResponse> cause) {
		this.message.value = message;
		this.code = code;
		this.cause = cause;
	}
	
	public static ErrorResponse of(String message, int code) {
		return of(message, String.valueOf(code));
	}
	
	public static ErrorResponse of(String message, String code) {
		return of(message, code, null);
	}
	
	public static ErrorResponse of(String message, String code, Collection<ErrorResponse> cause) {
		return new ErrorResponse(message, code, cause);
	}
	
	private ErrorResponse() {
		// default constructor to enable deserialization by Jackson
	}
	
	public String toJson() {
		final ObjectMapper om = new ObjectMapper();
		om.enable(SerializationFeature.WRAP_ROOT_VALUE);
		om.setSerializationInclusion(Include.ALWAYS);
		
		try {
			return om.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// should not happen since ErrorResponse is always valid
			throw new RuntimeException("Initialization error: an exception during the convertation of ErrorResponse to JSON", e);
		}
	}

	@JsonIgnore
	public String getMessageText() {
		return message.getValue();
	}

	public String getCode() {
		return code;
	}

	public Collection<ErrorResponse> getCause() {
		return cause;
	}

}