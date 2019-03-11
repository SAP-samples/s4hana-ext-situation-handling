package com.sap.cldfnd.situationshandling.util;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;

/**
 * Ensures that each element of the annotated collection is not null.
 * <p>
 * If your runtime supports Java Bean Validation 2.0, you can put standard @{@link NotNull} 
 * annotation before the collection element type:<br>
 * {@code Collection<}@{@code NotNull ElementType> collection;}
 *
 */
@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = EachNotNullValidator.class)
@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
public @interface EachNotNull {

	String message() default "element should not be null";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}