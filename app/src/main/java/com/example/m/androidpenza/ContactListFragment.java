package com.example.m.androidpenza;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m.androidpenza.model.AddressBook;
import com.example.m.androidpenza.model.Contact;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ContactListFragment extends Fragment {
    @BindView(R.id.contacts_recycler_view) SpeedyRecyclerView contactsRecyclerView;
    @BindView(R.id.empty_view) TextView emptyView;
    @BindString(R.string.pref_sort_date_value) String SORT_BY_DATE;
    @BindString(R.string.pref_sort_az_value) String SORT_BY_NAME_AZ;
    @BindString(R.string.pref_sort_za_value) String SORT_BY_NAME_ZA;
    private Unbinder unbinder;

    private ContactListAdapter adapter;
    private ContactListCallbacks listener;
    private List<Contact> contacts;

    private String baseFontSize;
    private String sortOrder;
    private String scrollSpeed;

    ItemTouchHelper.SimpleCallback deleteOnSwipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            // TODO: 28.01.2018 Low Реализовать информативное и красивое уведомление об удалении
            Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
            int position = viewHolder.getAdapterPosition();
            deleteContact(position, contacts.get(position).getId());
        }
    };

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

        contacts = AddressBook.getInstance(getActivity()).getContacts();
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
        loadPreferences();
        updateUI();
        adapter.notifyDataSetChanged();
    }

    @SuppressWarnings("ConstantConditions")
    private void loadPreferences() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        // TODO: 03.02.2018 заменить строки на ресурсы
        baseFontSize = sharedPref.getString("pref_fontSize", null);
        sortOrder = sharedPref.getString("pref_sortOrder", null);
        scrollSpeed = sharedPref.getString("pref_scrollSpeed", null);
    }

    private void updateUI() {
        if (contacts.size() == 0) {
            contactsRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            contactsRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        if (sortOrder.equals(SORT_BY_DATE)) {
            Collections.sort(contacts, new Contact.DateComparator());
        } else if (sortOrder.equals(SORT_BY_NAME_AZ)) {
            Collections.sort(contacts, new Contact.NameComparator());
        } else if (sortOrder.equals(SORT_BY_NAME_ZA)) {
            Collections.sort(contacts, new Contact.ReverseNameComparator());
        }
        contactsRecyclerView.setFlingFactor(Integer.valueOf(scrollSpeed));
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
        if (listener != null) {
            if (id == R.id.new_contact) {
                listener.onNewContactClicked();
                return true;
            } else if (id == R.id.settings) {
                listener.onSettingsClicked();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteContact(int position, UUID contactId) {
        AddressBook.getInstance(getActivity()).deleteContact(contactId);
        contacts.remove(position);
        adapter.notifyItemRemoved(position);
        updateUI();
    }

    public interface ContactListCallbacks {
        void onNewContactClicked();

        void onSettingsClicked();

        void onContactClicked(int position, UUID contactId);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Contact contact;

        @BindView(R.id.contact_surname) TextView surname;
        @BindView(R.id.contact_first_name) TextView firstName;
        @BindView(R.id.contact_middle_name) TextView middleName;
        @BindView(R.id.contact_phone_number) TextView phoneNumber;
        @BindView(R.id.contact_photo) ImageView photo;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.contact_list_item, parent, false));
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);

            surname.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.valueOf(baseFontSize));
            firstName.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.valueOf(baseFontSize) - 4);
            middleName.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.valueOf(baseFontSize) - 4);
            phoneNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.valueOf(baseFontSize) - 4);
        }

        void bind(Contact contact) {
            this.contact = contact;
            surname.setText(contact.getSurname());
            firstName.setText(contact.getFirstName());
            middleName.setText(contact.getMiddleName());
            phoneNumber.setText(contact.getPhoneNumber());
            photo.setImageResource(contact.getPhoto());
        }

        @Override
        public void onClick(View v) {
            listener.onContactClicked(getAdapterPosition(), contact.getId());
        }
    }

    private class ContactListAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Contact> contacts;

        ContactListAdapter(List<Contact> contacts) {
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
