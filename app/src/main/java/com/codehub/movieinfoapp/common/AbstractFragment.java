package com.codehub.movieinfoapp.common;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class AbstractFragment extends Fragment {
    abstract public int getLayoutRes();

    abstract public void startOperations(View view);

    abstract public void stopOperations();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutRes(),container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startOperations(view);

    }

    @Override
    public void onPause() {
        stopOperations();
        super.onPause();
    }
}
