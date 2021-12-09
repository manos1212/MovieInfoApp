package com.codehub.movieinfoapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.codehub.movieinfoapp.R;
import com.codehub.movieinfoapp.common.AbstractFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class MovieInfoFragment extends AbstractFragment{

    private int movieID;
    private FloatingActionButton movieBack;
    private ImageView movieImage;
    private TextView movieRating;
    private ImageView movieFavourite;
    private ImageView movieShare;
    private TextView movieTitle;
    private TextView movieDescription;

    private boolean favourite = false;


    public MovieInfoFragment() {

    }

    public static MovieInfoFragment newInstance(int id, String image, double rating, boolean favourite, String title, String description) {

        MovieInfoFragment fragment = new MovieInfoFragment();

        Bundle parameters = new Bundle();
        parameters.putInt("movie_id", id);
        parameters.putString("movie_image", image);
        parameters.putDouble("movie_rating", rating);
        parameters.putBoolean("movie_favourite", favourite);
        parameters.putString("movie_title", title);
        parameters.putString("movie_description", description);
        fragment.setArguments(parameters);

        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_movie_info;
    }

    @Override
    public void startOperations(View view) {

        movieBack = view.findViewById(R.id.back_fab);
        movieImage = view.findViewById(R.id.movie_image);
        movieRating = view.findViewById(R.id.movie_rating);
        movieFavourite = view.findViewById(R.id.movie_favourite);
        movieShare = view.findViewById(R.id.movie_share);
        movieTitle = view.findViewById(R.id.movie_title);
        movieDescription = view.findViewById(R.id.movie_description);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            movieID = (bundle.getInt("movie_id"));

            Picasso.get().load(bundle.getString("movie_image")).into(movieImage);
            movieRating.setText(String.valueOf(bundle.getDouble("movie_rating")));

            if(bundle.getBoolean("movie_favourite")) {
                movieFavourite.setImageResource(R.drawable.ic_baseline_favorite_filled_24);
            } else {
                movieFavourite.setImageResource(R.drawable.ic_baseline_favorite_no_fill_24);
            }

            movieTitle.setText((bundle.getString("movie_title")));
            movieDescription.setText((bundle.getString("movie_description")));
        }

        movieBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        movieFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favourite) {
                    movieFavourite.setImageResource(R.drawable.ic_baseline_favorite_no_fill_24);
                    favourite = false;
                } else {
                    movieFavourite.setImageResource(R.drawable.ic_baseline_favorite_filled_24);
                    favourite = true;
                }
            }
        });

        movieShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,"https://www.themoviedb.org/movie/" + movieID);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent,"Movie DB URL");
                startActivity(shareIntent);
            }
        });
    }

    @Override
    public void stopOperations() {

    }
}