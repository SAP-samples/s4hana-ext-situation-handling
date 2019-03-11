package com.sap.cldfnd.situationshandling.workflow.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;
import com.sap.cldfnd.situationshandling.ems.model.BusinessEvent;

/**
 * Context object of S/4HANA Business Situation to be used in
 * {@link NewWorkflowInstance} DTO.
 * <p>
 * It contains only {@code situationContextId} since this is the only useful
 * information that comes with {@link BusinessEvent} from S/4HANA. Additional
 * information about the situation can be requested via <a href=
 * "https://api.sap.com/api/API_BUSINESS_SITUATION_SRV/resource">Business
 * Situation - Read</a> OData API of SAP S/4HANA Cloud.
 * 
 * @see NewWorkflowInstance#setContext(Object)
 *
 */
public class SituationContext extends SituationInstance {
	
	private String situationContextId;
	
	/* constructors and fabric methods */
	
	public static SituationContext of(String situationContextId, String situationInstanceId) {
		return new SituationContext(situationContextId, situationInstanceId);
	}

	protected SituationContext(String situationContextId, String situationInstanceId) {
		super(situationInstanceId);
		this.situationContextId = situationContextId;
	}
	
	/**
	 * No-args constructor for Jackson
	 */
	@VisibleForTesting
	protected SituationContext() {
		super();
	}
	
	/* getters and setters */

	@JsonProperty("SituationContextId")
	@NotNull
	public String getSituationContextId() {
		return situationContextId;
	}

	public void setSituationContextId(String situationContextId) {
		this.situationContextId = situationContextId;
	}

	@Override
	public String toString() {
		return "SituationContext [situationContextId=" + situationContextId + ", " + super.toString() + "]";
	}

}
