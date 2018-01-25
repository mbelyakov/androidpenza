package com.example.m.androidpenza;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class LoginFragment extends Fragment {
    private OnFragmentInteractionListener listener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button continueButton = view.findViewById(R.id.continue_button);
        continueButton.setOnClickListener((View v) -> {
            if (listener != null)
                listener.onFragmentInteraction(OnFragmentInteractionListener.LOGIN_CONTINUE_PRESSED);
        });

        ImageView vkButton = view.findViewById(R.id.vk_imageButton);
        vkButton.setOnClickListener((View v) -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com"))));

        ImageView facebookButton = view.findViewById(R.id.fb_imageButton);
        facebookButton.setOnClickListener((View v) -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com"))));

        ImageView gPlusButton = view.findViewById(R.id.gplus_imageButton);
        gPlusButton.setOnClickListener((View v) -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com"))));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) activity.setTitle(R.string.authorization);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
