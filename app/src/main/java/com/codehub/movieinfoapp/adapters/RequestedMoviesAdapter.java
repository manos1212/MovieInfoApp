package com.codehub.movieinfoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codehub.movieinfoapp.R;
import com.codehub.movieinfoapp.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RequestedMoviesAdapter extends RecyclerView.Adapter<RequestedMoviesAdapter.MovieViewHolder> {
    public ArrayList<Movie> requestedMovies;
    private Context context;
    private LayoutInflater layoutInflater;

    //Url Prefix for used as thumbnail url prefix
    private static final String base_url = "https://image.tmdb.org/t/p/w500";

    public RequestedMoviesAdapter(Context context,ArrayList<Movie> requestedMovies) {
        this.requestedMovies = requestedMovies;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = layoutInflater.inflate(R.layout.holder_search_single_movie, parent, false);
            return new MovieViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        if(requestedMovies!=null){
        Movie movie = requestedMovies.get(position);
        holder.movieName_textView.setText(movie.movieName);
            if(movie.movieThumbnailUrl!=null) {
                //parse url inside xml image_thumbnail
                Picasso.get().load(base_url + movie.movieThumbnailUrl).into(holder.movieThumbnail);
//                Picasso.get().load(movie.movieThumbnailUrl).into(holder.movieThumbnail);
            }else{
                holder.movieThumbnail.setImageResource(R.drawable.image_place_holder);
//                Picasso.get().load(R.drawable.image_place_holder).into(holder.movieThumbnail);
            }
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
        public ImageView movieThumbnail;
        public TextView movieName_textView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieName_textView = (TextView) itemView.findViewById(R.id.search_movieName_textView);
            movieThumbnail = (ImageView) itemView.findViewById(R.id.search_movie_thumbnail);
        }
    }


    public  void filterList(ArrayList<Movie> filteredList){
        requestedMovies = filteredList;
        //refresh recycler_view
        notifyDataSetChanged();
    }
}
