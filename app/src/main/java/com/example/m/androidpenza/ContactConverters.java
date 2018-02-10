package com.example.m.androidpenza;

import android.graphics.Color;

import com.example.m.androidpenza.model.Contact;
import com.example.m.androidpenza.model.MockarooContact;

import java.util.ArrayList;
import java.util.List;

public class ContactConverters {

    public static final int DEFAULT_ALPHA_MASK = 0x40FFFFFF; // С альфой 0xFF цвета слишком насыщенные

    public static Contact MockarooToContact(MockarooContact m) {
        Contact c = new Contact();
        c.setFirstName(m.firstName);
        c.setMiddleName(m.secondName);
        c.setSurname(m.lastName);
        c.setPhoneNumber(m.phone);
        int color = Color.parseColor(m.color);
        c.setColor(color & DEFAULT_ALPHA_MASK);
        return c;
    }

    public static List<Contact> MockarooToContact(List<MockarooContact> mockarooContact) {
        List<Contact> result = new ArrayList<>(mockarooContact.size());
        for (int i = 0; i < mockarooContact.size(); i++) {
            result.add(MockarooToContact(mockarooContact.get(i)));
        }
        return result;
    }
}
