package com.example.m.androidpenza.model;

import android.os.Build;
import android.telephony.PhoneNumberUtils;

import com.example.m.androidpenza.R;

import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

public class Contact {
    private UUID id;
    private String firstName;
    private String middleName;
    private String surname;
    private String phoneNumber;
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
        this.color = R.color.defaultCardBackground; //todo получить значение ресурса
    }

    public int getColor() {
        return color;
    }

    public Contact setColor(int color) {
        this.color = color;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public Contact setId(UUID id) {
        this.id = id;
        return this;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Contact setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getMiddleName() {
        return middleName;
    }

    public Contact setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public Contact setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Contact setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getPhoneNumber() {
        String formatedNumber = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && phoneNumber != null) {
            formatedNumber = PhoneNumberUtils.formatNumber(phoneNumber, "RU");
        }
        return formatedNumber != null ? formatedNumber : phoneNumber;
    }

    public Contact setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public int getPhoto() {
        return photo;
    }

    public Contact setPhoto(int photo) {
        this.photo = photo;
        return this;
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
