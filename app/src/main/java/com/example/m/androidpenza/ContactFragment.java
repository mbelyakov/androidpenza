package com.example.m.androidpenza;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.m.androidpenza.database.ContactDAO;
import com.example.m.androidpenza.database.ContactDB;
import com.example.m.androidpenza.model.Contact;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Completable;

/**
 * Use the {@link ContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends Fragment {
    private static final String TAG = "ContactFragment";

    @BindView(R.id.photo) ImageView photo;
    @BindView(R.id.first_name) TextView firstName;
    @BindView(R.id.middle_name) TextView middleName;
    @BindView(R.id.surname) TextView surname;
    @BindView(R.id.phone_number) TextView phoneNumber;
    @BindView(R.id.card) View card;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private Unbinder unbinder;
    private Contact contact;
    private int cardColor;
    private boolean createNewContact;
    private ContactDAO dao;
    private ContactCallbacks listener;
    private UUID contactId;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param contactId Position of contact in list
     * @return A new instance of fragment ContactFragment.
     */
    public static ContactFragment newInstance(UUID contactId) {
        Log.d(TAG, "newInstance(" + contactId + ")");
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.CONTACT_ID, contactId.toString());
        fragment.setArguments(args);
        return fragment;
    }

    public static ContactFragment newInstance() {
        Log.d(TAG, "newInstance()");
        return new ContactFragment();
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach");
        super.onAttach(context);
        if (context instanceof ContactCallbacks) {
            listener = (ContactCallbacks) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ContactCallbacks");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        dao = ContactDB.getInstance(getActivity()).contactDao();
        setHasOptionsMenu(true);
        setMenuVisibility(true);

        if (getArguments() != null) {
            createNewContact = false;
            contactId = UUID.fromString(getArguments().getString(MainActivity.CONTACT_ID));
        } else {
            createNewContact = true;
            contact = new Contact();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (!createNewContact) {
            dao.getContact(contactId)
//                    .map(v -> {
//                        // Для отладки, чтобы прогрессбар покрутился подольше
//                        SystemClock.sleep(5000);
//                        return v;
//                    })
                    .compose(Utils.applySchedulers())
                    .subscribe(result -> {
                        Log.d(TAG, "Contact " + contactId + " loaded from database ");
                        contact = result;
                        fillCardByContact(contact);
                        progressBar.setVisibility(View.GONE);
                        if (getActivity() != null) {
                            getActivity().setTitle(contact.getFirstName() + " " + contact.getSurname());
                        }
                    });
        }
        return view;
    }

    private void fillCardByContact(Contact contact) {
        firstName.setText(contact.getFirstName());
        middleName.setText(contact.getMiddleName());
        surname.setText(contact.getSurname());
        phoneNumber.setText(contact.getPhoneNumber());
        photo.setImageResource(contact.getPhoto());
        card.setBackgroundColor(contact.getColor());
        cardColor = contact.getColor();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated");
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
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
            listener.onCancelClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.save_button)
    void saveContact() {
        if (createNewContact) {
            createNewContact();
        } else {
            updateContact();
        }
    }

    @OnClick(R.id.color_button)
    void pickColor() {
        final ColorPicker cp = new ColorPicker(getActivity(), Color.alpha(cardColor),
                Color.red(cardColor), Color.green(cardColor), Color.blue(cardColor));
        cp.show();
        cp.setCallback(color -> {
            cardColor = color;
            card.setBackgroundColor(color);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
        unbinder.unbind();
    }

    private void createNewContact() {
        contact.setFirstName(firstName.getText().toString());
        contact.setMiddleName(middleName.getText().toString());
        contact.setSurname(surname.getText().toString());
        contact.setPhoneNumber(phoneNumber.getText().toString());
        contact.setColor(cardColor);
        Completable.fromCallable(() -> dao.addContact(contact))
                .compose(Utils.applyCompletableSchedulers())
                .subscribe(() -> {
                            Log.d(TAG, "New contact successfully added to database");
                            listener.onSaveNewContactClicked();
                        },
                        (e) -> Log.w(TAG, "Add new contact to database error: " + e));
    }

    private void updateContact() {
        contact.setFirstName(firstName.getText().toString());
        contact.setMiddleName(middleName.getText().toString());
        contact.setSurname(surname.getText().toString());
        contact.setPhoneNumber(phoneNumber.getText().toString());
        contact.setColor(cardColor);
        Completable.fromCallable(() -> dao.updateContact(contact)).compose(Utils.applyCompletableSchedulers())
                .subscribe(() -> {
                            Log.d(TAG, "Contact successfully updated in database");
                            listener.onSaveEditedContactClicked();
                        },
                        (e) -> Log.w(TAG, "Update contact in database error: " + e));
    }

    public interface ContactCallbacks {
        void onCancelClicked();

        void onSaveNewContactClicked();

        void onSaveEditedContactClicked();
    }
}
