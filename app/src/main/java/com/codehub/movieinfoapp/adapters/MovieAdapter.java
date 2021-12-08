package com.codehub.movieinfoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codehub.movieinfoapp.R;
import com.codehub.movieinfoapp.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private Context context;
    private ArrayList<Movie> movies;
    private LayoutInflater inflater;
    private static final String base_url = "https://image.tmdb.org/t/p/w500";

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
        holder.movieName_textView.setText("");
        if(movie.movieThumbnailUrl!=null) {
            Picasso.get().load(base_url + movie.movieThumbnailUrl).into(holder.movieThumbnail);
        }else{
            Picasso.get().load(R.drawable.image_place_holder).into(holder.movieThumbnail);
        }
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
            movieName_textView = (TextView) itemView.findViewById(R.id.movieName_textView);
            movieThumbnail = (ImageView) itemView.findViewById(R.id.movie_thumbnail);
        }
    }
}