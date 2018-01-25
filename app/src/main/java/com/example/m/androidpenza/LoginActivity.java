package com.example.m.androidpenza;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener((View v) -> {
            startActivity(new Intent(this, LoginConfirmActivity.class));
        });

        ImageButton vkButton = findViewById(R.id.vk_imageButton);
        vkButton.setOnClickListener((View v) -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com")));
        });

        ImageButton facebookButton = findViewById(R.id.fb_imageButton);
        facebookButton.setOnClickListener((View v) -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com")));
        });

        ImageButton gPlusButton = findViewById(R.id.gplus_imageButton);
        gPlusButton.setOnClickListener((View v) -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com")));
        });
    }
}
