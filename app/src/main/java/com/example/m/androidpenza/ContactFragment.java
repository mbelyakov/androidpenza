package com.example.m.androidpenza;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Use the {@link ContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends Fragment {
    private static final String CONTACT_ID = "contact_id";
    @BindView(R.id.photo) ImageView photo;
    @BindView(R.id.first_name) TextView firstName;
    @BindView(R.id.middle_name) TextView middleName;
    @BindView(R.id.surname) TextView surname;
    @BindView(R.id.phone_number) TextView phoneNumber;
    private Contact contact;
    private boolean createNewContact;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param contactId Position of contact in list
     * @return A new instance of fragment ContactFragment.
     */
    public static ContactFragment newInstance(int contactId) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putInt(CONTACT_ID, contactId);
        fragment.setArguments(args);
        return fragment;
    }

    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            int contactId = getArguments().getInt(CONTACT_ID);
            contact = AddressBook.getInstance().getContact(contactId);
            createNewContact = false;
        } else {
            contact = new Contact();
            createNewContact = true;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this, view);

        firstName.setText(contact.getFirstName());
        middleName.setText(contact.getMiddleName());
        surname.setText(contact.getSurname());
        phoneNumber.setText(contact.getPhoneNumber());
        photo.setImageResource(contact.getPhoto());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setTitle(contact.getFirstName() + " " + contact.getSurname());

            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.save_button)
    public void saveContact() {
        if (createNewContact) {
            createNewContact();
        } else {
            updateContact();
        }
        // TODO: 28.01.2018 Корявый возврат к списку контактов. Подумать как сделать правильно.
        getActivity().onBackPressed();
    }

    private void createNewContact() {
        Contact newContact = new Contact(
                firstName.getText().toString(),
                middleName.getText().toString(),
                surname.getText().toString(),
                phoneNumber.getText().toString(),
                R.mipmap.ic_launcher_round);
        AddressBook.getInstance().addContact(newContact);
    }

    private void updateContact() {
        contact.setFirstName(firstName.getText().toString());
        contact.setMiddleName(middleName.getText().toString());
        contact.setSurname(surname.getText().toString());
        contact.setPhoneNumber(phoneNumber.getText().toString());
    }
}
