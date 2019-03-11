package com.sap.cldfnd.situationshandling.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;

import com.sap.cldfnd.situationshandling.testutil.ValidationUtil;

public class EachNotNullValidatorTest {
	
	@EachNotNull
	Collection<String> testee;

	@Test
	public void testValid() {
		// Given a collection with a single item
		testee = Collections.singleton("item");
		
		// When it is validated
		final Collection<String> violatedPropertyPaths = ValidationUtil.getViolatedPropertyPaths(this);
		
		// Then it is valid
		assertThat("violations", violatedPropertyPaths, is(empty()));
	}
	
	@Test
	public void testValidWhenNull() {
		// Given a collection that is null
		testee = null;
		
		// When it is validated
		final Collection<String> violatedPropertyPaths = ValidationUtil.getViolatedPropertyPaths(this);
		
		// Then it is valid
		assertThat("violations", violatedPropertyPaths, is(empty()));
	}
	
	@Test
	public void testInvalidWhenAnItemIsNull() {
		// Given a collection that contains a null item
		testee = Arrays.asList("nun-null item", null);
		
		// When it is validated
		final Collection<String> violatedPropertyPaths = ValidationUtil.getViolatedPropertyPaths(this);
		
		// Then it is invalid
		assertThat("violated property paths", violatedPropertyPaths, contains("testee"));
	}

}
