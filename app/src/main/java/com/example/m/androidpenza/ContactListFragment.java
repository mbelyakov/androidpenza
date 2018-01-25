package com.example.m.androidpenza;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class ContactListFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Какой-то некрасивый код получился далее, но как его исправить?
        final int MAX_LIST_SIZE = 20;
        final int PHONE_PREFIX = 8887766;

        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        activity.setTitle(R.string.contacts);

        ListView lv = view.findViewById(R.id.contacts);

        ArrayList<Contact> items = new ArrayList<>();
        for (int i = 1; i <= MAX_LIST_SIZE; i++) {
            items.add(new Contact("Имя Фамилия " + i, "+7999" + (PHONE_PREFIX + i)));
        }
        ContactListAdapter adapter = new ContactListAdapter(activity, android.R.layout.simple_list_item_2, items);
        lv.setAdapter(adapter);
    }
}
