package com.example.m.androidpenza;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class ContactListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final int MAX_LIST_SIZE = 20;
        final int PHONE_PREFIX = 8887766;

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        RecyclerView contactList = view.findViewById(R.id.contacts);
        // Возможено ли получить NPE при вызове getActivity() в данном месте?
        contactList.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<Contact> items = new ArrayList<>();
        for (int i = 1; i <= MAX_LIST_SIZE; i++) {
            items.add(new Contact("Имя Фамилия " + i, "+7999" + (PHONE_PREFIX + i), R.mipmap.ic_launcher_round));
        }
        contactList.setAdapter(new ContactListAdapter(items));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        if (activity != null) {
            activity.setTitle(R.string.contacts);
        }
    }
}