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

public class RequestedMoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<Movie> requestedMovies;
    public ArrayList<Movie> requestedCategoryMovies;
    private Context context;
    private LayoutInflater layoutInflater;

    //Url Prefix for used as thumbnail url prefix
    private static final String base_url = "https://image.tmdb.org/t/p/w500";

    public RequestedMoviesAdapter(Context context,ArrayList<Movie> requestedMovies,ArrayList<Movie> requestedCategoryMovies) {
        this.requestedMovies = requestedMovies;
        this.requestedCategoryMovies = requestedCategoryMovies;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 1;
        else return 2;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==2){
                View view = layoutInflater.inflate(R.layout.holder_search_single_movie, parent, false);
                return new MovieViewHolder(view);
            }else{
                View view = layoutInflater.inflate(R.layout.holder_single_movie_category, parent, false);
                return new CategoryMovieViewHolder(view);
            }




    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieViewHolder && requestedMovies!=null) {
            Movie movie = requestedMovies.get(position);
            ((MovieViewHolder)holder).movieName_textView.setText(movie.movieName);
            if(movie.movieThumbnailUrl!=null) {
                //parse url inside xml image_thumbnail
                Picasso.get().load(base_url + movie.movieThumbnailUrl).into(((MovieViewHolder)holder).movieThumbnail);
//                Picasso.get().load(movie.movieThumbnailUrl).into(holder.movieThumbnail);
            }else{
                ((MovieViewHolder)holder).movieThumbnail.setImageResource(R.drawable.image_place_holder);
//                Picasso.get().load(R.drawable.image_place_holder).into(holder.movieThumbnail);
            }

//            String name = getDataVertical().get(position-1).mName;
//            Log.d("###", "Setting name: " + name);
//            ((MyViewHolder) holder).getmDataTextView().setText(name);
        } else if (holder instanceof CategoryMovieViewHolder) {

            ((CategoryMovieViewHolder)holder).recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            ((CategoryMovieViewHolder)holder).recyclerView.setHasFixedSize(true);
            ((CategoryMovieViewHolder)holder).recyclerView.setAdapter(new MovieAdapter(context, requestedMovies));
//            ((CategoryMovieViewHolder)holder).categoryName.setText(categories.get(position).categoryName);
        }


//        if(requestedMovies!=null){
//        Movie movie = requestedMovies.get(position);
//        holder.movieName_textView.setText(movie.movieName);
//            if(movie.movieThumbnailUrl!=null) {
//                //parse url inside xml image_thumbnail
//                Picasso.get().load(base_url + movie.movieThumbnailUrl).into(holder.movieThumbnail);
////                Picasso.get().load(movie.movieThumbnailUrl).into(holder.movieThumbnail);
//            }else{
//                holder.movieThumbnail.setImageResource(R.drawable.image_place_holder);
////                Picasso.get().load(R.drawable.image_place_holder).into(holder.movieThumbnail);
//            }
//        }
    }

    @Override
    public int getItemCount() {
        if(requestedMovies!=null && requestedCategoryMovies!=null){
        return requestedMovies.size() +1;}
        else if(requestedMovies!=null){
            return requestedMovies.size();
        }else if(requestedCategoryMovies!=null){
            return 1;
        }else{
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

    private static class CategoryMovieViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView categoryName;

        public CategoryMovieViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.movies_recycler_view);
            categoryName = (TextView) itemView.findViewById(R.id.category_name_textView);
        }
    }
    public  void filterList(ArrayList<Movie> filteredList){
        requestedMovies = filteredList;
        //refresh recycler_view
        notifyDataSetChanged();
    }
}
