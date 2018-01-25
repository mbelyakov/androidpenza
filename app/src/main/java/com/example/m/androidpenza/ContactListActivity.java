package com.example.m.androidpenza;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {

    private static final int MAX_LIST_SIZE = 20;
    private static final int PHONE_PREFIX = 8887766;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        ListView lv = (ListView) findViewById(R.id.contacts);

        ArrayList<Contact> items = new ArrayList<>();
        for (int i = 1; i <= MAX_LIST_SIZE; i++) {
            items.add(new Contact("Имя Фамилия " + i, "+7999" + (PHONE_PREFIX + i)));
        }

        ContactListAdapter adapter = new ContactListAdapter(this, android.R.layout.simple_list_item_2, items);
        lv.setAdapter(adapter);
    }
}
