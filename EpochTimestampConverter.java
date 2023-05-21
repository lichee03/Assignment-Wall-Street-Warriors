package com.example.testing1.WallStreet;

import java.text.SimpleDateFormat;
import java.util.Date;
// convert time in 1683680400000 to yyyy-MM-dd HH:mm:ss
public class EpochTimestampConverter {
    private Object timestampObject;
    private String dateFormat ="yyyy-MM-dd HH:mm:ss" ;

    public EpochTimestampConverter(Object timestampObject) {
        this.timestampObject = timestampObject;
    }

    public String convertToDate() {
        long timestampInMillis = convertToLong(timestampObject);
        Date date = new Date(timestampInMillis);

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

    private static long convertToLong(Object object) {
        if (object instanceof Number) {
            return ((Number) object).longValue();
        } else if (object instanceof String) {
            try {
                return Long.parseLong((String) object);
            } catch (NumberFormatException e) {
                // Handle the exception if the string cannot be parsed to a long
            }
        }

        // Return a default value or throw an exception if the conversion is not possible
        return 0L;
    }


}
