package com.example.m.androidpenza;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LoginConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_confirm);

        Button enterButton = findViewById(R.id.enter_button);
        enterButton.setOnClickListener((View v) -> {
            String text = getResources().getString(R.string.i_am_the_best);
            Snackbar.make(v, text, Snackbar.LENGTH_LONG)
                    .addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            startActivity(new Intent(v.getContext(), ContactListActivity.class));
                        }
                    })
                    .show();
        });
    }
}
