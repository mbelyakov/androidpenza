package com.example.m.androidpenza.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.m.androidpenza.database.ContactCursorWrapper;
import com.example.m.androidpenza.database.ContactDbHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.m.androidpenza.database.ContactDbSchema.ContactEntry.*;

public class AddressBook {
    private static AddressBook ourInstance;
    private SQLiteDatabase database;

    private AddressBook(Context context) {
        database = new ContactDbHelper(context).getWritableDatabase();
    }

    public static AddressBook getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new AddressBook(context.getApplicationContext());
        }
        return ourInstance;
    }

    public Contact getContact(UUID id) {
        try (ContactCursorWrapper cursor = queryContactByCondition(
                COLUMN_NAME_ID + " = ?", new String[]{id.toString()}
        )) {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getContact();
        }
    }

    public List<Contact> getContacts() {
        List<Contact> crimes = new ArrayList<>();
        try (ContactCursorWrapper cursor = queryContactByCondition(null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getContact());
                cursor.moveToNext();
            }
        }
        return crimes;
    }

    @SuppressLint("Recycle")
    private ContactCursorWrapper queryContactByCondition(String whereClause, String[] whereArgs) {
        Cursor cursor = database.query(
                TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new ContactCursorWrapper(cursor);

    }

    public void addContact(Contact contact) {
        ContentValues values = getContentValues(contact);
        database.insert(TABLE_NAME, null, values);
    }

    public void updateContact(Contact contact) {
        ContentValues values = getContentValues(contact);
        database.update(TABLE_NAME,
                values,
                COLUMN_NAME_ID + " = ?",
                new String[]{contact.getId().toString()});
    }

    public void deleteContact(UUID contactId) {
        database.delete(TABLE_NAME,
                COLUMN_NAME_ID + " = ?",
                new String[]{contactId.toString()});
    }

    public static ContentValues getContentValues(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_ID, contact.getId().toString());
        values.put(COLUMN_NAME_FIRST_NAME, contact.getFirstName());
        values.put(COLUMN_NAME_MIDDLE_NAME, contact.getMiddleName());
        values.put(COLUMN_NAME_SURNAME, contact.getSurname());
        values.put(COLUMN_NAME_PHONE_NUMBER, contact.getPhoneNumber());
        values.put(COLUMN_NAME_PHOTO, contact.getPhoto());
        values.put(COLUMN_NAME_CREATE_DATE, contact.getCreateDate().getTime());
        values.put(COLUMN_NAME_COLOR, contact.getColor());
        return values;
    }
}
