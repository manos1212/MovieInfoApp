package com.codehub.movieinfoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.codehub.movieinfoapp.MovieInfoFragment;
import com.codehub.movieinfoapp.R;
import com.codehub.movieinfoapp.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<Movie> movies;
    private LayoutInflater inflater;

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_movie, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.movieName_textView.setText(movie.movieName);
        Picasso.get().load(movie.movieThumbnailUrl).into(holder.movieThumbnail);

        holder.movieThumbnail.setOnClickListener(new View.OnClickListener()
           {
               @Override
               public void onClick(View v) {

                   System.out.println("position clicked: " + holder.getAdapterPosition());

                   AppCompatActivity activity = (AppCompatActivity)v.getContext();
                   MovieInfoFragment fragment = new MovieInfoFragment();
                   FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

                   transaction.addToBackStack("MovieInfoFragment");
                   transaction.replace(R.id.fragmentContainer, MovieInfoFragment.newInstance(),"MovieInfoFragment").commit();

                   System.out.println("stack count" + activity.getSupportFragmentManager().getBackStackEntryCount());

                   FrameLayout fragmentContainer = activity.findViewById(R.id.fragmentContainer);
                   fragmentContainer.setTranslationY(1800);
                   fragmentContainer.animate().translationY(0).alpha(1).setDuration(250).setStartDelay(100).start();
               }
           }
        );
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public ImageView movieThumbnail;
        public TextView movieName_textView;

        public CustomViewHolder(View itemView) {
            super(itemView);
            movieName_textView = itemView.findViewById(R.id.movieName_textView);
            movieThumbnail = itemView.findViewById(R.id.movie_thumbnail);
        }
    }
}