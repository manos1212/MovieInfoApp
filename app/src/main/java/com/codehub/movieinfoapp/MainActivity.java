package com.codehub.movieinfoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codehub.movieinfoapp.adapters.CategoryAdapter;
import com.codehub.movieinfoapp.models.Movie;
import com.codehub.movieinfoapp.models.MoviesCategory;

import java.util.ArrayList;

public class MainActivity extends AbstractActivity {

    private RecyclerView category_recyclerView;
    private CategoryAdapter categoryAdapter;
    private ArrayList<MoviesCategory> categories;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void startOperations() {

        category_recyclerView = findViewById(R.id.category_recyclerView);
        categories = prepareData();
        categoryAdapter = new CategoryAdapter(categories,MainActivity.this);
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
        category_recyclerView.setLayoutManager(manager);
        category_recyclerView.setAdapter(categoryAdapter);
    }

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
            new AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Exit Application")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    moveTaskToBack(true);
                }
            })
            .setNegativeButton("No", null)
            .show();
        }

    }

    private ArrayList<MoviesCategory> prepareData() {
        ArrayList<MoviesCategory> categories = new ArrayList<>();
        // TRENDING
        MoviesCategory trending = new MoviesCategory();
        trending.id = 1;
        trending.categoryName = "Trending Now";
        trending.movies = new ArrayList<Movie>();

        Movie movie1 = new Movie();
        movie1.id = 1;
        movie1.movieName = "Beauty and the Beast";
        movie1.movieThumbnailUrl = "https://i0.wp.com/thefairytaletraveler.com/wp-content/uploads/2016/11/Beauty-and-the-Beast-Poster-1-1.jpg?w=1000&ssl=1";

        Movie movie2 = new Movie();
        movie2.id = 2;
        movie2.movieName = "Squid Game 2";
        movie2.movieThumbnailUrl =  "https://images7.alphacoders.com/118/thumb-1920-1181582.jpg";

        Movie movie3 = new Movie();
        movie3.id = 3;
        movie3.movieName = "Squid Game 2";
        movie3.movieThumbnailUrl =  "https://images7.alphacoders.com/118/thumb-1920-1181582.jpg";

        Movie movie4 = new Movie();
        movie4.id = 4;
        movie4.movieName = "Squid Game 2";
        movie4.movieThumbnailUrl =  "https://images7.alphacoders.com/118/thumb-1920-1181582.jpg";

        Movie movie5 = new Movie();
        movie5.id = 5;
        movie5.movieName = "Squid Game 2";
        movie5.movieThumbnailUrl =  "https://images7.alphacoders.com/118/thumb-1920-1181582.jpg";

        trending.movies.add(movie1);
        trending.movies.add(movie2);
        trending.movies.add(movie3);
        trending.movies.add(movie4);
        trending.movies.add(movie5);
        trending.movies.add(movie5);
        trending.movies.add(movie1);
/////////////////////////////////////////////////////
        //TOP RATED

        MoviesCategory topRated = new MoviesCategory();
        topRated.id = 2;
        topRated.categoryName = "Top Rated";
        topRated.movies = new ArrayList<Movie>();

        Movie movie6 = new Movie();
        movie6.id = 6;
        movie6.movieName = "Beauty and the Beast";
        movie6.movieThumbnailUrl = "https://i0.wp.com/thefairytaletraveler.com/wp-content/uploads/2016/11/Beauty-and-the-Beast-Poster-1-1.jpg?w=1000&ssl=1";

        Movie movie7 = new Movie();
        movie7.id = 7;
        movie7.movieName = "Squid Game 2";
        movie7.movieThumbnailUrl =  "https://images7.alphacoders.com/118/thumb-1920-1181582.jpg";

        Movie movie8 = new Movie();
        movie8.id = 8;
        movie8.movieName = "Squid Game 2";
        movie8.movieThumbnailUrl =  "https://images7.alphacoders.com/118/thumb-1920-1181582.jpg";

        Movie movie9 = new Movie();
        movie9.id = 9;
        movie9.movieName = "Squid Game 2";
        movie9.movieThumbnailUrl =  "https://images7.alphacoders.com/118/thumb-1920-1181582.jpg";

        Movie movie10 = new Movie();
        movie10.id = 10;
        movie10.movieName = "Squid Game 2";
        movie10.movieThumbnailUrl = "https://images7.alphacoders.com/118/thumb-1920-1181582.jpg";

        topRated.movies.add(movie6);
        topRated.movies.add(movie7);
        topRated.movies.add(movie8);
        topRated.movies.add(movie9);
        topRated.movies.add(movie10);
        topRated.movies.add(movie10);
        topRated.movies.add(movie10);

        categories.add(trending);
        categories.add(topRated);
        categories.add(topRated);
        categories.add(topRated);
        categories.add(topRated);
        categories.add(topRated);

        return categories;
    }
}