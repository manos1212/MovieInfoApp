package com.codehub.movieinfoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MovieInfoFragment extends Fragment{

    private FloatingActionButton backButton;
    private TextView ratingButton;
    private ImageView favouriteButton;
    private ImageView shareButton;
    private boolean favourite = true;

    public MovieInfoFragment() {

    }

    public static MovieInfoFragment newInstance() {
        return new MovieInfoFragment();
    }

    public static MovieInfoFragment newInstance(String image, double rating, boolean favourite, String title, String description) {

        MovieInfoFragment fragment = new MovieInfoFragment();

        Bundle parameters = new Bundle();
        parameters.putString("movie_image", image);
        parameters.putDouble("movie_rating", rating);
        parameters.putBoolean("movie_favourite", favourite);
        parameters.putString("movie_title", title);
        parameters.putString("movie_description", description);
        fragment.setArguments(parameters);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        backButton = view.findViewById(R.id.back_fab);
        ratingButton = view.findViewById(R.id.movie_rating);
        favouriteButton = view.findViewById(R.id.movie_favourite);
        shareButton = view.findViewById(R.id.movie_share);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favourite) {
                    favouriteButton.setImageResource(R.drawable.ic_baseline_star_on_24);
                    favourite = false;
                } else {
                    favouriteButton.setImageResource(R.drawable.ic_baseline_star_off_24);
                    favourite = true;
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle shareBundle = new Bundle();
                shareBundle.putString("movie URL", "https://www.themoviedb.org/movie/" + "movie.id");

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtras(shareBundle);
            }
        });
    }
}