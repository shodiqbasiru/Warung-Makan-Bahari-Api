package com.enigma.wmb_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<IndonesiaPhoneNumber,String> {

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("(\\()?(\\+62|62|0)(\\d{2,3})?\\)?[ .-]?\\d{2,4}[ .-]?\\d{2,4}[ .-]?\\d{2,4}");

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        return phone != null && PHONE_NUMBER_PATTERN.matcher(phone).matches();
    }
}
