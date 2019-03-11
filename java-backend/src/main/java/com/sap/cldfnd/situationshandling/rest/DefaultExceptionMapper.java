package com.sap.cldfnd.situationshandling.rest;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom exception mapper for CXF to be used with exception types for which no
 * other specific mapper exists.
 * <p>
 * The mapper overrides the default CXF mapper that always returns HTTP 500
 * Error code and the body with the exception's message in HTML format.
 * <p>
 * Class-level @{@link Priority} annotation is set to override JAX-RS
 * {@link Provider}s that can be discovered automatically from the class path,
 * e.g. {@link org.apache.olingo.odata2.core.rest.ODataExceptionMapperImpl} from
 * SAP S/4HANA Cloud SDK. According to {@link Priorities} javadocs, default
 * priority is {@link Priorities#USER} and <i>lower</i> values have higher
 * priority for all chains except the Post chain.
 */
@Provider
@Priority(Priorities.USER - 1)
public class DefaultExceptionMapper implements ExceptionMapper<Exception> {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultExceptionMapper.class);
	
	/**
	 * @return an error response with the default HTTP error code 500 and the
	 *         JSON body in {@link ErrorResponse} format. ErrorResponse.code is
	 *         the exception's class name and ErrorResponse.message is the
	 *         exception's message.
	 */
	@Override
	public Response toResponse(Exception exception) {
		final String errorMessage = exception.getMessage();
		final String errorCode = exception.getClass().getName();
		
		final String errorJson = ErrorResponse.of(errorMessage, errorCode).toJson();
		
		log.error(errorJson);
		
		return Response
				.status(Status.INTERNAL_SERVER_ERROR)
				.type(MediaType.APPLICATION_JSON)
				.entity(errorJson)
				.build();
	}

}
