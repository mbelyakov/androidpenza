package com.example.m.androidpenza.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.m.androidpenza.model.Contact;

import java.util.List;
import java.util.UUID;

@Dao
public interface ContactDAO {
    @Query("SELECT * FROM contact")
    List<Contact> getAll();

    @Query("SELECT * FROM contact WHERE _id = :id")
    Contact getContact(UUID id);

    @Insert
    void addContact(Contact contact);

    @Insert
    void addAll(List<Contact> contacts);

    @Update
    void updateContact(Contact contact);

    @Delete
    void deleteContact(Contact contact);

    @Delete
    void deleteAll(List<Contact> contacts);
}
