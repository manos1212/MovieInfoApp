package com.codehub.movieinfoapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codehub.movieinfoapp.R;
import com.codehub.movieinfoapp.common.AbstractFragment;
import com.codehub.movieinfoapp.ui.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends AbstractFragment {
    private Button signOutBtn;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_profile;
    }

    @Override
    public void startOperations(View view) {
    signOutBtn = view.findViewById(R.id.sign_out_btn);
    signOutBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FirebaseAuth.getInstance()
                    .signOut();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
    });
    }

    @Override
    public void stopOperations() {

    }
}