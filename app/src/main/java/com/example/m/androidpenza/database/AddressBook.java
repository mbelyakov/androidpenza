package com.example.m.androidpenza.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.m.androidpenza.model.Contact;

@Database(entities = {Contact.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AddressBook extends RoomDatabase {
    private static final String DATABASE_NAME = "Contacts.db";
    private static AddressBook ourInstance;
    private static final Object lock = new Object();

    public abstract ContactDAO contactDao();

    public static AddressBook getInstance(Context context) {
        synchronized (lock) {
            if (ourInstance == null) {
                ourInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AddressBook.class, DATABASE_NAME)
                        .allowMainThreadQueries() // TODO: 07.02.2018 Да-да, это плохая практика, работу с БД надо вынести в отдельный от UI поток. Но пока не знаю как это сделать
                        .build();
            }
        }
        return ourInstance;
    }
}
