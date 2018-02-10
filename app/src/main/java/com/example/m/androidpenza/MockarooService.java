package com.example.m.androidpenza;

import com.example.m.androidpenza.model.MockarooContact;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface MockarooService {
    @GET("/contacts.json?key=43dd2e50")
    Single<List<MockarooContact>> getContacts();
}
