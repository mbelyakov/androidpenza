package com.example.m.androidpenza.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.m.androidpenza.model.AddressBook;
import com.example.m.androidpenza.model.Contact;

import java.util.Locale;
import java.util.Random;

import static com.example.m.androidpenza.database.ContactDbSchema.ContactEntry.*;

public class ContactDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Contacts.db";

    public ContactDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME_ID + " TEXT NOT NULL, " +
                COLUMN_NAME_FIRST_NAME + " TEXT, " +
                COLUMN_NAME_MIDDLE_NAME + " TEXT, " +
                COLUMN_NAME_SURNAME + " TEXT, " +
                COLUMN_NAME_PHONE_NUMBER + " TEXT NOT NULL, " +
                COLUMN_NAME_PHOTO + " INTEGER, " +
                COLUMN_NAME_CREATE_DATE + " INTEGER," +
                COLUMN_NAME_COLOR + " INTEGER)");
        initContacts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Используем обновление для пересоздания БД
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private void initContacts(SQLiteDatabase db) {
        final int MAX_LIST_SIZE = 20;
        final String PHONE_PREFIX = "+79";
        final int MAX_PHONE_SUFFIX = 1_000_000_000;
        final String[] FIRST_NAMES = {"Александр", "Владимир", "Сергей", "Дмитрий", "Алексей", "Андрей", "Николай"};
        final String[] MIDDLE_NAMES = {"Александрович", "Владимирович", "Сергеевич", "Дмитриевич", "Алексеевич", "Андреевич", "Николаевич"};
        final String[] SURNAMES = {"Пушкин", "Толстой", "Достоевский", "Маяковский", "Гумилёв", "Белинский", "Есенин"};

        Random rnd = new Random();
        db.beginTransaction();
        try {
            boolean success = true;
            for (int i = 1; i <= MAX_LIST_SIZE; i++) {
                String firstName = FIRST_NAMES[rnd.nextInt(FIRST_NAMES.length)];
                String middleName = MIDDLE_NAMES[rnd.nextInt(MIDDLE_NAMES.length)];
                String surname = SURNAMES[rnd.nextInt(SURNAMES.length)];
                String phoneNumber = String.format(Locale.US, "%s%09d", PHONE_PREFIX, rnd.nextInt(MAX_PHONE_SUFFIX));

                Contact contact = new Contact()
                        .setFirstName(firstName)
                        .setMiddleName(middleName)
                        .setSurname(surname)
                        .setPhoneNumber(phoneNumber)
                        .setColor(rnd.nextInt());
                ContentValues values = AddressBook.getContentValues(contact);
                long newRowId = db.insert(TABLE_NAME, null, values);
                if (newRowId == -1) {
                    success = false;
                }
            }
            if (success) {
                db.setTransactionSuccessful();
            }
        } finally {
            db.endTransaction();
        }
    }
}
