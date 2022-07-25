package com.example.demo.Utils;

import java.util.regex.Pattern;

public class Vadilate {
    public static Boolean isEmail(String email){
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(email).matches();
    }

}
