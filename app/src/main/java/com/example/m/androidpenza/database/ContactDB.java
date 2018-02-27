package com.example.m.androidpenza.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.m.androidpenza.model.Contact;

@Database(entities = {Contact.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class ContactDB extends RoomDatabase {
    private static final String DATABASE_NAME = "Contacts.db";
    private static ContactDB ourInstance;
    private static final Object lock = new Object();

    public abstract ContactDAO contactDao();

    public static ContactDB getInstance(Context context) {
        synchronized (lock) {
            if (ourInstance == null) {
                ourInstance = Room.databaseBuilder(context.getApplicationContext(), ContactDB.class, DATABASE_NAME).build();
            }
        }
        return ourInstance;
    }
}
