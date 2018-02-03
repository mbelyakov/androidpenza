package com.example.m.androidpenza.database;


import android.provider.BaseColumns;

public final class ContactDbSchema {
    private ContactDbSchema() {
    }

    public static final class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_FIRST_NAME = "first_name";
        public static final String COLUMN_NAME_MIDDLE_NAME = "middle_name";
        public static final String COLUMN_NAME_SURNAME = "surname";
        public static final String COLUMN_NAME_PHONE_NUMBER = "phone_number";
        public static final String COLUMN_NAME_PHOTO = "photo_id";
        public static final String COLUMN_NAME_CREATE_DATE = "create_date";
        public static final String COLUMN_NAME_COLOR = "color";
    }
}
