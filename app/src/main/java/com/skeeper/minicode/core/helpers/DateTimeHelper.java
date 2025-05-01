package com.skeeper.minicode.core.helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeHelper {

    public static String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH");
        return now.format(formatter);
    }
}