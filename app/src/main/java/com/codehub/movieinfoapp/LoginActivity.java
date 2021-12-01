package com.codehub.movieinfoapp;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class LoginActivity extends AbstractActivity {

    @Override
    public int getLayoutRes() {
        return R.layout.login;
    }

    @Override
    public void startOperations() {

        Button login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("login_type", "Email");
                startActivity(intent);
            }
        });

        ImageView google_button  = findViewById(R.id.google_button);
        google_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("login_type", "Google");
                startActivity(intent);
            }
        });

        ImageView facebook_button  = findViewById(R.id.facebook_button);
        facebook_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("login_type", "Facebook");
                startActivity(intent);
            }
        });

        TextView forgot_password = findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void stopOperations() {
    }

}