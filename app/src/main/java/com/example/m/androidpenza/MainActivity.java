package com.example.m.androidpenza;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;

import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements ContactListFragment.ContactListCallbacks,
        EditDeleteDialogFragment.EditDeleteDialogCallbacks,
        ContactFragment.ContactCallbacks {

    public static final String POSITION = "position";
    public static final String CONTACT_ID = "contact_id";

    private final ContactListFragment contactListFragment = new ContactListFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);
        if (fragment == null) {
            fm.beginTransaction()
                    .add(R.id.container, contactListFragment)
                    .commit();
        }
    }

    @Override
    public void onContactClicked(int position, UUID contactId) {
        DialogFragment dialog = new EditDeleteDialogFragment();
        Bundle args = new Bundle();
        args.putString(CONTACT_ID, contactId.toString());
        args.putInt(POSITION, position);
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "edit_delete");
    }

    @Override
    public void onNewContactClicked() {
        gotoFragment(ContactFragment.newInstance());
    }

    @Override
    public void onSettingsClicked() {
        gotoFragment(new SettingsFragment());
    }

    @Override
    public void onDialogEditClick(DialogFragment dialog) {
        if (dialog.getArguments() != null) {
            UUID contactId = UUID.fromString(dialog.getArguments().getString(CONTACT_ID));
            gotoFragment(ContactFragment.newInstance(contactId));
        }
    }

    private void gotoFragment(Fragment newFragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, newFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDialogDeleteClick(DialogFragment dialog) {
        if (dialog.getArguments() != null) {
            ContactListFragment fragment = (ContactListFragment) getSupportFragmentManager().findFragmentById(R.id.container);
            int position = dialog.getArguments().getInt(POSITION);
            fragment.deleteContact(position);
        }
    }

    @Override
    public void onCancelClicked() {
        gotoFragment(contactListFragment);
    }

    @Override
    public void onSaveNewContactClicked() {
        gotoFragment(contactListFragment);
    }

    @Override
    public void onSaveEditedContactClicked() {
        gotoFragment(contactListFragment);
    }
}
