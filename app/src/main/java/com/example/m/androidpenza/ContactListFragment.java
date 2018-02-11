package com.example.m.androidpenza;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
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

import com.example.m.androidpenza.database.ContactDAO;
import com.example.m.androidpenza.database.ContactDB;
import com.example.m.androidpenza.model.Contact;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactListFragment extends Fragment {
    private static final String TAG = "ContactListFragment";
    public static final String STATE_CONTACTS_FETCHED = "contactsFetched";

    @BindView(R.id.contacts_recycler_view) SpeedyRecyclerView contactsRecyclerView;
    @BindView(R.id.empty_view) TextView emptyView;
    // Вопрос: Мне нужны константы, иницилизированные значениями из strings.xml, но объявить их
    // как final нельзя, так как они не инициализируются при создании экземпляра объекта. Я дал
    // им названия как константам, но это может ввести в заблужение. Как поступить? Переименовать
    // в стиле переменных?
    @BindString(R.string.pref_sort_date_value) String SORT_BY_DATE;
    @BindString(R.string.pref_sort_az_value) String SORT_BY_NAME_AZ;
    @BindString(R.string.pref_sort_za_value) String SORT_BY_NAME_ZA;
    @BindString(R.string.key_pref_fontSize) String KEY_PREF_FONT_SIZE;
    @BindString(R.string.key_pref_sortOrder) String KEY_PREF_SORT_ORDER;
    @BindString(R.string.key_pref_scrollSpeed) String KEY_PREF_SCROLL_SPEED;
    private Unbinder unbinder;

    private ContactListAdapter adapter;
    private ContactListCallbacks listener;
    private boolean contactsFetched = false;
    private ContactDAO dao;

    private String baseFontSize;
    private String sortOrder;
    private String scrollSpeed;

    final ItemTouchHelper.SimpleCallback deleteOnSwipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            // TODO: 28.01.2018 Low Реализовать информативное и красивое уведомление об удалении
            Toast.makeText(getActivity(), R.string.contact_deleted, Toast.LENGTH_SHORT).show();
            int position = viewHolder.getAdapterPosition();
            deleteContact(position, adapter.contacts.get(position));
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
        dao = ContactDB.getInstance(getActivity()).contactDao();

        List<Contact> contacts = dao.getAll();
        adapter = new ContactListAdapter(contacts);

        if (savedInstanceState != null) {
            contactsFetched = savedInstanceState.getBoolean(STATE_CONTACTS_FETCHED);
        }
        if (!contactsFetched) {
            fetchContacts();
        }
    }

    private void fetchContacts() {
        Log.d(TAG, "Fetching contacts");
        ContactsFetcher fetcher = new ContactsFetcher();
        if (fetcher.isOnline(getContext())) {
            fetcher.fetchContacts();
            contactsFetched = true;
        } else {
            Log.d(TAG, "No internet connection");
            Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        unbinder = ButterKnife.bind(this, view);

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_CONTACTS_FETCHED, contactsFetched);
    }

    @SuppressWarnings("ConstantConditions")
    private void loadPreferences() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        baseFontSize = sharedPref.getString(KEY_PREF_FONT_SIZE, null);
        sortOrder = sharedPref.getString(KEY_PREF_SORT_ORDER, null);
        scrollSpeed = sharedPref.getString(KEY_PREF_SCROLL_SPEED, null);
    }

    private void updateUI() {
        if (adapter.contacts.size() == 0) {
            contactsRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            contactsRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        if (sortOrder.equals(SORT_BY_DATE)) {
            Collections.sort(adapter.contacts, new Contact.DateComparator());
        } else if (sortOrder.equals(SORT_BY_NAME_AZ)) {
            Collections.sort(adapter.contacts, new Contact.NameComparator());
        } else if (sortOrder.equals(SORT_BY_NAME_ZA)) {
            Collections.sort(adapter.contacts, new Contact.ReverseNameComparator());
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
            } else if (id == R.id.reload_contacts) {
                fetchContacts();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteContact(int position) {
        deleteContact(position, adapter.contacts.get(position));
    }

    public void deleteContact(int position, Contact contact) {
        dao.deleteContact(contact);
        adapter.contacts.remove(position);
        adapter.notifyItemRemoved(position);
        updateUI();
    }

    public class ContactsFetcher {
        private static final String BASE_URL = "https://my.api.mockaroo.com/";

        public void fetchContacts() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            MockarooService service = retrofit.create(MockarooService.class);

            service.getContacts().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(ContactConverters::MockarooToContact)
                    .subscribe(this::handleSuccess, this::handleError);
        }

        @SuppressWarnings("ConstantConditions")
        public boolean isOnline(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }

        private void handleSuccess(@io.reactivex.annotations.NonNull List<Contact> c) {
            Log.d(TAG, "Successfully fetched " + c.size() + " contacts");
            dao.clearDB();
            dao.addAll(c);
            adapter.contacts = c;
            adapter.notifyDataSetChanged();
            updateUI();
        }

        private void handleError(@io.reactivex.annotations.NonNull Throwable e) {
            Log.w(TAG, "Fetch error: " + e);
        }
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
        @BindView(R.id.contact_card) CardView card;

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
            card.setCardBackgroundColor(contact.getColor());
        }

        @Override
        public void onClick(View v) {
            listener.onContactClicked(getAdapterPosition(), contact.getId());
        }
    }

    private class ContactListAdapter extends RecyclerView.Adapter<ViewHolder> {
        public List<Contact> contacts;

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
