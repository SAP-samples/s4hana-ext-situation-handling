package com.sap.cldfnd.situationshandling.s4hanaodata.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import javax.inject.Inject;

import org.apache.openejb.junit.jee.EJBContainerRule;
import org.apache.openejb.junit.jee.InjectRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.sap.cldfnd.situationshandling.s4hanaodata.controller.PurchaseRequisitionController;
import com.sap.cldfnd.situationshandling.testutil.MockUtils;

public class PurchaseRequisitionControllerIT {
	
	@ClassRule
	public static final MockUtils mockUtils = new MockUtils();
	
	@ClassRule
    public static final EJBContainerRule ejbContainerRule = new EJBContainerRule();
 
    @Rule
    public final InjectRule injectRule = new InjectRule(this, ejbContainerRule);
    
    @Inject
    PurchaseRequisitionController testee;
    
    @Test
    public void testPurchaseRequisitionServiceIsInjected() {
    	assertThat("PurchaseRequisitionBatchService should be injected", testee.prBatchService, is(notNullValue()));
    }
    
}
