package com.example.m.androidpenza;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactListAdapter extends ArrayAdapter<Contact> {
    private final LayoutInflater inflater;
    private final int layout;
    private ArrayList<Contact> items;

    public ContactListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Contact> items) {
        super(context, resource, items);
        layout = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Contact contact = items.get(position);
        holder.contactName.setText(contact.name);
        holder.phoneNumber.setText(contact.phoneNumber);
        holder.photo.setImageResource(contact.photo);

        return convertView;
    }

    static class ViewHolder {
        final TextView contactName;
        final TextView phoneNumber;
        final ImageView photo;

        ViewHolder(View itemView) {
            contactName = itemView.findViewById(R.id.contact_name);
            phoneNumber = itemView.findViewById(R.id.contact_phone_number);
            photo = itemView.findViewById(R.id.contact_photo);
        }
    }
}