package com.example.m.androidpenza;

import android.os.Build;
import android.support.annotation.NonNull;
import android.telephony.PhoneNumberUtils;

public class Contact implements Comparable<Contact> {
    private String firstName;
    private String middleName;
    private String surname;
    private String phoneNumber;
    private int photo;

    public Contact() {
        this.firstName = "";
        this.middleName = "";
        this.surname = "";
        this.phoneNumber = "";
        this.photo = R.mipmap.ic_launcher_round;
    }

    Contact(String firstName, String middleName, String surname, String phoneNumber, int photo) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNumber() {
        String formatedNumber;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            formatedNumber = PhoneNumberUtils.formatNumber(phoneNumber, "RU");
        } else {
            formatedNumber = phoneNumber;
        }
        return formatedNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    @Override
    public int compareTo(@NonNull Contact other) {
        return (surname + firstName + middleName).compareTo(other.surname + other.firstName + other.middleName);
    }
}
