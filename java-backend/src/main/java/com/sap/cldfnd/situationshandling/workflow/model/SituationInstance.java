package com.sap.cldfnd.situationshandling.workflow.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;
import com.sap.cldfnd.situationshandling.ems.model.BusinessEvent;

/**
 * Instance object of S/4HANA Business Situation to be used in
 * {@link NewWorkflowInstance} DTO.
 * <p>
 * This object may be passed as a context for a new workflow instance for
 * business situations which have no context defined. If a business situation
 * has a context defined (like in our case), consider using
 * {@link SituationContext} object.
 * <p>
 * It contains only {@code situationInstanceId} since this is the only useful
 * information that comes with {@link BusinessEvent} from S/4HANA. Additional
 * information about the situation can be requested via <a href=
 * "https://api.sap.com/api/API_BUSINESS_SITUATION_SRV/resource">Business
 * Situation - Read</a> OData API of SAP S/4HANA Cloud.
 * 
 * @see NewWorkflowInstance#setContext(Object)
 *
 */
public class SituationInstance {
	
	private String situationInstanceId;
	
	/**
	 * No-args constructor for Jackson
	 */
	@VisibleForTesting
	protected SituationInstance() {
		// nothing to do here
	}

	@VisibleForTesting
	protected SituationInstance(String situationInstanceId) {
		super();
		this.situationInstanceId = situationInstanceId;
	}
	
	/* getters and setters */
	
	@JsonProperty("SituationInstanceId")
	@NotNull
	public String getSituationInstanceId() {
		return situationInstanceId;
	}

	public void setSituationInstanceId(String situationInstanceId) {
		this.situationInstanceId = situationInstanceId;
	}

	@Override
	public String toString() {
		return "SituationInstance [situationInstanceId=" + situationInstanceId + "]";
	}

}
