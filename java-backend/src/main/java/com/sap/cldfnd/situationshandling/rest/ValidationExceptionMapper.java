package com.sap.cldfnd.situationshandling.rest;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;

/**
 * Custom exception mapper for CXF to be used to produce a nice HTTP error
 * response for a caught {@link ConstraintViolationException}
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
	
	private static final Logger log = LoggerFactory.getLogger(ValidationExceptionMapper.class);
	
	/**
	 * The error code that will be used for each of
	 * {@link ErrorResponse#getCause()}
	 */
	public static final String CAUSE_ERROR_CODE = "ConstraintViolation";

	/**
	 * @return an error response with JSON body in {@link ErrorResponse} format.
	 *         <p>
	 *         For Quality of Service level 1, we set HTTP code to 200 so that
	 *         Enterprise Messaging Service doesn't try to send the same message
	 *         again.
	 *         <p>
	 *         If you are using Quality of Service level 0, consider setting the
	 *         HTTP status code to 422 Unprocessable Entity.
	 * 
	 * @see ValidationExceptionMapper#toErrorResponse(ConstraintViolationException)
	 * @see ConstraintViolationException#getConstraintViolations()
	 * @see <a href="https://tools.ietf.org/html/rfc4918#section-11.2">RFC 4918,
	 *      section 11.2: 422 Unprocessable Entity</a>
	 * @see <a href=
	 *      "https://help.sap.com/viewer/bf82e6b26456494cbdd197057c09979f/Cloud/en-US/6a0e4c77e3014acb8738af039bd9df71.html">SAP
	 *      Business Technology Platform (BTP) Enterprise Messaging service documentation on SAP
	 *      Help Portal</a>
	 */
	@Override
	public Response toResponse(ConstraintViolationException exception) {
		final String errorJson = toErrorResponse(exception).toJson();
		
		log.error(errorJson);
		
		return Response
				.status(200) // Confirm the message delivery for QoS level 1
				.type(MediaType.APPLICATION_JSON)
				.entity(errorJson)
				.build();
	}

	/**
	 * @return {@link ErrorResponse} which {@code code} is the class name of
	 *         {@code exception}, {@code message} is {@code exception}'s message
	 *         and {@code cause} contains the list of constraint violations from
	 *         {@code exception}.
	 */
	public static ErrorResponse toErrorResponse(ConstraintViolationException exception) {
		final String errorMessage = exception.getMessage();
		final String errorCode = exception.getClass().getName();
		
		final List<ErrorResponse> cause = Optional.ofNullable(exception.getConstraintViolations())
				.orElse(Collections.emptySet())
				.stream()
				.filter(Objects::nonNull)
				.map(ValidationExceptionMapper::getViolationMessage)
				.filter(Objects::nonNull)
				.distinct()
				.map(message -> ErrorResponse.of(message, CAUSE_ERROR_CODE))
				.collect(Collectors.toList());
		
		final ErrorResponse errorResponse = ErrorResponse.of(errorMessage, errorCode, cause);
		return errorResponse;
	}

	@VisibleForTesting
	protected static String getViolationMessage(ConstraintViolation<?> violation) {
		return Stream.of(
					/* property path*/ Optional.ofNullable(violation.getPropertyPath()).map(Objects::toString).orElse(null), 
					/* violation message */ violation.getMessage())
				.filter(Objects::nonNull)
				.collect(Collectors.joining(" "));
	}

}
