package com.example.m.androidpenza;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactListAdapter extends ArrayAdapter<Contact> {
    private ArrayList<Contact> items;
    private LayoutInflater inflater;

    public ContactListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Contact> items) {
        super(context, resource, items);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
        }
        Contact contact = items.get(position);

        TextView nameView = (TextView) view.findViewById(android.R.id.text1);
        nameView.setText(contact.name);

        TextView phoneNumberView = (TextView) view.findViewById(android.R.id.text2);
        phoneNumberView.setText(contact.phoneNumber);

        return view;
    }
}
