package com.codehub.movieinfoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.codehub.movieinfoapp.R;
import com.codehub.movieinfoapp.models.Movie;
import com.codehub.movieinfoapp.ui.fragments.MovieInfoFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private Context context;
    private ArrayList<Movie> movies;
    private LayoutInflater inflater;
    private String baseURL = "https://image.tmdb.org/t/p/w500";

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.holder_single_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

        if(movie.getMovieThumbnailUrl()!=null) {
            Picasso.get().load(baseURL + movie.getMovieThumbnailUrl()).into(holder.movieThumbnail);
        } else {
            Picasso.get().load(R.drawable.image_place_holder).into(holder.movieThumbnail);
        }

        holder.movieName_textView.setText("");
        holder.movieThumbnail.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 System.out.println("position clicked: " + holder.getAdapterPosition());

                 AppCompatActivity activity = (AppCompatActivity) view.getContext();
                 FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

                 transaction.addToBackStack("MovieInfoFragment");
                 transaction.replace(R.id.fragmentContainer, MovieInfoFragment.newInstance(
                     movie.getId(),
                    baseURL + movie.getMovieThumbnailUrl(),
                     movie.getMovieRating(),
                     false,
                     movie.getMovieName(),
                     movie.getMovieDescription()
                 ),"MovieInfoFragment").commit();

                 System.out.println("stack count" + activity.getSupportFragmentManager().getBackStackEntryCount());

                 FrameLayout fragmentContainer = activity.findViewById(R.id.fragmentContainer);
                 fragmentContainer.setTranslationY(1800);
                 fragmentContainer.animate().translationY(0).alpha(1).setDuration(250).setStartDelay(100).start();
             }
         });
    }



    @Override
    public int getItemCount() {
        if(movies!=null){
            return movies.size();
        }else{
            return 0;
        }
    }


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public TextView movieName_textView;
        public ImageView movieThumbnail;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieName_textView = itemView.findViewById(R.id.movieName_textView);
            movieThumbnail = itemView.findViewById(R.id.movie_thumbnail);
        }
    }

}