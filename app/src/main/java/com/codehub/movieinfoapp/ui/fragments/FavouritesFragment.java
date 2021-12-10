package com.codehub.movieinfoapp.ui.fragments;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.codehub.movieinfoapp.R;
import com.codehub.movieinfoapp.adapters.FavouritesAdapter;
import com.codehub.movieinfoapp.common.AbstractFragment;
import com.codehub.movieinfoapp.models.Movie;

import java.util.ArrayList;

public class FavouritesFragment extends AbstractFragment {

    private RecyclerView favourites_recyclerView;
    private FavouritesAdapter favouritesAdapter;
    private ArrayList<Movie> favouritesMovies;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_favourites;
    }

    @Override
    public void startOperations(View view) {

        favouritesMovies = new ArrayList<>();
        Movie movie = new Movie();

        movie.setId(101);
        movie.setMovieName("Favorite");;
        movie.setMovieThumbnailUrl(null);
        movie.setMovieDescription("Favorite");
        movie.setMovieRating(4.8);

        favouritesMovies.add(movie);
        favouritesMovies.add(movie);
        favouritesMovies.add(movie);
        favouritesMovies.add(movie);
        favouritesMovies.add(movie);
        favouritesMovies.add(movie);
        favouritesMovies.add(movie);
        favouritesMovies.add(movie);
        favouritesMovies.add(movie);

        favourites_recyclerView = getActivity().findViewById(R.id.favourites_recycler_view);
        favouritesAdapter = new FavouritesAdapter(getContext(), favouritesMovies);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        favourites_recyclerView.setLayoutManager(manager);
        favourites_recyclerView.setAdapter(favouritesAdapter);

    }

    @Override
    public void stopOperations() {

    }
}