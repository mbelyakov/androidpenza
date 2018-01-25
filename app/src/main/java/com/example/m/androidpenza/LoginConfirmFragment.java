package com.example.m.androidpenza;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LoginConfirmFragment extends Fragment {
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

        View view = inflater.inflate(R.layout.fragment_login_confirm, container, false);

        Button enterButton = view.findViewById(R.id.enter_button);
        enterButton.setOnClickListener((View v) -> {
            String text = getResources().getString(R.string.i_am_the_best);
            Snackbar.make(v, text, Snackbar.LENGTH_SHORT)
                    .addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            if (listener != null)
                                listener.onFragmentInteraction(OnFragmentInteractionListener.LOGIN_ENTER_PRESSED);
                        }
                    })
                    .show();
        });

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
