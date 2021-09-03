package com.justanalytics.publishapi.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConversionUtil {

    public static final DateTimeFormatter DATE_FROMATTER = DateTimeFormatter.ofPattern("yyyy-mm-dd");
    public static final DateTimeFormatter TIMESTAMP_FROMATTER = DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss");
    
    public static Integer convertToInteger(String value) {
        return Integer.parseInt(value);
    }

    public static Long convertToLong(String value) {
        return Long.parseLong(value);
    }

    public static Float convertToFloat(String value) {
        return Float.parseFloat(value);
    }

    public static Double convertToDouble(String value) {
        return Double.parseDouble(value);
    }

    public static Date convertToDate(String value) {
        LocalDate localDate = LocalDate.parse(value, DATE_FROMATTER);
        return Date.valueOf(localDate);
    }

    public static Date convertToDate(String value, String timezone) {
        //TODO Implement this
        return null;
    }

    public static Boolean convertToBoolean(String value) {
        return Boolean.parseBoolean(value);
    }

    public static Timestamp convertToTimestamp(String value) {
        LocalDateTime localDateTime = LocalDateTime.parse(value, TIMESTAMP_FROMATTER);
        return Timestamp.valueOf(localDateTime);
    }

    public static Timestamp convertToTimestamp(String value, String timezone) {
        //TODO Implement this
        return null;
    }

    public static <T> List<T> convertTo(Function<String,T> function, List<Object> values) {
        return values.stream().map(value -> function.apply(value.toString())).collect(Collectors.toList());
    }
    
}
