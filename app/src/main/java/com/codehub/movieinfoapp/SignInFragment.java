package com.codehub.movieinfoapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class SignInFragment extends Fragment {
    TextInputLayout username;
    TextInputLayout password;
    TextView forgot_pwd;
    Button login_btn;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        username = view.findViewById(R.id.textField_email);
        password = view.findViewById(R.id.textField_pwd);
        forgot_pwd = view.findViewById(R.id.login_forgotten);
        login_btn = view.findViewById(R.id.login_btn);
        ;
        username.setTranslationX(800);
        password.setTranslationX(800);
        forgot_pwd.setTranslationX(800);
        login_btn.setTranslationX(800);

        username.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(100).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        forgot_pwd.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        login_btn.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
    }
}