package com.example.m.androidpenza;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ContactListFragment extends Fragment {
    @BindView(R.id.contacts_recycler_view) RecyclerView contactsRecyclerView;
    @BindView(R.id.empty_view) TextView emptyView;
    private ContactListAdapter adapter;
    private ContactListCallbacks listener;
    private List<Contact> contacts;
    ItemTouchHelper.SimpleCallback deleteOnSwipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            // TODO: 28.01.2018 Реализовать информативное и красивое уведомление об удалении
            Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
            int contactId = viewHolder.getAdapterPosition();
            deleteContactFromList(contactId);
        }
    };
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ContactListCallbacks) {
            listener = (ContactListCallbacks) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ContactListCallbacks");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        contacts = AddressBook.getInstance().getContacts();
        adapter = new ContactListAdapter(contacts);
        contactsRecyclerView.setAdapter(adapter);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(deleteOnSwipeCallback);
        itemTouchHelper.attachToRecyclerView(contactsRecyclerView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        if (contacts.size() == 0) {
            contactsRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            contactsRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setTitle(R.string.header_contacts);

            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(false);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.contact_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.new_contact) {
            if (listener != null) {
                listener.onNewContactPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteContactFromList(int contactId) {
        AddressBook.getInstance().deleteContact(contactId);
        updateUI();
    }

    public interface ContactListCallbacks {
        void onNewContactPressed();

        void onContactClicked(int contactId);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.contact_surname) TextView surname;
        @BindView(R.id.contact_first_name) TextView firstName;
        @BindView(R.id.contact_middle_name) TextView middleName;
        @BindView(R.id.contact_phone_number) TextView phoneNumber;
        @BindView(R.id.contact_photo) ImageView photo;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.contact_list_item, parent, false));
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        void bind(Contact contact) {
            surname.setText(contact.getSurname());
            firstName.setText(contact.getFirstName());
            middleName.setText(contact.getMiddleName());
            phoneNumber.setText(contact.getPhoneNumber());
            photo.setImageResource(contact.getPhoto());
        }

        @Override
        public void onClick(View v) {
            listener.onContactClicked(getAdapterPosition());
        }

    }

    private class ContactListAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Contact> contacts;

        public ContactListAdapter(List<Contact> contacts) {
            this.contacts = contacts;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new ViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Contact contact = contacts.get(position);
            holder.bind(contact);
        }

        @Override
        public int getItemCount() {
            return contacts.size();
        }
    }
}
