package com.example.m.androidpenza.activities;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.m.androidpenza.R;
import com.example.m.androidpenza.fragments.GreenFragment;
import com.example.m.androidpenza.fragments.TabbedFragment;
import com.example.m.androidpenza.fragments.VioletFragment;

public class InfoActivity extends AppCompatActivity {

    private static final String TAG = "InfoActivity";
    public static final String STATE_NAV_ITEM = "currentNavItem";

    private Fragment greenFragment = new GreenFragment();
    private Fragment violetFragment = new VioletFragment();
    private Fragment tabbedFragment = new TabbedFragment();

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment newFragment = null;
        switch (item.getItemId()) {
            case R.id.nav_green:
                newFragment = greenFragment;
                break;
            case R.id.nav_violet:
                newFragment = violetFragment;
                break;
            case R.id.navigation_notifications:
                newFragment = tabbedFragment;
                break;
        }
        if (newFragment != null) {
            fragmentTransaction.replace(R.id.tab_container, newFragment);
        } else {
            Log.w(TAG, "Выбран пункт меню, непредусмотренный в коде");
        }
        fragmentTransaction.commit();

        return true;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(navListener);

        int currentNavItem = savedInstanceState != null ? savedInstanceState.getInt(STATE_NAV_ITEM) : R.id.nav_green;
        navigation.setSelectedItemId(currentNavItem);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        outState.putInt(STATE_NAV_ITEM, navigation.getSelectedItemId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.set_color_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: 20.01.2018 Нужно ли сохранение цвета фона при пересоздании фрагмента?
        int colorId = 0;
        boolean selectedCorrectItem = true;
        switch (item.getItemId()) {
            case R.id.nav_cream:
                colorId = R.color.cream;
                break;
            case R.id.nav_amber:
                colorId = R.color.amber;
                break;
            case R.id.nav_tangerine:
                colorId = R.color.tangerine;
                break;
            default:
                selectedCorrectItem = false;
                break;
        }

        if (selectedCorrectItem) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            View v = fragmentManager.findFragmentById(R.id.tab_container).getView();
            if (v != null) {
                v.setBackgroundResource(colorId);
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
