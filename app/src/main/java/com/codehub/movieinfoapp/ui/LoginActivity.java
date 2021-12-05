package com.codehub.movieinfoapp.ui;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;

import com.codehub.movieinfoapp.common.AbstractActivity;
import com.codehub.movieinfoapp.R;
import com.codehub.movieinfoapp.adapters.VPAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LoginActivity extends AbstractActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private final String[] namesList ={"Sign In","Sign Up"};
    FloatingActionButton  google_btn,facebook_btn;
    ImageView app_logo;
    TextView welcome_text;

    @Override
    public int getLayoutRes() {
        return R.layout.login;
    }

    @Override
    public void startOperations() {

//                LoginActivity .this.getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        app_logo = findViewById(R.id.app_logo_icon);
        welcome_text = findViewById(R.id.login_title);
        tabLayout = findViewById(R.id.email_Tabs);
        viewPager = findViewById(R.id.Vpager);
        facebook_btn = findViewById(R.id.facebook_fab);
        google_btn = findViewById(R.id.google_fab);
        viewPager.setUserInputEnabled(false);//disable tabviews scroll

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(),getLifecycle());
        vpAdapter.addFragment(new SignInFragment());
        vpAdapter.addFragment(new SignUpFragment());
        viewPager.setAdapter(vpAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(namesList[position])
        ).attach();

        app_logo.setTranslationY(-300);
        welcome_text.setTranslationX(1000);
        google_btn.setTranslationY(300);
        facebook_btn.setTranslationY(300);
        tabLayout.setTranslationX(1000);
        app_logo.animate().translationY(0).alpha(1).setDuration(1200).setStartDelay(100).start();
        welcome_text.animate().translationX(0).alpha(1).setDuration(1200).setStartDelay(1000).start();
        google_btn.animate().translationY(0).alpha(1).setDuration(1200).setStartDelay(400).start();
        facebook_btn.animate().translationY(0).alpha(1).setDuration(1200).setStartDelay(600).start();
        tabLayout.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();

    }

    @Override
    public void stopOperations() {
    }

}