package com.codehub.movieinfoapp;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.codehub.movieinfoapp.adapters.VPAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LoginActivity extends AbstractActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private final String[] namesList ={"Sign In","Sign Up"};
    FloatingActionButton  google_btn,facebook_btn;

    @Override
    public int getLayoutRes() {
        return R.layout.login;
    }

    @Override
    public void startOperations() {
//
////        Button login_button = findViewById(R.id.login_button);
//        login_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                intent.putExtra("login_type", "Email");
//                startActivity(intent);
//            }
//        });

//                LoginActivity .this.getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        tabLayout = findViewById(R.id.email_Tabs);
        viewPager = findViewById(R.id.Vpager);
        facebook_btn = findViewById(R.id.facebook_fab);
        google_btn = findViewById(R.id.google_fab);
        viewPager.setUserInputEnabled(true);//disable tabviews scroll

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(),getLifecycle());
        vpAdapter.addFragment(new SignInFragment());
        vpAdapter.addFragment(new SignUpFragment());
        viewPager.setAdapter(vpAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(namesList[position])
        ).attach();

        google_btn.setTranslationY(300);
        facebook_btn.setTranslationY(300);
        tabLayout.setTranslationX(800);
        google_btn.animate().translationY(0).alpha(1).setDuration(1200).setStartDelay(400).start();
        facebook_btn.animate().translationY(0).alpha(1).setDuration(1200).setStartDelay(600).start();
        tabLayout.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();




//        ImageView google_button  = findViewById(R.id.google_button);
//        google_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                intent.putExtra("login_type", "Google");
//                startActivity(intent);
//            }
//        });

//        ImageView facebook_button  = findViewById(R.id.facebook_button);
//        facebook_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                intent.putExtra("login_type", "Facebook");
//                startActivity(intent);
//            }
//        });

//        TextView forgot_password = findViewById(R.id.forgot_password);
//        forgot_password.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void stopOperations() {
    }

}