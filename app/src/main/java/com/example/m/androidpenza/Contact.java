package com.example.m.androidpenza;

import android.os.Build;
import android.telephony.PhoneNumberUtils;

public class Contact {
    String name;
    String phoneNumber;
    int photo;

    public Contact(String name, String phoneNumber, int photo) {
        this.name = name;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.phoneNumber = PhoneNumberUtils.formatNumber(phoneNumber, "RU");
        } else {
            this.phoneNumber = phoneNumber;
        }
        this.photo = photo;
    }
}
