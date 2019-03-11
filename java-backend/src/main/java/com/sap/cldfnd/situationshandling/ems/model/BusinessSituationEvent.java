package com.sap.cldfnd.situationshandling.ems.model;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.cldfnd.situationshandling.util.EachNotNull;

public class BusinessSituationEvent extends BusinessEvent<BusinessSituationEvent.Payload> {
	
	public static final String JSON_TYPE_ID_CREATED = "BO.BusinessSituation.Created";
	public static final String JSON_TYPE_ID_CHANGED = "BO.BusinessSituation.Changed";
	
	public static class Payload {
		
		public static class BusinessSituationKey {
			@NotNull
			@JsonProperty("SITNINSTANCEID")
			protected String situationInstanceId;
		}
		
		@Valid
		@EachNotNull
		@NotNull
		@Size(min = 1, max = 1)
		@JsonProperty("KEY")
		protected Collection<BusinessSituationKey> keys;
		
		private String situationContextId;

		@JsonProperty("SITNDATACONTEXTID")
		public String getSituationContextId() {
			return situationContextId;
		}
		
		public String getSituationInstanceId() {
			return keys.iterator().next().situationInstanceId;
		}
		
		public void setSituationContextId(String situationContextId) {
			this.situationContextId = situationContextId;
		}
		
	}

	@NotNull
	@Valid
	@Override
	public Payload getPayload() {
		return super.getPayload();
	}

}
