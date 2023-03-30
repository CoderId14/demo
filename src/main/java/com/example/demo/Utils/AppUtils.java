package com.example.demo.Utils;

import com.example.demo.exceptions.WebapiException;
import org.springframework.http.HttpStatus;

import java.util.regex.Pattern;

public class AppUtils {
    public static Boolean isEmail(String email){
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(email).matches();
    }
    public static void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new WebapiException(HttpStatus.BAD_REQUEST, "Page number cannot be less than zero.");
        }

        if (size < 0) {
            throw new WebapiException(HttpStatus.BAD_REQUEST, "Size number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new WebapiException(HttpStatus.BAD_REQUEST, "Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }
}
