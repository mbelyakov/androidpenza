package com.example.m.androidpenza.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.m.androidpenza.model.Contact;

import java.util.Date;
import java.util.UUID;

import static com.example.m.androidpenza.database.ContactDbSchema.ContactEntry.*;

public class ContactCursorWrapper extends CursorWrapper {

    public ContactCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Contact getContact() {
        String id = getString(getColumnIndex(COLUMN_NAME_ID));
        String firstName = getString(getColumnIndex(COLUMN_NAME_FIRST_NAME));
        String middleName = getString(getColumnIndex(COLUMN_NAME_MIDDLE_NAME));
        String surname = getString(getColumnIndex(COLUMN_NAME_SURNAME));
        String phoneNumber = getString(getColumnIndex(COLUMN_NAME_PHONE_NUMBER));
        int photo = getInt(getColumnIndex(COLUMN_NAME_PHOTO));
        long createDate = getLong(getColumnIndex(COLUMN_NAME_CREATE_DATE));
        int color = getInt(getColumnIndex(COLUMN_NAME_COLOR));

        Contact contact = new Contact()
                .setId(UUID.fromString(id))
                .setFirstName(firstName)
                .setMiddleName(middleName)
                .setSurname(surname)
                .setPhoneNumber(phoneNumber)
                .setPhoto(photo)
                .setCreateDate(new Date(createDate))
                .setColor(color);
        return contact;
    }
}
