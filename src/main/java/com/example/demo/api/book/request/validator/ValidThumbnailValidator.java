package com.example.demo.api.book.request.validator;

import com.example.demo.api.book.request.CreateBookRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidThumbnailValidator implements ConstraintValidator<ValidThumbnail, CreateBookRequest> {

    @Override
    public boolean isValid(CreateBookRequest request, ConstraintValidatorContext context) {
        if (request.getThumbnail() != null && request.getThumbnailUrl() != null) {
            return false;
        }
        return true;
    }
}