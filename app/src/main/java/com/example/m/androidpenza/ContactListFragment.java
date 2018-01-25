package com.example.m.androidpenza;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ContactListFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Возможено ли получить null при вызове getActivity() в данном месте? IDE говорит, что "ДА"
        Activity activity = getActivity();
        if (activity != null) {
            activity.setTitle(R.string.contacts);
            setListAdapter(new ContactListAdapter(activity, R.layout.contact_list_item, getContacts()));
        }
    }

    @NonNull
    private ArrayList<Contact> getContacts() {
        final int MAX_LIST_SIZE = 20;
        final String PHONE_PREFIX = "+79";
        final int MAX_PHONE_SUFFIX = 1_000_000_000;
        final String[] NAMES = {"Александр", "Владимир", "Сергей", "Дмитрий", "Алексей", "Андрей", "Николай"};
        final String[] SURNAMES = {"Пушкин", "Толстой", "Достоевский", "Маяковский", "Гумилёв", "Белинский", "Есенин"};

        ArrayList<Contact> items = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 1; i <= MAX_LIST_SIZE; i++) {
            String contactName = NAMES[rnd.nextInt(NAMES.length)] + " " + SURNAMES[rnd.nextInt(NAMES.length)];
            String phoneNumber = PHONE_PREFIX + rnd.nextInt(MAX_PHONE_SUFFIX);
            items.add(new Contact(contactName, phoneNumber, R.mipmap.ic_launcher_round));
            Collections.sort(items);
        }
        return items;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }
}
