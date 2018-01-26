package com.example.m.androidpenza.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.m.androidpenza.R;
import com.example.m.androidpenza.fragments.BlueFragment;
import com.example.m.androidpenza.fragments.RedFragment;
import com.example.m.androidpenza.fragments.YellowFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private Fragment redFragment = new RedFragment();
    private Fragment blueFragment = new BlueFragment();
    private Fragment yellowFragment = new YellowFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment newFragment = null;
        switch (item.getItemId()) {
            case R.id.nav_red:
                newFragment = redFragment;
                break;
            case R.id.nav_blue:
                newFragment = blueFragment;
                break;
            case R.id.nav_yellow:
                newFragment = yellowFragment;
                break;
            case R.id.nav_next:
                startActivity(new Intent(this, InfoActivity.class));
                break;
        }

        if (newFragment != null) {
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle(item.getTitle());

            fragmentTransaction.replace(R.id.tab_container, newFragment);
        } else {
            Log.w(TAG, "Выбран пункт меню, непредусмотренный в коде");
        }
        fragmentTransaction.commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
