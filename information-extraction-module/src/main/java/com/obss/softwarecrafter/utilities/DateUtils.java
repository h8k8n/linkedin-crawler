package com.obss.softwarecrafter.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    public static Date parse(String dateString){
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
