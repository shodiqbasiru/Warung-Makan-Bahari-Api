package com.enigma.wmb_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IndonesiaPhoneNumber {
    String message() default "Invalid Indonesian phone number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
