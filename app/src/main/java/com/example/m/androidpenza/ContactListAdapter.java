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
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.nameView = convertView.findViewById(android.R.id.text1);
            viewHolder.phoneNumberView = convertView.findViewById(android.R.id.text2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Contact contact = items.get(position);
        viewHolder.nameView.setText(contact.name);
        viewHolder.phoneNumberView.setText(contact.phoneNumber);

        return convertView;
    }

    static class ViewHolder {
        TextView nameView;
        TextView phoneNumberView;
    }
}
