package com.example.m.androidpenza;

import android.telephony.PhoneNumberUtils;

public class Contact {
    String name;
    String phoneNumber;
    int photo;

    public Contact(String name, String phoneNumber, int photo) {
        this.name = name;
        this.phoneNumber = PhoneNumberUtils.formatNumber(phoneNumber, "RU");
        this.photo = photo;
    }
}
