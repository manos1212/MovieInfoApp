package com.codehub.movieinfoapp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codehub.movieinfoapp.R;
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
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        username = view.findViewById(R.id.textField_email);
        password = view.findViewById(R.id.textField_pwd);
        forgot_pwd = view.findViewById(R.id.login_forgotten);
        login_btn = view.findViewById(R.id.login_btn);
        ;
        username.setTranslationX(1000);
        password.setTranslationX(1000);
        forgot_pwd.setTranslationX(1000);
        login_btn.setTranslationX(1000);

        username.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(100).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        forgot_pwd.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        login_btn.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}