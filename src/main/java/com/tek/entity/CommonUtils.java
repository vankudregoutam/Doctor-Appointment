package com.tek.entity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CommonUtils {

    public static String getCurrentTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        var date = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        return date.format(dateTimeFormatter);
    }

}
