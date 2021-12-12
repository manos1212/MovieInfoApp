package com.codehub.movieinfoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Toast;

import com.codehub.movieinfoapp.common.AbstractActivity;
import com.codehub.movieinfoapp.rest_api.search_activity.SearchActivity;
import com.codehub.movieinfoapp.ui.fragments.FavouritesFragment;
import com.codehub.movieinfoapp.ui.fragments.HomeFragment;
import com.codehub.movieinfoapp.ui.fragments.ProfileFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AbstractActivity {
    private HomeFragment homeFragment = new HomeFragment();
    private FavouritesFragment favouritesFragment = new FavouritesFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment activeFragment = homeFragment;
    private boolean doubleBackToExitPressedOnce = false;
    MaterialToolbar topAppBar;
    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void startOperations() {
        topAppBar = findViewById(R.id.topAppBar);
        // add two below lines in order to be able to call toolbar in fragments - also i have overrode the onCreateOptionsMenu inside abstractActivity class
        setSupportActionBar(topAppBar);
        getSupportActionBar().setTitle("");
//        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,homeFragment).commit();
        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.search){
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(mOnNavigationItemSelectedListener) ;
        if(!profileFragment.isAdded()) {
            fragmentManager.beginTransaction().add(R.id.main_container, profileFragment, "3").hide(profileFragment).commit();
            fragmentManager.beginTransaction().add(R.id.main_container, favouritesFragment, "2").hide(favouritesFragment).commit();
            fragmentManager.beginTransaction().add(R.id.main_container, homeFragment, "1").commit();
        }

    }

    private BottomNavigationView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = new NavigationBarView.OnItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(item.getItemId()==R.id.home){
                fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
                activeFragment = homeFragment;
                return true;

            }else if(item.getItemId()==R.id.favourites){
                fragmentManager.beginTransaction().hide(activeFragment).show(favouritesFragment).commit();
                activeFragment = favouritesFragment;
//                favouritesFragment.getFavouritesFromDB();
                return true;
            }else if(item.getItemId()==R.id.profile){
                fragmentManager.beginTransaction().hide(activeFragment).show(profileFragment).commit();
                activeFragment = profileFragment;
                return true;
            }
            return false;
        }
    };

    @Override
    public void stopOperations() {

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            while(getSupportFragmentManager().getBackStackEntryCount() != 0) {
                super.onBackPressed();
            }
        } else {
//            fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
//            activeFragment = homeFragment;
            bottomNavigationView.setSelectedItemId(R.id.home);
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                @Override public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);

        }
    }
}