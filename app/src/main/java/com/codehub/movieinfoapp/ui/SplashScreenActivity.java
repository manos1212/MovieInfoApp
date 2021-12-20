package com.codehub.movieinfoapp.ui;

import android.content.Intent;
import com.codehub.movieinfoapp.MainActivity;
import com.codehub.movieinfoapp.R;
import com.codehub.movieinfoapp.common.AbstractActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SplashScreenActivity extends AbstractActivity {
    private FirebaseAuth mAuth;
    @Override
    public int getLayoutRes() {
        return R.layout.activity_splash_screen;
    }

    @Override
    public void startOperations() {

        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            if(currentUser.isEmailVerified()){
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        }else{
            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void stopOperations() {

    }
}