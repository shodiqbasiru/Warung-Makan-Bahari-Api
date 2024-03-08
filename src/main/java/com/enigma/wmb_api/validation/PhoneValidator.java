package com.enigma.wmb_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.enigma.wmb_api.constant.PhoneNumberRegex.*;


public class PhoneValidator implements ConstraintValidator<IndonesiaPhoneNumber, String> {

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        return phone != null && (
                TELKOMSEL_NUMBER_PATTERN.matcher(phone).matches() ||
                        SIMPATI_NUMBER_PATTERN.matcher(phone).matches() ||
                        AS_NUMBER_PATTERN.matcher(phone).matches() ||
                        INDOSAT_NUMBER_PATTERN.matcher(phone).matches() ||
                        IM3_NUMBER_PATTERN.matcher(phone).matches() ||
                        XL_NUMBER_PATTERN.matcher(phone).matches() ||
                        AXIS_NUMBER_PATTERN.matcher(phone).matches() ||
                        TRI_NUMBER_PATTERN.matcher(phone).matches() ||
                        SMARTFREN_NUMBER_PATTERN.matcher(phone).matches() ||
                        BYU_NUMBER_PATTERN.matcher(phone).matches() ||
                        OTHERS_NUMBER_PATTERN.matcher(phone).matches()
        );
    }
}
