package com.example.m.androidpenza.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;
import java.util.UUID;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static UUID fromString(String value) {
        return value == null ? null : UUID.fromString(value);
    }

    @TypeConverter
    public static String uuidToString(UUID id) {
        return id == null ? null : id.toString();
    }
}
