package org.shoptelegram.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoyalCardServices {

    public static boolean checkIsPhone(String phone){
        String regex = "\\+7\\d{10}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);

        return matcher.matches();
    }
    public static String formatPhone(String phone){
        StringBuilder temp = new StringBuilder(phone);
        if (temp.charAt(0) == '8' ){
            temp.deleteCharAt(0);
            temp.insert(0,"7");
            temp.insert(0,"+");
        }
        return temp.toString();
    }
}