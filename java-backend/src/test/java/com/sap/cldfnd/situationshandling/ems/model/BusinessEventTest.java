package com.sap.cldfnd.situationshandling.ems.model;

public class BusinessEventTest extends AbstractBusinessEventTest<BusinessEvent<?>> {
	
	@Override
	public BusinessEvent<?> newTestee() {
		return new BusinessEvent<>();
	}

	@Override
	public BusinessEvent<?> makeValid(BusinessEvent<?> event) {
		event.setContentType("application/json");
		return event;
	}
	
}
