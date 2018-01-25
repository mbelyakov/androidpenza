package com.example.m.androidpenza;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

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
        final int PHONE_PREFIX = 8887766;

        ArrayList<Contact> items = new ArrayList<>();
        for (int i = 1; i <= MAX_LIST_SIZE; i++) {
            items.add(new Contact("Имя Фамилия " + i, "+7999" + (PHONE_PREFIX + i), R.mipmap.ic_launcher_round));
        }
        return items;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }
}
