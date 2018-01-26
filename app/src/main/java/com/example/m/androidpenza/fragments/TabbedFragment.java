package com.example.m.androidpenza.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.m.androidpenza.R;

import java.util.ArrayList;
import java.util.List;

public class TabbedFragment extends Fragment {
    List<Fragment> tabs = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tabs.add(new PlaceholderFragment());
        tabs.add(new PlaceholderFragment());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabbed, container, false);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.tab_container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity == null) {
            return;
        }

        ActionBar toolbar = activity.getSupportActionBar();
        View view = getView();
        if (toolbar == null || view == null) {
            return;
        }

        String title = ((TextView) view.findViewById(R.id.text)).getText().toString();
        toolbar.setTitle(title);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabs.get(position);
        }

        @Override
        public int getCount() {
            return tabs.size();
        }
    }
}
