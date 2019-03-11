package com.sap.cldfnd.situationshandling.workflow.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;

/**
 * Data Transfer Object for {@code POST /v1/workflow-instances operation} of SAP
 * Workflow Service's REST API.
 * <p>
 * The type is generic e.g. it can be used for any workflow definition, this is
 * why the {@code context} parameter is of type {@link Object} so that any
 * object can be assigned to it.
 * 
 * @see <a href=
 *      "https://help.sap.com/doc/72317aec52144df8bc04798fd232a585/Cloud/en-US/wfs-core-api-docu.html#api-WorkflowInstances-v1WorkflowInstancesPost">SAP
 *      Workflow REST Service API documentation - POST /v1/workflow-instances
 *      operation</a>
 * 
 * @see <a href="https://api.sap.com/api/SAP_CP_Workflow/resource">Workflow API
 *      on SAP API Hub</a>
 *
 */
public class NewWorkflowInstance {
	
	/**
	 * Required body parameter in Workflow API
	 */
	@JsonProperty("definitionId")
	@NotNull
	private String definitionId;
	
	@JsonProperty("context")
	@Valid
	private Object context;
	
	public String getDefinitionId() {
		return definitionId;
	}

	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

	public Object getContext() {
		return context;
	}

	public void setContext(Object context) {
		this.context = context;
	}
	
	public static NewWorkflowInstance of(String definitionId, Object context) {
		return new NewWorkflowInstance(definitionId, context);
	}
	
	@VisibleForTesting
	protected NewWorkflowInstance(String definitionId, Object context) {
		super();
		
		if (definitionId == null) {
			throw new IllegalArgumentException("definitionId is null");
		}
		
		this.definitionId = definitionId;
		this.context = context;
	}

	@Override
	public String toString() {
		return "NewWorkflowInstance [definitionId=" + definitionId + ", context=" + context + "]";
	}

}
