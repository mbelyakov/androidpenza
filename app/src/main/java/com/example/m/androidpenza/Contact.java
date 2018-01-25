package com.example.m.androidpenza;

import android.telephony.PhoneNumberUtils;

public class Contact {
    String name;
    String phoneNumber;

    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = PhoneNumberUtils.formatNumber(phoneNumber, "RU");
    }
}
