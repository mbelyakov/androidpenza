package com.example.m.androidpenza.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.telephony.PhoneNumberUtils;

import com.example.m.androidpenza.R;

import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

@Entity
public class Contact {
    @PrimaryKey
    @ColumnInfo(name = "_id")
    @NonNull
    private UUID id;
    private String firstName;
    private String middleName;
    private String surname;
    private String phoneNumber; // TODO: 07.02.2018 @NonNull?
    private int photo;
    private Date createDate;
    private int color;

    public Contact() {
        this.id = UUID.randomUUID();
        this.firstName = "";
        this.middleName = "";
        this.surname = "";
        this.phoneNumber = "";
        this.photo = R.mipmap.ic_launcher_round;
        this.createDate = new Date();
        this.color = Color.WHITE;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @NonNull
    public UUID getId() {
        return id;
    }

    public void setId(@NonNull UUID id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
        String formatedNumber = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && phoneNumber != null) {
            formatedNumber = PhoneNumberUtils.formatNumber(phoneNumber, "RU");
        }
        return formatedNumber != null ? formatedNumber : phoneNumber;
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

    public static class DateComparator implements Comparator<Contact> {

        @Override
        public int compare(Contact o1, Contact o2) {
            Date date1 = o1.getCreateDate();
            Date date2 = o2.getCreateDate();
            return date1.compareTo(date2);
        }
    }

    public static class NameComparator implements Comparator<Contact> {

        @Override
        public int compare(Contact o1, Contact o2) {
            String name1 = o1.getSurname() + o1.getFirstName() + o1.getMiddleName();
            String name2 = o2.getSurname() + o2.getFirstName() + o2.getMiddleName();
            return name1.compareTo(name2);
        }
    }

    public static class ReverseNameComparator implements Comparator<Contact> {

        @Override
        public int compare(Contact o1, Contact o2) {
            String name1 = o1.getSurname() + o1.getFirstName() + o1.getMiddleName();
            String name2 = o2.getSurname() + o2.getFirstName() + o2.getMiddleName();
            return name2.compareTo(name1);
        }
    }
}
