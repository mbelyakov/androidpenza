package com.example.m.androidpenza.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class MockarooContact {
    @SerializedName("first_name")
    public String firstName;
    @SerializedName("last_name")
    public String lastName;
    @SerializedName("second_name")
    public String secondName;
    @SerializedName("phone")
    public String phone;
    @SerializedName("color")
    public String color;
}
