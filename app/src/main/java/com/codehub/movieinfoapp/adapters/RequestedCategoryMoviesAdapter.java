package com.codehub.movieinfoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.codehub.movieinfoapp.R;
import com.codehub.movieinfoapp.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RequestedCategoryMoviesAdapter extends RecyclerView.Adapter<RequestedCategoryMoviesAdapter.MovieViewHolder> {
    private Context context;
    private ArrayList<Movie> requestedMovies;
    private LayoutInflater inflater;
    //Url Prefix for used as thumbnail url prefix
    private static final String base_url = "https://image.tmdb.org/t/p/w500";

    public RequestedCategoryMoviesAdapter(Context context, ArrayList<Movie> requestedMovies) {
        this.context = context;
        this.requestedMovies = requestedMovies;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RequestedCategoryMoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.holder_single_movie, parent, false);
        return new RequestedCategoryMoviesAdapter.MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RequestedCategoryMoviesAdapter.MovieViewHolder holder, int position) {
        if(requestedMovies!=null) {
            Movie movie = requestedMovies.get(position);
            holder.movieName_textView.setText("");
            if (movie.movieThumbnailUrl != null) {
                Picasso.get().load(base_url + movie.movieThumbnailUrl).into(holder.movieThumbnail);
            } else {
                Picasso.get().load(R.drawable.image_place_holder).into(holder.movieThumbnail);
            }
        }else{

            holder.category_title.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(requestedMovies!=null){
            return requestedMovies.size();}
        else{
            return 0;

        }
    }


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public TextView movieName_textView;
        public ImageView movieThumbnail;
        TextView category_title;
        public MovieViewHolder(View itemView) {
            super(itemView);
            movieName_textView = (TextView) itemView.findViewById(R.id.movieName_textView);
            movieThumbnail = (ImageView) itemView.findViewById(R.id.movie_thumbnail);
             category_title= itemView.findViewById(R.id.search_category_title);
        }
    }

    public  void filterList(ArrayList<Movie> filteredList){
        requestedMovies = filteredList;
        //refresh recycler_view
        notifyDataSetChanged();
    }
}
