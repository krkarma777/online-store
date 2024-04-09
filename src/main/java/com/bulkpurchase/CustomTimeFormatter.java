package com.bulkpurchase;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

public class CustomTimeFormatter {

    public static String timeFormat(LocalDateTime localDateTime) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        return Optional.ofNullable(localDateTime).map(formatter::format).orElse(null);
    }

    public static String timeFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return Optional.ofNullable(date).map(formatter::format).orElse(null);
    }
}
