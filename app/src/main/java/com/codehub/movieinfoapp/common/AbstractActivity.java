package com.codehub.movieinfoapp.common;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.codehub.movieinfoapp.R;

public abstract class AbstractActivity extends AppCompatActivity {

    abstract public int getLayoutRes();

    abstract public void startOperations();

    abstract public void stopOperations();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar, menu);
        return true;
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        startOperations();
    }
    @Override
    protected void onPause() {
        stopOperations();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
