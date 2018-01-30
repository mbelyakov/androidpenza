package com.example.m.androidpenza;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

class AddressBook {
    private static AddressBook ourInstance;
    private List<Contact> contacts;

    private AddressBook() {
        initContacts();
    }

    static AddressBook getInstance() {
        if (ourInstance == null) {
            ourInstance = new AddressBook();
        }
        return ourInstance;
    }

    List<Contact> getContacts() {
        return contacts;
    }

    Contact getContact(int id) {
        if (id < getSize()) {
            return contacts.get(id);
        } else {
            return null;
        }
    }

    void addContact(Contact contact) {
        contacts.add(contact);
        Collections.sort(contacts);
    }

    void deleteContact(int position) {
        contacts.remove(position);
    }

    private int getSize() {
        return contacts.size();
    }

    private void initContacts() {
        final int MAX_LIST_SIZE = 10;
        final String PHONE_PREFIX = "+79";
        final int MAX_PHONE_SUFFIX = 1_000_000_000;
        final String[] FIRST_NAMES = {"Александр", "Владимир", "Сергей", "Дмитрий", "Алексей", "Андрей", "Николай"};
        final String[] MIDDLE_NAMES = {"Александрович", "Владимирович", "Сергеевич", "Дмитриевич", "Алексеевич", "Андреевич", "Николаевич"};
        final String[] SURNAMES = {"Пушкин", "Толстой", "Достоевский", "Маяковский", "Гумилёв", "Белинский", "Есенин"};

        contacts = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 1; i <= MAX_LIST_SIZE; i++) {
            String firstName = FIRST_NAMES[rnd.nextInt(FIRST_NAMES.length)];
            String middleName = MIDDLE_NAMES[rnd.nextInt(MIDDLE_NAMES.length)];
            String surname = SURNAMES[rnd.nextInt(SURNAMES.length)];

            String phoneNumber = String.format(Locale.US, "%s%09d", PHONE_PREFIX, rnd.nextInt(MAX_PHONE_SUFFIX));
            contacts.add(new Contact(firstName, middleName, surname, phoneNumber, R.mipmap.ic_launcher_round));
        }
        Collections.sort(contacts);
    }
}
