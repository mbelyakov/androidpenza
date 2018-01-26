package com.example.m.androidpenza.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.m.androidpenza.R;

public class GreenFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_green, container, false);
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
}
