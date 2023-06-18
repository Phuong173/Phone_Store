package com.example.app_phone_store_manager_du_an_1.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {
    private static final String DATE_PATTREN = "yyyy-MM-dd";
    private static DateFormat dateFormat = new SimpleDateFormat(DATE_PATTREN);

    public static Date stringToDate(String strDate) {
        try {
            return dateFormat.parse(strDate);
        } catch (Exception e) {
            return null;
        }
    }
    public static String dateToString(Date date) {
        return dateFormat.format(date);
    }
}
