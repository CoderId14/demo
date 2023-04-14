package com.example.demo.api.book.request.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidThumbnailValidator.class)
public @interface ValidThumbnail {
    String message() default "Only one of thumbnail and thumbnailUrl should be provided";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
