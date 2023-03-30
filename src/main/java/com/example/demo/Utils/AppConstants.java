package com.example.demo.Utils;

import org.springframework.stereotype.Component;

@Component
public class AppConstants {

    public static final String ROLE_USER = "ROLE_USER";

    public static final String EMAIL_SEND_FROM = "danghieu14th@gmail.com";
    public static final String LINK_VERIFY = "http://localhost:3000/register/callback?token=";

    public static final String LINK_FORGOT_PASSWORD = "http://localhost:3000/change-password?token=";

    public static final String DEFAULT_PAGE_NUMBER = "0";

    public static final String DEFAULT_PAGE_SIZE = "30";

    public static final String DEFAULT_CHAPTER = "1";

    public static final int MAX_PAGE_SIZE = 30;

    public static final String CREATED_DATE = "createdDate";
}
