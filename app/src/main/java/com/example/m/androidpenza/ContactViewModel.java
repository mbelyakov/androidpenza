package com.example.m.androidpenza;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class ContactViewModel extends BaseObservable {
    private Contact contact;

    @Bindable
    public String getFirstName() {
        return contact.getFirstName();
    }

    @Bindable
    public String getMiddleName() {
        return contact.getMiddleName();
    }

    @Bindable
    public String getSurname() {
        return contact.getSurname();
    }

    @Bindable
    public int getPhoto() {
        return contact.getPhoto();
    }

    @Bindable
    public String getPhoneNumber() {
        return contact.getPhoneNumber();
    }

    public void setContact(Contact contact) {
        this.contact = contact;
        notifyChange();
    }

}
