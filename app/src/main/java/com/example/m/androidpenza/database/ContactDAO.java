package com.example.m.androidpenza.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.m.androidpenza.model.Contact;

import java.util.List;
import java.util.UUID;

import io.reactivex.Single;

@Dao
public interface ContactDAO {
    @Query("SELECT * FROM contact")
    Single<List<Contact>> getAll();

    @Query("SELECT * FROM contact WHERE _id = :id")
    Single<Contact> getContact(UUID id);

    @Insert
    long addContact(Contact contact);

    @Insert
    void addAll(List<Contact> contacts);

    @Update
    int updateContact(Contact contact);

    @Delete
    int deleteContact(Contact contact);

    @Query("DELETE FROM contact")
    void clearDB();
}
