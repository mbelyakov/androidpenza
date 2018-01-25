package com.example.m.androidpenza;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {
    private ArrayList<Contact> contacts;

    public ContactListAdapter(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public ContactListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.contact_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactListAdapter.ViewHolder holder, int position) {
        Contact contact = contacts.get(position);

        holder.contactName.setText(contact.name);
        holder.phoneNumber.setText(contact.phoneNumber);
        holder.photo.setImageResource(contact.photo);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactName, phoneNumber;
        ImageView photo;

        ViewHolder(View itemView) {
            super(itemView);

            contactName = itemView.findViewById(R.id.contact_name);
            phoneNumber = itemView.findViewById(R.id.contact_phone_number);
            photo = itemView.findViewById(R.id.contact_photo);
        }
    }
}
