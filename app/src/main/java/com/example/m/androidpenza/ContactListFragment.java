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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m.androidpenza.database.ContactDAO;
import com.example.m.androidpenza.database.ContactDB;
import com.example.m.androidpenza.model.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Completable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactListFragment extends Fragment {
    @BindView(R.id.contacts_recycler_view) SpeedyRecyclerView contactsRecyclerView;
    @BindView(R.id.empty_view) TextView emptyView;
    @BindView(R.id.progressBar) ProgressBar progressBar;
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

    private static final String TAG = "ContactListFragment";
    private static final String STATE_CONTACTS_FETCHED = "contactsFetched";

    private Unbinder unbinder;
    private boolean contactsFetched = false;
    private ContactListAdapter adapter;
    private ContactListCallbacks listener;
    private ContactDAO dao;
    private float prefBaseFontSize;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
        if (context instanceof ContactListCallbacks) {
            listener = (ContactListCallbacks) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ContactListCallbacks");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setHasOptionsMenu(true);
        dao = ContactDB.getInstance(getActivity()).contactDao();
        adapter = new ContactListAdapter(this, new ArrayList<>());
//        loadContactsFromDB();

        if (savedInstanceState != null) {
            contactsFetched = savedInstanceState.getBoolean(STATE_CONTACTS_FETCHED);
        }
    }

    private void loadContactsFromDB() {
        dao.getAll()
                .compose(Utils.applySchedulers())
                .subscribe(result -> {
                    Log.d(TAG, "Loaded from database " + result.size() + " items");
                    adapter.setContacts(result);
                    progressBar.setVisibility(View.GONE);
                });
    }

    private void fetchContacts() {
        Log.d(TAG, "Fetching contacts");
        progressBar.setVisibility(View.VISIBLE);
        ContactsFetcher fetcher = new ContactsFetcher();
        if (fetcher.isOnline(getContext())) {
            fetcher.fetchContacts();
        } else {
            Log.d(TAG, "No internet connection");
            Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
        contactsFetched = true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (!contactsFetched) {
            fetchContacts();
        }

        Log.d(TAG, "Set adapter with " + adapter.getItemCount() + " items");
        contactsRecyclerView.setAdapter(adapter);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final ItemTouchHelper.SimpleCallback deleteOnSwipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                deleteContact(viewHolder.getAdapterPosition());
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(deleteOnSwipeCallback);
        itemTouchHelper.attachToRecyclerView(contactsRecyclerView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        loadPreferences();
        loadContactsFromDB();
    }

    @SuppressWarnings("ConstantConditions")
    private void loadPreferences() {
        Log.d(TAG, "Load preferences");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefBaseFontSize = Float.valueOf(sharedPref.getString(KEY_PREF_FONT_SIZE, null));

        String prefScrollSpeed = sharedPref.getString(KEY_PREF_SCROLL_SPEED, null);
        contactsRecyclerView.setFlingFactor(Integer.valueOf(prefScrollSpeed));

        String prefSortOrder = sharedPref.getString(KEY_PREF_SORT_ORDER, null);
        Comparator<Contact> comparator;
        if (prefSortOrder.equals(SORT_BY_DATE)) {
            comparator = new Contact.DateComparator();
        } else if (prefSortOrder.equals(SORT_BY_NAME_AZ)) {
            comparator = new Contact.NameComparator();
        } else if (prefSortOrder.equals(SORT_BY_NAME_ZA)) {
            comparator = new Contact.ReverseNameComparator();
        } else {
            Log.w(TAG, "Unknown compare type " + prefSortOrder);
            comparator = new Contact.NameComparator();
        }
        adapter.setContactsComparator(comparator);

    }

    private void updateUI() {
        Log.d(TAG, "Update UI");
        if (adapter.getItemCount() == 0) {
            contactsRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            contactsRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
        adapter.sortItems();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putBoolean(STATE_CONTACTS_FETCHED, contactsFetched);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
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
        Log.d(TAG, "onDestroyView");
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d(TAG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.contact_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
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
        Log.d(TAG, "deleteContact");
        Completable.fromCallable(() -> dao.deleteContact(adapter.getItem(position)))
                .compose(Utils.applyCompletableSchedulers())
                .subscribe(() -> {
                            Log.d(TAG, "Contact deleted from database");
                            adapter.removeItem(position);
                            Toast.makeText(getActivity(), R.string.contact_deleted, Toast.LENGTH_SHORT).show();
                        },
                        (e) -> Log.w(TAG, "Delete from database error: " + e));
    }

    public interface ContactListCallbacks {
        void onNewContactClicked();

        void onSettingsClicked();

        void onContactClicked(int position, UUID contactId);
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

            service.getContacts()
                    .compose(Utils.applySchedulers())
                    .map(ContactConverters::MockarooToContact)
                    .subscribe(this::handleSuccess, this::handleError);
        }

        private void handleSuccess(@NonNull List<Contact> contacts) {
            Log.d(TAG, "Successfully fetched " + contacts.size() + " contacts");
            Completable.fromCallable(() -> {
                dao.clearDB();
                dao.addAll(contacts);
                return true;
            })
                    .compose(Utils.applyCompletableSchedulers())
                    .subscribe(() -> {
                                Log.d(TAG, "Database filled by " + contacts.size() + " contacts");
                                adapter.setContacts(contacts);
                                progressBar.setVisibility(View.GONE);
                            },
                            (e) -> {
                                Log.w(TAG, "Database fill error: " + e);
                                progressBar.setVisibility(View.GONE);
                            });
        }

        private void handleError(@NonNull Throwable e) {
            Log.w(TAG, "Fetch error: " + e);
            progressBar.setVisibility(View.GONE);
        }

        @SuppressWarnings("ConstantConditions")
        public boolean isOnline(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.contact_surname) TextView surname;
        @BindView(R.id.contact_first_name) TextView firstName;
        @BindView(R.id.contact_middle_name) TextView middleName;
        @BindView(R.id.contact_phone_number) TextView phoneNumber;
        @BindView(R.id.contact_photo) ImageView photo;
        @BindView(R.id.contact_card) CardView card;
        private Contact contact;

        ContactViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.contact_list_item, parent, false));
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);

            surname.setTextSize(TypedValue.COMPLEX_UNIT_SP, prefBaseFontSize);
            firstName.setTextSize(TypedValue.COMPLEX_UNIT_SP, prefBaseFontSize - 4);
            middleName.setTextSize(TypedValue.COMPLEX_UNIT_SP, prefBaseFontSize - 4);
            phoneNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, prefBaseFontSize - 4);
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

    class ContactListAdapter extends RecyclerView.Adapter<ContactViewHolder> {
        private final ContactListFragment contactListFragment;
        private List<Contact> contacts;
        private Comparator<Contact> comparator;

        ContactListAdapter(ContactListFragment contactListFragment, List<Contact> contacts) {
            this.contactListFragment = contactListFragment;
            this.contacts = contacts;
        }

        @Override
        public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(contactListFragment.getActivity());
            return new ContactViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(ContactViewHolder holder, int position) {
            Contact contact = contacts.get(position);
            holder.bind(contact);
        }

        @Override
        public int getItemCount() {
            return contacts.size();
        }

        public void addItem(Contact contact) {
            int position = contacts.size();
            contacts.add(position, contact);
            notifyItemInserted(position);
            updateUI();
        }

        public void removeItem(int position) {
            Log.d(TAG, "Remove contact at position " + position);
            contacts.remove(position);
            notifyItemRemoved(position);
            updateUI();
        }

        public void sortItems() {
            Log.d(TAG, "Sort items in adapter");
            Collections.sort(contacts, comparator);
            notifyDataSetChanged();
        }

        public Contact getItem(int position) {
            Log.d(TAG, "Get item at position " + position);
            return contacts.get(position);
        }

        public void setContacts(List<Contact> contacts) {
            Log.d(TAG, "Set new contacts list to adapter");
            this.contacts = contacts;
            notifyDataSetChanged();
            updateUI();
        }

        public void setContactsComparator(Comparator<Contact> comparator) {
            Log.d(TAG, "Setup comparator " + comparator.getClass().getSimpleName());
            this.comparator = comparator;
        }
    }
}
