package com.sap.cldfnd.situationshandling.util;

import java.util.Collection;
import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EachNotNullValidator implements ConstraintValidator<EachNotNull, Collection<?>> {

	@Override
	public void initialize(EachNotNull constraintAnnotation) {
		// nothing to do here
	}

	@Override
	public boolean isValid(Collection<?> value, ConstraintValidatorContext context) {
		if (value == null || value.isEmpty()) {
			return true;
		}
		
		// Another option could be to iterate through the collection to get the
		// index of a wrong element and put it in the property path of the
		// validation context, but in our case this would make no sence since we
		// use the constraint on deserialized JSON objects only and JSON arrays
		// are unordered according to the JSON specification
		return !value.stream().anyMatch(Objects::isNull);
	}

}